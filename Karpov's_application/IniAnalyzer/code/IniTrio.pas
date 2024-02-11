unit IniTrio;

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants, System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, Vcl.ComCtrls, Vcl.StdCtrls;


type
  TIniTrio = class(TObject)
    public
    iniParamName : String;
    iniParamDescription : String;
    labl : TLabel;
    edit: TEdit;
    lablPic : TLabel;
    Constructor Create(iniParamName : String; iniParamDescription : String; labl : TLabel; edit: TEdit; lablPic : TLabel);
    Function myFunction() : String;
    published
  end;




implementation

// �������� �������
constructor TIniTrio.Create(iniParamName : String; iniParamDescription : String; labl : TLabel; edit: TEdit; lablPic : TLabel);
begin
  // ������� ����������� ������������ �����������
  inherited Create();  // ����� ������������� ������
  // ������ ��������� ���������� ���������
  self.iniParamName := iniParamName;
    self.iniParamDescription := iniParamDescription;
  self.labl := labl;
  self.edit := edit;
  self.lablPic := lablPic;
end;

Function TIniTrio.myFunction() : String;
Begin
  result := iniParamName;
End;

end.