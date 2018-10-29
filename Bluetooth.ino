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
            
short t[13];
short f[1];
short p[12];

t[0] = random (0,10000);
Serial1.print(t[0]);
  for (int i=1;i<14;i++){
    t[i] = random (0,10000);
    Serial1.print("-");
    Serial1.print(t[i],HEX);
    }
  for (int i=0;i<2;i++){
    f[i] = random (0,10000);
    Serial1.print("-");
    Serial1.print(f[i],HEX);
    }
  for (int i=0;i<13;i++){
    p[i] = random (0,10000);
    Serial1.print("-");
    Serial1.print(p[i],HEX);
    }
     Serial1.print("\n");

 delay(100); // wait for 0.1 second
 
      } 
 }
