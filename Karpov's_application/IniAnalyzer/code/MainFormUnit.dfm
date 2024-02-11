object MainForm: TMainForm
  Left = 0
  Top = 0
  BorderStyle = bsToolWindow
  Caption = #1040#1085#1072#1083#1080#1079#1072#1090#1086#1088' ini-'#1092#1072#1081#1083#1072
  ClientHeight = 809
  ClientWidth = 983
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'Tahoma'
  Font.Style = []
  OldCreateOrder = False
  OnCreate = FormCreate
  OnMouseWheel = FormMouseWheel
  PixelsPerInch = 96
  TextHeight = 13
  object editIniAddress: TEdit
    Left = 144
    Top = 17
    Width = 490
    Height = 24
    Hint = #1055#1086#1083#1085#1099#1081' '#1072#1076#1088#1077#1089' '#1082' ini-'#1092#1072#1081#1083#1091' ('#1076#1074#1086#1081#1085#1086#1081' '#1082#1083#1080#1082' '#1076#1083#1103' '#1074#1099#1073#1086#1088#1072' '#1092#1072#1081#1083#1072')'
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -13
    Font.Name = 'Tahoma'
    Font.Style = []
    ParentFont = False
    ParentShowHint = False
    ShowHint = True
    TabOrder = 0
    TextHint = #1055#1086#1083#1085#1099#1081' '#1072#1076#1088#1077#1089' '#1082' ini-'#1092#1072#1081#1083#1091' ('#1076#1074#1086#1081#1085#1086#1081' '#1082#1083#1080#1082' '#1076#1083#1103' '#1074#1099#1073#1086#1088#1072' '#1092#1072#1081#1083#1072')'
    OnDblClick = editIniAddressDblClick
  end
  object BtnSaveIni: TButton
    Left = 8
    Top = 18
    Width = 130
    Height = 25
    Caption = '&'#1057#1086#1093#1088#1072#1085#1080#1090#1100' Ini-'#1092#1072#1081#1083
    TabOrder = 1
    OnClick = BtnSaveIniClick
  end
  object btnReCompare: TButton
    Left = 8
    Top = 50
    Width = 130
    Height = 25
    Caption = '&'#1055#1077#1088#1077#1087#1088#1086#1074#1077#1088#1080#1090#1100
    TabOrder = 2
    OnClick = btnReCompareClick
  end
  object editIniModelAddress: TEdit
    Left = 144
    Top = 49
    Width = 490
    Height = 24
    Hint = #1055#1086#1083#1085#1099#1081' '#1072#1076#1088#1077#1089' '#1082' '#1092#1072#1081#1083#1091' - '#1084#1086#1076#1077#1083#1080' ('#1076#1074#1086#1081#1085#1086#1081' '#1082#1083#1080#1082' '#1076#1083#1103' '#1074#1099#1073#1086#1088#1072' '#1092#1072#1081#1083#1072')'
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -13
    Font.Name = 'Tahoma'
    Font.Style = []
    ParentFont = False
    ParentShowHint = False
    ShowHint = True
    TabOrder = 3
    TextHint = #1055#1086#1083#1085#1099#1081' '#1072#1076#1088#1077#1089' '#1082' '#1092#1072#1081#1083#1091' - '#1084#1086#1076#1077#1083#1080' ('#1076#1074#1086#1081#1085#1086#1081' '#1082#1083#1080#1082' '#1076#1083#1103' '#1074#1099#1073#1086#1088#1072' '#1092#1072#1081#1083#1072')'
    OnDblClick = editIniModelAddressDblClick
  end
  object PageControlSection: TPageControl
    Left = 8
    Top = 87
    Width = 969
    Height = 700
    MultiLine = True
    TabOrder = 4
    OnChange = PageControlSectionChange
  end
  object RadioGroup1: TRadioGroup
    Left = 640
    Top = 8
    Width = 335
    Height = 73
    Caption = #1055#1086#1089#1090#1088#1086#1077#1085#1080#1077' '#1089#1087#1080#1089#1082#1072' '#1087#1072#1088#1072#1084#1077#1090#1088#1086#1074
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'Tahoma'
    Font.Style = []
    ItemIndex = 0
    Items.Strings = (
      #1042#1079#1103#1090#1100' '#1076#1072#1085#1085#1099#1077' '#1087#1086' '#1087#1072#1088#1072#1084#1077#1090#1088#1072#1084' '#1080#1079' '#1086#1087#1080#1089#1072#1085#1080#1103' '#1074' '#1084#1086#1076#1077#1083#1080
      #1042#1079#1103#1090#1100' '#1076#1072#1085#1085#1099#1077' '#1087#1086' '#1087#1072#1088#1072#1084#1077#1090#1088#1072#1084' '#1080#1079' '#1084#1086#1076#1077#1083#1080
      #1042#1079#1103#1090#1100' '#1076#1072#1085#1085#1099#1077' '#1087#1086' '#1087#1072#1088#1072#1084#1077#1090#1088#1072#1084' '#1080#1079' ini-'#1092#1072#1081#1083#1072)
    ParentFont = False
    TabOrder = 5
    OnClick = RadioGroup1Click
  end
  object OpenFileDialog: TOpenDialog
    Filter = '*.ini|*.ini|'#1042#1089#1077' '#1092#1072#1081#1083#1099'|*.*'
    Left = 920
    Top = 40
  end
end
