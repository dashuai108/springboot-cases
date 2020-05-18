package com.da.shuai.rocketmqprovider.util;

import java.io.*;

/**
 * java 序列化与反序列化
 *
 * @Author: dashuai
 * @Date: 2020/5/14 11:29
 */
public class SerialzableUtil {

    @SuppressWarnings("unchecked")
    public static <T> T parse(byte[] rec, Class<T> classType) {
        ByteArrayInputStream arrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            arrayInputStream = new ByteArrayInputStream(rec);
            objectInputStream = new ObjectInputStream(arrayInputStream);
            T t = (T) objectInputStream.readObject();
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(arrayInputStream);
            closeQuietly(objectInputStream);
        }
        return null;
    }

    public static byte[] toByte(Object obj) {
        ByteArrayOutputStream arrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            arrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            byte[] rtn = arrayOutputStream.toByteArray();
            return rtn;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(objectOutputStream);
            closeQuietly(arrayOutputStream);
        }
        return null;
    }

    public static void closeQuietly(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
    }

    public static void closeQuietly(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (Exception e) {
            }
        }
    }
}
