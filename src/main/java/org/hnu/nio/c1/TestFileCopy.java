package org.hnu.nio.c1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class TestFileCopy {
    //使用walk方法拷贝目标文件
    public static void main(String[] args) throws IOException {
        String FROM = "file";
        String TO = "file_copy";

        Files.walk(Paths.get(FROM)).forEach(path -> {
            try {
                String targetName = path.toString().replaceFirst(FROM, TO);
                if(Files.isDirectory(path) && !Files.exists(Paths.get(targetName))) {
                        Files.createDirectory(Paths.get(targetName));
                }
                else if(Files.isRegularFile(path)) {
                    //使用StandardCopyOption.REPLACE_EXISTING来指定如果目标文件存在则覆盖，不使用该参数且目标文件存在时会抛出异常
                    Files.copy(path, Path.of(targetName), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
