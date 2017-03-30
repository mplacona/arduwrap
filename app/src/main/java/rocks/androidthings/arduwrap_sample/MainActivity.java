package rocks.androidthings.arduwrap_sample;

import android.databinding.DataBindingUtil;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import rocks.androidthings.arduwrap.Arduino;
import rocks.androidthings.arduwrap.Dht22Driver;
import rocks.androidthings.arduwrap_sample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String UART_DEVICE_NAME = "UART0";
    private static final int BAUD_RATE = 115200;
    private static final int DATA_BITS = 8;
    private static final int STOP_BITS = 1;
    private Dht22Driver dht22Driver;
    private TextView mTemperature;
    private TextView mHumidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        FloatingActionButton fab = binding.floatingActionButton;

        mTemperature = binding.temperatureTv;
        mHumidity = binding.humidityTv;

        Arduino mArduino = new Arduino.ArduinoBuilder()
                .uartDeviceName(UART_DEVICE_NAME)
                .baudRate(BAUD_RATE)
                .dataBits(DATA_BITS)
                .stopBits(STOP_BITS)
                .build();

        dht22Driver = new Dht22Driver(mArduino);
        dht22Driver.startup();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTemperature.setText(String.format("%s°C", dht22Driver.getTemperature()));
                mHumidity.setText(String.format("%s%%", dht22Driver.getHumidity()));
            }
        });

    }

    @Override
    protected void onDestroy(){
        dht22Driver.shutdown();
        super.onDestroy();
    }
}
