package org.hnu.nio.c1;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class TestByteBuffer {
    public static void main(String[] args) {
        //FileChannel
        //1.输入输出流、 2.RandomAccessFile
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            //准备缓冲区，缓冲区的大小不能无限大，需要我们逐步读取
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while(true) {
                //从channel中读取数据，即向缓冲区写入
                int len = channel.read(buffer); //当read返回结果为-1，则代表缓冲区的数据已经全部读完
                log.debug("读取到的字节{}", len);
                if(len == -1) {
                    break;
                }
                //打印buffer的内容
                buffer.flip(); //切换到读模式
                while(buffer.hasRemaining()) {
                    byte b = buffer.get();//一次读取一个字节
                    System.out.println((char)b);
                }

                //读完一次之后，切换为写模式
                buffer.clear();
            }

        } catch (IOException e) {
        }
    }
}
