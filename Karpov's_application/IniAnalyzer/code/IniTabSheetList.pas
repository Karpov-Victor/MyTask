unit IniTabSheetList;
// https://delphisources.ru/pages/faq/base/dynamic_pagecontrol_tabsheet2.html

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants, System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, Vcl.ComCtrls, Vcl.StdCtrls, IniTrio, System.IniFiles;

// Класс содержит список секций
type
  TIniTabSheetList = class(TObject)
      sectionsList : TStringList;
      public
      iniFileAddress : String;  // адрес ini-файла
      published

      Constructor Create(address: String); // Создание объекта класса
      function getSectionsList(address: String) : TStringList;

  end;


implementation

// Создание объекта
constructor TIniTabSheetList.Create(address : String);
var
  i : Integer;
begin
  inherited Create();                                                 // Сначала выполняется родительский конструктор
  self.iniFileAddress := address;                                     // Присваимваем полю переданный адрес
  self.sectionsList := getSectionsList(address);                      // Получаем в поле список всех секций
end;

// Функция получает список секций в TStringList
function TIniTabSheetList.getSectionsList(address: String) : TStringList;
Var
  ini: TIniFile;
  i : Integer;
  sectionsList : TStringList;
begin
  ini:=TiniFile.Create(address);      // Инициализируем ini-файл
  sectionsList := TStringList.Create; // Создаем список, в  который поместим список секций
  ini.ReadSections(sectionsList);     // Помещаем список секций в список

  // Не помещаем в список секций секцию с именем 'Global'
  if sectionsList.IndexOf('Global') <> -1 then sectionsList.Delete(sectionsList.IndexOf('Global'));
  Ini.Free;                           // Освобождаем ini-файл
  getSectionsList := sectionsList;    // Возвращаем результат работы функции
end;



end.
