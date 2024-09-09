package org.hnu.nio.c1;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.hnu.nio.c1.ByteBufferUtil.debugAll;

//字符串与byteBuffer的相互转换
public class TestByteBufferString {
    public static void main(String[] args) {
        //1.字符串转换为ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put("hello".getBytes());
        debugAll(buffer);//可以看到position

        //2.Charset字符集，
        ByteBuffer hello = StandardCharsets.UTF_8.encode("hello");
        debugAll(hello); //使用encode方法，自动切换到读模式

        //3.wrap与字符集方法相同
        ByteBuffer wrap = ByteBuffer.wrap("hello".getBytes());
        debugAll(wrap);

        //转换为字符串，需要切换到读模式
        String string = StandardCharsets.UTF_8.decode(hello).toString();
        System.out.println(string);

        //如果不切换到读模式,读取为空
        String string1 = StandardCharsets.UTF_8.decode(buffer).toString();
        System.out.println(string1);//未切换到读模式，读取为空
        buffer.flip();
        String string2 = Charset.forName("UTF-8").decode(buffer).toString();
        System.out.println(string2);

        String string3 = StandardCharsets.UTF_8.decode(wrap).toString();
        System.out.println(string3);
    }
}
