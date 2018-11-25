#include <SPI.h>

#define CS  10
#define RDY 9
#define WAIT 1

long rawValue = 0;

void setup() {
  //Initialise UART
  Serial.begin(115200);

  //Initialize SPI
  SPI.begin();
  pinMode(RDY,INPUT);
  SPI.beginTransaction(SPISettings(12000000,MSBFIRST,SPI_MODE1)); //12MHz, MSB shifted out first, SPI Mode 1
  delay(5);

  //Set up ADS1120
  SPI.transfer(0b00000110);     //reset
  delay(WAIT);                  
  SPI.transfer(0b01000010);     //specify to write regs 0,1,2
  SPI.transfer(0b00000001);     //Vdiff between A0 and A1, Gain of 1, pga disabled
  SPI.transfer(0b00000100);     //Data rate = 20sps, Normal mode, single conversion, temp disabled, current sources disabled
  SPI.transfer(0b00000000);     //Internal ref V, no filter, no pwr switch, current sources off
  delay(200);

  //Read back registers
  Serial.println("ADC Register Contents:");
  SPI.transfer(0b00100000);
  Serial.println(SPI.transfer(0),BIN);  //Read reg0
  SPI.transfer(0b00100100);
  Serial.println(SPI.transfer(0),BIN);  //Read reg1
  SPI.transfer(0b00101000);
  Serial.println(SPI.transfer(0),BIN);  //Read reg2
  delay(200);

  Serial.println("ADC Start");
}

void loop() {
  SPI.transfer(0b00001000);
  rawValue = readADC();
  Serial.println(rawValue, BIN);
  delay(500);
}

long readADC(){
  long result = 0;
  SPI.transfer(0b0001000);
  result = SPI.transfer16(0);
  return result;
}

