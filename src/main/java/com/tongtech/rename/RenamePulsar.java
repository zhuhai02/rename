package com.tongtech.rename;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class RenamePulsar {

//    private static final List<String> notNeedModifyDirs = Arrays.asList("testmocks", "managed-ledger", "jcloud");

    public static void main(String[] args) throws Exception {
        String directoryPath = "/home/zhuhai/project/TongPulsar-rename";
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            traverseDirectory(directory);
        } else {
            System.out.println("指定的目录不存在或不是一个有效的目录。");
        }
    }

    public static void traverseDirectory(File directory) throws Exception {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (file.getName().equals(".git")) {
                        continue;
                    }
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

    /**
     * 递归删除目录及其所有内容
     * @param directory 要删除的目录
     * @throws Exception 如果删除过程中发生错误
     */
    public static void deleteDirectory(File directory) throws Exception {
        // 检查是否为目录
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("提供的路径不是一个目录: " + directory.getAbsolutePath());
        }

        // 列出目录中的所有文件和子目录
        File[] contents = directory.listFiles();
        if (contents != null) {
            for (File content : contents) {
                if (content.isDirectory()) {
                    // 递归删除子目录
                    deleteDirectory(content);
                }
                // 删除文件或空目录
                if (!content.delete()) {
                    throw new Exception("无法删除文件或目录: " + content.getAbsolutePath());
                }
            }
        }

        // 删除空目录
        if (!directory.delete()) {
            throw new Exception("无法删除目录: " + directory.getAbsolutePath());
        }
    }

    public static String modifyDirectoryName(File file) {
//        if (notNeedModify(file)) {
//            return file.getName();
//        }
        if (file.getName().equals("org")) {
            return "com";
        }
        if  (file.getName().equals("apache")) {
            return "tongtech";
        }
        if  (file.getName().equals("pulsar")) {
            return "tlqcn";
        }
        if  (file.getName().contains("pulsar")) {
            return file.getName().replace("pulsar", "tlqcn");
        }
        return file.getName();
    }

//    public static boolean notNeedModify(File file) {
//        return notNeedModifyDirs.stream().anyMatch(dir -> file.getAbsolutePath().contains(dir));
//    }

    public static String modifyFileName(String originalName) {
        return originalName.replace("Pulsar", "Tlqcn").replace("pulsar", "tlqcn");
    }

    public static void modifyFileContent(File file) throws IOException {
        List<String> beforeImportLines = new ArrayList<>();
        List<String> importStaticLines = new ArrayList<>();
        List<String> importLines = new ArrayList<>();
        List<String> afterImportLines = new ArrayList<>();
        boolean startImport = false;
        boolean endImport = false;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().startsWith("import")) {
                    startImport = true;
                    String modifiedLine = line.replace("org.apache.pulsar", "com.tongtech.tlqcn")
                            .replace("org.apache.bookkeeper", "com.tongtech.bookkeeper")
                            .replace("Pulsar", "Tlqcn")
                            .replace("pulsar", "tlqcn")
                            .replace("PULSAR", "TLQCN");
                    if (line.contains("static")) {
                        importStaticLines.add(modifiedLine);
                    } else {
                        importLines.add(modifiedLine);
                    }
                } else {
                    String modifiedLine = line
                            .replace("org\\.apache\\.pulsar", "com\\.tongtech\\.tlqcn")
                            .replace("org.apache.pulsar", "com.tongtech.tlqcn")
                            .replace("org.apache.bookkeeper", "com.tongtech.bookkeeper")
                            .replace("org/apache/pulsar", "com/tongtech/tlqcn")
                            .replace("org/apache/bookkeeper", "com/tongtech/bookkeeper")
                            .replace("apache-pulsar", "tlq-tlqcn")
                            .replace("Apache Pulsar", "TongLINK/Q Tlqcn")
                            .replace("4.0.7", "10.0.5.0")
                            .replace("<bookkeeper.version>4.17.2</bookkeeper.version>", "<bookkeeper.version>10.0.5.0</bookkeeper.version>")
                            .replace("org[\\\\/]apache[\\\\/]pulsar","com[\\\\/]tongtech[\\\\/]tlqcn")
                            .replace("Pulsar", "Tlqcn")
                            .replace("pulsar", "tlqcn")
                            .replace("PULSAR", "TLQCN");
                    if (!startImport) {
                        beforeImportLines.add(modifiedLine);
                    } else {
                        if (!line.trim().equals("") && !endImport){
                            endImport = true;
                            afterImportLines.add("");
                        }
                        if (endImport) {
                            afterImportLines.add(modifiedLine);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("读取文件时出现错误: " + e.getMessage());
            return;
        }

        List<String> staticCollect = importStaticLines.stream().map(s -> s.replace(";", "")).collect(Collectors.toList());
        Collections.sort(staticCollect);
        List<String> sortImportStatics = staticCollect.stream().map(s -> s + ";").collect(Collectors.toList());

        List<String> collect = importLines.stream().map(s -> s.replace(";", "")).collect(Collectors.toList());
        Collections.sort(collect);
        List<String> sortImports = collect.stream().map(s -> s + ";").collect(Collectors.toList());
        // 合并所有行
        List<String> allLines = new ArrayList<>();
        allLines.addAll(beforeImportLines);
        allLines.addAll(sortImportStatics);
        allLines.addAll(sortImports);
        allLines.addAll(afterImportLines);

        // 将排序后的内容写回文件
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