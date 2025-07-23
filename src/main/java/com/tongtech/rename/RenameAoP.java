package com.tongtech.rename;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RenameAoP {


    public static void main(String[] args) throws IOException {
        String directoryPath = "/home/zhuhai/project/aop-4.0.4.2";
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
                    // 修改文件名
                    String newFileName = modifyFileName(file.getName());
                    File newFile = new File(file.getParent(), newFileName);
                    if (file.renameTo(newFile)) {
                        System.out.println("文件重命名成功: " + file.getName() + " -> " + newFileName);
                        // 修改文件内容
                        modifyFileContent(newFile);
                    } else {
                        System.out.println("文件重命名失败: " + file.getName());
                    }
                }
            }
        }
    }

    public static String modifyDirectoryName(File file) {
        if (file.getName().equals("io")) {
            return "com";
        }
        if  (file.getName().equals("streamnative")) {
            return "tongtech";
        }
        if  (file.getName().equals("pulsar")) {
            return "cnmq";
        }
        return file.getName();
    }


    public static String modifyFileName(String originalName) {
        return originalName
                .replace("pulsar", "cnmq")
                .replace("Pulsar", "Cnmq");
    }

    public static void modifyFileContent(File file) throws IOException {
        List<String> allLines = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String modifiedLine = line
                        .replace("org.apache.pulsar", "com.tongtech.cnmq")
                        .replace("io.streamnative.pulsar", "com.tongtech.cnmq")
                        .replace("io.streamnative", "com.tongtech.cnmq")
                        .replace("org.apache.bookkeeper", "com.tongtech.bookkeeper")
                        .replace("4.0.4.2", "10.0.5.0")
                        .replace("Pulsar", "Cnmq")
                        .replace("pulsar", "cnmq")
                        .replace("PULSAR", "CNMQ");
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