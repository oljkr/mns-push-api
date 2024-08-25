package kr.wishtarot.mnspushapi.web.controller;

import kr.wishtarot.mnspushapi.domain.MqMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kr.wishtarot.mnspushapi.service.impl.MqProduceServiceImpl;

@RestController
@RequestMapping("/api/v1/mq-produce")
public class MqProduceController {

    private final MqProduceServiceImpl mqProduceService;

    @Autowired
    public MqProduceController(MqProduceServiceImpl mqProduceService) {
        this.mqProduceService = mqProduceService;
    }

    @PostMapping("/produce")
    public ResponseEntity<String> produceMessage(@RequestBody MqMessage mqMessage) {
        try {
            String resultMsg = mqProduceService.processAndSendPushNotification(mqMessage);
            return ResponseEntity.ok(resultMsg);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("FAIL|" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAIL|" + e.getMessage());
        }
    }
}
