package com.tongtech.rename;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class RenameBookkeeper {

    private static final List<String> notNeedModifyDirs = Arrays.asList();

    public static void main(String[] args) throws IOException {
        String directoryPath = "/home/zhuhai/project/bookkeeper-release-4.17.1";
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            traverseDirectory(directory);
        } else {
            System.out.println("指定的目录不存在或不是一个有效的目录。");
        }
    }

    public static void traverseDirectory(File directory) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String newDirectoryName = modifyDirectoryName(file);
                    File newDirectory = new File(file.getParent(), newDirectoryName);
                    if (file.renameTo(newDirectory)) {
                        System.out.println("目录重命名成功: " + file.getName() + " -> " + newDirectoryName);
                    } else {
                        System.out.println("目录重命名失败: " + file.getName());
                    }
                    // 递归遍历子目录
                    traverseDirectory(newDirectory);
                } else {
                    // 修改文件内容
                    modifyFileContent(file);
                }
            }
        }
    }

    public static String modifyDirectoryName(File file) {
        if (file.getName().equals("org")) {
            return "com";
        }
        if  (file.getName().equals("apache")) {
            return "tongtech";
        }
        return file.getName();
    }

    public static void modifyFileContent(File file) throws IOException {
        List<String> allLines = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String modifiedLine = line
                        .replace("org\\.apache\\.bookkeeper", "com\\.tongtech\\.bookkeeper")
                        .replace("org.apache.bookkeeper", "com.tongtech.bookkeeper")
                        .replace("org/apache/bookkeeper", "com/tongtech/bookkeeper")
                        .replace("org_apache_bookkeeper", "com_tongtech_bookkeeper")
                        .replace("4.17.1", "10.0.5.0");
                allLines.add(modifiedLine);
            }
        } catch (FileNotFoundException e) {
            System.err.println("读取文件时出现错误: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : allLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("写入文件时出现错误: " + e.getMessage());
        }
    }
}