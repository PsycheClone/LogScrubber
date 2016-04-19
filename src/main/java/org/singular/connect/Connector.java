package org.singular.connect;

import com.jcraft.jsch.*;

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

    public BufferedReader readFileBackwards(String file) throws JSchException, IOException {
        channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand("awk '{a[NR]=$0}END{for(i=NR;i>=1;i--)print a[i]}' "+file);
        channel.connect();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
        return bufferedReader;
    }

    public void disconnect() {
        session.disconnect();
        channel.disconnect();
    }
}
