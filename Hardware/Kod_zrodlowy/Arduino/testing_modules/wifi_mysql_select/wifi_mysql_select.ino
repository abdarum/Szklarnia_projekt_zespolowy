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
//byte mac[6];
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };

WiFiServer server(80);
IPAddress ip(192, 168, 43, 212);
IPAddress gateway(192, 168, 43, 1);
IPAddress subnet(255, 255, 255, 0);

WiFiClient client;
MySQL_Connection conn((Client *)&client);


//SELECT light, heating, cooling

// 337 char
// SELECT projektz_greenhouse_data.excitation.light, projektz_greenhouse_data.excitation.heating, projektz_greenhouse_data.excitation.cooling FROM projektz_greenhouse_data.excitation WHERE projektz_greenhouse_data.excitation.timestamp = ( SELECT MAX( projektz_greenhouse_data.excitation.timestamp ) FROM projektz_greenhouse_data.excitation)
char INSERT_SQL[] = "INSERT INTO projektz_greenhouse_data.sensors_data (humidity, light, temperature) VALUES (20, 21, 22)";
//char INSERT_SQL[] = "INSERT INTO projektz_greenhouse_data.sensors_data (humidity, light, temperature) VALUES (20, 21, 22)";
char SELECT_SQL[] = "SELECT projektz_greenhouse_data.excitation.light, projektz_greenhouse_data.excitation.heating, projektz_greenhouse_data.excitation.cooling FROM projektz_greenhouse_data.excitation WHERE projektz_greenhouse_data.excitation.timestamp = ( SELECT MAX( projektz_greenhouse_data.excitation.timestamp ) FROM projektz_greenhouse_data.excitation)";
char SELECT_SQL2[] = "SELECT projektz_greenhouse_data.excitation.light, projektz_greenhouse_data.excitation.heating, projektz_greenhouse_data.excitation.cooling FROM projektz_greenhouse_data.excitation FROM projektz_greenhouse_data.excitation";
char query[350];

IPAddress server_addr(65, 19, 141, 67);       // MySQL server IP
char user[] = "projektz_gh_user";              // MySQL user login username
char password[] = "kVwLK5sTx4Wx";        // MySQL user login password


float temperature = 0;
float humidity = 0;
float light = 0;

float light_e = 0;
float heating_e = 0;
float cooling_e = 0;

void setup() {

  Serial.begin(9600);

  pinMode(D7, INPUT);
  pinMode(D9, OUTPUT);
  digitalWrite(D7, HIGH);
  digitalWrite(D9, HIGH);
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
  Serial.print(mac[5], HEX);
  Serial.print(":");
  Serial.print(mac[4], HEX);
  Serial.print(":");
  Serial.print(mac[3], HEX);
  Serial.print(":");
  Serial.print(mac[2], HEX);
  Serial.print(":");
  Serial.print(mac[1], HEX);
  Serial.print(":");
  Serial.println(mac[0], HEX);
  Serial.println("");
  Serial.print("Assigned IP: ");
  Serial.print(WiFi.localIP());
  Serial.println("");
  //
  //  Serial.println("Connecting to database");
  //
  //  while (conn.connect(server_addr, 3306, user, password) != true) {
  //    delay(200);
  //    Serial.print ( "." );
  //  }

  Serial.println("");
  Serial.println("Connected to SQL Server!");




}

void loop() {
  row_values *row = NULL;
  digitalWrite(D9, LOW);
  delay(10000); //10 sec
  digitalWrite(D9, HIGH);


  Serial.println("Connecting to database");

  while (conn.connect(server_addr, 3306, user, password) != true) {
    delay(200);
    Serial.print ( "." );
  }

  MySQL_Cursor *cur_mem = new MySQL_Cursor(&conn);

  cur_mem->execute(SELECT_SQL);
  column_names *columns = cur_mem->get_columns();

  do {
    row = cur_mem->get_next_row();
    if (row != NULL) {
      light_e = atof(row->values[0]);
      heating_e = atof(row->values[1]);
      cooling_e = atof(row->values[2]);
    }
  } while (row != NULL);
  char execution[64];
  sprintf(execution, "Light: %.2f; Heat: %.2f; Cool: %.2f", light_e, heating_e, cooling_e);
  Serial.println(execution);

  delete cur_mem;

  if (!digitalRead(D7)) {
    digitalWrite(D9, LOW);
    delay(1000);
    conn.close();
  }
  conn.close();
}
