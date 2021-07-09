package com.bayi.rerobot.util;

/**
 * @author yinxiaowei
 * @date 2020/7/4
 * @description:
 */

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Android运行linux命令
 */
public final class USBCardFinerUtil {
    private static ArrayList<String> cardNameList = new ArrayList<>();

    static {
        cardNameList.add("Bothlent UAC Dongle");
        cardNameList.add("AC108 USB Audio");
    }

    private static final String TAG = "USBCardFinerUtil";
    private static boolean mHaveRoot = false;
    private static int cardNum = 0;

    /**
     * 判断机器Android是否已经root，即是否获取root权限
     */
    public static boolean haveRoot() {
        if (!mHaveRoot) {
            int ret = execRootCmdSilent("echo test"); // 通过执行测试命令来检测
            if (ret != -1) {
                Log.i(TAG, "have root!");
                mHaveRoot = true;
            } else {
                Log.i(TAG, "not root!");
            }
        } else {
            Log.i(TAG, "mHaveRoot = true, have root!");
        }
        return mHaveRoot;
    }

    public static int fetchCards() {
        cardNum = execRootCmd("cat /proc/asound/cards", cardNameList);
        return cardNum;
    }


    /**
     * 执行命令并且输出结果
     */
    private static int execRootCmd(String cmd, List<String> cardNameList) {
        int cardN = -1;
        DataOutputStream dos = null;
        DataInputStream dis = null;
        try {
            Process p = Runtime.getRuntime().exec("su");// 经过Root处理的android系统即有su命令
            dos = new DataOutputStream(p.getOutputStream());
            dis = new DataInputStream(p.getInputStream());

            Log.i(TAG, cmd);
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line = null;
            //while ((line = dis.readUTF()) != null) {

            while ((line = dis.readLine()) != null) {
                for (String cardName : cardNameList) {
                    if (line.contains(cardName)) {
                        Log.d(TAG, "Find USB card:" + line);
                        line = line.replace('[', ',');
                        line = line.replace(']', ',');
                        Log.d(TAG, "Find USB card parse:" + line);
                        String[] strs = line.split(",");
                        if (strs.length > 0) {
                            String numStr = strs[0].trim();
                            cardN = Integer.parseInt(numStr);
                            Log.d(TAG, "USB card Number=" + cardN);
                            return cardN;
                        }
                    }
                }
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cardN;
    }

    /**
     * 执行命令但不关注结果输出
     */
    private static int execRootCmdSilent(String cmd) {
        int result = -1;
        DataOutputStream dos = null;

        try {
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());

            Log.i(TAG, cmd);
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            p.waitFor();
            result = p.exitValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
