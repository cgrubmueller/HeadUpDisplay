package at.ac.tgm.projekte.headupdisplay;

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

import java.text.SimpleDateFormat;
import java.util.Date;

public class Anzeige_2 extends AppCompatActivity {
    private boolean spiegeln;
    private TextView timeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anzeige_2);
        //Screen um 90 Grad drehen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //aktuelle Zeit anzeigen
        this.timeField = findViewById(R.id.timeField2);

        //jede Sekunde die Zeit aktualisieren
        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!this.isInterrupted()) {
                        //alle 1000 Millisekunden
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tv = findViewById(R.id.timeField2);
                                tv.setText(getCurrentTime());
                            }
                        });
                    }
                } catch(InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        };
        thread.start();
    }

    /**
     * Gibt die aktuelle Uhrzeit zurueck.
     * @return aktuelle Uhrzeit in String-Form
     */
    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm").format(new Date());
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
                        //Auf Anzeige 1 wechseln
                        Context context = getApplicationContext();
                        Intent intent = new Intent(context, at.ac.tgm.projekte.headupdisplay.MainActivityAlt.class);
                        startActivity(intent);
                        break;
                    case "Anzeige 2":
                        //gleiche Activity
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
        //Mirror Display of speed and its label
        View temp = findViewById(R.id.speed2);
        mirrorView(temp);
        temp = findViewById(R.id.speedLabel2);
        mirrorView(temp);
        //Mirror battery and its label
        temp = findViewById(R.id.akkuIcon2);
        mirrorView(temp);
        temp = findViewById(R.id.akkuNumber2);
        mirrorView(temp);
        //Mirror Popup Menu
        temp = findViewById(R.id.option2);
        mirrorView(temp);
        //Miror Mirror-Button
        temp = findViewById(R.id.mirrorButton2);
        mirrorView(temp);
        //Mirror time
        temp = findViewById(R.id.timeField2);
        mirrorView(temp);
    }

    /**
     * Mirror single View (graphical Element). Meant to be called in mirrorDisplay()
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

    //SETTER-METHODEN

    /**
     * Display new speed
     * @param speed km/h
     */
    public void setSpeed(int speed) {
        if (speed > 0) {
            TextView speedField = findViewById(R.id.speed);
            speedField.setText(speed);
        }
    }

    /**
     * Display current state of battery (percentage) and update the icon accordingly.
     * @param batteryPercentage %
     */
    public void setBattery(int batteryPercentage) {
        if ((batteryPercentage > 0) && (batteryPercentage <= 100)) {
            TextView batteryField = findViewById(R.id.akkuNumber2);
            batteryField.setText(batteryPercentage);
            updateBatteryIcon(batteryPercentage);
        }
    }

    /**
     * Gets called in setBattery() and updates the icon of the battery
     * @param percentage %
     */
    private void updateBatteryIcon(int percentage) {
        ImageView icon = findViewById(R.id.akkuIcon2);
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
            TextView field = findViewById(R.id.errorText2);
            field.setText(errorText);
        }
    }
}