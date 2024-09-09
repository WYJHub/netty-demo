package org.hnu.nio.c1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static org.hnu.nio.c1.ByteBufferUtil.debugAll;

public class TestScatteringReads {
    public static void main(String[] args) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile("words.txt", "rw")) {
            FileChannel channel = randomAccessFile.getChannel();
            ByteBuffer a = ByteBuffer.allocate(3);
            ByteBuffer b = ByteBuffer.allocate(3);
            ByteBuffer c = ByteBuffer.allocate(5);

            //向buffer中分散写入
            long read = channel.read(new ByteBuffer[]{a, b, c});
            System.out.println(read);
//            a.flip();
//            b.flip();
//            c.flip();
            debugAll(a);
            debugAll(b);
            debugAll(c);
        } catch (IOException e) {
        }
    }
}
