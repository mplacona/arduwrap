package rocks.androidthings.arduwrap;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.TextView;

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

public class SensorReadThread extends HandlerThread {
    private static final int TEMPERATURE = 1;
    private static final int HUMIDITY = 2;
    private Handler mHandler;
    private Dht22Driver mSensorDriver;
    private Handler mUiHandler;

    public SensorReadThread(Dht22Driver sensorDriver, Handler uiHandler) {
        super("SensorReadThread");
        mSensorDriver = sensorDriver;
        mUiHandler = uiHandler;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        mHandler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case TEMPERATURE:
                        try {
                            mSensorDriver.getTemperature(new OnMessageCompleteListener() {
                                @Override
                                public void onMessageComplete(String message) {
                                    mUiHandler.sendMessage(mUiHandler.obtainMessage(TEMPERATURE, message));
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case HUMIDITY:
                        try {
                            mSensorDriver.getHumidity(new OnMessageCompleteListener() {
                                @Override
                                public void onMessageComplete(String message) {
                                    mUiHandler.sendMessage(mUiHandler.obtainMessage(HUMIDITY, message));
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
    }

    public void getTemperature() {
        mHandler.sendEmptyMessage(TEMPERATURE);
    }

    public void getHumidity() {
        mHandler.sendEmptyMessage(HUMIDITY);
    }


}
