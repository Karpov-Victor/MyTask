unit ThreadUnit;

interface

uses
  System.Classes, System.SysUtils, Winapi.Windows, Vcl.Dialogs;

type
  TStrunaThread = class(TThread)
  protected
    procedure Execute; override;
  end;

implementation
  uses unit1, ComObj;

{
  Important: Methods and properties of objects in visual components can only be
  used in a method called using Synchronize, for example,

      Synchronize(UpdateCaption);  

  and UpdateCaption could look like,

    procedure TStrunaThread.UpdateCaption;
    begin
      Form1.Caption := 'Updated in a thread';
    end; 
    
    or 
    
    Synchronize( 
      procedure 
      begin
        Form1.Caption := 'Updated in thread via an anonymous method' 
      end
      )
    );
    
  where an anonymous method is passed.
  
  Similarly, the developer can call the Queue method with similar parameters as 
  above, instead passing another TThread class as the first parameter, putting
  the calling thread in a queue with the other thread.
    
}

{ TStrunaThread }

procedure counter();
begin
  while Form1.CheckBoxCyrcle.Checked do
  begin
   Form1.GetData—hannelToMemo;
   sleep(2000);
  end;
end;

procedure TStrunaThread.Execute;
begin
  NameThreadForDebugging('StrunaThread');
  counter;
end;




end.
