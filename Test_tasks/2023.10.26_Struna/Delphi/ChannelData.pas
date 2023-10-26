unit ChannelData;
// http://www.delphibasics.ru/Constructor.php
// http://www.delphibasics.ru/Class.php

interface

uses classes, Sysutils;

type
  // ����������� ������������� ������ �����������, �� ���������, �� TObject
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

//    Constructor Create; overload;   // ���� ����������� ���������� ���������
    Constructor Create(channelNumber : byte; timeStamp : TDateTime);
    function dataToString(Sender: TChannelData) : string;
  end;


implementation
{ TStatistic }

// �������� �������  - ������ � �����������
constructor TChannelData.Create(channelNumber : byte; timeStamp : TDateTime);
begin
  // �� c����� ����������� ������������ ����������� - ��������� ����������
  // � ��������� timeStamp
  self.timeStamp := timeStamp;
  self.channelNumber := channelNumber;
end;

function TChannelData.dataToString(Sender: TChannelData) : string;
begin
 dataToString :=
    FormatDateTime('hh:nn:ss.zzz', Sender.timeStamp)
  + ' | ����� ' + IntToStr(Sender.channelNumber)
  + ' | �� ' + FloatToStr(Sender.level)
  + ' | �� ' + FloatToStr(Sender.volume)
  + ' | ��� ' + FloatToStr(Sender.mass)
  + ' | ���� ' + FloatToStr(Sender.density)
  + ' | ���� ' + FloatToStr(Sender.temp[0])
  + ' | �� ���� ' + IntToStr(Sender.water)
  ;
end;


end.
