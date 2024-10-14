package com.iot.iot_BE.service;

import com.iot.iot_BE.model.HistoryAction;
import com.iot.iot_BE.model.HistorySensor;
import com.iot.iot_BE.repository.HistoryActionRepository;
import com.iot.iot_BE.repository.HistorySensorRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqttService {
    @Autowired
    private HistorySensorRepository historySensorRepository;

    @Autowired
    private HistoryActionRepository historyActionRepository;
    private MqttClient client;

    public MqttService() throws MqttException {
        // Tạo kết nối MQTT
        client = new MqttClient("tcp://192.168.1.49:1884", MqttClient.generateClientId());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("Hoang_Van_Minh_Hieu");
        options.setPassword("b21dccn051".toCharArray());
        client.connect(options);

        // Subscribe topic
        client.subscribe("data_sensor", this::handleMessage);
    }

    private void handleMessage(String topic, MqttMessage message) {
        // Chuyển đổi message MQTT thành dữ liệu JSON
        String payload = new String(message.getPayload());
        // Giả định payload là một JSON object
        JSONObject json = new JSONObject(payload);
        double temp = json.getDouble("temperature");
        double humi = json.getDouble("humidity");
        double light = json.getDouble("lux");


        // Lưu dữ liệu vào cơ sở dữ liệu
        HistorySensor sensorData = new HistorySensor();
        sensorData.setTemperature(temp);
        sensorData.setHumidity(humi);
        sensorData.setLight(light);

        historySensorRepository.save(sensorData);


        String fanState = json.getString("fan");
        String lightState = json.getString("light");
        String acState = json.getString("ac");

        HistoryAction actionData = HistoryAction.builder()
                .fan(fanState)
                .light(lightState)
                .ac(acState)
                .build();

        historyActionRepository.save(actionData);
    }
}
