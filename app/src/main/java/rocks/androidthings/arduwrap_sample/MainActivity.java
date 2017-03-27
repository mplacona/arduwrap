package rocks.androidthings.arduwrap_sample;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import rocks.androidthings.arduwrap.Arduino;
import rocks.androidthings.arduwrap.Dht22Driver;
import rocks.androidthings.arduwrap.OnMessageCompleteListener;
import rocks.androidthings.arduwrap_sample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements OnMessageCompleteListener {

    private static final String TAG = "MainActivity";
    private static final String UART_DEVICE_NAME = "UART0";
    private static final int BAUD_RATE = 115200;
    private static final int DATA_BITS = 8;
    private static final int STOP_BITS = 1;
    Dht22Driver dht22Driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        final TextView temperature = binding.temperatureTv;
        final TextView humidity = binding.humidityTv;

        Arduino mArduino = new Arduino.ArduinoBuilder()
                .uartDeviceName(UART_DEVICE_NAME)
                .baudRate(BAUD_RATE)
                .dataBits(DATA_BITS)
                .stopBits(STOP_BITS)
                .build();

        dht22Driver = new Dht22Driver(mArduino);
        dht22Driver.startup();


        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    dht22Driver.getTemperature(temperature, MainActivity.this);
                    Log.d(TAG, "run: Temperature");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    dht22Driver.getHumidity(humidity, MainActivity.this);
                    Log.d(TAG, "run: Humidity");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1000);

    }

    @Override
    protected void onDestroy(){
        dht22Driver.shutdown();
        super.onDestroy();
    }

    @Override
    public void onMessageComplete(String message, TextView field) {
        field.setText(message);
    }
}
