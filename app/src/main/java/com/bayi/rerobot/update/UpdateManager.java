package com.bayi.rerobot.update;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;


import androidx.core.content.FileProvider;

import com.bayi.rerobot.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class UpdateManager {
	private static final int DOWNLOAD = 1;
	private static final int DOWNLOAD_FINISH = 2;
	HashMap<String, String> mHashMap;
	private String mSavePath;
	private int progress;
	private boolean cancelUpdate = false;

	private Context mContext;
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD:
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context) {
		this.mContext = context;
	}

//	public void checkUpdate() {
//		if (isUpdate()) {
//			showNoticeDialog(mContext);
//		} else {
//			Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG)
//					.show();
//		}
//	}

	public boolean isUpdate() {
		String versionCode = getVersionCode(mContext) + "";

		// int versionCode =2;

		System.out.println("本软件的版本是：" + versionCode);
		HttpURLConnection connection;
		URL url;
		InputStream stream = null;
		try {
			url = new URL("http://115.231.60.194:9090/public/uploads/apk/new.xml");
			// url = new
			// URL("http://yzj.cxcard.com/appupdate/CXWMS_version.xml");
			// >http://yzj.cxcard.com/appupdate/vip19_sxgc_version.xml
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			stream = connection.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (stream == null) {
			return false;
		}
		ParseXmlService service = new ParseXmlService();
		try {
			mHashMap = service.parseXml(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != mHashMap) {
			String serviceCode = mHashMap.get("version").toString().trim()
					.toString();
			if (Integer.parseInt(serviceCode) > Integer.parseInt(versionCode)) {
				return true;
			}
		}
		return false;
	}

	private int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			versionCode = context.getPackageManager().getPackageInfo(
					"com.bayi.rerobot", 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	public void showNoticeDialog(Context context) {
		Builder builder = new Builder(context);
		builder.setTitle("软件更新");
		builder.setMessage("检查到新版本，需要更新吗");
		builder.setPositiveButton("更新",
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						//showDownloadDialog();
					}
				});
		builder.setNegativeButton("稍后更新",
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	/*private void showDownloadDialog() {
		Builder builder = new Builder(mContext);
		builder.setTitle("正在更新");
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progressBar);
		builder.setView(v);
		builder.setNegativeButton("取消更新",
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						cancelUpdate = true;
					}
				});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		mDownloadDialog.setCancelable(false);
		downloadApk();
	}*/
	private DownloadManager downloadManager;
	//下载的ID
	private long downloadId;
	private String name;
	private String pathstr;
	public void downloadAPK() {
		try {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String sdpath = Environment.getExternalStorageDirectory()
					+ "/";
			mSavePath = sdpath;
			String url =mHashMap.get("url").trim();
			//创建下载任务
			DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
			//移动网络情况下是否允许漫游
			request.setAllowedOverRoaming(false);
			//在通知栏中显示，默认就是显示的
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
			request.setTitle("机器人apk");
			request.setDescription("新版机器人下载中...");
			request.setVisibleInDownloadsUi(true);

			//设置下载的路径
			File file = new File(mSavePath, mHashMap.get("name").trim());
			if (file.exists()) {
				file.delete();
			}
			request.setDestinationUri(Uri.fromFile(file));
			pathstr = file.getAbsolutePath();
			//获取DownloadManager
			if (downloadManager == null)
				downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
			//将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
			if (downloadManager != null) {
				downloadId = downloadManager.enqueue(request);
			}

		//注册广播接收者，监听下载状态
		mContext.registerReceiver(receiver,
				new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		 }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//广播监听下载的各个状态
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			checkStatus();
		}
	};
	//检查下载状态
	private void checkStatus() {
		DownloadManager.Query query = new DownloadManager.Query();
		//通过下载的id查找
		query.setFilterById(downloadId);
		Cursor cursor = downloadManager.query(query);
		if (cursor.moveToFirst()) {
			int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
			switch (status) {
				//下载暂停
				case DownloadManager.STATUS_PAUSED:
					break;
				//下载延迟
				case DownloadManager.STATUS_PENDING:
					break;
				//正在下载
				case DownloadManager.STATUS_RUNNING:
					break;
				//下载完成
				case DownloadManager.STATUS_SUCCESSFUL:
					//下载完成安装APK
					installApk();
					cursor.close();
					break;
				//下载失败
				case DownloadManager.STATUS_FAILED:
					App.toast("下载失败");
					cursor.close();
					mContext.unregisterReceiver(receiver);
					break;
			}
		}
	}

	private void downloadApk() {
		new downloadApkThread().start();
	}

	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath;
					URL url = new URL(mHashMap.get("url").trim());
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, mHashMap.get("name")
							.trim());
					FileOutputStream fos = new FileOutputStream(apkFile);

					int count = 0;
					byte buf[] = new byte[1024];
					do {
						int numread = is.read(buf);
						count += numread;
						progress = (int) (((float) count / length) * 100);
						mHandler.sendEmptyMessage(DOWNLOAD);
//						System.out
//								.println((int) (((float) count / length) * 100));
						if (numread <= 0) {
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);

							break;
						}
						// if ((int) (((float) count / length) * 100) == 100) {
						// mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
						// break;
						// }
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mDownloadDialog.dismiss();
			cancelUpdate=false;
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, mHashMap.get("name").trim());
		if (!apkfile.exists()) {
			return;
		}

		Uri apkUri =
				FileProvider.getUriForFile(mContext, "com.bayi.rerobot.fileprovider", apkfile);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		// 由于没有在Activity环境下启动Activity,设置下面的标签
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//添加这一句表示对目标应用临时授权该Uri所代表的文件
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
		mContext.startActivity(intent);


	}
}
