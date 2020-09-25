package chat.rocket.reactnative.proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import chat.rocket.reactnative.cschat.CSSocket;

public class TunnelProxy {
    int port;

    ServerSocket serverSocket;
    private boolean running;
    long count;

    public TunnelProxy(int port) {
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;

            new Thread(() -> {
                try {
                    while (running) {
                        Socket socket = serverSocket.accept();
                        handleIncomingConnection(socket);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleIncomingConnection(Socket socket) {
        count++;
        Thread thread = new Thread(new ProxyRequestHandler(socket, count));
        thread.start();
    }

    public void stop() {
        running = false;
    }
}
