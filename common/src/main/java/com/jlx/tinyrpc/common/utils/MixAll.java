package com.jlx.tinyrpc.common.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author liqiang.dong
 * @date 2018/5/30 17:13
 */
public class MixAll {

    public static String file2String(final String fileName) throws IOException {
        File file = new File(fileName);
        return file2String(file);
    }

    public static String file2String(final File file) throws IOException {
        if (file.exists()) {
            byte[] data = new byte[(int) file.length()];
            boolean result;

            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
                int len = inputStream.read(data);
                result = len == data.length;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }

            if (result) {
                return new String(data);
            }
        }
        return null;
    }

    public static String file2String(final URL url) {
        InputStream in = null;
        try {
            URLConnection urlConnection = url.openConnection();
            urlConnection.setUseCaches(false);
            in = urlConnection.getInputStream();
            int len = in.available();
            byte[] data = new byte[len];
            in.read(data, 0, len);
            return new String(data, "UTF-8");
        } catch (Exception ignored) {
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }

        return null;
    }

    public static void string2File(final String str, final String fileName) throws IOException {

        String tmpFile = fileName + ".tmp";
        string2FileNotSafe(str, tmpFile);

        String bakFile = fileName + ".bak";
        String prevContent = file2String(fileName);
        if (prevContent != null) {
            string2FileNotSafe(prevContent, bakFile);
        }

        File file = new File(fileName);
        file.delete();

        file = new File(tmpFile);
        file.renameTo(new File(fileName));
    }

    public static void string2FileNotSafe(final String str, final String fileName) throws IOException {
        File file = new File(fileName);
        File fileParent = file.getParentFile();
        if (fileParent != null) {
            fileParent.mkdirs();
        }
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(str);
        } catch (IOException e) {
            throw e;
        } finally {
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
    }

}
