package org.hnu.nio.c1;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class TestFileDelete {
    public static void main(String[] args) throws IOException {
        //！！！注意删除操作，一定要在可删除的目录下进行,最好是copy的目录
        Path fileCopy = Paths.get("file_copy");
        Files.walkFileTree(fileCopy, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println("删除文件" + file.toFile().getName());
                Files.delete(file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                System.out.println("删除目录" + dir.getFileName().toString());
                Files.delete(dir);
                return super.postVisitDirectory(dir, exc);
            }
        });

    }
}
