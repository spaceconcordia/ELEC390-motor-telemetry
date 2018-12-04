  /*
  ELEC390 - ROCKET ENGINE CONTROL BLUETOOTH
  */
  #include <SPI.h>
  #define ledPin PC13
  #define CS  10 
  #define RDY 9
  #define WAIT 1
  int LEDSTATUS = 0;
  char incomingByte = 0;   // for incoming serial data
  uint16_t SensorRawValue[29];
  String buffer = "";
  bool fired = false;

uint16_t rawValue = 0;

  
  int POTPin = PB0;
  int HeaderPin = PA0;
  int ThermoPin = PA3;
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

// SPI CODE
void setup() {
  //Initialise UART

  //Initialize SPI
  SPI.begin();
  pinMode(RDY,INPUT);
  SPI.beginTransaction(SPISettings(6000000,MSBFIRST,SPI_MODE1)); //12MHz, MSB shifted out first, SPI Mode 1
   //SPI.setBitOrder(MSBFIRST); // Set the SPI-1 bit order (*)  
  //SPI.setDataMode(SPI_MODE1); //Set the  SPI-1 data mode (**)  
  //Set the SPI speed
  //SPI.setClockDivider(SPI_BAUD_PCLK_DIV_16);   // Slow speed (72
  delay(5);

  //Set up ADS1120
  SPI.transfer(0b00000110);     //reset
  delay(WAIT);                  
  SPI.transfer(0b01000000);     //specify to write to reg 0
  SPI.transfer(0b00000001);     //Vdiff between A0 and A1, Gain of 1, pga disabled
  delay(WAIT);
  SPI.transfer(0b01000100);     //specify to write to reg 1
  SPI.transfer(0b00000100);     //Data rate = 90sps, Normal mode, single conversion, temp disabled, current sources disabled
  delay(WAIT);
  SPI.transfer(0b01001000);     //specify to write to reg 2
  SPI.transfer(0b00000000);     //Internal ref V, no filter, no pwr switch, current sources off
  delay(200);

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
      SensorRawValue[0] = analogRead(ThermoPin);

      SensorRawValue[16] = analogRead(HeaderPin);
    if(SensorRawValue[16]<200){ 
      SensorRawValue[16] = -1;
      }

//SPI DAVID CODE
SPI.transfer(0b00001000);
  delay(12);

  //Get TC1
    rawValue = readADC();
    if (rawValue !=0){
  SensorRawValue[1] = readADC();
    }
  //Change to TC2 Port
  SPI.transfer(0b01000000);     //specify to write to reg 0
  SPI.transfer(0b01010001);     //Vdiff between A2 and A3, Gain of 1, pga disabled
  delay(WAIT);

  //Start
  SPI.transfer(0b00001000);
  delay(12);
  
  //Get TC2
  rawValue = readADC();
  if (rawValue !=0){
  SensorRawValue[2] = rawValue;
  }
  //Change to TC1 Port
  SPI.transfer(0b01000000);     //specify to write to reg 0
  SPI.transfer(0b00000001);     //Vdiff between A0 and A1, Gain of 1, pga disabled
  delay(WAIT);;
//End of SPI DAVID CODE
        
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

uint16_t readADC(){
  uint16_t result = 0;
  SPI.transfer(0b00010000);
  result = SPI.transfer(0);
  result = result << 8;
  result += SPI.transfer(0);
  return result;
}
