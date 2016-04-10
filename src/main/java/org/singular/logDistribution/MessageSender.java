package org.singular.logDistribution;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Service
public class MessageSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(final Destination dest, final String text) throws JsonProcessingException {

        jmsTemplate.send(dest, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return  session.createTextMessage(text);
            }
        });
    }
}
