package org.hnu.nio.c1;

import java.nio.ByteBuffer;

import static org.hnu.nio.c1.ByteBufferUtil.debugAll;

//模拟粘包问题
public class TestByteBufferExam {
    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,World\nI'm zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);
    }

    private static void split(ByteBuffer buffer) {
        //读取buffer，将buffer切换到读模式
        buffer.flip();
        for(int i = 0; i < buffer.limit(); i++) {
            // 找到一条完整消息
            if(buffer.get(i) == '\n') {
                int length = i + 1 - buffer.position();
                ByteBuffer target = ByteBuffer.allocate(length);
                //从buffer中读取写入target
                for(int j = 0; j < length; j++) {
                    target.put(buffer.get());
                }
                debugAll(target);
            }
        }
        buffer.compact();
    }
    private static void split2(ByteBuffer source) {
        source.flip();
        int allLength = source.limit();
        for (int i = 0; i < allLength; i++) {
            if (source.get(i) == '\n') {
                System.out.println(i);
                ByteBuffer target = ByteBuffer.allocate(i + 1 - source.position());
                // 0 ~ limit
                // source当前为读模式，即position为0，limit为当前位置+1
                source.limit(i + 1); //通过修改limit，然后拷贝buffer的方法，比spilt中的普通get()方法然后粘贴过去更快。
                target.put(source); // 从source 读，向 target 写
                debugAll(target);
                source.limit(allLength);//将limit修改回来
            }
        }
        source.compact();//将source切换为写模式
    }
}
