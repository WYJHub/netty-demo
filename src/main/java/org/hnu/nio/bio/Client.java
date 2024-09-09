package org.hnu.nio.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 9000);
        OutputStream outputStream = socket.getOutputStream();

        outputStream.write("hello".getBytes());
        outputStream.write("world".getBytes());
        outputStream.write("你好".getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
