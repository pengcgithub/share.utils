package com.share.network;

import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * FTP工具类<br>
 * 用于操作文件上传、下载、删除等操作
 *
 * FTP工具类缺少登录实现，此方法需要对应不同的用户名和密码实现，具体的实现步骤使用者可以根据自己的喜好修改。
 *
 * @author pengc
 * @since 2017/7/25
 */
public class FtpUtil {

	private static FtpClient ftpClient;

	/**
	 * 连接FTP服务器<br/>
	 *
	 * @param ip  ip地址
	 * @param port 端口号
	 * @param user 用户名
	 * @param password  密码
	 * @param path  FTP的根目录
	 * @return true 连接成功，否则 连接失败
	 */
	public static boolean connectServer(String ip, int port, String user, String password, String path) {
		boolean connectStatus = false;
		try {
			ftpClient = FtpClient.create();
			try {
				SocketAddress address = new InetSocketAddress(ip, port);
				ftpClient.connect(address);
				ftpClient.login(user, password.toCharArray());
				// 用2进制上传、下载
				ftpClient.setBinaryType();
				if (path != null && path.length() != 0) {
					// 把远程系统上的目录切换到参数path所指定的目录
					ftpClient.changeDirectory(path);
				}

				connectStatus = true;
			} catch (FtpProtocolException e) {
				e.printStackTrace();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return connectStatus;
	}


	/**
	 * 关闭连接
	 */
	public static void closeConnect() {
		try {
			ftpClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 登录FTP服务器<br/>
	 * @return true 登录成功， false 登录失败
	 */
	public static Boolean loginToFtpServer() {
		String serverUrl = null,
				userName = null,
				passWord = null;

		//todo 登录方法需要获取对应文件服务器的用户名或密码

		return connectServer(serverUrl, 21, userName, passWord, null);
	}


	/**
	 * 通过输入流的方式上传文件至FTP服务器<br/>
	 *
	 * @param inputStream 输入流文件
	 * @param serverPath 服务器路径
	 * @param fileName 文件名称
	 * @return true 上传成功，否则上传失败
	 */
	public static boolean uploadToServer(InputStream inputStream,
                                         String serverPath, String fileName) throws Exception {
		boolean uploadStatus = false;
		OutputStream os = null;
		InputStream is = null;
		try {
			boolean loginStatus = loginToFtpServer();
			if (!loginStatus) return false;

			// 检查路径是否存在，如果不存在则创建
			if (!isDirExist(serverPath.replace("\\", "/"))) {
				createDir(serverPath.replace("\\", "/"));
				ftpClient.changeDirectory(serverPath.replace("\\", "/"));
			}

			try {
				// 将远程文件加入输出流中
				String remoteFile = serverPath + "/" + fileName;
				os = ftpClient.putFileStream(remoteFile);
			} catch (FtpProtocolException e) {
				e.printStackTrace();
			}

			is = inputStream;
			//创建一个缓冲区
			byte[] bytes = new byte[1024];
			int c;
			while ((c = is.read(bytes)) != -1) {
     				os.write(bytes, 0, c);
			}

			uploadStatus = true;
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			closeStream(is, os);
		}

		return uploadStatus;
	}

	/**
	 * 通过{@link File File}的方式，将文件上传至文件服务器<br/>
	 *
	 * @param file {@link File File}
	 * @param serverPath 服务器基础路径
	 * @param fileName 文件名称
	 * @return true 上传成功， 否则上传失败
	 */
	public static boolean uploadToServer(File file, String serverPath, String fileName) throws Exception {
		InputStream inputStream = getLocalFileInputStream(file);
		return uploadToServer(inputStream, serverPath, fileName);
	}

	/**
	 * 删除FTP文件<br/>
	 *
	 * @param serverPath 服务器路径
	 * @param fileName 文件名称
	 * @return true 删除成功，false 删除失败
	 * @throws Exception
	 */
	public static boolean deleteFtpFile(String serverPath, String fileName) throws Exception {
		boolean deleteStatus = false;
		try{
			if (!isDirExist(serverPath.replace("\\", "/"))) return deleteStatus;

			ftpClient.changeDirectory(serverPath.replace("\\", "/"));
			ftpClient.deleteFile(fileName);

			deleteStatus = true;
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeConnect();
		}

		return deleteStatus;
	}

	/**
	 * 下载Ftp文件<br/>
	 * 通过remoteFile文件路径，将文件下载到本地的localFile路径
	 * @param remoteFile 远程路径
	 * @param localFile 本地路径
	 * @throws Exception
	 */
	public static void download(String remoteFile, String localFile) throws Exception {
		InputStream is = null;
		FileOutputStream os = null;
		try {
			//获取远程机器上的文件filename，借助TelnetInputStream把该文件传送到本地。
			try {
				is = ftpClient.getFileStream(remoteFile);
			} catch (FtpProtocolException e) {
				e.printStackTrace();
			}
			File file_in = new File(localFile);
			os = new FileOutputStream(file_in);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = is.read(bytes)) != -1) {
				os.write(bytes, 0, c);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally{
			closeStream(is, os);
		}
	}

	/**
	 * 判断FTP服务器已经连接并且登陆<br/>
	 *
	 * @return true 处于连接状态， false 已经断开
	 */
	public static boolean isConAndLogin(){
		boolean flag = ftpClient.isLoggedIn() && ftpClient.isConnected();
		return flag ;
	}

	/**
	 * 检查路径是否存在<br/>
	 *
	 * @param dir 检查的路径
	 * @return true 路径存在， false 路径不存在
	 */
	public static Boolean isDirExist(String dir) {
		try {
			ftpClient.changeDirectory(dir);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 创建文件夹<br/>
	 *
	 * @param dir 文件路径
	 * @return true 创建成功， false 创建失败
	 */
	public static boolean createDir(String dir) {
		try {
			if(isConAndLogin()) {
				ftpClient.makeDirectory(dir);
				return true;
			}
		} catch (FtpProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 重命名<br/>
	 * @param sPath 源文件名
	 * @param dPath 目标文件名
	 * @return true 命名成功，否则失败
	 */
	public Boolean renameDir(String sPath , String dPath ) {
		try {
			if (isConAndLogin()) {
				ftpClient.rename(sPath, dPath);
			}
			return true;
		} catch (FtpProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 得到ftp客户端的输出流<br/>
	 *
	 * @param remoteFile 文件路径
	 * @return ftp客户端输出流
	 * @throws IOException
	 * @throws FtpProtocolException
	 */
	private static OutputStream getFtpClientOutputStream(String remoteFile) throws FtpProtocolException, IOException {
		return ftpClient.putFileStream(remoteFile);
	}

	/**
	 * 得到本地文件的输入流<br/>
	 *
	 * @param file {@link File File}
	 * @return 返回文件输入流
	 * @throws FileNotFoundException
	 */
	private static InputStream getLocalFileInputStream(File file)
			throws FileNotFoundException {
		return new FileInputStream(file);
	}

	/**
	 * 将文件写入到Ftp服务器<br/>
	 *
	 * @param outputStream 文件输出流
	 * @param inputStream 文件输入流
	 * @throws IOException
	 */
	private static void writeToFtpServer(OutputStream outputStream, InputStream inputStream) throws IOException {
		byte[] bytes = new byte[1024];
		while (inputStream.read(bytes) != -1) {
			outputStream.write(bytes, 0, bytes.length);
		}
		outputStream.flush();
	}

	/**
	 * 关闭输入/输出流<br/>
	 * @param is 输入流
	 * @param os 输出流
	 */
	private static void closeStream(InputStream is, OutputStream os) {
		try {
			if(is != null){
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(os != null){
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
