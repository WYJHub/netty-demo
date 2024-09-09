package org.hnu.nio.c1;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

//集中写,多个buffer写入一个channel
public class TestGatheringWriters {
    public static void main(String[] args) {
        try (RandomAccessFile rw = new RandomAccessFile("words2.txt", "rw")) {
            ByteBuffer hello = StandardCharsets.UTF_8.encode("hello");
            ByteBuffer world = StandardCharsets.UTF_8.encode("world");
            ByteBuffer test = StandardCharsets.UTF_8.encode("你好");
            rw.getChannel().write(new ByteBuffer[]{hello, world, test});

        } catch (IOException e) {
        }
    }

}
