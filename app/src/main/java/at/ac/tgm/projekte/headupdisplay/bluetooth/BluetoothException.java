package at.ac.tgm.projekte.headupdisplay.bluetooth;

public class BluetoothException extends Exception{
    private String infotext;

    public BluetoothException(String infotext){
        this.infotext=infotext;
    }

    public String toString(){
        return infotext;
    }
}
