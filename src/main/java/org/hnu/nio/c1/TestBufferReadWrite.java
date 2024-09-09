package org.hnu.nio.c1;

import java.nio.ByteBuffer;

import static org.hnu.nio.c1.ByteBufferUtil.debugAll;

public class TestBufferReadWrite {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) '1'); //'a'
        debugAll(buffer);
        buffer.put(new byte[]{'2', '3', '4'});
        debugAll(buffer);

        //切换到读模式
        buffer.flip();
        System.out.println(buffer.get());
        debugAll(buffer);

        //切换到写入模式，将未读取的内容前移，但是原位置不会清零，重新写入会覆盖
        buffer.compact();
        debugAll(buffer);

        buffer.put(new byte[]{'5', '6', '7'});
        debugAll(buffer);

        //mark记录当前position，get两次position+2，reset重置到原先
        buffer.mark();
        buffer.get();
        buffer.get();
        debugAll(buffer);
        buffer.reset();
        debugAll(buffer);
    }
}
