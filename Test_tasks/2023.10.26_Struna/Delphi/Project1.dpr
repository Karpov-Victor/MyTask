program Project1;

uses
  Vcl.Forms,
  Unit1 in 'Unit1.pas' {Form1},
  StrunaConst in 'StrunaConst.pas',
  ThreadUnit in 'ThreadUnit.pas',
  LogWriterUnit in 'LogWriterUnit.pas';

{$R *.res}

begin
  Application.Initialize;
  Application.MainFormOnTaskbar := True;
  Application.CreateForm(TForm1, Form1);
  Application.Run;
end.
