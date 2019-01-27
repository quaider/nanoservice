package com.quaider.nanoservice.blockchain.core.io;

import java.io.*;

public class PlainFileManager {

    /**
     * 将字符串写入指定的路径
     * @param fileName  文件全路径
     * @param content   文件字符形式内容
     */
    public static void saveFile(String fileName, String content) throws IOException {
        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.write(content);
        }
    }

    /**
     * 读取整个文件内容
     * @param path  文件全路径
     * @return  文件内容
     */
    public static String readAllText(String path) throws FileNotFoundException, IOException {
        File file = new File(path);
        try (FileReader fileReader = new FileReader(file);
             BufferedReader reader = new BufferedReader(fileReader);
             StringWriter sw = new StringWriter()) {

            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if (i > 0)
                    sw.write("\n");
                sw.write(line);
                i++;
            }

            return sw.toString();
        }
    }
}
