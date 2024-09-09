package org.hnu.nio.c1;

import java.nio.ByteBuffer;

//了解不同ByteBuffer的区别
public class TestBufferAllocate {
    public static void main(String[] args) {
        System.out.println(ByteBuffer.allocate(16).getClass());
        System.out.println(ByteBuffer.allocateDirect(16).getClass());
        /*
        class java.nio.HeapByteBuffer - 使用java堆内存， 读写效率较低，受到GC的影响，内存位置可能发生变动
        class java.nio.DirectByteBuffer - 直接内存， 读写效率更高（少一次拷贝），不会受到GC的影响
                                        直接内存需要操作系统调用，分配效率低，而且使用不当可能造成内存泄露
         */

    }
}
/*
向buffer写入
    int len = channel.read(buffer)
    buffer.put()
从buffer读取数据
    调用channel的write方法
    调用buffer自己的get方法
    get 方法会让 position 读指针向后走，如果想重复读取数据
    * 可以调用 rewind 方法将 position 重新置为 0
    * 或者调用 get(int i) 方法获取索引 i 的内容，它不会移动读指针


 */