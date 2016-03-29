package org.singular.connect;

import com.jcraft.jsch.*;

import java.io.*;

public class Connector {
    private static String user = "svc";
    private static String password = "ic5xeRHS";

    private Session session;
    private Channel channel;

    public Connector connect(String host) throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(user, host, 2222);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(password);
        session.connect();
        return this;
    }

    public BufferedReader getStream(String file) throws JSchException, IOException {
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
