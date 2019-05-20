#include "DHT.h"
#define LED_ESP D9
#define LED_OUT D6
#define PELT_HEAT D7
#define PELT_COOL D8

#define DHT11_SENS D5
#define LDR_SENS A0


DHT dht;



void turn_on_led_output(){digitalWrite(LED_OUT, LOW);}
void turn_off_led_output(){digitalWrite(LED_OUT, HIGH);}

void executors_test(){
  delay(500);
  digitalWrite(LED_ESP, LOW);
  delay(2000);
  digitalWrite(LED_ESP, HIGH);
  delay(500);
  
  turn_on_led_output();
  delay(4000);
  turn_off_led_output();
  delay(2000);
  
  
  digitalWrite(PELT_HEAT, HIGH);
  delay(4000);
  digitalWrite(PELT_HEAT, LOW);
  delay(2000);
  
  digitalWrite(PELT_COOL, HIGH);
  delay(4000);
  digitalWrite(PELT_COOL, LOW);
  delay(2000);

}
void setup_sensors(){
  dht.setup(DHT11_SENS);  
}

void setup_executors(){
  pinMode(LED_ESP, OUTPUT);
  digitalWrite(LED_ESP, HIGH);
  pinMode(LED_OUT, OUTPUT);
  turn_off_led_output(); //  digitalWrite(LED_OUT, HIGH);
  pinMode(PELT_HEAT, OUTPUT);
  digitalWrite(PELT_HEAT, LOW);
  pinMode(PELT_COOL, OUTPUT);
  digitalWrite(PELT_COOL, LOW);
  pinMode(DHT11_SENS, INPUT);
//  pinMode(LDR_SENS, INPUT);
}

void setup() {
  setup_executors();
  setup_sensors();
  Serial.begin(9600);
}

void loop() {
//  executors_test()

  int light = analogRead(A0);
  Serial.println(light);

  int wilgotnosc = dht.getHumidity();
  Serial.print(wilgotnosc);
  Serial.print("%RH | ");
  
  //Pobranie informacji o temperaturze
  int temperatura = dht.getTemperature();
  Serial.print(temperatura);
  Serial.println("*C");
  
  delay(3000); 
}
