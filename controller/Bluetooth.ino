  /*
  ELEC390 - ROCKET ENGINE CONTROL BLUETOOTH
  */
  
  #define ledPin PC13
  int LEDSTATUS = 0;
  char incomingByte = 0;   // for incoming serial data
  short s = 0;
  String buffer = "";
  bool fired = false;
  
  unsigned long previousPacketMillis = 0;
  unsigned long previousBlinkMillis = 0;
  unsigned long currentMillis;
  unsigned long Ping;

  const int Packetinterval = 100; //ms to send packets
  const int TimeOut = 2000; //ms to time out and force stop engine
  const int TimeOutBlinktime = 200;
  const int IddleBlinkTime = 1000;
  int blinktime = IddleBlinkTime;
  char RocketStatus; // F = fired, X= Emergency stop, I = Iddle, D = Disconnected, x= stopped because disconnect while firing
  
  // the setup function runs once when you press reset or power the board
  void setup() {
    pinMode(ledPin, OUTPUT);
    digitalWrite(ledPin, HIGH);   
    Serial1.begin(9600);
    RocketStatus = 'I';
  }

  // the loop function runs over and over again forever
  void loop() {
    
              if (Serial1.available()) {
                    incomingByte = Serial1.read();
              }
                  // read the incoming byte:
                  if (incomingByte=='S' &&  RocketStatus == 'I'){
                  RocketStatus = 'F';
                      digitalWrite(ledPin, LOW);
                      fired = true;  
                    } else if (incomingByte=='X'){
                  RocketStatus = 'X';
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
    blinktime = TimeOutBlinktime;
    }
    if (!fired){ // if not already emergency stopped
    RocketStatus = 'D'; // Set disconnect
    }

    
    if (currentMillis - previousBlinkMillis >= blinktime) {
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
              
              
   if (currentMillis - previousPacketMillis >= Packetinterval) {
   previousPacketMillis = currentMillis;
buffer = "";

buffer += String(RocketStatus);
    for (int i=0;i<29;i++){
      buffer +="-";
      s = random (0,256);
      buffer += String(s,HEX);
      }
buffer += "\n";

Serial1.print(buffer);
         }
      
   }
