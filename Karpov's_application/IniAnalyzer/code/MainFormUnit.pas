unit MainFormUnit;

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants, System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, Vcl.ComCtrls, Vcl.StdCtrls, IniTrio, System.IniFiles, IniTabSheetList, IniTabSheet,
  Vcl.ExtCtrls, System.RegularExpressions;

type
  TMainForm = class(TForm)
    editIniAddress: TEdit;
    BtnSaveIni: TButton;
    btnReCompare: TButton;
    editIniModelAddress: TEdit;
    PageControlSection: TPageControl;
    OpenFileDialog: TOpenDialog;
    RadioGroup1: TRadioGroup;


    procedure onFormIniTabSheetCreate(Sender: TObject; iniTabSheet : TIniTabSheet);
    procedure FormCreate(Sender: TObject);
    procedure GetDataFromIniToTabSheet(tabSheet : TIniTabSheet);
    procedure DrawAllSections (addressIniFile : String; addressIniFileModel : String; CreateByParam: Boolean);
    procedure DrawAllSectionsAndGetData;
    procedure editIniAddressDblClick(Sender: TObject);
    procedure editIniModelAddressDblClick(Sender: TObject);
    procedure CompareDataInActiveTabSheet;
    procedure SaveEditToIniFile(address : String);
    procedure BtnSaveIniClick(Sender: TObject);
    procedure ClearPageControlSection;
    procedure Button1Click(Sender: TObject);
    function isRewriteAdnRefreshAllData : Boolean;
    procedure compareRange(iniTrio : TIniTrio; addressIniFileModel : string);
    procedure compareEquality(iniTrio : TIniTrio; addressIniFileModel : string);
    procedure PageControlSectionChange(Sender: TObject);
    procedure compareRegEx(iniTrio : TIniTrio; addressIniFileModel : string);
    procedure btnReCompareClick(Sender: TObject);
    procedure EditDblClick(Sender: TObject);
    function InputComboCreate(const ACaption, APrompt: string; const AList: TStrings): string;
    function getAvailableComPort: TStringList;
    procedure RadioGroup1Click(Sender: TObject);
    procedure FormMouseWheel(Sender: TObject; Shift: TShiftState;
      WheelDelta: Integer; MousePos: TPoint; var Handled: Boolean);

  private
    { Private declarations }
  public
    { Public declarations }
  end;

  const
  compare_equality = '#compare_equality#='; // Префикс перед параметром, который требует проверки на равенство
  compare_range = '#compare_range#=';  // Префикс перед параметром, который требует проверки на вхождение в диапазон
  compare_RegEx = '#compare_RegEx#=';  // Префикс перед параметром, который требует проверки с регулярным выражением
  getComPortStr = '#compare_RegEx#=(^COM\d+$)';     // Получить список портов в отдельном окне

var
  MainForm: TMainForm;
  LabelPicColor : TColor;
  activeIndexRadioGroup1 : Integer;

implementation

{$R *.dfm}

// Реакция на
procedure TMainForm.EditDblClick(Sender: TObject);
var
  choiceComPort: TMainForm;
  List: TStringList;
  tabSheet : TIniTabSheet;
  ini: Tinifile;
  paramModelVolume : String;
  getPort : String;
Begin
  Try
  ini := TiniFile.Create(editIniModelAddress.Text);
  tabSheet := TIniTabSheet(MainForm.PageControlSection.ActivePage);
  paramModelVolume := ini.ReadString(tabSheet.sectionName, TEdit(sender).Hint, '');
  if paramModelVolume.StartsWith(getComPortStr, true)
    then
    Begin
    getPort := InputComboCreate('Выбор Com-порта', 'Доступные порты', getAvailableComPort);
    if getPort.Length > 0 then TEdit(sender).Text := getPort;
    CompareDataInActiveTabSheet;
    End;
  Except
  End;
End;

// Получает список доступных com-портов
function TMainForm.getAvailableComPort: TStringList;
var
    i : Integer;
    hPort: THandle;
    comPort: string;
Begin
 result := TStringList.Create;
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
    result.Add('COM'+ IntToStr(i+1));
    CloseHandle(hPort);
    end;
   end;
End;


// Создает окно с разворачивающимся списком
function TMainForm.InputComboCreate(const ACaption, APrompt: string; const AList: TStrings): string;
  function GetCharSize(Canvas: TCanvas): TPoint;
  var
    I: Integer;
    Buffer: array[0..51] of Char;
  begin
    for I := 0 to 25 do Buffer[I] := Chr(I + Ord('A'));
    for I := 0 to 25 do Buffer[I + 26] := Chr(I + Ord('a'));
    GetTextExtentPoint(Canvas.Handle, Buffer, 52, TSize(Result));
    Result.X := Result.X div 52;
  end;
var
  Form: TForm;
  Prompt: TLabel;
  Combo: TComboBox;
  DialogUnits: TPoint;
  ButtonTop, ButtonWidth, ButtonHeight: Integer;
begin
  result := '';
  Form := TForm.Create(Application);
  with Form do
    try
    Canvas.Font := Font;
    DialogUnits := GetCharSize(Canvas);
    BorderStyle := bsDialog;
    Caption := ACaption;
    ClientWidth := MulDiv(180, DialogUnits.X, 4);
    Position := poScreenCenter;
    Prompt := TLabel.Create(Form);
  with Prompt do
    begin
    Parent := Form;
    Caption := APrompt;
    Left := MulDiv(8, DialogUnits.X, 4);
    Top := MulDiv(8, DialogUnits.Y, 8);
    Constraints.MaxWidth := MulDiv(164, DialogUnits.X, 4);
    WordWrap := True;
    end;
  Combo := TComboBox.Create(Form);
  with Combo do
    begin
    Parent := Form;
    Style := csDropDownList;
    //Style := csDropDown;
    Items.Assign(AList);
    ItemIndex := 0;
    Left := Prompt.Left;
    Top := Prompt.Top + Prompt.Height + 5;
    Width := MulDiv(164, DialogUnits.X, 4);
    end;
  ButtonTop := Combo.Top + Combo.Height + 15;
  ButtonWidth := MulDiv(50, DialogUnits.X, 4);
  ButtonHeight := MulDiv(14, DialogUnits.Y, 8);
  with TButton.Create(Form) do
  begin
    Parent := Form;
    Caption := 'Выбрать';
    ModalResult := mrOk;
    default := True;
    SetBounds(MulDiv(38, DialogUnits.X, 4), ButtonTop, ButtonWidth,
    ButtonHeight);
  end;
  with TButton.Create(Form) do
  begin
  Parent := Form;
  Caption := 'Отменить';
  ModalResult := mrCancel;
  Cancel := True;
  SetBounds(MulDiv(92, DialogUnits.X, 4), Combo.Top + Combo.Height + 15,
  ButtonWidth, ButtonHeight);
  Form.ClientHeight := Top + Height + 13;
  end;
  if ShowModal = mrOk then
  begin
    Result := Combo.Text;
  end;
finally
  Form.Free;
end;
end;


procedure TMainForm.FormCreate(Sender: TObject);
begin
// Ищем файлы ini и модели в папке, откуда запускается программа.
//  MainForm.editIniAddress.Text := ExtractFilePath(ParamStr(0)) + 'settings.ini';
  if FileExists(ExtractFilePath(ParamStr(0)) + 'settings.ini')
    then MainForm.editIniAddress.Text := ExtractFilePath(ParamStr(0)) + 'settings.ini'
    else
      Begin
      MessageDlg('Не обнаружен ini-файл.' + #13#10 + 'Выберите ini-файл', mtWarning, [mbOk], 0);
      editIniAddressDblClick(Sender);
      End;

  if FileExists(ExtractFilePath(ParamStr(0)) + 'settings_model.ini')
    then MainForm.editIniModelAddress.Text := ExtractFilePath(ParamStr(0)) + 'settings_model.ini'
    else
      Begin
      MessageDlg('Не обнаружена модель.' + #13#10 + 'Выберите модель.', mtWarning, [mbOk], 0);
      editIniModelAddressDblClick(Sender);
      if NOT FileExists(MainForm.editIniModelAddress.Text)
        then MainForm.editIniModelAddress.Text := MainForm.editIniAddress.Text;
      End;

  if FileExists(MainForm.editIniAddress.Text)
    then
      Begin
      // Отрисовываем параметры и получаем в них значения
      DrawAllSectionsAndGetData;
      activeIndexRadioGroup1 := RadioGroup1.ItemIndex;
      End;
end;

procedure TMainForm.FormMouseWheel(Sender: TObject; Shift: TShiftState;
  WheelDelta: Integer; MousePos: TPoint; var Handled: Boolean);
  var tabSheet : TIniTabSheet;
begin
tabSheet := TIniTabSheet(MainForm.PageControlSection.ActivePage);
// showMessage(InttoStr(tabSheet.scrollBox.VertScrollBar.Position));
tabSheet.scrollBox.VertScrollBar.Position := tabSheet.scrollBox.VertScrollBar.Position - WheelDelta;
end;

// Отрисовывает все параметры и получает в них знаяения
procedure TMainForm.DrawAllSectionsAndGetData;
var
  i : Integer;
Begin
  ClearPageControlSection;
  // Отрисовываем все закладки и вместе с названиями параметров в них
  Case RadioGroup1.ItemIndex of
  0 : if FileExists(MainForm.editIniModelAddress.Text)
    then DrawAllSections(MainForm.editIniModelAddress.Text, MainForm.editIniModelAddress.Text, false)
    else exit;
  1 : if FileExists(MainForm.editIniModelAddress.Text)
    then DrawAllSections(MainForm.editIniModelAddress.Text, MainForm.editIniModelAddress.Text, true)
    else exit;
  2 : if FileExists(MainForm.editIniAddress.Text)
    then DrawAllSections(MainForm.editIniAddress.Text, MainForm.editIniModelAddress.Text, true)
    else exit;
  End;

  PageControlSection.visible := false;
  // Получам данные из ini-файла
  for i := 0 to PageControlSection.PageCount -1 do
  begin
    GetDataFromIniToTabSheet(TIniTabsheet(PageControlSection.Pages[i]));
  end;
  // Сравниваем значения с моделью
  CompareDataInActiveTabSheet;
  PageControlSection.visible := true;
End;

// Отрисовываем на форме в Page control-е все закладки
procedure TMainForm.DrawAllSections (addressIniFile : String; addressIniFileModel : String; CreateByParam: Boolean);
var
  iniTabSheetList : TIniTabsheetList;
  iniTabSheet : TIniTabSheet;
  i : Integer;
  sectionName : String;
  ini: Tinifile;
begin
  // Создаем лист с параметрами
  iniTabSheetList := TIniTabsheetList.Create(addressIniFile);

  for i := 0 to iniTabSheetList.sectionsList.Count - 1 do
  Begin
    sectionName := iniTabSheetList.sectionsList.Strings[i];
    //  строим по названию параметра или по описанию
    // (addressIniFile - откуда берем параметры, addressIniFileModel - откуда берем описание)
//    if ((RBtn_createByParam.Checked) OR (RBtn_createByIniFile.Checked))
//      then iniTabSheet := TIniTabsheet.Create(self, sectionName, addressIniFile, addressIniFileModel, CreateByParam)
//      else iniTabSheet := TIniTabsheet.Create(self, sectionName, addressIniFile, addressIniFileModel, CreateByParam);
    iniTabSheet := TIniTabsheet.Create(self, sectionName, addressIniFile, addressIniFileModel, CreateByParam);
    MainForm.onFormIniTabSheetCreate(self, iniTabSheet);
  End;

end;

// Получить все данные параметров в уже созданную закладку
procedure TMainForm.GetDataFromIniToTabSheet(tabSheet : TIniTabSheet);
var
  ini: Tinifile;
  paramValue : String;
  paramName : String;
  iniTrio : TIniTrio;
  edit : TEdit;
  i : Integer;
begin
  //создали файл в директории программы
  ini:=TiniFile.Create(MainForm.editIniAddress.Text);
  for i := 0 to tabSheet.iniTrioArrayList.Count - 1 do
    Begin
      iniTrio := tabSheet.iniTrioArrayList[i];
      edit := iniTrio.edit;
      paramValue := ini.ReadString(tabSheet.sectionName, iniTrio.iniParamName, '');
      edit.Text := paramValue;
    End;
  Ini.Free;
end;


// На форме есть PageControl. Динамически в рантайме на нём создаются закладки. На закладках динамичеки создаётся ScroolBox
// На ScroolBox динамически на основании данных файла с моделью (ini-файла) создаются метки, эдиты и т.д.
procedure TMainForm.onFormIniTabSheetCreate(Sender: TObject; iniTabSheet : TIniTabSheet);
var
    iniTrio : TIniTrio;
    labl : TLabel;
    edit : TEdit;
    lablText : String;
    lablPic : TLabel;
    i : Integer;
//    ini: Tinifile;
    iniParamDescription : String;
begin
    iniTabSheet.Caption := iniTabSheet.sectionName;
    iniTabSheet.PageControl := PageControlSection;
    LabelPicColor := PageControlSection.Canvas.Brush.Color;

//    ini:=TiniFile.Create(MainForm.editIniModelAddress.Text);

    iniTabSheet.scrollBox.Parent :=  iniTabSheet;
    iniTabSheet.scrollBox.Visible := true;
    iniTabSheet.scrollBox.Width := iniTabSheet.Width;
    iniTabSheet.scrollBox.Height := iniTabSheet.Height - 20;
    iniTabSheet.scrollBox.Top := 0;
    iniTabSheet.scrollBox.Left := 0;

    PageControlSection.visible := false;
    for i := 0 to iniTabSheet.iniTrioArrayList.Count - 1 do
    Begin
      iniTrio := iniTabSheet.iniTrioArrayList[i];
      lablText := iniTrio.iniParamName + ' = ';
      iniParamDescription := iniTrio.iniParamDescription;
      labl := iniTrio.labl;
      edit := iniTrio.edit;
      lablPic := iniTrio.lablPic;

      labl.Parent := iniTabSheet.scrollBox;
      labl.Visible := true;
      labl.Font.Size := 10;
      labl.Font.Style := [fsBold];
      labl.Caption := lablText;
      labl.Left := 10;
      labl.Top := i*30 + 10;
      labl.Width := 280;
      labl.Alignment := taRightJustify;
      labl.ShowHint := True;
      if NOT iniParamDescription.Equals('') then labl.Hint := iniParamDescription;

      edit.Parent := iniTabSheet;
      edit.Parent := iniTabSheet.scrollBox;
      edit.Font.Size := 10;
//    edit.Font.Style := [fsBold];
      edit.Text := 'Адрес ini-файла';
      edit.Left := labl.Left + labl.Width + 5;
      edit.Top := labl.Top-3;
      edit.Width := 600;
      edit.OnDblClick := EditDblClick;
      if NOT iniParamDescription.Equals('') then edit.textHint := iniParamDescription;
      edit.Hint := iniTrio.iniParamName;


      lablPic.Parent := iniTabSheet;
      lablPic.Parent := iniTabSheet.scrollBox;
      lablPic.Font.Size := 28;
      lablPic.Font.Style := [fsBold];
      lablPic.Caption := '●';
      lablPic.Left := edit.Left + edit.Width + 10;
      lablPic.Top := edit.Top - 15;
      lablPic.Width := 20;
      lablPic.Font.Color := LabelPicColor;
      lablPic.Alignment := taRightJustify;
    End;
    PageControlSection.visible := true;
end;



procedure TMainForm.PageControlSectionChange(Sender: TObject);
begin
CompareDataInActiveTabSheet;
end;

procedure TMainForm.RadioGroup1Click(Sender: TObject);
begin
if RadioGroup1.ItemIndex = activeIndexRadioGroup1 then exit;
if isRewriteAdnRefreshAllData
  then
    Begin
      ClearPageControlSection;
      DrawAllSectionsAndGetData;
      activeIndexRadioGroup1 := RadioGroup1.ItemIndex;
    End
  else
    Begin
    RadioGroup1.ItemIndex := activeIndexRadioGroup1;
    End;
end;

function TMainForm.isRewriteAdnRefreshAllData : Boolean;
Var
messageResult : TModalResult;
Begin
  messageResult := MessageDlg('Внимание!' + #13#10 + 'Данные по всем позициям будут обновлены.',mtWarning, mbOKCancel, 0);
  if messageResult = mrOk
    then result := true
    else result := false;
End;


procedure TMainForm.editIniAddressDblClick(Sender: TObject);
begin
if openFileDialog.Execute
  then
  Begin
  MainForm.editIniAddress.text := openFileDialog.FileName;
  RadioGroup1.itemIndex := 2;
  End;

end;

procedure TMainForm.editIniModelAddressDblClick(Sender: TObject);
begin
if openFileDialog.Execute
  then
    Begin
      MainForm.editIniModelAddress.text := openFileDialog.FileName;
      CompareDataInActiveTabSheet;
    End;
end;

procedure TMainForm.btnReCompareClick(Sender: TObject);
begin
if FileExists(MainForm.editIniModelAddress.Text)
  then CompareDataInActiveTabSheet;
end;

procedure TMainForm.BtnSaveIniClick(Sender: TObject);
begin
  SaveEditToIniFile(MainForm.editIniAddress.Text);
end;


procedure TMainForm.CompareDataInActiveTabSheet;
var
  i : integer;
  tabSheet : TIniTabSheet;
  iniTrio : TIniTrio;
  ini : TIniFile;
  paramModelValue : String;
begin
  tabSheet := TIniTabSheet(MainForm.PageControlSection.ActivePage);
  ini := TiniFile.Create(MainForm.editIniModelAddress.Text);

  for i := 0 to tabSheet.iniTrioArrayList.Count - 1 do
  Begin
    iniTrio := tabSheet.iniTrioArrayList[i];
    iniTrio.lablPic.Font.Color := PageControlSection.Canvas.Brush.Color;
    paramModelValue := ini.ReadString(tabSheet.sectionName, iniTrio.iniParamName, '');
    if paramModelValue.StartsWith(compare_equality, true) then compareEquality(iniTrio, editIniModelAddress.Text);
    if paramModelValue.StartsWith(compare_range, true) then compareRange(iniTrio, editIniModelAddress.Text);
    if paramModelValue.StartsWith(compare_RegEx, true) then compareRegEx(iniTrio, editIniModelAddress.Text);
  End;
end;



procedure TMainForm.SaveEditToIniFile(address : String);
var
  tabSheet : TIniTabSheet;
  iniTrio : TIniTrio;
  i, j : Integer;
  ini: TIniFile;
Begin
  ini:=TiniFile.Create(address);

  // Проход по всем вкладкам
  for i := 0 to MainForm.PageControlSection.PageCount - 1 do
  Begin
    tabSheet := TIniTabSheet(MainForm.PageControlSection.Pages[i]);
    // Проход по всем праметрам вкладки
    for j := 0 to tabSheet.iniTrioArrayList.Count - 1 do
    Begin
      iniTrio := tabSheet.iniTrioArrayList[j];
      ini.WriteString(tabSheet.sectionName, iniTrio.iniParamName, ' ' + iniTrio.edit.text);
    End;
  End;
  Ini.Free;
End;

procedure TMainForm.Button1Click(Sender: TObject);
begin
ClearPageControlSection;
DrawAllSectionsAndGetData;
end;


// Очистка PageControlSection
// Удаление всех компонентов tabSheet и самого Tabsheet
procedure TMainForm.ClearPageControlSection;
var
i, j : Integer;
tabSheet : TIniTabSheet;
iniTrio : TIniTrio;
Begin
  PageControlSection.visible := false;
  // Проход по всем вкладкам
  for i := MainForm.PageControlSection.PageCount - 1 downto 0 do
  Begin
    tabSheet := TIniTabSheet(MainForm.PageControlSection.Pages[i]);
    // Проход по всем праметрам вкладки
    for j := 0 to tabSheet.iniTrioArrayList.Count - 1 do
    Begin
      iniTrio := tabSheet.iniTrioArrayList[j];
      iniTrio.labl.Free;
      iniTrio.edit.Free;
      iniTrio.lablPic.Free;
    End;
    tabSheet.Free;
  End;
  PageControlSection.visible := true;
End;


//Сравнивает данные по диапозону
procedure TMainForm.CompareRange(iniTrio : TIniTrio; addressIniFileModel : string);
var
  tabSheet : TIniTabSheet;
  ini: Tinifile;
  range : String;
  rangeFrom : integer;
  rangeTo : integer;
Begin
  Try
  ini:=TiniFile.Create(addressIniFileModel);
  tabSheet := TIniTabSheet(MainForm.PageControlSection.ActivePage);
  range := ini.ReadString(tabSheet.sectionName, iniTrio.iniParamName, '');
  range := Copy(range, compare_range.Length + 1, range.Length);
  rangeFrom := StrToInt(Copy(range, 0, pos('-', range)-1));
  rangeTo := StrToInt(Copy(range, pos('-', range) + 1, 50));
  if ((StrToInt(iniTrio.edit.Text) >= rangeFrom) and (StrToInt(iniTrio.edit.Text) <= rangeTo))
    then
      Begin
      iniTrio.lablPic.Font.Color := clGreen;
      exit;
      End
    else
    Begin
      iniTrio.lablPic.Font.Color := clRed;
      exit;
    End;
  Except
  End;
  iniTrio.lablPic.Font.Color := clYellow;
End;

// Сравнение значений
procedure TMainForm.compareEquality(iniTrio : TIniTrio; addressIniFileModel : string);
var
  tabSheet : TIniTabSheet;
  ini: Tinifile;
  paramModelVolume : String;
Begin
  Try
  ini := TiniFile.Create(addressIniFileModel);
  tabSheet := TIniTabSheet(MainForm.PageControlSection.ActivePage);
  paramModelVolume := ini.ReadString(tabSheet.sectionName, iniTrio.iniParamName, '');
  paramModelVolume := Copy(paramModelVolume, compare_equality.Length + 1, paramModelVolume.Length);
  if paramModelVolume.Equals(iniTrio.edit.Text)
    then
      Begin
      iniTrio.lablPic.Font.Color := clGreen;
      exit;
      End
    else
    Begin
      iniTrio.lablPic.Font.Color := clRed;
      exit;
    End;
  Except
  End;
  iniTrio.lablPic.Font.Color := clYellow;
End;

// Сравнение через регулярные выражения
procedure TMainForm.compareRegEx(iniTrio : TIniTrio; addressIniFileModel : string);
var
  tabSheet : TIniTabSheet;
  ini: Tinifile;
  paramModelVolume : String;
  var RegEx: TRegEx;
Begin
  Try
  ini := TiniFile.Create(addressIniFileModel);
  tabSheet := TIniTabSheet(MainForm.PageControlSection.ActivePage);
  paramModelVolume := ini.ReadString(tabSheet.sectionName, iniTrio.iniParamName, '');
  paramModelVolume := Copy(paramModelVolume, compare_range.Length + 1, paramModelVolume.Length);
  if RegEx.IsMatch (iniTrio.edit.Text, paramModelVolume)
    then
      Begin
      iniTrio.lablPic.Font.Color := clGreen;
      exit;
      End
    else
      Begin
      iniTrio.lablPic.Font.Color := clRed;
      exit;
      End;
  Except
  End;
  iniTrio.lablPic.Font.Color := clYellow;
End;


end.
