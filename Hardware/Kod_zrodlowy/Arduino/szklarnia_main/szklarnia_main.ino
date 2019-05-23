#include <DHT.h>
#include <MySQL_Connection.h>
#include <MySQL_Cursor.h>
#include <ESP8266WiFi.h>
#include <WiFiClient.h>


#define LED_ESP D9
#define LED_OUT D6
#define PELT_HEAT D7
#define PELT_COOL D8

#define DHT11_SENS D5
#define LDR_SENS A0

DHT dht;

char ssid[] = "Stefan";                 // Network Name
char pass[] = "116e1138a1fc";                 // Network Password

//char ssid[] = "Test_arduino";                 // Network Name
//char pass[] = "test_arduino";                 // Network Password
byte mac[6];

WiFiServer server(80);
IPAddress ip(192, 168, 43, 212);
IPAddress gateway(192, 168, 43, 1);
IPAddress subnet(255, 255, 255, 0);

WiFiClient client;
MySQL_Connection conn((Client *)&client);

char INSERT_SQL[] = "INSERT INTO projektz_greenhouse_data.sensors_data (humidity, light, temperature) VALUES (20, 21, 22)";
char INSERT_SQL_PART[] = "INSERT INTO projektz_greenhouse_data.sensors_data (temperature, humidity, light) VALUES (";
char SELECT_SQL[] = "SELECT projektz_greenhouse_data.excitation.light, projektz_greenhouse_data.excitation.heating, projektz_greenhouse_data.excitation.cooling FROM projektz_greenhouse_data.excitation WHERE projektz_greenhouse_data.excitation.timestamp = ( SELECT MAX( projektz_greenhouse_data.excitation.timestamp ) FROM projektz_greenhouse_data.excitation)";
char query[128];

IPAddress server_addr(65,19,141,67);          // MySQL server IP
char user[] = "projektz_gh_user";              // MySQL user login username
char password[] = "kVwLK5sTx4Wx";        // MySQL user login password


bool heating_auto = 0;
bool cooling_auto = 0;
bool light_auto = 0;

bool heating_current = 0;
bool cooling_current = 0;
bool light_current = 0;

void turn_on_led_esp(){digitalWrite(LED_ESP, LOW);}
void turn_off_led_esp(){digitalWrite(LED_ESP, HIGH);}

void turn_on_led_output(){digitalWrite(LED_OUT, LOW);}
void turn_off_led_output(){digitalWrite(LED_OUT, HIGH);}

void turn_on_pelt_heat(){digitalWrite(PELT_HEAT, HIGH);}
void turn_off_pelt_heat(){digitalWrite(PELT_HEAT, LOW);}

void turn_on_pelt_cool(){digitalWrite(PELT_COOL, HIGH);}
void turn_off_pelt_cool(){digitalWrite(PELT_COOL, LOW);}

  
void setup_wifi(){
  Serial.println("Initialising connection");
  Serial.print(F("Setting static ip to : "));
  Serial.println(ip);

  Serial.println("");
  Serial.println("");
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.mode(WIFI_STA);
  WiFi.hostname("arduino weather"); 
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
}

void connect_to_database(){
    Serial.println("Connecting to database");

  while (conn.connect(server_addr, 3306, user, password) != true) {
    delay(200);
    Serial.print ( "." );
  }
  turn_on_led_esp();
//  Serial.println("");
  Serial.println("Connected to SQL Server!");  
  Serial.println("");
}

void close_database(){
    conn.close();
    turn_off_led_esp();
}


void insertValues(float temperature_insert, float humidity_insert,
  float light_insert){
  Serial.println("********** INSERT **********");
  connect_to_database();
  sprintf(query, "%s%.2f, %.2f, %.2f)",INSERT_SQL_PART, temperature_insert,
      humidity_insert, light_insert);
  Serial.println("Recording data.");
  Serial.println(query);
  
  MySQL_Cursor *cur_mem = new MySQL_Cursor(&conn);
  cur_mem->execute(query);
  delete cur_mem;
  close_database();
  Serial.print( "\n" );
}

void selectValues(){
  Serial.println("********** SELECT **********");
  connect_to_database(); 
  float light_e = 0;
  float heating_e = 0;
  float cooling_e = 0; 
  row_values *row = NULL;
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
  close_database();
  Serial.print( "\n" );
}

void check_executors_state(){
  Serial.println("********** CHECK EXECUTION **********");
  connect_to_database(); 
  float light_e = 0;
  float heating_e = 0;
  float cooling_e = 0; 
  row_values *row = NULL;
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
  delete cur_mem;
  close_database();
  
  if (light_e < 0 || heating_e < 0 || cooling_e < 0){
    heating_current = heating_auto;
    cooling_current = cooling_auto;
    light_current = light_auto;
  } else {
    char execution[64];
    Serial.print("Execution active ");
    sprintf(execution, "Light: %.2f; Heat: %.2f; Cool: %.2f", light_e, heating_e, cooling_e);
    Serial.println(execution);
    Serial.print( "\n" );
    heating_current = heating_e;
    cooling_current = cooling_e;
    light_current = light_e;
  }
}

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
  
  turn_on_pelt_heat();
  delay(4000);
  turn_off_pelt_heat();
  delay(2000);
  
  turn_on_pelt_cool();
  delay(4000);
  turn_off_pelt_cool();
  delay(2000);
}

void sensors_test(){
  Serial.println("********** SENSORS TEST **********");
  int light = analogRead(A0);
  Serial.print(light);
  Serial.print(" | ");

  int wilgotnosc = dht.getHumidity();
  Serial.print(wilgotnosc);
  Serial.print("%RH | ");
  
  int temperatura = dht.getTemperature();
  Serial.print(temperatura);
  Serial.println("*C");
}

void insert_sensors_data(){
  Serial.println("********** INSERT SENSORS DATA **********                      !!!");
  int light = analogRead(A0);
  Serial.print("Sensors_insert: ");
  Serial.print(light);
  Serial.print(" | ");

  int humidity = dht.getHumidity();
  Serial.print(humidity);
  Serial.print("%RH | ");
  
  int temperature = dht.getTemperature();
  Serial.print(temperature);
  Serial.println("*C");
  if ((humidity >= 0 && humidity <= 100) || 
  (temperature >= -50 && temperature <= 100)){
    insertValues(temperature, humidity, light);    
  }
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
}


void setup() {

  Serial.begin(9600);
  setup_wifi();
  setup_executors();
  setup_sensors();
  delay(2500);
}

void loop() {
  static unsigned long lastRefreshTimeSensors = 0;
  static const unsigned long REFRESH_INTERVAL_SENSORS = 60000; // ms 
  static unsigned long lastRefreshTimeExecutors = 0;
  static const unsigned long REFRESH_INTERVAL_EXECUTORS = 20000; // ms 
  if(millis() - lastRefreshTimeExecutors  >= REFRESH_INTERVAL_EXECUTORS){
    lastRefreshTimeExecutors += REFRESH_INTERVAL_EXECUTORS;
    Serial.println();
    Serial.println("*******************************");
    Serial.println("********** MAIN LOOP **********");
    Serial.println("*******************************");
    Serial.println();
    sensors_test();
//    selectValues();
    check_executors_state();
    if(millis() - lastRefreshTimeSensors >= REFRESH_INTERVAL_SENSORS)
    {
      lastRefreshTimeSensors += REFRESH_INTERVAL_SENSORS;  
      insert_sensors_data();
    }
  }
  //  executors_test(); //Należy podłączyć 12V żeby było widać efekty
}
