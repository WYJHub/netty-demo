package org.hnu.nio.c1;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class TestFileCount {
    /*
    jdk7 引入了 Path 和 Paths 类
    * Path 用来表示文件路径
    * Paths 是工具类，用来获取 Path 实例
    Path source = Paths.get("1.txt"); // 相对路径 使用 user.dir 环境变量来定位 1.txt
    Path source = Paths.get("d:\\1.txt"); // 绝对路径 代表了  d:\1.txt
    Path source = Paths.get("d:/1.txt"); // 绝对路径 同样代表了  d:\1.txt
    Path projects = Paths.get("d:\\data", "projects"); // 代表了  d:\data\projects
    * `.` 代表了当前路径
    * `..` 代表了上一级路径
     */
    public static void main(String[] args) throws IOException {
        String path = "file";
        //统计该目录下文件与文件夹的数量
        Path path1 = Paths.get(path);
        AtomicInteger fileCount = new AtomicInteger(0);
        AtomicInteger dirCount = new AtomicInteger(0);

        //统计txt文件的数量
        AtomicInteger txtFileCount = new AtomicInteger(0);

        Files.walkFileTree(path1, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                dirCount.incrementAndGet();
                System.out.println("====>" + dir.toString());
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file.toString());
                //注意file.getFileName().toString()只能获取文件名，不能获取后缀名
                //file.toFile().getName()可以获取后缀名
                //file.toString()会包括全部路径
                if(file.toString().endsWith(".txt")) {
                    txtFileCount.incrementAndGet();
                }
                fileCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                System.out.println("<=====" + dir.toString());
                return super.postVisitDirectory(dir, exc);
            }
        });
        System.out.println("文件数量" + (fileCount.get()));
        System.out.println("文件夹数量" + (dirCount.get()));
        System.out.println("txt文件数量" + (txtFileCount.get()));
    }
}
