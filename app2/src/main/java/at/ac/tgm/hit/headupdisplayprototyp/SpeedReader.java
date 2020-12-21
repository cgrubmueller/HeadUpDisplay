package at.ac.tgm.hit.headupdisplayprototyp;

import android.bluetooth.BluetoothSocket;
import com.github.pires.obd.commands.SpeedCommand;

import java.io.IOException;

public class SpeedReader {
    private SpeedCommand speed;

    public SpeedReader() {
        this.speed = new SpeedCommand();
    }

    /**
     * Gibt die aktuelle Geschwindigkeit ohne Einheiten zurück.
     * @param socket die Verbindung zum ELM327 OBDII Bluetooth-Dongle
     * @return den Aktuellen Akkustand
     * @throws IOException, wenn der Socket nicht korrekt ist
     * @throws InterruptedException
     */
    public String getSpeed(BluetoothSocket socket) throws IOException, InterruptedException {
        System.out.println("Speed wird ausgelesen");
        this.speed.run(socket.getInputStream(), socket.getOutputStream());
        return this.speed.getResult();
    }

    /**
     * Gibt die aktuelle Geschwindigkeit mit Einheiten zurück (kmH).
     * @param socket die Verbindung zum ELM327 OBDII Bluetooth-Dongle
     * @return den Aktuellen Akkustand
     * @throws IOException, wenn der Socket nicht korrekt ist?
     * @throws InterruptedException
     */
    public String getFormattedSpeed(BluetoothSocket socket) throws IOException, InterruptedException {
        this.speed.run(socket.getInputStream(), socket.getOutputStream());
        return this.speed.getFormattedResult();
    }
}
