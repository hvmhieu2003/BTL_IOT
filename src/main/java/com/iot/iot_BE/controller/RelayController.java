package com.iot.iot_BE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relay")
public class RelayController {
//    @Autowired
//    private RelayRepository relayRepository;
//
//    @PostMapping
//    public ResponseEntity<String> saveRelayData(@RequestBody Relay relayData) {
//        relayRepository.save(relayData);
//        return ResponseEntity.ok("Relay data received");
//    }
}
