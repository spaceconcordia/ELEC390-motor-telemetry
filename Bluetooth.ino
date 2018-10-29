/*
ELEC390 - ROCKET ENGINE CONTROL BLUETOOTH
*/

#define ledPin PC13
char incomingByte = 0;   // for incoming serial data
bool START;
bool STOP;

// the setup function runs once when you press reset or power the board
void setup() {
  pinMode(ledPin, OUTPUT);
  digitalWrite(ledPin, HIGH);   
  Serial1.begin(9600);
  START = false;
  STOP = false;
}

// the loop function runs over and over again forever
void loop() {
  
short s;


            if (Serial1.available()) {
                  incomingByte = Serial1.read();
            }
            delay(10);
                // read the incoming byte:
                if (incomingByte=='S'){
START = true;
  digitalWrite(ledPin, LOW);  
                  }

while(START && !STOP){
  
            if (Serial1.available()) {
                  incomingByte = Serial1.read();
            }
           if (incomingByte=='X'){
                STOP = true;
            digitalWrite(ledPin, HIGH);   
            }
            


s = random (0,10000);
Serial1.print(s,HEX);
  for (int i=1;i<29;i++){
    s = random (0,1000);
    Serial1.print("-");
    Serial1.print(s,HEX);
    }
     Serial1.print("\n");

 delay(100); // wait for 0.1 second
 
      } 
 }
