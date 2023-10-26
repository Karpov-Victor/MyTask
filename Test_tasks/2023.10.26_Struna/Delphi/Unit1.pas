unit Unit1;

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants, System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, Vcl.StdCtrls, StrunaConst, ChannelData, ThreadUnit, LogWriterUnit,
  Data.DB, Data.Win.ADODB;

type
  TForm1 = class(TForm)
    Button1: TButton;
    Label1: TLabel;
    BtnSendAndGetData: TButton;
    EditQuery: TEdit;
    BtnSendCommand: TButton;
    ComboBox1: TComboBox;
    ComboBox2: TComboBox;
    ComboBoxChNumber: TComboBox;
    EditAnswer: TEdit;
    BtnGetDataСhannel: TButton;
    Memo1: TMemo;
    CheckBoxCyrcle: TCheckBox;
    ADOConnection1: TADOConnection;
    ADOQuery1: TADOQuery;
    CheckBoxWriteToDB: TCheckBox;
    EditConnectionString: TEdit;
    procedure Button1Click(Sender: TObject);
    procedure BtnSendAndGetDataClick(Sender: TObject);
    procedure BtnSendCommandClick(Sender: TObject);
    procedure FormCreate(Sender: TObject);
    procedure FormClose(Sender: TObject; var Action: TCloseAction);
    procedure BtnGetDataСhannelClick(Sender: TObject);
    procedure CheckBoxCyrcleClick(Sender: TObject);
    procedure GetDataСhannelToMemo;
    procedure ComboBox1Change(Sender: TObject);
    procedure ComboBoxChNumberChange(Sender: TObject);
    procedure saveChannelDataToDB(channelData: TChannelData);
    procedure CheckBoxWriteToDBClick(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  Form1: TForm1;
  hPort: THandle;
  ChannelData : TChannelData;
  comPort: string;
  WriteTomemoThread : TStrunaThread;

  bytesRead, bytesWritten: DWORD;
  buffer: array[0..255] of Byte;
  dataFromDevice: array[0..255] of Byte;


implementation

{$R *.dfm}

{ В данных возвращается значение параметра. Значение параметра передаётся
тремя байтами в виде комбинации двоичной целой части и двоично-десятичной
дробной части. }
function AnswerParser(inArray : array of Byte):double;
var str : String;
begin
 // взять последние 4 бита из байта= byte shr 4
 // взять первые 4 бита из байта = byte and $0F  // Дробная часть 0..9
 // IntToHex(buffer[2],2) - пребразовать число в текст (2 символа)
//  showmessage(IntToHex(inArray[0], 2) + ' ' + IntToHex(inArray[1], 2) + ' ' + IntToHex(inArray[2], 2));

      str := '$' + IntToHex((inArray[2] shr 4), 2)  // Старший разряд целого числа
           + IntToHex(inArray[1], 2)
           + IntToHex(inArray[0], 2);
       AnswerParser :=  StrToInt(str) + (inArray[2] and $0F) /10;
end;

{ Анализ байта с температурой
Единица измерения – 0.5 °С. Первый байт содержит значение нижнего датчика.
Четвертый байт содержит среднюю температуру продукта. Структура одного байта:
1-7 Двоичное значение температуры
8 знак: 1 – отрицательный, 0 – положительный.}
function AnswerTempParser(tempByte : Byte): double;
begin
 // взять последние 4 бита из байта= byte shr 4
 // взять первые 4 бита из байта = byte and $0F  // Дробная часть 0..9
 // IntToHex(buffer[2],2) - пребразовать число в текст (2 символа)
//  showmessage(IntToHex(inArray[0], 2) + ' ' + IntToHex(inArray[1], 2) + ' ' + IntToHex(inArray[2], 2));
AnswerTempParser := -274;
// Определение отрицательности температуры
if (tempByte shr 7) = 1
  then
    AnswerTempParser := (tempByte and 127)/2 * (-1)
  else
    AnswerTempParser := (tempByte and 127)/2;

end;

// Определяет индек в массве данных, на котором данные заканчиваются (далее только нули)
function endDataindex() : integer;
  var
  i : integer;
  begin
  endDataindex := -1;
  for i := 255 downto 0 do
   if buffer[i] <> 0 then
    begin
     endDataindex := i;
     break;
    end;
  end;




// Очистка буфера приёма
procedure clearBuffer();
  var i : integer;
begin
  for i := 0 to 255 do
  buffer[i] := 0;
end;

// Записать полученные данные в EditAnswer
procedure writeDataToEditAnswer();
Var
    resultStr : String;
    I: Integer;
begin
  resultStr := '';
  for i := 0 to endDataindex do
  begin
    resultStr := resultStr + IntToHex(buffer[i], 2) + ' ';
  end;
  Form1.EditAnswer.text := resultStr;
end;

// Отправка команды в указанный порт
procedure sendData(hPort: THandle; command : integer);
begin
  buffer[0] := command;
  Form1.EditQuery.text := IntToHex(buffer[0], 2);
  WriteFile(hPort, buffer, 1, bytesWritten, nil);
end;

// Получение данных из указанного порта
// -1000000 - никакие значения не получены
Function getData(hPort: THandle; delay : integer) : Byte;
var i, delayDelta : integer;
begin
  getData := 6; // Ошибка связи по умолчанию
  clearBuffer(); // Очищаем буфер данных
  // Получаем данные
  // Запускаем цикл ожидания данных из порта
  i:= 0;
  delayDelta := Trunc(delay / 10);
  while i < delay do
  begin
    bytesRead := 0;
    ReadFile(hPort, buffer, SizeOf(buffer), bytesRead, nil);
    if (bytesRead > 0) and (endDataindex <> -1)
     then // Если получен какой-то ответ
      begin
      getData := buffer[0];
      i := delay; // Выходим из цикла
      end;
    sleep(delayDelta);
    i := i + delayDelta;
    // showmessage(inttostr(i));
  end;
  writeDataToEditAnswer;
end;

function sendAndGetData(hPort: THandle; command : integer; delay : integer) : Byte;
begin
  sendData(hPort, command);
  sendAndGetData := getData(hPort, delay);
end;

// Проверка ответа на корректность и его расшифровка
function checkAnswerMessage(answerResult : byte) : string;
begin


   checkAnswerMessage := 'Неизвестная ошибка';
   case answerResult of
            0: checkAnswerMessage := 'Проверка связи: связь установлена';
            4: checkAnswerMessage := 'Проверка связи: Неисправность канала или параметра';
            6: checkAnswerMessage := 'Проверка связи: Ошибка связи или не получен ответ';
           12: checkAnswerMessage := 'Проверка связи: Неопознанная команда';
          238: checkAnswerMessage := 'Инициализация (режим инициализации системы)';
          253: checkAnswerMessage := 'Расчитанная контрольная смумма не соответствует полученной';
          255: checkAnswerMessage := 'Отсутствие канала или параметра в конфигурации системы';
        end;
end;

// Проверка ответа на корректность и его расшифровка
function checkConnect(answerResult : byte) : boolean;
var answerBytesCounter, i, checkSumm : byte;
begin
   // Проверяем сколько байт в ответе. Если больше 3-ёх, то вычисляем контрольную сумму
   answerBytesCounter := endDataindex() + 1;
   // showmessage('В ответе ' + inttostr(answerBytesCounter) + ' байт');
   // showmessage('Байт четности = ' + inttohex(buffer[answerBytesCounter - 1]));
   if answerBytesCounter >=4 then
    begin
      checkSumm := buffer[0];
        for i := 1 to (answerBytesCounter - 2) do
        begin
          checkSumm := checkSumm XOR buffer[i];
        end;
        if (checkSumm <> buffer[answerBytesCounter - 1]) then answerResult := 254;

    end;

   checkConnect := true;
   if endDataindex() = 0  then
   case answerResult of
            0: checkConnect := true;
            4: checkConnect := false;
            6: checkConnect := false;
           12: checkConnect := false;
          238: checkConnect := false;
          254: checkConnect := false;
          255: checkConnect := false;
        end;
end;

// ФункциЯ Перевода байта в строку
function ByteToBinStr(b:byte):string;
var bb:Byte;
begin
  Result:='        ';
  for bb:=7 downto 0 do
    begin
      Result[8-bb]:=Chr(48+(b shr bb) and 1);
      showmessage('');
    end;
end;

// Проверка связи с уровнемером
Function checkConnectionFunction(hPort: THandle) : Boolean;
var answerResult : byte;
    message : String;
Begin
  // Отправляем в порт команду на проверку связи
  buffer[0] := StrunaConst.checkConnection;
  message := 'Неизвестная ошибка';
  // Получаем ответ
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if (answerResult = 0) or (answerResult = 85)
    then
      begin
        checkConnectionFunction := true;
        message := 'Связь с утройством установлена';
      end
    else
    begin
        checkConnectionFunction := false;
        message := checkAnswerMessage(answerResult);
    end;
  ShowMessage(message);
End;

// ПОЛУЧЕНИЕ ДАННЫХ О КОНФИГУРАЦИИ
Function checkСonfigurationFunction(hPort: THandle) : String;
var
    message : String;
    chNumber, answerResult : byte;
Begin
  // Отправляем в порт команду на проверку связи
  buffer[0] := StrunaConst.configuration;
  message := 'Неизвестная ошибка';
  // Получаем ответ
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if checkConnect(answerResult)
    then
      begin
        message := 'Получен корректный ответ с данными:';
       // Получаем номер канала для анализа
       chNumber := Form1.ComboBoxChNumber.itemIndex + 1;
       message := 'Получен ответ с данными по каналу №' + IntToStr(chNumber-1)
       + #13#10
       + 'Измерение уровня продукта - ' + Chr(48+(buffer[chNumber] shr 0) and 1)
       + #13#10
       + 'Измерение температуры продукта - ' + Chr(48+(buffer[chNumber] shr 1) and 1)
       + #13#10
       + 'Наличие информации по объему - ' + Chr(48+(buffer[chNumber] shr 2) and 1)
       + #13#10
       + 'Измерение уровня подтоварной воды - ' + Chr(48+(buffer[chNumber] shr 4) and 1)
       + #13#10
       + 'Измерение плотности продукта - ' + Chr(48+(buffer[chNumber] shr 5) and 1)
       + #13#10
       + 'Наличие канала - ' + Chr(48+(buffer[chNumber] shr 7) and 1)
       + #13#10;
      end
    else
      begin
        message := checkAnswerMessage(answerResult);
      end;
  checkСonfigurationFunction := message;
  ShowMessage(message);
End;

// ПОЛУЧЕНИЕ ДАННЫХ О ТОМ ПОДКЛЮЧЕН ЛИ КАНАЛ
Function checkChannelFunction(hPort: THandle; Channel : byte) : boolean;
var answerResult : byte;
Begin
  // Отправляем в порт команду на проверку связи
  buffer[0] := StrunaConst.configuration;
  // Получаем ответ
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if checkConnect(answerResult)
    then
      begin
        if (buffer[Channel] shr 7) = 1
          then checkChannelFunction := true
          else checkChannelFunction := false;
      end
    else
      begin
          checkChannelFunction := false;
      end;

End;


// ПОЛУЧЕНИЕ ДАННЫХ О СОСТОЯНИИ БЛОКА ВЫЧИСЛИТЕЛЬНОГО (БВ)
Function checkСonfigurationState(hPort: THandle) : Boolean;
var
    message : String;
    answerResult : byte;
Begin
  checkСonfigurationState := false;
  // Отправляем в порт команду на проверку связи
  buffer[0] := StrunaConst.state;
  message := 'Неизвестная ошибка';
  // Получаем ответ
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if checkConnect(answerResult)
    then
      begin
        if (buffer[0] shr 7) = 1
        then
          Begin
           checkСonfigurationState := true;
           message := 'БВ ГОТОВ';
          End
         else
          Begin
           checkСonfigurationState := false;
           message := 'БВ НЕ ГОТОВ';
          End;
      end
    else
      begin
        message := checkAnswerMessage(answerResult);
      end;
  ShowMessage(message);
End;


// ПОЛУЧЕНИЕ ДАННЫХ ОБ УРОВНЕ
// hPort - порт, channel - Канал, показывать или нет сообщение
Function checkLevel(hPort: THandle; channel : byte; showAnswerMessage : Boolean) : double;
var dresult : double;
    message : String;
    answerResult : byte;
Begin
  message := 'Неизвестная ошибка';
  // Отправляем в порт команду на проверку связи
  buffer[0] := StrunaConst.level + channel;
  // Получаем ответ
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if checkConnect(answerResult)
    then
      begin
        dresult := AnswerParser(buffer);
        checkLevel := dresult;
        message := 'Значение уровня по каналу № ' + IntToStr(channel) + #13#10 + FloatToStr(dresult);
      end
    else
      begin
        checkLevel := -999999;
        message := checkAnswerMessage(answerResult);
      end;
  if showAnswerMessage then ShowMessage(message);
End;

// ПОЛУЧЕНИЕ ДАННЫЕ О ПЛОТНОСТИ
// hPort - порт, channel - Канал, показывать или нет сообщение
Function checkDensity(hPort: THandle; channel : byte; showAnswerMessage : Boolean) : double;
var
    dresult : double;
    message : String;
    answerResult : byte;
Begin
  message := 'Неизвестная ошибка';
  // Отправляем в порт команду на проверку связи
  buffer[0] := StrunaConst.density + channel;
  // Получаем ответ
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if checkConnect(answerResult)
    then
      begin
        dresult := AnswerParser(buffer);
        checkDensity := dresult;
        message := 'Значение плотности по каналу № ' + IntToStr(channel) + #13#10 + FloatToStr(dresult);
      end
    else
      begin
        checkDensity := -999999;
        message := checkAnswerMessage(answerResult);
      end;
  if showAnswerMessage then ShowMessage(message);
End;

// ПОЛУЧЕНИЕ ДАННЫЕ ОБ ОБЪЁМЕ
// hPort - порт, channel - Канал, показывать или нет сообщение
Function checkVolume(hPort: THandle; channel : byte; showAnswerMessage : Boolean) : double;
var
    dresult : double;
    message : String;
    answerResult : byte;
Begin
  message := 'Неизвестная ошибка';
  // Отправляем в порт команду на проверку связи
  buffer[0] := StrunaConst.volume + channel;
  // Получаем ответ
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if checkConnect(answerResult)
    then
      begin
        dresult := AnswerParser(buffer);
        checkVolume := dresult;
        message := 'Значение объёма по каналу № ' + IntToStr(channel) + #13#10 + FloatToStr(dresult);
      end
    else
      begin
        checkVolume := -999999;
        message := checkAnswerMessage(answerResult);
      end;
  if showAnswerMessage then ShowMessage(message);
End;

// ПОЛУЧЕНИЕ ДАННЫЕ О МАССЕ
// hPort - порт, channel - Канал, показывать или нет сообщение
Function checkMass(hPort: THandle; channel : byte; showAnswerMessage : Boolean) : double;
var
    dresult : double;
    message : String;
    answerResult : byte;
Begin
  message := 'Неизвестная ошибка';
  // Отправляем в порт команду на проверку связи
  buffer[0] := StrunaConst.mass + channel;
  // Получаем ответ
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if checkConnect(answerResult)
    then
      begin
        dresult := AnswerParser(buffer);
        checkMass := dresult;
        message := 'Значение массы по каналу № ' + IntToStr(channel) + #13#10 + FloatToStr(dresult);
      end
    else
      begin
        checkMass := -999999;
        message := checkAnswerMessage(answerResult);
      end;
  if showAnswerMessage then ShowMessage(message);
End;

// ПОЛУЧЕНИЕ ДАННЫХ О ТЕМПЕРАТУРЕ
// hPort - порт, channel - Канал, показывать или нет сообщение
// sensorNumber - номер сенсора (0 - среднее значение)
Function checkTemp(hPort: THandle; channel : byte; showAnswerMessage : Boolean; sensorNumber : Byte ) : double;
var
    message : String;
    answerResult : byte;
Begin

  message := 'Неизвестная ошибка';
  // Отправляем в порт команду на проверку связи
  buffer[0] := StrunaConst.temp + channel;
  // Получаем ответ
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if checkConnect(answerResult)
    then
      begin
        if sensorNumber = 0
          then checkTemp := AnswerTempParser(buffer[3])
          else
            begin
             checkTemp := AnswerTempParser(buffer[sensorNumber]);
            end;
        message := 'Значение температуры по каналу № ' + IntToStr(channel) +
         #13#10 + 'Датчик № 1 =' + FloatToStr(AnswerTempParser(buffer[0])) +
         #13#10 + 'Датчик № 2 =' + FloatToStr(AnswerTempParser(buffer[1])) +
         #13#10 + 'Датчик № 3 =' + FloatToStr(AnswerTempParser(buffer[2])) +
         #13#10 + 'Среднее значение =' + FloatToStr(AnswerTempParser(buffer[3]));
      end
    else
      begin
        checkTemp := -999999;
        message := checkAnswerMessage(answerResult);
      end;
  if showAnswerMessage then ShowMessage(message);
End;

// ПОЛУЧЕНИЕ ДАННЫХ О ПОДТОВАРНОЙ ВОДЕ
// hPort - порт, channel - Канал, показывать или нет сообщение
//
Function checkWater(hPort: THandle; channel : byte; showAnswerMessage : Boolean) : integer;
var
    message : String;
    answerResult : byte;
Begin

  message := 'Неизвестная ошибка';
  // Отправляем в порт команду на проверку связи
  buffer[0] := StrunaConst.water + channel;
  // Получаем ответ
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if checkConnect(answerResult)
    then
      begin
        message := 'Значение подтоварной воды по каналу № ' + IntToStr(channel) + inttoStr(buffer[0]);
        checkWater := buffer[0];
      end
    else
      begin
        checkWater := -999999;
        message := checkAnswerMessage(answerResult);
      end;
  if showAnswerMessage then ShowMessage(message);
End;

// ПОЛУЧЕНИЕ ДАННЫХ О ТЕМПЕРАТУРЕ С ВЕРХНЕГО ДАТЧИКА
// hPort - порт, channel - Канал, показывать или нет сообщение
Function checkTempUp(hPort: THandle; channel : byte; showAnswerMessage : Boolean) : double;
var
    message : String;
    answerResult : byte;
Begin

  message := 'Неизвестная ошибка';
  // Отправляем в порт команду на проверку связи
  buffer[0] := StrunaConst.tempUp + channel;
  // Получаем ответ
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if checkConnect(answerResult)
    then
      begin
        checkTempUp := AnswerTempParser(buffer[3]);
        message := 'Значение температуры по каналу № ' + IntToStr(channel) +
         #13#10 + 'Верхний датчик = ' + FloatToStr(AnswerTempParser(buffer[0]));
      end
    else
      begin
        checkTempUp := -999999;
        message := checkAnswerMessage(answerResult);
      end;
  if showAnswerMessage then ShowMessage(message);
End;



procedure TForm1.Button1Click(Sender: TObject);
begin
  LogWriterUnit.writeToLog(Sender.toString + ' Нажата кнопка открыть/закрыть порт');
  if Button1.Caption = 'Открыть порт'
   then
    Begin
    comPort := ComboBox2.Items.Strings[ComboBox2.ItemIndex]; // Укажем COM-порт, который вы хотите открыть

    hPort := CreateFile(
      PChar('\\.\' + comPort), GENERIC_READ or GENERIC_WRITE,
      0, nil, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, 0);

    if hPort <> INVALID_HANDLE_VALUE
      then
        begin
          Label1.caption := comPort + ' открыт';
          Button1.Caption := 'Закрыть порт';
          BtnSendCommand.Enabled := true;
          BtnSendAndGetData.Enabled := true;
          BtnGetDataСhannel.Enabled := true;
          CheckBoxCyrcle.Enabled := true;
          LogWriterUnit.writeToLog(comPort + ' открыт');
        end
      else
          begin
            // Не удалось открыть COM-порт
            ShowMessage('Не удалось открыть COM-порт: ' + comPort);
          end;
    End
   else
    Begin
      CloseHandle(hPort); // Закрываем Com-порт
      Label1.caption := 'COM-порт закрыт';
      Button1.Caption := 'Открыть порт';
      BtnSendCommand.Enabled := false;
      BtnSendAndGetData.Enabled := false;
      BtnGetDataСhannel.Enabled := false;
      CheckBoxCyrcle.Checked := false;
      CheckBoxCyrcle.Enabled := false;
      LogWriterUnit.writeToLog(comPort + ' закрыт');
    End;
end;



function getChannelData(ChannelNumber : byte) : TChannelData;
Begin
  getChannelData := TChannelData.Create(ChannelNumber, Now);
  getChannelData.level := checkLevel(hPort, ChannelNumber, false);
  getChannelData.density := checkDensity(hPort, ChannelNumber, false);
  getChannelData.volume := checkVolume(hPort, ChannelNumber, false);
  getChannelData.mass := checkMass(hPort, ChannelNumber, false);
  getChannelData.temp[0] := checkTemp(hPort, ChannelNumber, false, 0);
  getChannelData.temp[1] := checkTemp(hPort, ChannelNumber, false, 1);
  getChannelData.temp[2] := checkTemp(hPort, ChannelNumber, false, 2);
  getChannelData.temp[3] := checkTemp(hPort, ChannelNumber, false, 3);
  getChannelData.water := checkWater(hPort, ChannelNumber, false);
  getChannelData.tempUp := checkTempUp(hPort, ChannelNumber, false);
End;

procedure TurnOnButtons();
begin
  Form1.BtnSendCommand.Enabled := true;
  Form1.BtnSendAndGetData.Enabled := true;
  Form1.BtnGetDataСhannel.Enabled := true;
end;

procedure TurnOffButtons();
begin
  Form1.BtnSendCommand.Enabled := false;
  Form1.BtnSendAndGetData.Enabled := false;
  Form1.BtnGetDataСhannel.Enabled := false;
end;

procedure Tform1.GetDataСhannelToMemo;
// https://programmersforum.ru/showthread.php?t=17303 - автоскролинг
var i : byte;
begin
for i := 0 to 15 do
  begin
    if checkChannelFunction(hPort, i)
      then
        begin
          ChannelData := getChannelData(i);
          Form1.Memo1.Lines.Append(ChannelData.DataToString(ChannelData));
          if Form1.CheckBoxWriteToDB.Checked then Form1.saveChannelDataToDB(ChannelData);
        end;
  end;
Form1.Memo1.Lines.Append('------------------------------------------------');
end;

procedure TForm1.BtnGetDataСhannelClick(Sender: TObject);
begin
  LogWriterUnit.writeToLog(Sender.ToString + ': Нажата кнопка "Получить данные по каналам"');
  GetDataСhannelToMemo;
end;

procedure TForm1.CheckBoxCyrcleClick(Sender: TObject);
var i :integer;
begin
{
  if CheckBoxCyrcle.Checked
    then
      begin
         LogWriterUnit.writeToLog(Sender.ToString + ' режим опроса в цикле включён.');
         TurnOffButtons();
         writeToMemothread := TStrunaThread.Create(true); // <<true означает ручной запуск потока
         writeToMemothread.FreeOnTerminate := true;       // <<Экземпляр должен само уничтожиться после выполнения
         writeToMemothread.Priority:=tpNormal;            // <<Выставляем приоритет потока
         writeToMemothread.Resume;
      end
    else
      begin
        TurnOnButtons();
         LogWriterUnit.writeToLog(Sender.ToString + ' режим опроса в цикле отключён.');
        // writeToMemothread.Terminate;
      end;
 }

  while Form1.CheckBoxCyrcle.Checked do
  begin

   Form1.GetDataСhannelToMemo;
   for I := 1 to 2000 do
    Begin
    Application.ProcessMessages;
    sleep(1);
    End;
  end;


end;





procedure TForm1.CheckBoxWriteToDBClick(Sender: TObject);
begin
  if CheckBoxWriteToDB.Checked = true then
    Begin
      try
        ADOConnection1.ConnectionString := EditConnectionString.Text;
        ADOConnection1.Connected := true;
        EditConnectionString.enabled := False;
      except
        CheckBoxWriteToDB.Checked := False;
        showMessage('Не удается установить связь с БД.');
      end;
    End
    else
    Begin
        EditConnectionString.enabled := true;
        ADOConnection1.Connected := false;
    End;



end;

procedure TForm1.ComboBox1Change(Sender: TObject);
begin
 LogWriterUnit.writeToLog(Sender.ToString + ': выбрана команда: ' + combobox1.items.strings[combobox1.ItemIndex]);
end;

procedure TForm1.ComboBoxChNumberChange(Sender: TObject);
begin
 LogWriterUnit.writeToLog(Sender.ToString + ': выбран канал: ' + intTostr(ComboBoxChNumber.ItemIndex));
end;

procedure TForm1.BtnSendAndGetDataClick(Sender: TObject);
Var command : Byte;
begin
  command := StrToint('$' + EditQuery.Text);
  sendAndGetData(hPort, command, 1000);
end;

// Отправить команду в порт, получить и расшифровать ответ.
procedure TForm1.BtnSendCommandClick(Sender: TObject);
begin
  LogWriterUnit.writeToLog(Sender.ToString + ': Выбран канал ' + intTostr(ComboBoxChNumber.ItemIndex)
   + '. Выполняем команду: ' + combobox1.items.strings[combobox1.ItemIndex]);


  case ComboBox1.ItemIndex of
   0 : checkConnectionFunction(hPort);
   1 : checkСonfigurationFunction(hPort);
   2 : checkСonfigurationState(hPort);
   3 : checkLevel(hPort, ComboBoxChNumber.ItemIndex, true);
   4 : checkDensity(hPort, ComboBoxChNumber.ItemIndex, true);
   5 : checkVolume(hPort, ComboBoxChNumber.ItemIndex, true);
   6 : checkMass(hPort, ComboBoxChNumber.ItemIndex, true);
   7 : checkTemp(hPort, ComboBoxChNumber.ItemIndex, true, 0);
   8 : checkWater(hPort, ComboBoxChNumber.ItemIndex, true);
   9 : checkTempUp(hPort, ComboBoxChNumber.ItemIndex, true);
  end;
end;


procedure TForm1.FormClose(Sender: TObject; var Action: TCloseAction);
begin
 CloseHandle(hPort);
end;

procedure TForm1.FormCreate(Sender: TObject);
Var i:Integer;
begin
LogWriterUnit.writeToLog('Начало работы программы.');


ComboBox1.Items.Add(checkConnectionStringName);
ComboBox1.Items.Add(configurationStringName);
ComboBox1.Items.Add(stateStringName);
ComboBox1.Items.Add(levelStringName);
ComboBox1.Items.Add(densityStringName);
ComboBox1.Items.Add(volumeStringName);
ComboBox1.Items.Add(massStringName);
ComboBox1.Items.Add(tempStringName);
ComboBox1.Items.Add(waterStringName);
ComboBox1.Items.Add(tempUpStringName);
ComboBox1.ItemIndex := 0;
 for i:=0 to 255 do
  begin
    comPort := 'COM'+intToStr(i+1);
    hPort := CreateFile(
    PChar('\\.\' + comPort),
    GENERIC_READ or GENERIC_WRITE,
    0,
    nil,
    OPEN_EXISTING,
    FILE_ATTRIBUTE_NORMAL,
    0
    );
    if hPort<>invalid_handle_value then
    begin
    Combobox2.Items.Add('COM'+ IntToStr(i+1));
    CloseHandle(hPort);
    end;
   end;
   if ComboBox2.Items.Capacity > 0 then ComboBox2.ItemIndex := 0;
   ComboBoxChNumber.ItemIndex := 0;
end;

procedure Tform1.saveChannelDataToDB(channelData: TChannelData);
var dateStr : String;
Oldseparator: char;
Begin
    Oldseparator := FormatSettings.DecimalSeparator;
    FormatSettings.DecimalSeparator := '.';

    dateStr :=     FormatDateTime('YYYY-MM-DD hh:nn:ss.zzz', channelData.timeStamp);
    Form1.ADOQuery1.SQL.Clear;

    Form1.ADOQuery1.SQL.Text :=
      'INSERT INTO ChannelData (dateTime, channelNumber, level, density, volume, mass, temp, water, tempUp) VALUES  (''' +
      dateStr + ''', ' +
      IntToStr(channelData.channelNumber) + ', ' +
      FloatToStr(channelData.level) + ', ' +
      FloatToStr(channelData.density) + ', ' +
      FloatToStr(channelData.volume) + ', ' +
      FloatToStr(channelData.mass) + ', ' +
      FloatToStr(channelData.temp[0]) + ', ' +
      IntToStr(channelData.water) + ', ' +
      FloatToStr(channelData.tempUp) +
      ')';
    Form1.ADOQuery1.ExecSQL;
    FormatSettings.DecimalSeparator := Oldseparator;
end;

end.
