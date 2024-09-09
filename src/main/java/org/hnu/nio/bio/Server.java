package org.hnu.nio.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 通过BIO来观察消息的边界问题
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000);
        while(true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[4];
            while(true) {
                int len = inputStream.read(buffer);
                if(len == -1) {
                    break;
                }
                System.out.println(new String(buffer, 0, len));
            }
        }
    }
}
