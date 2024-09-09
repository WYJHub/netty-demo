package org.hnu.nio.c1;

import java.nio.ByteBuffer;

import static org.hnu.nio.c1.ByteBufferUtil.debugAll;

public class TestBufferRead {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put(new byte[]{'1','2','3','4'});
        buffer.flip();
        buffer.get(new byte[4]);
        debugAll(buffer);

        //rewind将position置为0，mark= -1
        buffer.rewind();
        buffer.get();
        debugAll(buffer);

        //mark做一个标记,reset返回标记位
        buffer.mark();//标记当前position =1
        buffer.get();
        buffer.get();
        debugAll(buffer); //position = 3
        buffer.reset();
        debugAll(buffer); //position = 1

        //通过索引直接get，不会影响position
        buffer.get(0);
        debugAll(buffer);

    }
}
