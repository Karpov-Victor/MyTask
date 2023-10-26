unit LogWriterUnit;

interface

uses Vcl.Dialogs, Sysutils;

procedure writeToLog(incomeString : String);

implementation

// https://space-base.ru/library/delphi/sozdanie-i-rabota-s-tekstovyimi-fajlami-v-delphi
procedure writeToLog(incomeString : String);
var NameFile:string;
    FileOut:TextFile;
    MdRes:byte;
begin
  NameFile := 'struna.log';
  try
    AssignFile(FileOut,NameFile);
    if NOT FileExists(NameFile)
     then Rewrite(FileOut) 
     else Append (FileOut);
    writeln(FileOut,FormatDateTime('hh:nn:ss.zzz', now) + ' ' +incomeString);
    CloseFile(FileOut);
  except
    MessageDlg('Ошибка сохранения протокола работы в файл!',mtError,[mbOk],0);
  end;

end;

end.
