package com.tongtech.rename;

import java.io.File;

public class RenameTool {

    public static void main(String[] args) {
        // 指定要遍历的目录路径
        String directoryPath = "F:\\outProject\\pulsar";
        File directory = new File(directoryPath);

        // 检查目录是否存在
        if (directory.exists() && directory.isDirectory()) {
            // 调用递归方法遍历目录
            traverseDirectory(directory, 0);
        } else {
            System.out.println("指定的目录不存在或不是一个有效的目录。");
        }
    }

    public static void traverseDirectory(File directory, int level) {
        // 获取目录下的所有文件和子目录
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                // 根据当前层级输出缩进
                for (int i = 0; i < level; i++) {
                    System.out.print("  ");
                }

                if (file.isDirectory()) {
                    // 如果是目录，打印目录名并递归调用方法
                    System.out.println("目录: " + file.getName());
                    traverseDirectory(file, level + 1);
                } else {
                    // 如果是文件，打印文件名
                    System.out.println("文件: " + file.getName());
                }
            }
        }
    }
}
