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
    BtnGetData�hannel: TButton;
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
    procedure BtnGetData�hannelClick(Sender: TObject);
    procedure CheckBoxCyrcleClick(Sender: TObject);
    procedure GetData�hannelToMemo;
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

{ � ������ ������������ �������� ���������. �������� ��������� ���������
����� ������� � ���� ���������� �������� ����� ����� � �������-����������
������� �����. }
function AnswerParser(inArray : array of Byte):double;
var str : String;
begin
 // ����� ��������� 4 ���� �� �����= byte shr 4
 // ����� ������ 4 ���� �� ����� = byte and $0F  // ������� ����� 0..9
 // IntToHex(buffer[2],2) - ������������ ����� � ����� (2 �������)
//  showmessage(IntToHex(inArray[0], 2) + ' ' + IntToHex(inArray[1], 2) + ' ' + IntToHex(inArray[2], 2));

      str := '$' + IntToHex((inArray[2] shr 4), 2)  // ������� ������ ������ �����
           + IntToHex(inArray[1], 2)
           + IntToHex(inArray[0], 2);
       AnswerParser :=  StrToInt(str) + (inArray[2] and $0F) /10;
end;

{ ������ ����� � ������������
������� ��������� � 0.5 ��. ������ ���� �������� �������� ������� �������.
��������� ���� �������� ������� ����������� ��������. ��������� ������ �����:
1-7 �������� �������� �����������
8 ����: 1 � �������������, 0 � �������������.}
function AnswerTempParser(tempByte : Byte): double;
begin
 // ����� ��������� 4 ���� �� �����= byte shr 4
 // ����� ������ 4 ���� �� ����� = byte and $0F  // ������� ����� 0..9
 // IntToHex(buffer[2],2) - ������������ ����� � ����� (2 �������)
//  showmessage(IntToHex(inArray[0], 2) + ' ' + IntToHex(inArray[1], 2) + ' ' + IntToHex(inArray[2], 2));
AnswerTempParser := -274;
// ����������� ��������������� �����������
if (tempByte shr 7) = 1
  then
    AnswerTempParser := (tempByte and 127)/2 * (-1)
  else
    AnswerTempParser := (tempByte and 127)/2;

end;

// ���������� ����� � ������ ������, �� ������� ������ ������������� (����� ������ ����)
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




// ������� ������ �����
procedure clearBuffer();
  var i : integer;
begin
  for i := 0 to 255 do
  buffer[i] := 0;
end;

// �������� ���������� ������ � EditAnswer
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

// �������� ������� � ��������� ����
procedure sendData(hPort: THandle; command : integer);
begin
  buffer[0] := command;
  Form1.EditQuery.text := IntToHex(buffer[0], 2);
  WriteFile(hPort, buffer, 1, bytesWritten, nil);
end;

// ��������� ������ �� ���������� �����
// -1000000 - ������� �������� �� ��������
Function getData(hPort: THandle; delay : integer) : Byte;
var i, delayDelta : integer;
begin
  getData := 6; // ������ ����� �� ���������
  clearBuffer(); // ������� ����� ������
  // �������� ������
  // ��������� ���� �������� ������ �� �����
  i:= 0;
  delayDelta := Trunc(delay / 10);
  while i < delay do
  begin
    bytesRead := 0;
    ReadFile(hPort, buffer, SizeOf(buffer), bytesRead, nil);
    if (bytesRead > 0) and (endDataindex <> -1)
     then // ���� ������� �����-�� �����
      begin
      getData := buffer[0];
      i := delay; // ������� �� �����
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

// �������� ������ �� ������������ � ��� �����������
function checkAnswerMessage(answerResult : byte) : string;
begin


   checkAnswerMessage := '����������� ������';
   case answerResult of
            0: checkAnswerMessage := '�������� �����: ����� �����������';
            4: checkAnswerMessage := '�������� �����: ������������� ������ ��� ���������';
            6: checkAnswerMessage := '�������� �����: ������ ����� ��� �� ������� �����';
           12: checkAnswerMessage := '�������� �����: ������������ �������';
          238: checkAnswerMessage := '������������� (����� ������������� �������)';
          253: checkAnswerMessage := '����������� ����������� ������ �� ������������� ����������';
          255: checkAnswerMessage := '���������� ������ ��� ��������� � ������������ �������';
        end;
end;

// �������� ������ �� ������������ � ��� �����������
function checkConnect(answerResult : byte) : boolean;
var answerBytesCounter, i, checkSumm : byte;
begin
   // ��������� ������� ���� � ������. ���� ������ 3-��, �� ��������� ����������� �����
   answerBytesCounter := endDataindex() + 1;
   // showmessage('� ������ ' + inttostr(answerBytesCounter) + ' ����');
   // showmessage('���� �������� = ' + inttohex(buffer[answerBytesCounter - 1]));
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

// ������� �������� ����� � ������
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

// �������� ����� � �����������
Function checkConnectionFunction(hPort: THandle) : Boolean;
var answerResult : byte;
    message : String;
Begin
  // ���������� � ���� ������� �� �������� �����
  buffer[0] := StrunaConst.checkConnection;
  message := '����������� ������';
  // �������� �����
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if (answerResult = 0) or (answerResult = 85)
    then
      begin
        checkConnectionFunction := true;
        message := '����� � ���������� �����������';
      end
    else
    begin
        checkConnectionFunction := false;
        message := checkAnswerMessage(answerResult);
    end;
  ShowMessage(message);
End;

// ��������� ������ � ������������
Function check�onfigurationFunction(hPort: THandle) : String;
var
    message : String;
    chNumber, answerResult : byte;
Begin
  // ���������� � ���� ������� �� �������� �����
  buffer[0] := StrunaConst.configuration;
  message := '����������� ������';
  // �������� �����
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if checkConnect(answerResult)
    then
      begin
        message := '������� ���������� ����� � �������:';
       // �������� ����� ������ ��� �������
       chNumber := Form1.ComboBoxChNumber.itemIndex + 1;
       message := '������� ����� � ������� �� ������ �' + IntToStr(chNumber-1)
       + #13#10
       + '��������� ������ �������� - ' + Chr(48+(buffer[chNumber] shr 0) and 1)
       + #13#10
       + '��������� ����������� �������� - ' + Chr(48+(buffer[chNumber] shr 1) and 1)
       + #13#10
       + '������� ���������� �� ������ - ' + Chr(48+(buffer[chNumber] shr 2) and 1)
       + #13#10
       + '��������� ������ ����������� ���� - ' + Chr(48+(buffer[chNumber] shr 4) and 1)
       + #13#10
       + '��������� ��������� �������� - ' + Chr(48+(buffer[chNumber] shr 5) and 1)
       + #13#10
       + '������� ������ - ' + Chr(48+(buffer[chNumber] shr 7) and 1)
       + #13#10;
      end
    else
      begin
        message := checkAnswerMessage(answerResult);
      end;
  check�onfigurationFunction := message;
  ShowMessage(message);
End;

// ��������� ������ � ��� ��������� �� �����
Function checkChannelFunction(hPort: THandle; Channel : byte) : boolean;
var answerResult : byte;
Begin
  // ���������� � ���� ������� �� �������� �����
  buffer[0] := StrunaConst.configuration;
  // �������� �����
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


// ��������� ������ � ��������� ����� ��������������� (��)
Function check�onfigurationState(hPort: THandle) : Boolean;
var
    message : String;
    answerResult : byte;
Begin
  check�onfigurationState := false;
  // ���������� � ���� ������� �� �������� �����
  buffer[0] := StrunaConst.state;
  message := '����������� ������';
  // �������� �����
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if checkConnect(answerResult)
    then
      begin
        if (buffer[0] shr 7) = 1
        then
          Begin
           check�onfigurationState := true;
           message := '�� �����';
          End
         else
          Begin
           check�onfigurationState := false;
           message := '�� �� �����';
          End;
      end
    else
      begin
        message := checkAnswerMessage(answerResult);
      end;
  ShowMessage(message);
End;


// ��������� ������ �� ������
// hPort - ����, channel - �����, ���������� ��� ��� ���������
Function checkLevel(hPort: THandle; channel : byte; showAnswerMessage : Boolean) : double;
var dresult : double;
    message : String;
    answerResult : byte;
Begin
  message := '����������� ������';
  // ���������� � ���� ������� �� �������� �����
  buffer[0] := StrunaConst.level + channel;
  // �������� �����
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if checkConnect(answerResult)
    then
      begin
        dresult := AnswerParser(buffer);
        checkLevel := dresult;
        message := '�������� ������ �� ������ � ' + IntToStr(channel) + #13#10 + FloatToStr(dresult);
      end
    else
      begin
        checkLevel := -999999;
        message := checkAnswerMessage(answerResult);
      end;
  if showAnswerMessage then ShowMessage(message);
End;

// ��������� ������ � ���������
// hPort - ����, channel - �����, ���������� ��� ��� ���������
Function checkDensity(hPort: THandle; channel : byte; showAnswerMessage : Boolean) : double;
var
    dresult : double;
    message : String;
    answerResult : byte;
Begin
  message := '����������� ������';
  // ���������� � ���� ������� �� �������� �����
  buffer[0] := StrunaConst.density + channel;
  // �������� �����
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if checkConnect(answerResult)
    then
      begin
        dresult := AnswerParser(buffer);
        checkDensity := dresult;
        message := '�������� ��������� �� ������ � ' + IntToStr(channel) + #13#10 + FloatToStr(dresult);
      end
    else
      begin
        checkDensity := -999999;
        message := checkAnswerMessage(answerResult);
      end;
  if showAnswerMessage then ShowMessage(message);
End;

// ��������� ������ �� ��ڨ��
// hPort - ����, channel - �����, ���������� ��� ��� ���������
Function checkVolume(hPort: THandle; channel : byte; showAnswerMessage : Boolean) : double;
var
    dresult : double;
    message : String;
    answerResult : byte;
Begin
  message := '����������� ������';
  // ���������� � ���� ������� �� �������� �����
  buffer[0] := StrunaConst.volume + channel;
  // �������� �����
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if checkConnect(answerResult)
    then
      begin
        dresult := AnswerParser(buffer);
        checkVolume := dresult;
        message := '�������� ������ �� ������ � ' + IntToStr(channel) + #13#10 + FloatToStr(dresult);
      end
    else
      begin
        checkVolume := -999999;
        message := checkAnswerMessage(answerResult);
      end;
  if showAnswerMessage then ShowMessage(message);
End;

// ��������� ������ � �����
// hPort - ����, channel - �����, ���������� ��� ��� ���������
Function checkMass(hPort: THandle; channel : byte; showAnswerMessage : Boolean) : double;
var
    dresult : double;
    message : String;
    answerResult : byte;
Begin
  message := '����������� ������';
  // ���������� � ���� ������� �� �������� �����
  buffer[0] := StrunaConst.mass + channel;
  // �������� �����
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if checkConnect(answerResult)
    then
      begin
        dresult := AnswerParser(buffer);
        checkMass := dresult;
        message := '�������� ����� �� ������ � ' + IntToStr(channel) + #13#10 + FloatToStr(dresult);
      end
    else
      begin
        checkMass := -999999;
        message := checkAnswerMessage(answerResult);
      end;
  if showAnswerMessage then ShowMessage(message);
End;

// ��������� ������ � �����������
// hPort - ����, channel - �����, ���������� ��� ��� ���������
// sensorNumber - ����� ������� (0 - ������� ��������)
Function checkTemp(hPort: THandle; channel : byte; showAnswerMessage : Boolean; sensorNumber : Byte ) : double;
var
    message : String;
    answerResult : byte;
Begin

  message := '����������� ������';
  // ���������� � ���� ������� �� �������� �����
  buffer[0] := StrunaConst.temp + channel;
  // �������� �����
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
        message := '�������� ����������� �� ������ � ' + IntToStr(channel) +
         #13#10 + '������ � 1 =' + FloatToStr(AnswerTempParser(buffer[0])) +
         #13#10 + '������ � 2 =' + FloatToStr(AnswerTempParser(buffer[1])) +
         #13#10 + '������ � 3 =' + FloatToStr(AnswerTempParser(buffer[2])) +
         #13#10 + '������� �������� =' + FloatToStr(AnswerTempParser(buffer[3]));
      end
    else
      begin
        checkTemp := -999999;
        message := checkAnswerMessage(answerResult);
      end;
  if showAnswerMessage then ShowMessage(message);
End;

// ��������� ������ � ����������� ����
// hPort - ����, channel - �����, ���������� ��� ��� ���������
//
Function checkWater(hPort: THandle; channel : byte; showAnswerMessage : Boolean) : integer;
var
    message : String;
    answerResult : byte;
Begin

  message := '����������� ������';
  // ���������� � ���� ������� �� �������� �����
  buffer[0] := StrunaConst.water + channel;
  // �������� �����
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if checkConnect(answerResult)
    then
      begin
        message := '�������� ����������� ���� �� ������ � ' + IntToStr(channel) + inttoStr(buffer[0]);
        checkWater := buffer[0];
      end
    else
      begin
        checkWater := -999999;
        message := checkAnswerMessage(answerResult);
      end;
  if showAnswerMessage then ShowMessage(message);
End;

// ��������� ������ � ����������� � �������� �������
// hPort - ����, channel - �����, ���������� ��� ��� ���������
Function checkTempUp(hPort: THandle; channel : byte; showAnswerMessage : Boolean) : double;
var
    message : String;
    answerResult : byte;
Begin

  message := '����������� ������';
  // ���������� � ���� ������� �� �������� �����
  buffer[0] := StrunaConst.tempUp + channel;
  // �������� �����
  answerResult := sendAndGetData(hPort, buffer[0], 1000);
  if checkConnect(answerResult)
    then
      begin
        checkTempUp := AnswerTempParser(buffer[3]);
        message := '�������� ����������� �� ������ � ' + IntToStr(channel) +
         #13#10 + '������� ������ = ' + FloatToStr(AnswerTempParser(buffer[0]));
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
  LogWriterUnit.writeToLog(Sender.toString + ' ������ ������ �������/������� ����');
  if Button1.Caption = '������� ����'
   then
    Begin
    comPort := ComboBox2.Items.Strings[ComboBox2.ItemIndex]; // ������ COM-����, ������� �� ������ �������

    hPort := CreateFile(
      PChar('\\.\' + comPort), GENERIC_READ or GENERIC_WRITE,
      0, nil, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, 0);

    if hPort <> INVALID_HANDLE_VALUE
      then
        begin
          Label1.caption := comPort + ' ������';
          Button1.Caption := '������� ����';
          BtnSendCommand.Enabled := true;
          BtnSendAndGetData.Enabled := true;
          BtnGetData�hannel.Enabled := true;
          CheckBoxCyrcle.Enabled := true;
          LogWriterUnit.writeToLog(comPort + ' ������');
        end
      else
          begin
            // �� ������� ������� COM-����
            ShowMessage('�� ������� ������� COM-����: ' + comPort);
          end;
    End
   else
    Begin
      CloseHandle(hPort); // ��������� Com-����
      Label1.caption := 'COM-���� ������';
      Button1.Caption := '������� ����';
      BtnSendCommand.Enabled := false;
      BtnSendAndGetData.Enabled := false;
      BtnGetData�hannel.Enabled := false;
      CheckBoxCyrcle.Checked := false;
      CheckBoxCyrcle.Enabled := false;
      LogWriterUnit.writeToLog(comPort + ' ������');
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
  Form1.BtnGetData�hannel.Enabled := true;
end;

procedure TurnOffButtons();
begin
  Form1.BtnSendCommand.Enabled := false;
  Form1.BtnSendAndGetData.Enabled := false;
  Form1.BtnGetData�hannel.Enabled := false;
end;

procedure Tform1.GetData�hannelToMemo;
// https://programmersforum.ru/showthread.php?t=17303 - ������������
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

procedure TForm1.BtnGetData�hannelClick(Sender: TObject);
begin
  LogWriterUnit.writeToLog(Sender.ToString + ': ������ ������ "�������� ������ �� �������"');
  GetData�hannelToMemo;
end;

procedure TForm1.CheckBoxCyrcleClick(Sender: TObject);
var i :integer;
begin
{
  if CheckBoxCyrcle.Checked
    then
      begin
         LogWriterUnit.writeToLog(Sender.ToString + ' ����� ������ � ����� �������.');
         TurnOffButtons();
         writeToMemothread := TStrunaThread.Create(true); // <<true �������� ������ ������ ������
         writeToMemothread.FreeOnTerminate := true;       // <<��������� ������ ���� ������������ ����� ����������
         writeToMemothread.Priority:=tpNormal;            // <<���������� ��������� ������
         writeToMemothread.Resume;
      end
    else
      begin
        TurnOnButtons();
         LogWriterUnit.writeToLog(Sender.ToString + ' ����� ������ � ����� ��������.');
        // writeToMemothread.Terminate;
      end;
 }

  while Form1.CheckBoxCyrcle.Checked do
  begin

   Form1.GetData�hannelToMemo;
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
        showMessage('�� ������� ���������� ����� � ��.');
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
 LogWriterUnit.writeToLog(Sender.ToString + ': ������� �������: ' + combobox1.items.strings[combobox1.ItemIndex]);
end;

procedure TForm1.ComboBoxChNumberChange(Sender: TObject);
begin
 LogWriterUnit.writeToLog(Sender.ToString + ': ������ �����: ' + intTostr(ComboBoxChNumber.ItemIndex));
end;

procedure TForm1.BtnSendAndGetDataClick(Sender: TObject);
Var command : Byte;
begin
  command := StrToint('$' + EditQuery.Text);
  sendAndGetData(hPort, command, 1000);
end;

// ��������� ������� � ����, �������� � ������������ �����.
procedure TForm1.BtnSendCommandClick(Sender: TObject);
begin
  LogWriterUnit.writeToLog(Sender.ToString + ': ������ ����� ' + intTostr(ComboBoxChNumber.ItemIndex)
   + '. ��������� �������: ' + combobox1.items.strings[combobox1.ItemIndex]);


  case ComboBox1.ItemIndex of
   0 : checkConnectionFunction(hPort);
   1 : check�onfigurationFunction(hPort);
   2 : check�onfigurationState(hPort);
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
LogWriterUnit.writeToLog('������ ������ ���������.');


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
