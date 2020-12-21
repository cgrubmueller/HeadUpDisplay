package at.ac.tgm.hit.headupdisplayprototyp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothSocket;
import android.widget.TextView;

import at.ac.tgm.hit.headupdisplayprototyp.bluetooth.Utils;
import at.ac.tgm.hit.headupdisplayprototyp.exceptions.BluetoothException;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private TextView errorView;
    private SpeedReader speed;
    private FuelReader fuel;
    private TextView speedView;
    private TextView fuelView;
    private BluetoothSocket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        errorView = findViewById(R.id.errorView);

        this.speedView = findViewById(R.id.speedView);
        this.fuelView = findViewById(R.id.fuelView);

        this.speed = new SpeedReader();
        this.fuel = new FuelReader();

        try {
            this.socket = Utils.getSocket(Utils.getDevice());

            Updater updater = new Updater(new Runnable() {
                @Override
                public void run() {

                    try {
                        speedView.setText(speed.getFormattedSpeed(socket));
                        fuelView.setText(fuel.getFormattedFuelLevel(socket));
                    } catch (IOException e) {
                        errorView.setText("in run():    " + e.getMessage());
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        errorView.setText(e.getMessage());
                        e.printStackTrace();
                    }
                    //speedView.setText(System.nanoTime()+"");
                    //fuelView.setText(System.nanoTime()+"");
                }
            }, 500);// 500ms => 0.5s

            updater.startUpdates();

        } catch (IOException e) {
            errorView.setText("in onCreate:    " + e.getMessage());
            e.printStackTrace();
        } catch (BluetoothException e) {
            errorView.setText(e.getMessage());
            e.printStackTrace();
        }
    }
}