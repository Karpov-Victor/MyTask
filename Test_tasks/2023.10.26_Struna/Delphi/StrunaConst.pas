unit StrunaConst;

interface
const
checkConnection:  Byte = $10;  // ��������� �����
checkConnectionStringName:  String = '��������� ����� � ��';

configuration:  Byte = $11;  // ������������
configurationStringName:  String = '������������';

state:  Byte = $14;  // ���������
stateStringName:  String = '���������';

level:  Byte = $20;  // �������� ������. �� 20 �� 2F
levelStringName:  String = '�������� �������� ������';

density:  Byte = $50;  // �������� ���������. �� 50 �� 5F
densityStringName:  String = '�������� �������� ���������';

volume:  Byte = $80;  // �������� ���������. �� 80 �� 8F
volumeStringName:  String = '�������� �������� ������';

mass:  Byte = $B0;  // �������� ���������. �� B0 �� BF
massStringName:  String = '�������� �������� �����';

temp:  Byte = $30;  // �������� ���������. �� 30 �� 3F
tempStringName:  String = '�������� �������� �����������';

water:  Byte = $40;  // �������� ���������. �� 40 �� 4F
waterStringName:  String = '�������� �������� ����������� ����';

tempUp:  Byte = $60;  // �������� ���������. �� 60 �� 6F
tempUpStringName:  String = '�������� ����. ����. �������� ��';

implementation

end.
