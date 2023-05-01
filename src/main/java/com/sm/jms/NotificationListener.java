package com.sm.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Slf4j
@Component
public class NotificationListener {

    @JmsListener(destination = "NOTIFICATION.Q")
    public void receive(Message message) throws JMSException {
        TextMessage textMessage = (TextMessage) message;
        log.info("Notification Receive Message: {} and correlation id: {}",
                textMessage.getText(), textMessage.getJMSCorrelationID());
    }
}
