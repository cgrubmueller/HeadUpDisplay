package at.ac.tgm.projekte.headupdisplay;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import at.ac.tgm.projekte.headupdisplay.bluetooth.BluetoothException;
import at.ac.tgm.projekte.headupdisplay.bluetooth.Utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Anzeige_1 extends AppCompatActivity {
    //spiegeln
    private boolean spiegeln;

    //Bluetooth
    public static BluetoothSocket socket;

    //Fehleranzeige
    private TextView errorView;

    //Fahrdaten auslesen
    private SpeedReader speed;
    private FuelReader fuel;

    //Fahrdaten auslese
    private TextView speedView;
    private TextView fuelView;

    //Updater
    private Updater updaterTime;
    private Updater updaterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_anzeige_1);
        errorView = findViewById(R.id.errorText);

        this.speedView = findViewById(R.id.speed);
        this.fuelView = findViewById(R.id.akkuNumber);

        this.speed = new SpeedReader();
        this.fuel = new FuelReader();

        updaterTime = new Updater(new Runnable() {
            @Override
            public void run() {
                displayTime();
            }
        },1000);



        try {
            socket = Utils.getSocket(Utils.getDevice());

            updaterData = new Updater(new Runnable() {
                @Override
                public void run() {
                    try {
                        speedView.setText(speed.getFormattedSpeed(socket));
                        fuelView.setText(fuel.getFormattedFuelLevel(socket));
                    } catch (IOException e) {
                        errorView.setText(e.getMessage());
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        errorView.setText(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }, 500);// 500ms => 0.5s
        } catch (IOException e) {
            errorView.setText(e.getMessage());
            e.printStackTrace();
        } catch (BluetoothException e) {
            errorView.setText(e.getMessage());
            e.printStackTrace();
        } catch(Exception e){
            errorView.setText(e.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updaterTime.startUpdates();
        updaterData.startUpdates();

    }

    @Override
    protected void onPause() {
        super.onPause();

        updaterTime.stopUpdates();
        updaterData.stopUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        socket = null;
    }

    /**
     * Display Option Menu
     * @param view Image of Menu
     */
    public void showOptions(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.activity_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case "Anzeige 1":
                        //gleiche Activity
                        break;
                    case "Anzeige 2":
                        Context context = getApplicationContext();
                        Intent intent = new Intent(context, Anzeige_2.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    /**
     * Mirrors the entire display.
     * @param view calling view
     */
    public void mirrorDisplay(View view) {
        this.spiegeln = !this.spiegeln;
        //Mirror Display of speed, label and icon
        View temp = findViewById(R.id.speed);
        mirrorView(temp);
        temp = findViewById(R.id.speedLabel);
        mirrorView(temp);
        temp = findViewById(R.id.speedIcon);
        mirrorView(temp);
        //Mirror battery and its label and icon
        temp = findViewById(R.id.akkuIcon);
        mirrorView(temp);
        temp = findViewById(R.id.akkuNumber);
        mirrorView(temp);
        temp = findViewById(R.id.akkuLabel);
        mirrorView(temp);
        //Mirror Popup Menu
        temp = findViewById(R.id.option);
        mirrorView(temp);
        //Miror Mirror-Button
        temp = findViewById(R.id.mirrorButton);
        mirrorView(temp);
        //Mirror time
        temp = findViewById(R.id.timeField);
        mirrorView(temp);
    }

    /**
     * Mirror single View (graphical Element).
     * @param view View to mirror
     */
    public void mirrorView(View view) {
        if (this.spiegeln == false) {
            view.setScaleX(-1);
            view.setScaleY(-1);
            view.setTranslationX(-1);
        } else {
            view.setScaleX(1);
            view.setScaleY(1);
            view.setTranslationX(1);
        }
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    //SETTER-METHODEN

    /**
     * Display new speed
     * @param speed km/h
     */
    public void setSpeed(int speed) {
        if (speed >= 0) {
            TextView speedField = findViewById(R.id.speed);
            speedField.setText(""+speed);
        }
    }

    /**
     * Display current state of battery (percentage) and update the icon accordingly.
     * @param batteryPercentage %
     */
    public void setBattery(double batteryPercentage) {
        if ((batteryPercentage > 0) && (batteryPercentage <= 100)) {
            TextView batteryField = findViewById(R.id.akkuNumber);
            batteryField.setText("" + batteryPercentage);
            updateBatteryIcon(batteryPercentage);
        }
    }

    /**
     * Gets called in setBattery() and updates the icon of the battery
     * @param percentage %
     */
    private void updateBatteryIcon(double percentage) {
        ImageView icon = findViewById(R.id.akkuIcon);
        //0 - 32
        if (percentage < 33) {
            icon.setImageResource(R.drawable.battery33);
        }
        //33 - 65
        else if ((percentage >= 33) && (percentage < 66)) {
            icon.setImageResource(R.drawable.battery66);
        }
        //66 - 100
        else {
            icon.setImageResource(R.drawable.battery100);
        }
    }

    /**
     * Display the description of an thrown error.
     * @param errorText description of error
     */
    public void displayError(String errorText) {
        if (errorText != null) {
            TextView field = findViewById(R.id.errorText);
            field.setText(errorText);
        }
    }

    /**
     * Displays the current time in the UI.
     */
    public void displayTime() {
        TextView timeField = (TextView) findViewById(R.id.timeField);
        timeField.setText(getCurrentTime());
    }
}