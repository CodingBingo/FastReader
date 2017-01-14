package com.codingbingo.fastreader.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.codingbingo.fastreader.model.LocalFile;
import com.codingbingo.fastreader.utils.encode.BytesEncodingDetect;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/1/5.
 */

public class FileUtils {
    /**
     * 得到文件的编码
     *
     * @param filePath 文件路径
     * @return 文件的编码
     */
    public static String getJavaEncode(String filePath) {
        BytesEncodingDetect bytesEncodingDetect = new BytesEncodingDetect();
        String fileCode = BytesEncodingDetect.javaname[
                bytesEncodingDetect.detectEncoding(new File(filePath))
                ];
        return fileCode;
    }

    /**
     * 从媒体库中获取指定后缀的文件列表
     *
     * @param context
     * @param searchFileSuffix 文件后缀列表，eg: new String[]{"epub","mobi","pdf","txt"};
     * @return
     */
    public static List<LocalFile> getSupportFileList(Context context, String[] searchFileSuffix) {
        ArrayList<LocalFile> searchFileList = null;
        if (null == context || null == searchFileSuffix
                || searchFileSuffix.length == 0) {
            return null;
        }

        String searchPath = "";
        int length = searchFileSuffix.length;
        for (int index = 0; index < length; index++) {
            searchPath += (MediaStore.Files.FileColumns.DATA + " LIKE '%" + searchFileSuffix[index] + "' ");
            if ((index + 1) < length) {
                searchPath += "or ";
            }
        }
        searchFileList = new ArrayList<>();
        Uri uri = MediaStore.Files.getContentUri("external");
        Cursor cursor = context.getContentResolver().query(
                uri, new String[]{MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.SIZE}, searchPath, null, null);

        if (cursor == null) {
            System.out.println("Cursor 获取失败!");
        } else {
            if (cursor.moveToFirst()) {
                do {
                    String filepath = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                    if (filepath != null) {
                        File file = new File(filepath);
                        if (file.exists()) {
                            try {
                                String path = new String(filepath.getBytes("UTF-8"));

                                String fileName = path.substring(path.lastIndexOf(File.separator) + 1);
                                long fileSize = file.getTotalSpace();

                                LocalFile localFile = new LocalFile();
                                localFile.setFileName(fileName);
                                localFile.setFilePath(path);
                                localFile.setFileSize(fileSize);

                                searchFileList.add(localFile);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } while (cursor.moveToNext());
            }

            if (!cursor.isClosed()) {
                cursor.close();
            }
        }

        return searchFileList;
    }

    /**
     * 判断文件是否存在
     */
    public static boolean isFileExist(String filePath) {
        if (filePath == null) {
            return false;
        }

        File file = new File(filePath);
        return file.exists();
    }
}
