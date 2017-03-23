package rocks.androidthings.arduwrap;

import java.util.Arrays;
import java.util.List;

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

public class Arduino {
    private final String uartDeviceName;
    private final int baudRate;
    private final int dataBits;
    private final int stopBits;

    private Arduino(ArduinoBuilder builder) {
        this.uartDeviceName = builder.uartDeviceName;
        this.baudRate = builder.baudRate;
        this.dataBits = builder.dataBits;
        this.stopBits = builder.stopBits;
    }

    String getUartDeviceName() {
        return uartDeviceName;
    }

    int getBaudRate() {
        return baudRate;
    }

    int getDataBits() {
        return dataBits;
    }

    int getStopBits() {
        return stopBits;
    }

    public static class ArduinoBuilder {
        private String uartDeviceName = "UART0";
        private int baudRate = 115200;
        private int dataBits = 8;
        private int stopBits = 1;


        public ArduinoBuilder uartDeviceName(String uartDeviceName) {
            if (uartDeviceName == null)
                throw new IllegalArgumentException("fileFormat == null");

            this.uartDeviceName = uartDeviceName;
            return this;
        }

        public ArduinoBuilder baudRate(int baudRate) {
            List<Integer> baudRates = Arrays.asList( 9600, 19200, 38400, 57600, 115200);
            boolean test = baudRates.contains(baudRate);
            if(!test)
                throw new IllegalArgumentException("baudRates are:" + baudRates.toString());

            this.baudRate = baudRate;
            return this;
        }

        public ArduinoBuilder dataBits(int dataBits) {
            if (dataBits < 1)
                throw new IllegalArgumentException("dataBits must > 1");

            this.dataBits = dataBits;
            return this;
        }

        public ArduinoBuilder stopBits(int stopBits) {
            if (stopBits < 1)
                throw new IllegalArgumentException("stopBits must > 1");

            this.stopBits = stopBits;
            return this;
        }

        public Arduino build() {
            return new Arduino(this);
        }

    }
}