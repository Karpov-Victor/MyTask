unit IniTabSheet;
// https://delphisources.ru/pages/faq/base/dynamic_pagecontrol_tabsheet2.html

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants, System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, Vcl.ComCtrls, Vcl.StdCtrls, IniTrio, System.IniFiles, StrUtils;

// ќписание одной закладки PageControl? €вл€етс€ наследником TTabSheet
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

// —оздание объекта
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
    // —начала выполн€етс€ родительский конструктор
    inherited Create(AOwner);

    self.sectionName := sectionName;
    // —оздаем лист, в котором будет хранитьс€ трио (им€ параметра, метка, текстовое поле)
    self.iniTrioArrayList := TList.Create;
    // »нициализируем ini-файл
    ini := TiniFile.Create(addressIniFile);
    iniModel := TiniFile.Create(addressIniFileModel);



    // создаем список список, в который отправим все параметры без описани€
    sectionParametersList := TStringList.Create;
    ini.ReadSection(sectionName, sectionParametersList);


    if CreateByParam //True - строить по названию параметра, False - строить по описанию параметра
    then
      Begin
      // ѕостроение списка по названию параметра (без _Description)
        for i := 0 to sectionParametersList.Count - 1 do
          Begin
          // получаем им€ параметра
          parameterName := sectionParametersList.Strings[i];
          iniParamDescription := iniModel.ReadString(sectionName, parameterName + description, '');
          if NOT AnsiEndsStr (description, parameterName)
            then  // ¬ итоговый список добавл€ем только те параметры, у которых нет описани€ (без _Description)
              Begin
                self.iniTrioArrayList.Add(TIniTrio.Create(parameterName, iniParamDescription, TLabel.Create(self), TEdit.Create(self), TLabel.Create(self))); // ‘ормируем “рио
              End
          End;
      End
    else
      Begin
      // ѕостроение списка по названию описанию параметра
        for i := 0 to sectionParametersList.Count - 1 do
          Begin
          // получаем им€ параметра
          parameterName := sectionParametersList.Strings[i];
          iniParamDescription := iniModel.ReadString(sectionName, parameterName, '');
          if AnsiEndsStr (description, parameterName)
            then  // ¬ итоговый список добавл€ем только те параметры, у которых есть описание (с _Description)
              Begin
                parameterName := parameterName.Substring(0, parameterName.Length - description.Length);
                self.iniTrioArrayList.Add(TIniTrio.Create(parameterName, iniParamDescription, TLabel.Create(self), TEdit.Create(self), TLabel.Create(self))); // ‘ормируем “рио
              End;
          End;
      End;

    // ќсвобождаем ini-файл
    Ini.Free;
    self.scrollBox := TscrollBox.Create(self);
end;

end.