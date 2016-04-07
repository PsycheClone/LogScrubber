package org.singular.connect;

import com.jcraft.jsch.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Connector {

    private Session session;
    private Channel channel;

    public Connector connect(String host, String user, String password) throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(user, host, 2222);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(password);
        session.connect();
        return this;
    }

    public BufferedReader getStreamTail1000(String file) throws JSchException, IOException {
        channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand("tail -1000 "+file);
        channel.connect();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
        return bufferedReader;
    }

    public void disconnect() {
        session.disconnect();
        channel.disconnect();
    }
}
