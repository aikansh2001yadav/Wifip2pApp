package com.example.wifip2papp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;

import androidx.loader.content.CursorLoader;

public class Utility {

	/**
	 * �����ļ���׺����ö�Ӧ��MIME���͡�
	 * @param file
	 */
	static private String getMIMEType(File file)
	{
	    String type="*/*";
	    String fName=file.getName();
	    //��ȡ��׺��ǰ�ķָ���"."��fName�е�λ�á�
	    int dotIndex = fName.lastIndexOf(".");
	    if(dotIndex < 0){
	        return type;
	    }
	    /* ��ȡ�ļ��ĺ�׺�� */
	    String end=fName.substring(dotIndex,fName.length()).toLowerCase();
	    if(end=="")return type;
	    //��MIME���ļ����͵�ƥ������ҵ���Ӧ��MIME���͡�
	    for(int i=0;i<MIME_MapTable.length;i++){
	        if(end.equals(MIME_MapTable[i][0]))
	            type = MIME_MapTable[i][1];
	    }
	    return type;
	}
	/**
	 * ���ļ�
	 * @param activity
	 * @param file
	 */
	static public void openFile(Activity activity, File file){
	    //Uri uri = Uri.parse("file://"+file.getAbsolutePath());
	    Intent intent = new Intent();
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    //����intent��Action����
	    intent.setAction(Intent.ACTION_VIEW);
	    //��ȡ�ļ�file��MIME����
	    String type = getMIMEType(file);
	    //����intent��data��Type���ԡ�
	    intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
	    //��ת
	    activity.startActivity(intent);
	}

	//����һ��MIME�������ļ���׺����ƥ��� 	"�ļ����͡���MIME����"��ƥ���:
	static private final String[][] MIME_MapTable={
	    //{��׺����    MIME����}
	    {".3gp",    "video/3gpp"},
	    {".apk",    "application/vnd.android.package-archive"},
	    {".asf",    "video/x-ms-asf"},
	    {".avi",    "video/x-msvideo"},
	    {".bin",    "application/octet-stream"},
	    {".bmp",      "image/bmp"},
	    {".c",        "text/plain"},
	    {".class",    "application/octet-stream"},
	    {".conf",    "text/plain"},
	    {".cpp",    "text/plain"},
	    {".doc",    "application/msword"},
	    {".exe",    "application/octet-stream"},
	    {".gif",    "image/gif"},
	    {".gtar",    "application/x-gtar"},
	    {".gz",        "application/x-gzip"},
	    {".h",        "text/plain"},
	    {".htm",    "text/html"},
	    {".html",    "text/html"},
	    {".jar",    "application/java-archive"},
	    {".java",    "text/plain"},
	    {".jpeg",    "image/jpeg"},
	    {".jpg",    "image/jpeg"},
	    {".js",        "application/x-javascript"},
	    {".log",    "text/plain"},
	    {".m3u",    "audio/x-mpegurl"},
	    {".m4a",    "audio/mp4a-latm"},
	    {".m4b",    "audio/mp4a-latm"},
	    {".m4p",    "audio/mp4a-latm"},
	    {".m4u",    "video/vnd.mpegurl"},
	    {".m4v",    "video/x-m4v"},
	    {".mov",    "video/quicktime"},
	    {".mp2",    "audio/x-mpeg"},
	    {".mp3",    "audio/x-mpeg"},
	    {".mp4",    "video/mp4"},
	    {".mpc",    "application/vnd.mpohun.certificate"},
	    {".mpe",    "video/mpeg"},
	    {".mpeg",    "video/mpeg"},
	    {".mpg",    "video/mpeg"},
	    {".mpg4",    "video/mp4"},
	    {".mpga",    "audio/mpeg"},
	    {".msg",    "application/vnd.ms-outlook"},
	    {".ogg",    "audio/ogg"},
	    {".pdf",    "application/pdf"},
	    {".png",    "image/png"},
	    {".pps",    "application/vnd.ms-powerpoint"},
	    {".ppt",    "application/vnd.ms-powerpoint"},
	    {".prop",    "text/plain"},
	    {".rar",    "application/x-rar-compressed"},
	    {".rc",        "text/plain"},
	    {".rmvb",    "audio/x-pn-realaudio"},
	    {".rtf",    "application/rtf"},
	    {".sh",        "text/plain"},
	    {".tar",    "application/x-tar"},
	    {".tgz",    "application/x-compressed"},
	    {".txt",    "text/plain"},
	    {".wav",    "audio/x-wav"},
	    {".wma",    "audio/x-ms-wma"},
	    {".wmv",    "audio/x-ms-wmv"},
	    {".wps",    "application/vnd.ms-works"},
	    //{".xml",    "text/xml"},
	    {".xml",    "text/plain"},
	    {".z",        "application/x-compress"},
	    {".zip",    "application/zip"},
	    {"",        "*/*"}
	};

	static public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (inetAddress instanceof Inet4Address)
						if (!inetAddress.isLoopbackAddress()) {
							return inetAddress.getHostAddress();
						}
				}
			}
		} catch (SocketException ex) {
			Log.e("NetworkAddress", "getLocalIPAddress()", ex);
		} catch (NullPointerException ex) {
			Log.e("NetworkAddress", "getLocalIPAddress()", ex);
		}
		return null;
	}

	static public boolean sendPeerInfo(String host, int port) {
		Socket socket = new Socket();
		String strIP = getLocalIpAddress();
		boolean result = true;
		Log.d(WiFiDirectActivity.TAG, "peer:" + strIP);
		try {
			Log.d(WiFiDirectActivity.TAG, "Opening client socket - ");
			socket.bind(null);
			socket.connect((new InetSocketAddress(host, port)), ConfigInfo.SOCKET_TIMEOUT);// host

			Log.d(WiFiDirectActivity.TAG,
					"Client socket - " + socket.isConnected());
			OutputStream stream = socket.getOutputStream();
			stream.write(ConfigInfo.COMMAND_ID_SEND_PEER_INFO);// id
			String strSend = "peer:" + strIP + "port:" + port;
			stream.write(strSend.getBytes(), 0, strSend.length());
			Log.d(WiFiDirectActivity.TAG, "Client: Data written strSend:"
					+ strSend);

		} catch (IOException e) {
			Log.e(WiFiDirectActivity.TAG, e.getMessage());
			result = false;
		} finally {
			if (socket != null) {
				if (socket.isConnected()) {
					try {
						socket.close();
						Log.d(WiFiDirectActivity.TAG, "socket.close();");
					} catch (IOException e) {
						// Give up
						e.printStackTrace();
					}
				}
			}
		}
		return result;
	}

//	static public boolean sendFileInfo(String name, int size, String host, int port) {
//		Socket socket = new Socket();
//		try {
//			Log.d(WiFiDirectActivity.TAG, "Opening client socket - ");
//			socket.bind(null);
//			socket.connect((new InetSocketAddress(host, port)), ConfigInfo.SOCKET_TIMEOUT);
//
//			Log.d(WiFiDirectActivity.TAG,
//					"Client socket - " + socket.isConnected());
//			OutputStream stream = socket.getOutputStream();
//			String strSend = "size:" + size + "name:" + name;
//			stream.write(ConfigInfo.COMMAND_ID_REQUEST_SEND_FILE);// id
//			stream.write(strSend.length());
//			stream.write(strSend.getBytes(), 0, strSend.length());
//			Log.d(WiFiDirectActivity.TAG, "Client: Data written strSend:"
//					+ strSend);
//			return true;
//
//		} catch (IOException e) {
//			Log.e(WiFiDirectActivity.TAG, e.getMessage());
//			return false;
//		} finally {
//			if (socket != null) {
//				if (socket.isConnected()) {
//					try {
//						socket.close();
//						Log.d(WiFiDirectActivity.TAG, "socket.close();");
//					} catch (IOException e) {
//						// Give up
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//	}

//	static public Pair<String, Integer> getFileNameAndSize(Activity activaty, Uri uri)
//			throws IOException {
//		try {
//		String[] proj = { MediaStore.Images.Media.DATA, MediaStore.Video.Media.DATA,
//				MediaStore.Audio.Media.DATA, MediaStore.Files.FileColumns.DATA };
//		Cursor actualimagecursor = activaty.managedQuery(uri,
//				proj, null, null, null);
//		int actual_image_column_index = actualimagecursor
//				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//		actualimagecursor.moveToFirst();
//		String img_path = actualimagecursor
//				.getString(actual_image_column_index);
//		File file = new File(img_path);
//		FileInputStream fis = new FileInputStream(file);
//		int fileLen = fis.available();
//		fis.close();
//
//		return new Pair<String, Integer>(file.getName(), fileLen);
//		} catch (IOException e) {
//			Log.e(WiFiDirectActivity.TAG, e.getMessage());
//			return null;
//		}
//	}

//	public void sendFileInfo2(Activity activaty, Uri uri) {
//		Socket socket = new Socket();
//		int port = ConfigInfo.LISTEN_PORT;
//		try {
//			Log.d(WiFiDirectActivity.TAG, "Opening client socket - ");
//			socket.bind(null);
//			socket.connect((new InetSocketAddress("192.168.49.1", port)), ConfigInfo.SOCKET_TIMEOUT);// host
//
//			Log.d(WiFiDirectActivity.TAG,
//					"Client socket - " + socket.isConnected());
//			OutputStream stream = socket.getOutputStream();
//
//			String[] proj = { MediaStore.Images.Media.DATA };
//			Cursor actualimagecursor = activaty.managedQuery(uri,
//					proj, null, null, null);
//			int actual_image_column_index = actualimagecursor
//					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//			actualimagecursor.moveToFirst();
//			String img_path = actualimagecursor
//					.getString(actual_image_column_index);
//			File file = new File(img_path);
//			FileInputStream fis = new FileInputStream(file);
//			int fileLen = fis.available();
//			fis.close();
//			String strSend = "size:" + fileLen + "name:" + file.getName();
//			stream.write(ConfigInfo.COMMAND_ID_REQUEST_SEND_FILE);// id
//			stream.write(strSend.length());
//			stream.write(strSend.getBytes(), 0, strSend.length());
//			Log.d(WiFiDirectActivity.TAG, "Client: Data written strSend:"
//					+ strSend);
//
//		} catch (IOException e) {
//			Log.e(WiFiDirectActivity.TAG, e.getMessage());
//		} finally {
//			if (socket != null) {
//				if (socket.isConnected()) {
//					try {
//						socket.close();
//						Log.d(WiFiDirectActivity.TAG, "socket.close();");
//					} catch (IOException e) {
//						// Give up
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//	}

	static public boolean sendStream (String host, int port, InputStream data) {
		Socket socket = new Socket();
		boolean result = true;

		try {
			Log.d(WiFiDirectActivity.TAG, "Opening client socket - ");
			socket.bind(null);
			socket.connect((new InetSocketAddress(host, port)), ConfigInfo.SOCKET_TIMEOUT);// host

			Log.d(WiFiDirectActivity.TAG,
					"Client socket - " + socket.isConnected());
			OutputStream stream = socket.getOutputStream();
			copyStream(data, stream);
			Log.d(WiFiDirectActivity.TAG, "Client: Data written data's length:" + data.available());

		} catch (IOException e) {
			Log.e(WiFiDirectActivity.TAG, e.getMessage());
			result = false;
		} finally {
			if (socket != null) {
				if (socket.isConnected()) {
					try {
						socket.close();
						Log.d(WiFiDirectActivity.TAG, "socket.close();");
					} catch (IOException e) {
						// Give up
						e.printStackTrace();
					}
				}
			}
		}
		return result;
	}

	static public long copyStream(InputStream ins, OutputStream outs) {
		long copyLen = 0;
		byte buf[] = new byte[1024];
		int len;
		try {
			while ((len = ins.read(buf)) != -1) {
				outs.write(buf, 0, len);
				copyLen = copyLen + len;
			}
		} catch (IOException e) {
			Log.d(WiFiDirectActivity.TAG, e.toString());
			return 0;
		}
		return copyLen;
	}

	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @author paulburke
	 */
//	public static String getPath(final Context context, final Uri uri) {
//
//		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//
//		// DocumentProvider
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//				// ExternalStorageProvider
//				if (isExternalStorageDocument(uri)) {
//					final String docId;
//					docId = DocumentsContract.getDocumentId(uri);
//					final String[] split = docId.split(":");
//					final String type = split[0];
//
//					if ("primary".equalsIgnoreCase(type)) {
//						return Environment.getExternalStorageDirectory() + "/" + split[1];
//					}
//
//					// TODO handle non-primary volumes
//				}
//				// DownloadsProvider
//				else if (isDownloadsDocument(uri)) {
//
//					final String id = DocumentsContract.getDocumentId(uri);
//					final Uri contentUri = ContentUris.withAppendedId(
//							Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//					return getDataColumn(context, contentUri, null, null);
//				}
//				// MediaProvider
//				else if (isMediaDocument(uri)) {
//					final String docId = DocumentsContract.getDocumentId(uri);
//					final String[] split = docId.split(":");
//					final String type = split[0];
//
//					Uri contentUri = null;
//					if ("image".equals(type)) {
//						contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//					} else if ("video".equals(type)) {
//						contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//					} else if ("audio".equals(type)) {
//						contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//					}
//
//					final String selection = "_id=?";
//					final String[] selectionArgs = new String[] {
//							split[1]
//					};
//
//					return getDataColumn(context, contentUri, selection, selectionArgs);
//				}
//			}
//			// MediaStore (and general)
//			else if ("content".equalsIgnoreCase(uri.getScheme())) {
//				return getDataColumn(context, uri, null, null);
//			}
//			// File
//			else if ("file".equalsIgnoreCase(uri.getScheme())) {
//				return uri.getPath();
//			}
//		}
//
//		return null;
//	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
//	public static String getDataColumn(Context context, Uri uri, String selection,
//									   String[] selectionArgs) {
//
//		Cursor cursor = null;
//		final String column = "_data";
//		final String[] projection = {
//				column
//		};
//
//		try {
//			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
//					null);
//			if (cursor != null && cursor.moveToFirst()) {
//				final int column_index = cursor.getColumnIndexOrThrow(column);
//				return cursor.getString(column_index);
//			}
//		} finally {
//			if (cursor != null)
//				cursor.close();
//		}
//		return null;
//	}


//	/**
//	 * @param uri The Uri to check.
//	 * @return Whether the Uri authority is ExternalStorageProvider.
//	 */
//	public static boolean isExternalStorageDocument(Uri uri) {
//		return "com.android.externalstorage.documents".equals(uri.getAuthority());
//	}
//
//	/**
//	 * @param uri The Uri to check.
//	 * @return Whether the Uri authority is DownloadsProvider.
//	 */
//	public static boolean isDownloadsDocument(Uri uri) {
//		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
//	}
//
//	/**
//	 * @param uri The Uri to check.
//	 * @return Whether the Uri authority is MediaProvider.
//	 */
//	public static boolean isMediaDocument(Uri uri) {
//		return "com.android.providers.media.documents".equals(uri.getAuthority());
//	}

}
