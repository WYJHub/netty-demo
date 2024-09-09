package org.hnu.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

public class TestByteBuf {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
        // heapBuffer 堆内存分配快，访问慢
        // directBuffer 直接内存，分配慢，访问快
        //通过VM options来设定是否开启池化功能 -Dio.netty.allocator.type={unpooled|pooled}
        System.out.println(buffer.getClass());
        /*
        池化的最大意义在于可以重用 ByteBuf，优点有
        * 没有池化，则每次都得创建新的 ByteBuf 实例，这个操作对直接内存代价昂贵，就算是堆内存，也会增加 GC 压力
        * 有了池化，则可以重用池中 ByteBuf 实例，并且采用了与 jemalloc 类似的内存分配算法提升分配效率
        * 高并发时，池化功能更节约内存，减少内存溢出的可能
         */
        log(buffer);

        //byteBuf
        //读写双指针，不需要再切换模式了
        //capacity与maxCapacity，动态扩容
//        buffer.writeInt(250); //大端写入，先写高位，一般大端
//        buffer.writeIntLE(250); //小端写入，先写低位，不是完全逆序，而是按照字节分段逆序
//        log(buffer);

        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        log(buffer);
        /*
        扩容规则是
        * 如何写入后数据大小未超过 512，则选择下一个 16 的整数倍，例如写入后大小为 12 ，则扩容后 capacity 是 16
        * 如果写入后数据大小超过 512，则选择下一个 2^n，例如写入后大小为 513，则扩容后 capacity 是 2^10=1024（2^9=512 已经不够了）
        * 扩容不能超过 max capacity 会报错
         */
        System.out.println(buffer.readByte());
        System.out.println(buffer.readByte());
        System.out.println(buffer.readByte());
        log(buffer);//读过的内容，就属于废弃部分了，再读只能读那些尚未读取的部分
        //如果需要重复读取，可以通过mark-reset的方式或者getXXX方法直接读取
        buffer.markReaderIndex();//标记读索引
        System.out.println(buffer.readByte());
        log(buffer);
        buffer.resetReaderIndex();
        System.out.println("重复读取" + buffer.readByte());
        /*
        内存回收
        由于 Netty 中有堆外内存的 ByteBuf 实现，堆外内存最好是手动来释放，而不是等 GC 垃圾回收。

        * UnpooledHeapByteBuf 使用的是 JVM 内存，只需等 GC 回收内存即可
        * UnpooledDirectByteBuf 使用的就是直接内存了，需要特殊的方法来回收内存
        * PooledByteBuf 和它的子类使用了池化机制，需要更复杂的规则来回收内存
        回收内存的源码实现，请关注下面方法的不同实现
        protected abstract void deallocate()

        Netty 这里采用了引用计数法来控制回收内存，每个 ByteBuf 都实现了 ReferenceCounted 接口

        * 每个 ByteBuf 对象的初始计数为 1
        * 调用 release 方法计数减 1，如果计数为 0，ByteBuf 内存被回收
        * 调用 retain 方法计数加 1，表示调用者没用完之前，其它 handler 即使调用了 release 也不会造成回收
        * 当计数为 0 时，底层内存会被回收，这时即使 ByteBuf 对象还在，其各个方法均无法正常使用
         */
    }

    private static void log(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buffer.readerIndex())
                .append(" write index:").append(buffer.writerIndex())
                .append(" capacity:").append(buffer.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(buf, buffer);
        System.out.println(buf.toString());
    }
}
