unit Model;

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants, System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, Vcl.ComCtrls, Vcl.StdCtrls, System.IniFiles;


type
  TModel = class(TObject)
    public
    absoluteIniFilePath : String;
    absoluteIniFileModelPath : String;
    iniFile : TiniFile;               // ������� ini-����
    iniFileModel : TiniFile;          // Ini - ����-������ � ��������� �����. �������� ��������� ��������� �� ��, ����� �� �������� ������ ���� ��������. �������� ��������� + _description �������� �������� ���������, ������� ��������������� � hint
    Constructor Create();
    Procedure

    published
  end;




implementation

// �������� �������
constructor TModel.Create();
begin
  // ������� ����������� ������������ �����������
  inherited Create();  // ����� ������������� ������
  // ������ ��������� ���������� ���������
end;

Procedure refreshIni(absoluteIniFilePath : String);
Begin
//  iniFile := TiniFile.Create(absoluteIniFilePath);
End;


end.