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
    iniFile : TiniFile;               // Главный ini-файл
    iniFileModel : TiniFile;          // Ini - файл-модель с описанием полей. Значение параметра указывает на то, каким по значению должен быть параметр. Значение параметра + _description содержит описание параметра, которое устанавливается в hint
    Constructor Create();
    Procedure

    published
  end;




implementation

// Создание объекта
constructor TModel.Create();
begin
  // Сначала выполняется родительский конструктор
  inherited Create();  // Вызов родительского метода
  // Теперь сохраняем переданный параметры
end;

Procedure refreshIni(absoluteIniFilePath : String);
Begin
//  iniFile := TiniFile.Create(absoluteIniFilePath);
End;


end.