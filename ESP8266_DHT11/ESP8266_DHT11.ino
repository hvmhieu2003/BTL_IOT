#include "DHT.h"
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>

#define WIFI_SSID "1506"   
#define WIFI_PASSWORD "lig1506b"

// #define WIFI_SSID "iPhone 16 Promax"   
// #define WIFI_PASSWORD "Minhchau2008"

// Raspberry Pi Mosquitto MQTT Broker
#define MQTT_HOST "172.20.10.6"
#define MQTT_PORT 1884
#define MQTT_PUB_SENSOR "data_sensor"
#define MQTT_SUB_DEVICE_ACTION "device/action"
#define MQTT_PUB_WARN "count"

#define DHTPIN 14
#define LIGHT_SENSOR_PIN A0
#define LED1_PIN D2
#define FAN_PIN D1
#define AIRCONDITIONER_PIN D4
#define WARNING_LED_PIN D7  // Đèn cảnh báo ở chân D7
#define DHTTYPE DHT11

#define MQTT_USERNAME "Hoang_Van_Minh_Hieu"
#define MQTT_PASSWORD "b21dccn051"

DHT dht(DHTPIN, DHTTYPE);
float temp;
float hum;
int light;
float dustLevel; // Thêm biến độ bụi
int dustCount = 0;
WiFiClient wifiClient;
PubSubClient mqttClient(wifiClient);

unsigned long previousMillis = 0;
unsigned long warningLedMillis = 0; // Biến lưu thời gian cho đèn cảnh báo
const long interval = 5000;
const long blinkInterval = 500; // Thời gian nhấp nháy đèn cảnh báo (500ms)

bool led1Status = LOW;
bool fanStatus = LOW;
bool airConditionerStatus = LOW;
bool warningLedStatus = LOW;  // Trạng thái của đèn cảnh báo
bool dustExceedThreshold = false;
bool warningLedBlinking = false;
unsigned long previousBlinkMillis = 0;

void connectToWifi() {
  Serial.println("Connecting to Wi-Fi...");
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
}

void reconnectToMqtt() {
  while (!mqttClient.connected()) {
    Serial.println("Connecting to MQTT...");
    if (mqttClient.connect("ESP8266Client", MQTT_USERNAME, MQTT_PASSWORD)) {
      Serial.println("Connected to MQTT.");
      mqttClient.subscribe(MQTT_SUB_DEVICE_ACTION);
    } else {
      Serial.print("Failed to connect to MQTT, rc=");
      Serial.print(mqttClient.state());
      delay(2000);
    }
  }
}

// Callback xử lý message từ MQTT
void callback(char* topic, byte* payload, unsigned int length) {
  Serial.printf("Message arrived [%s]: ", topic);

  if (strcmp(topic, MQTT_SUB_DEVICE_ACTION) == 0) {
    Serial.println("Received message for device/action");

    // Phân tích JSON từ payload
    DynamicJsonDocument doc(256);
    String message;
    for (int i = 0; i < length; i++) {
      message += (char)payload[i];
    }
    deserializeJson(doc, message);

    // Xử lý thiết bị
    if (doc.containsKey("device")) {
      String deviceName = doc["device"];
      String status = doc["status"];

      if (deviceName == "Led") {
        led1Status = (status == "On") ? HIGH : LOW;
        digitalWrite(LED1_PIN, led1Status);
      } else if (deviceName == "Fan") {
        fanStatus = (status == "On") ? HIGH : LOW;
        digitalWrite(FAN_PIN, fanStatus);
      } else if (deviceName == "Airconditioner") {
        airConditionerStatus = (status == "On") ? HIGH : LOW;
        digitalWrite(AIRCONDITIONER_PIN, airConditionerStatus);
      } else if (deviceName == "WarningLed") {
        warningLedStatus = (status == "On") ? HIGH : LOW;
        digitalWrite(WARNING_LED_PIN, warningLedStatus);
        warningLedBlinking = false; // Tắt chế độ nhấp nháy khi nhận lệnh bật/tắt
      } else if (deviceName == "WarningLedBlink") {
        warningLedBlinking = (status == "On"); // Bật chế độ nhấp nháy khi nhận lệnh "Blink"
      }

    }
  } else {
    Serial.println("Unknown message topic");
  }
}

void setup() {
  Serial.begin(115200);
  Serial.println();

  dht.begin();

  pinMode(LED1_PIN, OUTPUT);
  pinMode(FAN_PIN, OUTPUT);
  pinMode(AIRCONDITIONER_PIN, OUTPUT);
  pinMode(WARNING_LED_PIN, OUTPUT);  // Đặt chân đèn cảnh báo làm OUTPUT

  mqttClient.setServer(MQTT_HOST, MQTT_PORT);
  mqttClient.setCallback(callback);

  connectToWifi();
}

void loop() {
  if (!mqttClient.connected()) {
    reconnectToMqtt();
  }
  mqttClient.loop();

  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= interval) {
    previousMillis = currentMillis;

    // Đọc dữ liệu từ cảm biến
    hum = dht.readHumidity();
    temp = dht.readTemperature();
    int analogValue = analogRead(LIGHT_SENSOR_PIN);
    float light = 500 - (analogValue / 1023.0) * 500;

    // Tạo giá trị độ bụi ngẫu nhiên (giả sử từ 0 đến 100%).
    dustLevel = random(0, 101);

    // Làm tròn giá trị nhiệt độ, độ ẩm, ánh sáng và độ bụi đến 2 chữ số thập phân
    float roundedTemp = round(temp * 100) / 100.0;
    float roundedHum = round(hum * 100) / 100.0;
    float roundedLight = round(light * 100) / 100.0;
    float roundedDust = round(dustLevel * 100) / 100.0;

    // Tạo đối tượng JSON chứa dữ liệu cảm biến
    DynamicJsonDocument doc(1024);
    doc["temperature"] = roundedTemp;
    doc["humidity"] = roundedHum;
    doc["light"] = roundedLight;
    doc["dust"] = roundedDust; // Thêm dữ liệu về độ bụi

    // Gửi dữ liệu lên MQTT broker
    String jsonData;
    serializeJson(doc, jsonData);
    mqttClient.publish(MQTT_PUB_SENSOR, jsonData.c_str());
    Serial.print("Published: ");
    Serial.println(jsonData);
  }

  // Kiểm tra giá trị bụi và điều khiển đèn cảnh báo
  if (dustLevel > 60) {
    if (!dustExceedThreshold) {  // Chỉ tăng nếu chưa vượt quá ngưỡng
      dustCount++;  // Tăng biến đếm mỗi khi độ bụi vượt quá 90
      dustExceedThreshold = true;  // Đặt trạng thái là đã vượt quá ngưỡng

      // Tạo message JSON riêng cho biến đếm dustCount
      DynamicJsonDocument countData(256);
      countData["dustCount"] = dustCount; // Chỉ gửi số lần vượt quá ngưỡng

      // Gửi message riêng qua MQTT
      String countJsonData;
      serializeJson(countData, countJsonData);
      mqttClient.publish(MQTT_PUB_WARN, countJsonData.c_str());

      Serial.print("Published dust exceed count: ");
      Serial.println(countJsonData);
    }
  }else {
    dustExceedThreshold = false; 
  }
  

  if (warningLedBlinking) {
    unsigned long currentMillis = millis();
    if (currentMillis - previousBlinkMillis >= blinkInterval) {
      previousBlinkMillis = currentMillis;
      digitalWrite(WARNING_LED_PIN, !digitalRead(WARNING_LED_PIN)); // Đảo trạng thái của đèn cảnh báo
    }
  } else {
    digitalWrite(WARNING_LED_PIN, warningLedStatus); // Duy trì trạng thái bật/tắt nếu không nhấp nháy
  }
}