package chat.rocket.reactnative.proxy;

import com.dylanvann.fastimage.cschat.CSSocket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ProxyRequestHandler implements Runnable {
    private Socket clientSocket;
    private long connectionId;

    public ProxyRequestHandler(Socket clientSocket, long connectionId) {
        this.clientSocket = clientSocket;
        this.connectionId = connectionId;
        try {
            clientSocket.setSoTimeout(60000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            Socket csSocket = new CSSocket();

            while (true) {
                byte[] inData = readWithTimeout(inputStream, 200, 50);
                if (inData.length == 0) {
                    continue;
                }

                csSocket.getOutputStream().write(inData);
                OutputStream outputStream = clientSocket.getOutputStream();

                int outSize;
                byte[] outBuffer = new byte[2048];
                InputStream tunnelInStream = csSocket.getInputStream();

                while ((outSize = tunnelInStream.read(outBuffer)) > 0) {
                    outputStream.write(outBuffer, 0, outSize);
                    outputStream.flush();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    byte[] readWithTimeout(InputStream inputStream, int startTimeout, long timeout) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        long start = System.currentTimeMillis();
        int inSize;
        byte[] inBuffer = new byte[2048];
        long targetTimeout = startTimeout;
        while (System.currentTimeMillis()-start < targetTimeout) {
            if (inputStream.available() > 0) {
                inSize = inputStream.read(inBuffer);
                baos.write(inBuffer, 0, inSize);
                start = System.currentTimeMillis();
                targetTimeout = timeout;
            }
        }
        return baos.toByteArray();
    }
}
