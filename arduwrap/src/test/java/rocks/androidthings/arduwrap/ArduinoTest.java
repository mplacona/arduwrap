package rocks.androidthings.arduwrap;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

public class ArduinoTest {
    @Test(expected = IllegalArgumentException.class)
    public void testNullUartDeviceNameThrows() {
        String uartDeviceName = null;
        new Arduino.ArduinoBuilder().uartDeviceName(uartDeviceName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongBaudRateThrows() {
        int baudRate = 1;
        new Arduino.ArduinoBuilder().baudRate(baudRate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataBitsThrows() {
        int dataBits = 0;
        new Arduino.ArduinoBuilder().dataBits(dataBits);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStopBitsThrows() {
        int stopBits = 0;
        new Arduino.ArduinoBuilder().stopBits(stopBits);
    }

    @Test
    public void testBuilderCreatesCorrectInstance() {
        String uartDeviceName = "Device";
        int baudRate = 115200;
        int dataBits = 8;
        int stopBits = 1;


        Arduino arduino = new Arduino.ArduinoBuilder()
                .uartDeviceName(uartDeviceName)
                .baudRate(baudRate)
                .dataBits(dataBits)
                .stopBits(stopBits)
                .build();

        assertEquals(uartDeviceName, arduino.getUartDeviceName());
        assertEquals(baudRate, arduino.getBaudRate());
        assertEquals(dataBits, arduino.getDataBits());
        assertEquals(stopBits, arduino.getStopBits());
    }
}
