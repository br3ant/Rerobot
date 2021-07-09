package com.bayi.rerobot.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.io.Writer;

public class FileUtil {

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static synchronized boolean isFileExists(String filePath) {
        return isFileExists(getFileFromPath(filePath));
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static synchronized boolean isFileExists(File file) {
        return file != null && file.exists();
    }


    /**
     * 重命名文件
     *
     * @param filePath 文件路径
     * @param newName  新名称
     * @return {@code true}: 重命名成功<br>{@code false}: 重命名失败
     */
    public static synchronized boolean renameFile(String filePath, String newName) {
        return rename(getFileFromPath(filePath), newName);
    }

    /**
     * 重命名文件
     *
     * @param file    文件
     * @param newName 新名称
     * @return {@code true}: 重命名成功<br>{@code false}: 重命名失败
     */
    public static synchronized boolean rename(File file, String newName) {
        // 文件为空返回false
        if (file == null) return false;
        // 文件不存在返回false
        if (!file.exists()) return false;
        // 新的文件名为空返回false
        if (TextUtils.isEmpty(newName)) return false;
        // 如果文件名没有改变返回true
        if (newName.equals(file.getName())) return true;
        File newFile = new File(file.getParent() + File.separator + newName);
        // 如果重命名的文件已存在返回false
        return !newFile.exists()
                && file.renameTo(newFile);
    }

    /**
     * 判断是否是目录
     *
     * @param dirPath 目录路径
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static synchronized boolean isDir(String dirPath) {
        return isDir(getFileFromPath(dirPath));
    }

    /**
     * 判断是否是目录
     *
     * @param file 文件
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static synchronized boolean isDir(File file) {
        return isFileExists(file) && file.isDirectory();
    }

    /**
     * 判断是否是文件
     *
     * @param filePath 文件路径
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static synchronized boolean isFile(String filePath) {
        return isFile(getFileFromPath(filePath));
    }

    /**
     * 判断是否是文件
     *
     * @param file 文件
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static synchronized boolean isFile(File file) {
        return isFileExists(file) && file.isFile();
    }


    /**
     * 重命名文件
     *
     * @param file    文件
     * @param newName 新名字
     * @return 新文件
     */
    public static synchronized File renameFile(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (!newFile.equals(file)) {
            if (newFile.exists()) {
                if (newFile.delete()) {
                    Log.d("FileUtil", "Delete old " + newName + " file");
                }
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to " + newName);
            }
        }
        return newFile;
    }

    public static synchronized Uri getUriFromFilePath(String filePath) {
        return getUriFromFile(getFileFromPath(filePath));
    }

    public static synchronized Uri getUriFromFile(File file) {
        return file == null ? null : Uri.fromFile(file);
    }

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public static synchronized File getFileFromPath(String filePath) {
        return TextUtils.isEmpty(filePath) ? null : new File(filePath);
    }

    public static synchronized File getFileFromUri(Context context, Uri fileUri) {
        String filePath = getRealPathFromURI(context, fileUri);
        return TextUtils.isEmpty(filePath) ? null : new File(filePath);
    }

    /**
     * 获取真实的路径
     *
     * @param context 上下文
     * @param uri     file uri
     * @return 文件路径
     */
    public static synchronized String getRealPathFromURI(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String realPath = cursor.getString(index);
            cursor.close();
            return realPath;
        }
    }

    /**
     * 获取文件名称
     *
     * @param context 上下文
     * @param uri     uri
     * @return 文件名称
     */
    static String getFileNameFromUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static String getMimeType(String filePath) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
        if (TextUtils.isEmpty(extension)) {
            return "file/*";
        }
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if (!TextUtils.isEmpty(mimeType)) {
            return mimeType;
        }
        return "file/*";
    }

    /*public static String saveMsgToUpload(String msg) {

        StringBuffer sb = new StringBuffer();

        sb.append(msg);

        Writer writer = new StringWriter();

        String result = writer.toString();
        sb.append(result);
        try {

            String fileName = "msg.txt";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File("mnt/sdcard/bayi/");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(new File(dir, fileName),true);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {

        }
        return null;
    }*/
}
