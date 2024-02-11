unit IniTabSheetList;
// https://delphisources.ru/pages/faq/base/dynamic_pagecontrol_tabsheet2.html

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants, System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, Vcl.ComCtrls, Vcl.StdCtrls, IniTrio, System.IniFiles;

// ����� �������� ������ ������
type
  TIniTabSheetList = class(TObject)
      sectionsList : TStringList;
      public
      iniFileAddress : String;  // ����� ini-�����
      published

      Constructor Create(address: String); // �������� ������� ������
      function getSectionsList(address: String) : TStringList;

  end;


implementation

// �������� �������
constructor TIniTabSheetList.Create(address : String);
var
  i : Integer;
begin
  inherited Create();                                                 // ������� ����������� ������������ �����������
  self.iniFileAddress := address;                                     // ������������ ���� ���������� �����
  self.sectionsList := getSectionsList(address);                      // �������� � ���� ������ ���� ������
end;

// ������� �������� ������ ������ � TStringList
function TIniTabSheetList.getSectionsList(address: String) : TStringList;
Var
  ini: TIniFile;
  i : Integer;
  sectionsList : TStringList;
begin
  ini:=TiniFile.Create(address);      // �������������� ini-����
  sectionsList := TStringList.Create; // ������� ������, �  ������� �������� ������ ������
  ini.ReadSections(sectionsList);     // �������� ������ ������ � ������

  // �� �������� � ������ ������ ������ � ������ 'Global'
  if sectionsList.IndexOf('Global') <> -1 then sectionsList.Delete(sectionsList.IndexOf('Global'));
  Ini.Free;                           // ����������� ini-����
  getSectionsList := sectionsList;    // ���������� ��������� ������ �������
end;



end.
