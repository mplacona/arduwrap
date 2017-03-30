Arduwrap
========
A wrapper around your Arduino so you can use any sensors available to the Arduino with Android Things

Program your Arduino
-------
https://create.arduino.cc/editor/mplacona/db600d03-e19c-444e-891b-88f3569ba7e4/preview

Download
--------

Gradle:
```
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
dependencies {
    compile 'com.github.mplacona:arduwrap:v0.2'
}
```
Using:
---------
- Create a new Arduino instance
```
Arduino mArduino = new Arduino.ArduinoBuilder()
    .uartDeviceName(UART_DEVICE_NAME)
    .baudRate(BAUD_RATE)
    .dataBits(DATA_BITS)
    .stopBits(STOP_BITS)
    .build();
```

- Create an instance of the driver you would like to use
```
Dht22Driver dht22Driver = new Dht22Driver(mArduino);
        dht22Driver.startup();
```

- Call it 
```
String temperature = dht22Driver.getTemperature();
String humidity = dht22Driver.getHumidity();
```

Connections
----------
![Connections](https://github.com/mplacona/arduwrap/blob/master/arduwrap.png?raw=true)

License
=======

    Copyright 2017 Marcos Placona

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
