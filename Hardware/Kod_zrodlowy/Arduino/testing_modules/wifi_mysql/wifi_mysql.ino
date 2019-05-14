//#include <DHT.h>
#include <MySQL_Connection.h>
#include <MySQL_Cursor.h>
#include <ESP8266WiFi.h>
#include <WiFiClient.h>

#define sensorPin1 0

//#define sensorPin2 D2
//#define typeDHT DHT11
//DHT dht(sensorPin1, typeDHT);

char ssid[] = "Stefan";                 // Network Name
char pass[] = "116e1138a1fc";                 // Network Password
byte mac[6];

WiFiServer server(80);
IPAddress ip(192, 168, 43, 212);
IPAddress gateway(192, 168, 43, 1);
IPAddress subnet(255, 255, 255, 0);

WiFiClient client;
MySQL_Connection conn((Client *)&client);

char INSERT_SQL[] = "INSERT INTO projektz_greenhouse_data.sensors_data (humidity, light, temperature) VALUES (20, 21, 22)";
char query[128];

IPAddress server_addr(65,19,141,67);          // MySQL server IP
char user[] = "projektz_gh_user";              // MySQL user login username
char password[] = "kVwLK5sTx4Wx";        // MySQL user login password

void setup() {

  Serial.begin(9600);

  pinMode(sensorPin1, INPUT);
  //pinMode(sensorPin2, INPUT);

  Serial.println("Initialising connection");
  Serial.print(F("Setting static ip to : "));
  Serial.println(ip);

  Serial.println("");
  Serial.println("");
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.config(ip, gateway, subnet); 
  WiFi.begin(ssid, pass);

  while (WiFi.status() != WL_CONNECTED) {
    delay(200);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi Connected");

  WiFi.macAddress(mac);
  Serial.print("MAC: ");
  Serial.print(mac[5],HEX);
  Serial.print(":");
  Serial.print(mac[4],HEX);
  Serial.print(":");
  Serial.print(mac[3],HEX);
  Serial.print(":");
  Serial.print(mac[2],HEX);
  Serial.print(":");
  Serial.print(mac[1],HEX);
  Serial.print(":");
  Serial.println(mac[0],HEX);
  Serial.println("");
  Serial.print("Assigned IP: ");
  Serial.print(WiFi.localIP());
  Serial.println("");

  Serial.println("Connecting to database");

  while (conn.connect(server_addr, 3306, user, password) != true) {
    delay(200);
    Serial.print ( "." );
  }

  Serial.println("");
  Serial.println("Connected to SQL Server!");  

}

void loop() {

//  int soil_hum = 1024 - analogRead(sensorPin1);
//  //float t = dht.readTemperature();
//
//  //Serial.println(t);
//
  delay(10000); //10 sec
//
//  sprintf(query, INSERT_SQL, soil_hum);
//  //sprintf(query, INSERT_SQL, soil_hum, t);

  sprintf(query, INSERT_SQL);
  Serial.println("Recording data.");
  Serial.println(query);
  
  MySQL_Cursor *cur_mem = new MySQL_Cursor(&conn);
  
  cur_mem->execute(query);

  delete cur_mem;

}
