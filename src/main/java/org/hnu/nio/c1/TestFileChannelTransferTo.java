package org.hnu.nio.c1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

//FileChannel是阻塞的，只有socketChannel等网络相关的channel才能非阻塞
public class TestFileChannelTransferTo {
    public static void main(String[] args) {
        //大文件可以通过utils中的工具类生成。
        long start = System.currentTimeMillis();
        try (
                FileChannel from = new FileInputStream("data.txt").getChannel();
                FileChannel to = new FileOutputStream("to_large_file.txt").getChannel();
        ) {
            //底层使用到操作系统的零拷贝进行优化
            from.transferTo(0, from.size(), to);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("transferTo 用时：" + (end - start) + "ms");

        //机械硬盘与固态硬盘貌似不太一样，黑马P18中描述超过2g的文件可以通过该方法拷贝，会经过多次传输，实际测试还是只有一次
        long start2 = System.currentTimeMillis();
        try (
                FileChannel from = new FileInputStream("data.txt").getChannel();
                FileChannel to = new FileOutputStream("to_large_file2.txt").getChannel();
        ) {
            long size = from.size();
            for(long left = size; left > 0;) {
                System.out.println("position:" + (size - left) + " left:" + left);
                left -= from.transferTo((size - left), left, to);
            }
            //底层使用到操作系统的零拷贝进行优化
            from.transferTo(0, from.size(), to);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end2 = System.currentTimeMillis();
        System.out.println("transferTo 用时：" + (end2 - start2) + "ms");
    }
}
