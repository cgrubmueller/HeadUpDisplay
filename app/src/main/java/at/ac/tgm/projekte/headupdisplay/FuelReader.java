package at.ac.tgm.projekte.headupdisplay;

import android.bluetooth.BluetoothSocket;
import com.github.pires.obd.commands.fuel.FuelLevelCommand;

import java.io.IOException;

public class FuelReader {
    private FuelLevelCommand fuelLevel;

    public FuelReader() {
        this.fuelLevel = new FuelLevelCommand();
    }

    /**
     * Gibt den aktuellen Akkustand/Treibstoffstand ohne Einheiten zurück.
     * @param socket die Verbindung zum ELM327 OBDII Bluetooth-Dongle
     * @return den Aktuellen Akkustand
     * @throws IOException, wenn der Socket nicht korrekt ist
     * @throws InterruptedException
     */
    public double getFuelLevel(BluetoothSocket socket) throws IOException, InterruptedException {
        this.fuelLevel.run(socket.getInputStream(), socket.getOutputStream());
        String result = this.fuelLevel.getFormattedResult();
        double cache = Double.parseDouble(result.substring(0,result.indexOf("%")));
        return cache;
    }

    /**
     * Gibt den aktuellen Akkustand/Treibstoffstand mit Einheiten zurück (%).
     * @param socket die Verbindung zum ELM327 OBDII Bluetooth-Dongle
     * @return den Aktuellen Akkustand
     * @throws IOException, wenn der Socket nicht korrekt ist
     * @throws InterruptedException
     */
    public String getFormattedFuelLevel(BluetoothSocket socket) throws IOException, InterruptedException {
        this.fuelLevel.run(socket.getInputStream(), socket.getOutputStream());
        return this.fuelLevel.getFormattedResult();
    }
}
