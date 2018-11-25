  /*
  ELEC390 - ROCKET ENGINE CONTROL BLUETOOTH
  */
  #include <SPI.h>
  #define ledPin PC13
  
  int LEDSTATUS = 0;
  char incomingByte = 0;   // for incoming serial data
  short SensorRawValue[29];
  String buffer = "";
  bool fired = false;
  
  int POTPin = PB0;
  int HeaderPin = PA0;
  int STOPLEDPin = PB10;

  unsigned long previousPacketMillis = 0;
  unsigned long previousBlinkMillis = 0;
  unsigned long currentMillis;
  unsigned long Ping;

  const int Packetinterval = 100; //ms to send packets
  const int TimeOut = 1000; //ms to time out and force stop engine
  
  const int StopBlinktime = 100;
  const int UnconnectedBlinkTime = 1000;
  int blinktime = UnconnectedBlinkTime;
  
  char RocketStatus; // F = fired, X= Emergency stop, I = Iddle, D = Disconnected, x= stopped because disconnect while firing
  
  // the setup function runs once when you press reset or power the board
  void setup() {
    pinMode(ledPin, OUTPUT);
    pinMode(STOPLEDPin, OUTPUT);
    digitalWrite(ledPin, HIGH);   
    digitalWrite(STOPLEDPin, LOW);   
    Serial1.begin(9600);
    RocketStatus = 'I';
  }

/*
 * Built in LED is reveserd on my bluepill
 */
 
  void loop() {
    
              if (Serial1.available()) {
                    incomingByte = Serial1.read();
              }
                  // read the incoming byte:
                  if (incomingByte=='S' &&  RocketStatus == 'I'){ //Receive Start and is Idle
                  RocketStatus = 'F';   // Fire from android 
                      digitalWrite(ledPin, LOW);
                      fired = true;  
                    } else if (incomingByte=='X'){ // Emergency Stop from android
                              digitalWrite(STOPLEDPin, HIGH);   
                  RocketStatus = 'X'; // STOP
              digitalWrite(ledPin, HIGH);
              fired =true;   
              } else if (incomingByte=='P'){ // Ping from Android
               Ping  = millis();
              }

//Bluetooth Connection Detector
  currentMillis = millis();
  if (currentMillis - Ping >= TimeOut) { // if timeout

        if (fired){ // If fired, abort
    RocketStatus = 'x';
    blinktime = StopBlinktime;
        digitalWrite(STOPLEDPin, HIGH);   
    }
    if (!fired){ // if not already emergency stopped
    RocketStatus = 'D'; // Set disconnect
    }

    //Blinking script
    if (currentMillis - previousBlinkMillis >= blinktime ) { 
    previousBlinkMillis = currentMillis;

    // if the LED is off turn it on and vice-versa:
      if (LEDSTATUS) {
      digitalWrite(ledPin, LOW);
      LEDSTATUS = !LEDSTATUS;
      } else {
      digitalWrite(ledPin, HIGH);
      LEDSTATUS = !LEDSTATUS;
      }
    }
  }else if(!fired){
   digitalWrite(ledPin, HIGH);
   RocketStatus = 'I';
  }

// Also blink fast is Emergency stop was activated:
    if (currentMillis - previousBlinkMillis >= StopBlinktime && RocketStatus == 'X' ) { 
    previousBlinkMillis = currentMillis;

    // if the LED is off turn it on and vice-versa:
      if (LEDSTATUS) {
      digitalWrite(ledPin, LOW);
      LEDSTATUS = !LEDSTATUS;
      } else {
      digitalWrite(ledPin, HIGH);
      LEDSTATUS = !LEDSTATUS;
      }
    }              


             // THIS FUNCTION IS WHERE ALL THE DATA IS GET AND THE PACKET IS CREATED AND HAPPEN
   if (currentMillis - previousPacketMillis >= Packetinterval) {
   previousPacketMillis = currentMillis;
   

    for (int i=0;i<29;i++){ // All Sensors value to disconnected (-1) by default
      SensorRawValue[i] = -1;
      }

//Real sensor Values added to the SensorRawValue[] array here
      //POT READINGS
      SensorRawValue[14] = analogRead(POTPin);
      if(SensorRawValue[14]<100){ // If its under 100, set it back to disconnected
      SensorRawValue[14] = -1;
      }
      //PRESSURE HEADER
      SensorRawValue[16] = analogRead(HeaderPin);
    if(SensorRawValue[16]<50){ 
      SensorRawValue[16] = -1;
      }
        
      buffer = ""; // Packet buffer set to empty
      buffer += String(RocketStatus); // First Char is rocket Status

    //All Sensor Values are then sent
    for (int i=0;i<29;i++){
      buffer +="-";
      if (SensorRawValue[i] == -1){
              buffer += String('X');
        } else {
      buffer += String(SensorRawValue[i],HEX);
        }
      }
buffer += "\n";

Serial1.print(buffer);
         }
      
   }
