unit ChannelData;
// http://www.delphibasics.ru/Constructor.php
// http://www.delphibasics.ru/Class.php

interface

uses classes, Sysutils;

type
  // Определение родительского класса основанного, по умолчанию, на TObject
  TChannelData = class(TObject)
  public
    channelNumber : byte;
    timeStamp : TDateTime;
    level : double;
    density : double;
    volume : double;
    mass : double;
    temp : array[0..3] of double;
    water: integer;
    tempUp: double;

//    Constructor Create; overload;   // Этот конструктор использует умолчания
    Constructor Create(channelNumber : byte; timeStamp : TDateTime);
    function dataToString(Sender: TChannelData) : string;
  end;


implementation
{ TStatistic }

// Создание объекта  - версия с параметрами
constructor TChannelData.Create(channelNumber : byte; timeStamp : TDateTime);
begin
  // Не cможет выполниться родительский конструктор - параметры отличаются
  // И сохраняем timeStamp
  self.timeStamp := timeStamp;
  self.channelNumber := channelNumber;
end;

function TChannelData.dataToString(Sender: TChannelData) : string;
begin
 dataToString :=
    FormatDateTime('hh:nn:ss.zzz', Sender.timeStamp)
  + ' | канал ' + IntToStr(Sender.channelNumber)
  + ' | ур ' + FloatToStr(Sender.level)
  + ' | об ' + FloatToStr(Sender.volume)
  + ' | мас ' + FloatToStr(Sender.mass)
  + ' | плот ' + FloatToStr(Sender.density)
  + ' | темп ' + FloatToStr(Sender.temp[0])
  + ' | ур воды ' + IntToStr(Sender.water)
  ;
end;


end.
