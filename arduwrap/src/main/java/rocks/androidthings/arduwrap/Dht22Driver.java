package rocks.androidthings.arduwrap;

import android.util.Log;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.UartDevice;
import com.google.android.things.pio.UartDeviceCallback;

import java.io.IOException;

/**
 * Copyright (C) 2017 mplacona.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class Dht22Driver implements BaseSensor {
    private static final String TAG = "Dht22Driver";
    private final Arduino arduino;

    private UartDevice mDevice;
    private StringBuffer message = new StringBuffer();
    private boolean receiving = false;
    private OnMessageCompleteListener messageCompleteListener;

    public Dht22Driver(Arduino arduino){
        this.arduino = arduino;
    }

    @Override
    public void startup() {
        PeripheralManagerService mPeripheralManagerService = new PeripheralManagerService();

        try {
            mDevice = mPeripheralManagerService.openUartDevice(arduino.getUartDeviceName());
            mDevice.setDataSize(arduino.getDataBits());
            mDevice.setParity(UartDevice.PARITY_NONE);
            mDevice.setStopBits(arduino.getStopBits());
            mDevice.setBaudrate(arduino.getBaudRate());
        } catch (IOException e){
            throw new IllegalStateException("Sensor can't start", e);
        }

        try {
            mDevice.registerUartDeviceCallback(mUartCallback);
        } catch (IOException e) {
            throw new IllegalStateException("Sensor can't register callback - App is foobar'd", e);
        }
    }

    public void getDHT(String mode, OnMessageCompleteListener listener) throws IOException {
        setMessageCompleteListener(listener);
        mDevice.write(mode.getBytes(), mode.length());
    }

    // TODO: Refactor this to use bytes directly
    public void getTemperature(OnMessageCompleteListener listener) throws IOException {
        setMessageCompleteListener(listener);
        String mode = "T";
        mDevice.write(mode.getBytes(), mode.length());
    }

    // TODO: And this....
    public void getHumidity(OnMessageCompleteListener listener) throws IOException {
        setMessageCompleteListener(listener);
        String mode = "H";
        mDevice.write(mode.getBytes(), mode.length());
    }

    @Override
    public void onStart() {
        try {
            mDevice.registerUartDeviceCallback(mUartCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        mDevice.unregisterUartDeviceCallback(mUartCallback);
    }

    @Override
    public void shutdown() {
        if (mDevice != null) {
            try {
                mDevice.close();
                mDevice = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close UART device", e);
            }
        }
    }

    private UartDeviceCallback mUartCallback = new UartDeviceCallback() {
        @Override
        public boolean onUartDeviceDataAvailable(UartDevice uart) {
            // Read available data from the UART device
            try {
                readUartBuffer(uart);
            } catch (IOException e) {
                Log.w(TAG, "Unable to access UART device", e);
            }

            // Continue listening for more interrupts
            return true;
        }

        @Override
        public void onUartDeviceError(UartDevice uart, int error) {
            Log.w(TAG, uart + ": Error event " + error);
        }
    };

    private void readUartBuffer(UartDevice uart) throws IOException {
        byte[] buffer = new byte[1];

        uart.read(buffer, buffer.length);
        String character = new String(buffer, "UTF-8");

        if(character.compareTo("#") == 0){
            receiving = false;
            this.messageCompleteListener.onMessageComplete(message.toString());
            Log.d(TAG, "Complete Message: " + message);
        }

        if(receiving){
            message.append(character);
        }

        if(character.compareTo("$") == 0){
            receiving = true;
        }
    }

    private void setMessageCompleteListener(OnMessageCompleteListener messageCompleteListener){
        this.messageCompleteListener = messageCompleteListener;
    }
}
