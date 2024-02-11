unit IniTabSheet;
// https://delphisources.ru/pages/faq/base/dynamic_pagecontrol_tabsheet2.html

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants, System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, Vcl.ComCtrls, Vcl.StdCtrls, IniTrio, System.IniFiles, StrUtils;

// �������� ����� �������� PageControl? �������� ����������� TTabSheet
type
  TIniTabSheet = class(TTabSheet)
      iniTrioArrayList : TList;
      scrollBox : TScrollBox;
      public
      sectionName : String;
      published
      Constructor Create(AOwner: TComponent; sectionName : String; addressIniFile : String; addressIniFileModel : String; CreateByParam: Boolean);
      const
      description = '_Description';
  end;


implementation

// �������� �������
constructor TIniTabSheet.Create(AOwner: TComponent; sectionName : String;
            addressIniFile : String; addressIniFileModel : String; CreateByParam: Boolean);
Var
  ini, iniModel: TIniFile;
  i : Integer;
  sectionsList : TStringList;
  sectionParametersList : TStringList;
  parameterName : String;
  parameterNameDescription : String;
  iniParamDescription : String;
begin
    // ������� ����������� ������������ �����������
    inherited Create(AOwner);

    self.sectionName := sectionName;
    // ������� ����, � ������� ����� ��������� ���� (��� ���������, �����, ��������� ����)
    self.iniTrioArrayList := TList.Create;
    // �������������� ini-����
    ini := TiniFile.Create(addressIniFile);
    iniModel := TiniFile.Create(addressIniFileModel);



    // ������� ������ ������, � ������� �������� ��� ��������� ��� ��������
    sectionParametersList := TStringList.Create;
    ini.ReadSection(sectionName, sectionParametersList);


    if CreateByParam //True - ������� �� �������� ���������, False - ������� �� �������� ���������
    then
      Begin
      // ���������� ������ �� �������� ��������� (��� _Description)
        for i := 0 to sectionParametersList.Count - 1 do
          Begin
          // �������� ��� ���������
          parameterName := sectionParametersList.Strings[i];
          iniParamDescription := iniModel.ReadString(sectionName, parameterName + description, '');
          if NOT AnsiEndsStr (description, parameterName)
            then  // � �������� ������ ��������� ������ �� ���������, � ������� ��� �������� (��� _Description)
              Begin
                self.iniTrioArrayList.Add(TIniTrio.Create(parameterName, iniParamDescription, TLabel.Create(self), TEdit.Create(self), TLabel.Create(self))); // ��������� ����
              End
          End;
      End
    else
      Begin
      // ���������� ������ �� �������� �������� ���������
        for i := 0 to sectionParametersList.Count - 1 do
          Begin
          // �������� ��� ���������
          parameterName := sectionParametersList.Strings[i];
          iniParamDescription := iniModel.ReadString(sectionName, parameterName, '');
          if AnsiEndsStr (description, parameterName)
            then  // � �������� ������ ��������� ������ �� ���������, � ������� ���� �������� (� _Description)
              Begin
                parameterName := parameterName.Substring(0, parameterName.Length - description.Length);
                self.iniTrioArrayList.Add(TIniTrio.Create(parameterName, iniParamDescription, TLabel.Create(self), TEdit.Create(self), TLabel.Create(self))); // ��������� ����
              End;
          End;
      End;

    // ����������� ini-����
    Ini.Free;
    self.scrollBox := TscrollBox.Create(self);
end;

end.