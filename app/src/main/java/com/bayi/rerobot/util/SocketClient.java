package com.bayi.rerobot.util;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import static java.lang.Thread.sleep;

/**
 * socket客户端的多线程实现
 */
public class SocketClient
{
	private Socket client;
	private Context context;
	private int port;           //IP
	private String site;            //端口
	private Thread thread;
	public static Handler mHandler;
	private boolean isClient=false;
	private PrintWriter out;
	private InputStream in;
	private String str;

	/**
	 * @effect 开启线程建立连接开启客户端
	 * */
	public void openClientThread(){
		new Thread(new Runnable( )
		{
			@Override
			public void run()
			{
				while (true){
					if(client==null||!client.isConnected()){
						Log.e("socketclient","正在连接");
						try {
							/**
							 *  connect()步骤
							 * */
							client=new Socket( site,port );

//                    client.setSoTimeout ( 5000 );//设置超时时间
							if (client!=null)
							{
								isClient=true;
								forOut();
								forIn ();
							}else {
								isClient=false;
								Log.e("socketclient","socket连接失败");
							}
							Log.i ( "socketclient","site="+site+" ,port="+port );
						}catch (Exception e) {
							e.printStackTrace ();
							Log.i ( "socketclient","6" );

						}

					}
					try {
						sleep(5000);//五秒判断一次
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}}}).start();


	}

	/**
	 * 调用时向类里传值
	 * */
	public void clintValue(String site, int port)
	{
		this.site=site;
		this.port=port;
	}

	/**
	 * @effect 得到输出字符串
	 * */
	public void forOut()
	{
		try {
			out=new PrintWriter( client.getOutputStream () );
		}catch (IOException e){
			e.printStackTrace ();
			Log.i ( "socketclient","8" );
		}
	}
	public  void closed(){
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @steps read();
	 * @effect 得到输入字符串
	 * */
	public void forIn(){

		while (isClient) {
			try {
				in=client.getInputStream ();

				/**得到的是16进制数，需要进行解析*/
				byte[] bt = new byte[50];
				in.read ( bt );
				str=new String( bt,"UTF-8" );
			} catch (IOException e) {}
			if (str!=null) {
				Message msg = new Message( );
				msg.obj =str ;
				mHandler.sendMessage ( msg );
			}

		}
	}

	/**
	 * @steps write();
	 * @effect 发送消息
	 * */
	public void sendMsg(final String[][] cmd)
	{

		new Thread(new Runnable( )
		{
			@Override
			public void run()
			{
				for (int i = 0; i < cmd.length; i++) {
				try {
				        String []msgCmd=cmd[i];
						System.out.println("Client：Connecting");
						//IP地址和端口号（对应服务端），我这的IP是本地路由器的IP地址
						Socket socket = new Socket(site, port);
						//发送给服务端的消息
						StringBuilder sb = new StringBuilder();
						for (String s1 : msgCmd) {
							sb.append(s1);
						}

						byte[] message = CRC16M.HexString2Buf2(sb.toString());
						try {
							System.out.println("Client Sending: '" + message + Arrays.toString(message));

							//第二个参数为True则为自动flush
							OutputStream out = socket.getOutputStream();
							out.write(message);
							out.close();
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							//关闭Socket
							socket.close();
							System.out.println("Client:Socket closed");
						}
					} catch(UnknownHostException e1){
						e1.printStackTrace();
					} catch(IOException e){
						e.printStackTrace();
					}
					try {
						sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		} ).start ();

	}



}
