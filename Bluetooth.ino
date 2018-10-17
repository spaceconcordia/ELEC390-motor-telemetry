/*
ELEC390 - ROCKET ENGINE CONTROL BLUETOOTH
*/

#define ledPin PC13

// the setup function runs once when you press reset or power the board
void setup() {
  // initialize digital pin LED_BUILTIN as an output.
  pinMode(ledPin, OUTPUT);
  Serial1.begin(9600);
}

// the loop function runs over and over again forever
void loop() {
  Serial1.print("SPACE CADET BLUETOOTH ONLINE \n");
  digitalWrite(ledPin, HIGH);   // turn the LED on (HIGH is the voltage level)
  delay(200);                       // wait for a second
  digitalWrite(ledPin, LOW);    // turn the LED off by making the voltage LOW
  delay(1000);                       // wait for a second
}
