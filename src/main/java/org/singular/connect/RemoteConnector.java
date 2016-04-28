package org.singular.connect;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RemoteConnector {

    private Logger LOGGER = LoggerFactory.getLogger(RemoteConnector.class);
    private Session session;
    private Channel channel;

    public RemoteConnector connect(String host, String user, String password) {
        JSch jsch = new JSch();
        try {
            session = jsch.getSession(user, host, 2222);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();
        } catch (JSchException e) {
            LOGGER.error("Error connecting to host " + host + ". " + e.getMessage());
        }
        return this;
    }

    public BufferedReader readFileBackwards(String file) {
        BufferedReader bufferedReader = null;
        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand("awk '{a[NR]=$0}END{for(i=NR;i>=1;i--)print a[i]}' " + file);
            channel.connect();
            bufferedReader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
        } catch (JSchException e) {
            LOGGER.error("Error executing command. " + e.getMessage());
        } catch (IOException e) {
            LOGGER.error("Error getting inputstream. " + e.getMessage());
        }
        return bufferedReader;
    }

    public void disconnect() {
        session.disconnect();
        channel.disconnect();
    }
}
