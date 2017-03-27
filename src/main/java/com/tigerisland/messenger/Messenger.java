package com.tigerisland.messenger;

import com.tigerisland.GlobalSettings;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class Messenger implements Runnable {

    protected BlockingQueue<Message> outboundQueue;
    private GlobalSettings globalSettings;
    private PrintWriter writer;
    private Socket socket;
    final boolean offline;

    public Messenger(GlobalSettings globalSettings) {
        this.outboundQueue = globalSettings.outboundQueue;
        this.globalSettings = globalSettings;
        this.offline = globalSettings.getServerSettings().offline;
    }

    public void run() {
        try {
            socket = new Socket(globalSettings.getServerSettings().IPaddress, globalSettings.getServerSettings().port);
            writer = new PrintWriter(socket.getOutputStream(), true);

            while(true) {
                try {
                    writer.println(removeMessageFromQueue());
                } catch (InterruptedException exception) {
                    break;
                }
            }
            closeLocalServer();
        } catch (IOException exception) {
            return;
        }

    }

    protected Message removeMessageFromQueue() throws InterruptedException {
        return outboundQueue.take();
    }

    private void closeLocalServer() {
        if (offline) {
            try {
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("END");
                socket.close();
            } catch(IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
