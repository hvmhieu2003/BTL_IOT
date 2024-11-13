package com.iot.iot_BE.service;

import com.iot.iot_BE.model.HistoryAction;
import com.iot.iot_BE.model.HistorySensor;
import com.iot.iot_BE.repository.CountWarningRepository;
import com.iot.iot_BE.repository.HistoryActionRepository;
import com.iot.iot_BE.repository.HistorySensorRepository;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqttService {
    private static final Logger logger = LoggerFactory.getLogger(MqttService.class);
    @Autowired
    private HistorySensorRepository historySensorRepository;
    @Autowired
    private HistoryActionRepository historyActionRepository;
    @Autowired
    private CountWarningService countWarningService;
    private MqttClient client;
    public MqttService() throws MqttException {
        // Tạo kết nối MQTT
        client = new MqttClient("tcp://192.168.1.49:1884", MqttClient.generateClientId());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("Hoang_Van_Minh_Hieu");
        options.setPassword("b21dccn051".toCharArray());
        client.connect(options);

        // Subscribe các topic
        client.subscribe("data_sensor", this::handleSensorMessage);
        client.subscribe("device/action", this::handleDeviceActionMessage);
        client.subscribe("count", this::handleCountMessage);
    }

    private void handleSensorMessage(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        logger.info("Received MQTT message from sensor: {}", payload);

        try {
            JSONObject json = new JSONObject(payload);
            double temp = json.getDouble("temperature");
            double humi = json.getDouble("humidity");
            double light = json.getDouble("light");
            double dust = json.getDouble("dust");

            logger.info("Temperature: {}, Humidity: {}, Light: {}, Dust: {}", temp, humi, light, dust);

            HistorySensor sensorData = new HistorySensor();
            sensorData.setTemperature(temp);
            sensorData.setHumidity(humi);
            sensorData.setLight(light);
            sensorData.setDust(dust);

            historySensorRepository.save(sensorData);
            logger.info("Saved sensor data: {}", sensorData);

        } catch (Exception e) {
            logger.error("Error handling sensor message: {}", e.getMessage());
        }
    }

    private void handleDeviceActionMessage(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        logger.info("Received MQTT message for device action: {}", payload);

        try {
            JSONObject json = new JSONObject(payload);
            String device = json.getString("device");
            String status = json.getString("status");

            logger.info("Device: {}, Status: {}", device, status);

            HistoryAction actionData = HistoryAction.builder()
                    .device(device)
                    .action(status)
                    .build();

            historyActionRepository.save(actionData);
            logger.info("Saved action data: {}", actionData);

        } catch (Exception e) {
            logger.error("Error handling device action message: {}", e.getMessage());
        }
    }

    private void handleCountMessage(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        logger.info("Received MQTT message for count warning: {}", payload);

        try {
            JSONObject json = new JSONObject(payload);
            long dustCount = json.getLong("dustCount");

            logger.info("Dust count: {}", dustCount);

            countWarningService.updateDustCount(dustCount);
            logger.info("Updated and saved dust count: {}", dustCount);

        } catch (Exception e) {
            logger.error("Error handling count warning message: {}", e.getMessage());
        }
    }

    public void sendDeviceCommand(String device, boolean state) {
        String message = state ? "ON" : "OFF";
        String topic = "devices/" + device;

        try {
            if (client.isConnected()) {
                client.publish(topic, message.getBytes(), 0, false);
                logger.info("Sent command to {}: {}", device, message);
            } else {
                logger.warn("MQTT client is not connected. Unable to send command.");
            }
        } catch (MqttException e) {
            logger.error("Failed to send command to {}: {}", device, e.getMessage());
        } catch (Exception e) {
            logger.error("Error sending command to {}: {}", device, e.getMessage());
        }
    }
}
