package com.sm.rest;

import com.ibm.mq.jms.MQQueue;
import com.sm.model.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequestMapping("notification")
@RestController
public class NotificationController {

    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody NotificationRequest notification) throws JMSException {
        log.info("Notification Message : '{}' ", notification.getMessage());

        MQQueue notificationRequestQueue = new MQQueue("NOTIFICATION.Q");
        jmsTemplate.convertAndSend(notificationRequestQueue, notification.getMessage(), textMessage -> {
            textMessage.setJMSCorrelationID(notification.getIdentifier());
            return textMessage;
        });

        return new ResponseEntity(notification, HttpStatus.OK);
    }


    @Deprecated // this was just to show how to find a message by correlation Id
    @GetMapping
    public ResponseEntity<NotificationRequest> findOrderByCorrelationId(@RequestParam String correlationId) throws JMSException {
        log.info("Looking for message '{}'", correlationId);
        String convertedId = bytesToHex(correlationId.getBytes());
        final String selectorExpression = String.format("JMSCorrelationID='ID:%s'", convertedId);
        final TextMessage responseMessage = (TextMessage) jmsTemplate.receiveSelected("NOTIFICATION.REQUEST", selectorExpression);
        NotificationRequest response = NotificationRequest.builder().message(responseMessage.getText()).identifier(correlationId).build();
        return new ResponseEntity(response, HttpStatus.OK);
    }

    // You could use Apache Commons Codec library instead
    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes();

    public static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }
}
