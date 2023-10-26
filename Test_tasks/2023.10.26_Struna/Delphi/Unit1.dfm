object Form1: TForm1
  Left = 0
  Top = 0
  BorderStyle = bsSingle
  Caption = 'Struna (ver. 1.4)'
  ClientHeight = 545
  ClientWidth = 531
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'Tahoma'
  Font.Style = []
  OldCreateOrder = False
  OnClose = FormClose
  OnCreate = FormCreate
  PixelsPerInch = 96
  TextHeight = 13
  object Label1: TLabel
    Left = 8
    Top = 521
    Width = 104
    Height = 16
    Caption = 'COM-'#1087#1086#1088#1090' '#1079#1072#1082#1088#1099#1090
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -13
    Font.Name = 'Tahoma'
    Font.Style = []
    ParentFont = False
  end
  object Button1: TButton
    Left = 103
    Top = 6
    Width = 139
    Height = 25
    Caption = #1054#1090#1082#1088#1099#1090#1100' '#1087#1086#1088#1090
    TabOrder = 0
    OnClick = Button1Click
  end
  object BtnSendAndGetData: TButton
    Left = 319
    Top = 463
    Width = 204
    Height = 25
    Caption = #1054#1090#1087#1088#1072#1074#1080#1090#1100' '#1080' '#1087#1086#1083#1091#1095#1080#1090#1100' '#1076#1072#1085#1085#1099#1077
    Enabled = False
    TabOrder = 1
    OnClick = BtnSendAndGetDataClick
  end
  object EditQuery: TEdit
    Left = 8
    Top = 467
    Width = 305
    Height = 21
    TabOrder = 2
  end
  object BtnSendCommand: TButton
    Left = 343
    Top = 39
    Width = 180
    Height = 25
    Caption = #1054#1090#1087#1088#1072#1074#1080#1090#1100' '#1082#1086#1084#1072#1085#1076#1091
    Enabled = False
    TabOrder = 3
    OnClick = BtnSendCommandClick
  end
  object ComboBox1: TComboBox
    Left = 8
    Top = 40
    Width = 234
    Height = 21
    Style = csDropDownList
    DropDownCount = 20
    TabOrder = 4
    OnChange = ComboBox1Change
  end
  object ComboBox2: TComboBox
    Left = 8
    Top = 8
    Width = 89
    Height = 21
    Style = csDropDownList
    TabOrder = 5
  end
  object ComboBoxChNumber: TComboBox
    Left = 248
    Top = 40
    Width = 89
    Height = 21
    Style = csDropDownList
    TabOrder = 6
    OnChange = ComboBoxChNumberChange
    Items.Strings = (
      #1050#1072#1085#1072#1083' '#8470' 0'
      #1050#1072#1085#1072#1083' '#8470' 1'
      #1050#1072#1085#1072#1083' '#8470' 2'
      #1050#1072#1085#1072#1083' '#8470' 3'
      #1050#1072#1085#1072#1083' '#8470' 4'
      #1050#1072#1085#1072#1083' '#8470' 5'
      #1050#1072#1085#1072#1083' '#8470' 6'
      #1050#1072#1085#1072#1083' '#8470' 7'
      #1050#1072#1085#1072#1083' '#8470' 8'
      #1050#1072#1085#1072#1083' '#8470' 9'
      #1050#1072#1085#1072#1083' '#8470' 10'
      #1050#1072#1085#1072#1083' '#8470' 11'
      #1050#1072#1085#1072#1083' '#8470' 12'
      #1050#1072#1085#1072#1083' '#8470' 13'
      #1050#1072#1085#1072#1083' '#8470' 14'
      #1050#1072#1085#1072#1083' '#8470' 15')
  end
  object EditAnswer: TEdit
    Left = 8
    Top = 494
    Width = 515
    Height = 21
    TabOrder = 7
  end
  object BtnGetDataСhannel: TButton
    Left = 343
    Top = 8
    Width = 180
    Height = 25
    Caption = #1055#1086#1083#1091#1095#1080#1090#1100' '#1076#1072#1085#1085#1099#1077' '#1087#1086' '#1082#1072#1085#1072#1083#1072#1084
    Enabled = False
    TabOrder = 8
    OnClick = BtnGetDataСhannelClick
  end
  object Memo1: TMemo
    Left = 8
    Top = 70
    Width = 515
    Height = 363
    ReadOnly = True
    ScrollBars = ssBoth
    TabOrder = 9
  end
  object CheckBoxCyrcle: TCheckBox
    Left = 248
    Top = 8
    Width = 89
    Height = 25
    Caption = #1054#1087#1088#1086#1089' '#1074' '#1094#1080#1082#1083#1077
    Enabled = False
    TabOrder = 10
    OnClick = CheckBoxCyrcleClick
  end
  object CheckBoxWriteToDB: TCheckBox
    Left = 8
    Top = 440
    Width = 145
    Height = 17
    Caption = #1057#1086#1093#1088#1072#1085#1103#1090#1100' '#1076#1072#1085#1085#1099#1077' '#1074' '#1041#1044
    ParentShowHint = False
    ShowHint = False
    TabOrder = 11
    OnClick = CheckBoxWriteToDBClick
  end
  object EditConnectionString: TEdit
    Left = 159
    Top = 440
    Width = 364
    Height = 21
    Hint = #1057#1090#1088#1086#1082#1072' '#1089#1086#1077#1076#1080#1085#1077#1085#1080#1103' '#1089' '#1041#1044
    TabOrder = 12
    Text = 
      'Provider=SAOLEDB.16;Password=XXX;Persist Security Info=True;User' +
      ' ID=XXX;Data Source=StationODBC'
  end
  object ADOConnection1: TADOConnection
    ConnectionString = 
      'Provider=SAOLEDB.16;Password=ctosqlpass;Persist Security Info=Tr' +
      'ue;User ID=dba;Data Source=StationODBC'
    Provider = 'SAOLEDB.16'
    Left = 184
    Top = 520
  end
  object ADOQuery1: TADOQuery
    Connection = ADOConnection1
    Parameters = <>
    Left = 216
    Top = 520
  end
end
