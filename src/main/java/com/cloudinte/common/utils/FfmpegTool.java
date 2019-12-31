package com.cloudinte.common.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkgem.jeesite.common.utils.StringUtils;

/**
 * ffmpeg工具
 * 
 * @author 廖水平
 */
public class FfmpegTool {

	private static Logger logger = LoggerFactory.getLogger(FfmpegTool.class);

	public static void main(String[] args) throws Exception {
		/*
				//
				fetchFrame("C:\\Users\\pc\\Videos\\1.mp4", "D:/test4.jpg");
		
				if (1 == 1) {
					return;
				}
		
				//String viedoFilePath = "C:\\Users\\Administrator\\Videos\\独角戏 - 许茹芸.mp3";
				//String viedoFilePath = "C:\\Users\\Administrator\\Videos\\0300080100577.mp4";
				String viedoFilePath = "C:\\Users\\Administrator\\Videos\\b4db7a78d9a14f4b939b6fd43e0ae943.amr";//读不出来时长？
				//String viedoFilePath = "C:\\Users\\Administrator\\Videos\\e71eb6a3b8074a398d833155293326a2.aac";
				String ffmpegPath = "D:\\ffmpeg.exe";
				logger.debug("时长：" + getDuration(viedoFilePath, ffmpegPath));
		
				logger.debug("" + getAmrDuration(new File(viedoFilePath)));
		
				String codcFilePath = "C:\\Users\\Administrator\\Videos\\0300080100577.flv";
				transcoding(ffmpegPath, viedoFilePath, codcFilePath);
		
				String picPath = "C:\\Users\\Administrator\\Videos\\0300080100577.jpg";
				capturePicture(ffmpegPath, viedoFilePath, picPath, -1, 600);
		
				picPath = "C:\\Users\\Administrator\\Videos\\0300080100577.jpeg";
				getThumb(ffmpegPath, viedoFilePath, picPath, -1, -1, 0, 0, 1);
		
				picPath = "C:\\Users\\Administrator\\Videos\\0300080100577_start.jpeg";
				getThumbStart(ffmpegPath, viedoFilePath, picPath, 800, 600);
		
				picPath = "C:\\Users\\Administrator\\Videos\\0300080100577_end.jpeg";
				//getThumbEnd(ffmpegPath, viedoFilePath, picPath, 800, 600);
		*/

		FfmpegTool.transcoding264("d:\\ffmpeg.exe", "C:\\Users\\pc\\Videos\\5.mp4", "C:\\Users\\pc\\Videos\\5-1.mp4");

		fetchFrame("C:\\Users\\pc\\Videos\\1.mp4", "D:/test4.jpg");
	}

	/**
	 * 解决视频不能边下载边播放问题。（mp4视频有metadata，通常在文件尾部，而flash读到这个metadata才开始播放， 解决办法是用qt_faststart工具处理一下mp4， 把它的metadata移至文件头部。）
	 * 
	 * @param qt_faststartPath qt_faststart目录路径
	 * @param viedoFilePath    源视频
	 * @param codedFilePath    目的视频
	 */
	public static boolean qtFastStart(String qt_faststartPath, String viedoFilePath, String codedFilePath) throws IOException {
		List<String> command = new ArrayList<>();
		command.add(qt_faststartPath);
		command.add(viedoFilePath);
		command.add(codedFilePath);
		ProcessBuilder builder = new ProcessBuilder();
		logger.debug("command -----> {}", StringUtils.join(command, " "));
		builder.command(command);
		builder.redirectErrorStream(true);
		builder.start();
		return true;
	}

	/**
	 * 获取音视频总时间
	 */
	public static long getDuration(String avFilePath, String ffmpegPath) {
		if (avFilePath.toLowerCase().endsWith(".amr")) {
			return getAmrDuration(new File(avFilePath)) / 1000; //毫秒
		}
		return getAvTime1(avFilePath, ffmpegPath);
	}

	/**
	 * 利用ffmpeg命令获取音视频总时间（秒）
	 * 
	 * @param avFilePath 音视频路径
	 * @param ffmpegPath ffmpeg安装路径
	 * @return 音视频总时间（秒），读取失败返回-1
	 */
	@Deprecated
	public static int getAvTime(String avFilePath, String ffmpegPath) {
		List<String> commands = new ArrayList<>();
		commands.add(ffmpegPath);
		commands.add("-i");
		commands.add(avFilePath);
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			final Process p = builder.start();

			//从输入流中读取视频信息
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();

			String content = sb.toString();

			logger.debug("执行命令返回内容：" + content);

			//从视频信息中解析时长
			String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
			Pattern pattern = Pattern.compile(regexDuration);
			Matcher m = pattern.matcher(content);
			if (m.find()) {
				logger.debug("Duration:" + m.group(1));
				int time = getTimelen(m.group(1));
				logger.debug(avFilePath + "，时长（秒）：" + time + "，开始时间：" + m.group(2) + "，比特率：" + m.group(3) + "kb/s");
				return time;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;
	}

	public static int getAvTime1(String avFilePath, String ffmpegPath) {
		List<String> commands = new ArrayList<>();
		commands.add(ffmpegPath);
		commands.add("-i");
		commands.add(avFilePath);
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			final Process p = builder.start();

			//从输入流中读取视频信息
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();

			String content = sb.toString();

			logger.debug("执行命令返回内容：" + content);

			//从视频信息中解析时长
			/*String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
			Pattern pattern = Pattern.compile(regexDuration);
			Matcher m = pattern.matcher(content);
			if (m.find()) {
				logger.debug("Duration:" + m.group(1));
				int time = getTimelen(m.group(1));
				logger.debug(avFilePath + "，时长（秒）：" + time + "，开始时间：" + m.group(2) + "，比特率：" + m.group(3)
						+ "kb/s");
				return time;
			}*/

			int beginIndex = content.indexOf("Duration:") + "Duration:".length();
			int endIndex = content.indexOf(",", beginIndex);
			String timeStr = content.substring(beginIndex, endIndex).trim();
			logger.debug("timeStr=" + timeStr);
			return getTimelen(timeStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;
	}

	/**
	 * 得到amr类型文件的时长(毫秒)
	 */
	public static long getAmrDuration(File file) {
		long duration = -1;
		int[] packedSize = { 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0, 0, 0 };
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(file, "rw");
			long length = file.length();//文件的长度
			int pos = 6;//设置初始位置
			int frameCount = 0;//初始帧数
			int packedPos = -1;
			/////////////////////////////////////////////////////
			byte[] datas = new byte[1];//初始数据值
			while (pos <= length) {
				randomAccessFile.seek(pos);
				if (randomAccessFile.read(datas, 0, 1) != 1) {
					duration = length > 0 ? (length - 6) / 650 : 0;
					break;
				}
				packedPos = datas[0] >> 3 & 0x0F;
				pos += packedSize[packedPos] + 1;
				frameCount++;
			}
			/////////////////////////////////////////////////////
			duration += frameCount * 20;//帧数*20
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (randomAccessFile != null) {
				try {
					randomAccessFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return duration;
	}

	//格式:"00:00:10.68"
	private static int getTimelen(String timelen) {
		try {
			int min = 0;
			String strs[] = timelen.split(":");
			if (strs[0].compareTo("0") > 0) {
				min += Integer.valueOf(strs[0]) * 60 * 60;//秒
			}
			if (strs[1].compareTo("0") > 0) {
				min += Integer.valueOf(strs[1]) * 60;
			}
			if (strs[2].compareTo("0") > 0) {
				min += Math.round(Float.valueOf(strs[2]));
			}
			return min;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 利用ffmpeg命令对视频保存截图.（第17秒？！，建议使用替代方法： {@link #getThumbStart(String, String, String, int, int)}）
	 * 
	 * @param ffmpegPath    转码工具的存放路径
	 * @param viedoFilePath 要截图的视频源文件
	 * @param picPath       截图保存路径
	 */
	@Deprecated
	public static boolean capturePicture(String ffmpegPath, String viedoFilePath, String picPath, int width, int height) throws IOException {
		// 创建一个List集合来保存从视频中截取图片的命令
		List<String> command = new ArrayList<>();
		command.add(ffmpegPath);
		command.add("-i");
		command.add(viedoFilePath);
		command.add("-y");
		command.add("-f");
		command.add("image2");
		command.add("-ss"); // 添加参数＂-ss＂，该参数指定截取的起始时间
		command.add("17"); // 添加起始时间为第17秒 
		command.add("-t"); // 添加参数＂-t＂，该参数指定持续时间
		command.add("0.001"); // 添加持续时间为1毫秒
		if (width > 0 && height > 0) {
			command.add("-s"); // 添加参数＂-s＂，该参数指定截取的图片大小
			command.add(width + "*" + height); // 宽*高
		}
		command.add(picPath); // 截取的图片的保存路径

		ProcessBuilder builder = new ProcessBuilder();
		builder.command(command);
		// 如果此属性为 true，则任何由通过此对象的 start() 方法启动的后续子进程生成的错误输出都将与标准输出合并，因此两者均可使用 Process.getInputStream() 方法读取。这使得关联错误消息和相应的输出变得更容易
		builder.redirectErrorStream(true);
		builder.start();
		return true;
	}

	/**
	 * 利用ffmpeg命令对视频转码.
	 * 
	 * @param ffmpegPath    转码工具的存放路径
	 * @param viedoFilePath 用于指定要转码的文件
	 * @param codedFilePath 格式转换后的文件保存路径
	 * @throws IOException
	 */
	public static boolean transcoding(String ffmpegPath, String viedoFilePath, String codedFilePath) throws IOException {
		// 创建一个List集合来保存转换视频文件为flv格式的命令
		List<String> command = new ArrayList<>();
		command.add(ffmpegPath); // 添加转换工具路径
		command.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
		command.add(viedoFilePath); // 添加要转换格式的视频文件的路径
		command.add("-qscale"); //指定转换的质量
		command.add("6");
		command.add("-ab"); //设置音频码率
		command.add("64");
		command.add("-ac"); //设置声道数
		command.add("2");
		command.add("-ar"); //设置声音的采样频率
		command.add("22050");
		command.add("-r"); //设置帧频
		command.add("24");
		command.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
		command.add(codedFilePath);

		ProcessBuilder builder = new ProcessBuilder();
		builder.command(command);
		builder.redirectErrorStream(true);
		builder.start();
		return true;
	}

	public static boolean transcoding264(String ffmpegPath, String viedoFilePath, String codedFilePath) throws IOException {
		// 创建一个List集合来保存转换视频文件为flv格式的命令
		List<String> command = new ArrayList<>();
		command.add(ffmpegPath); // 添加转换工具路径
		command.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
		command.add(viedoFilePath); // 添加要转换格式的视频文件的路径
		command.add("-vcodec libx264");
		//command.add("-bsf h264_mp4toannexb");
		command.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
		command.add(codedFilePath);

		ProcessBuilder builder = new ProcessBuilder();
		builder.command(command);
		builder.redirectErrorStream(true);
		Process process = builder.start();
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return process.exitValue() == 0;
	}

	public static boolean transcodingAudio(String ffmpegPath, String viedoFilePath, String codedFilePath) throws IOException {
		// 创建一个List集合来保存转换视频文件为flv格式的命令
		List<String> command = new ArrayList<>();
		command.add(ffmpegPath); // 添加转换工具路径
		command.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
		command.add(viedoFilePath); // 添加要转换格式的视频文件的路径
		command.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
		command.add(codedFilePath);

		ProcessBuilder builder = new ProcessBuilder();
		builder.command(command);
		builder.redirectErrorStream(true);
		builder.start();
		return true;
	}

	/*在
	 G:\>ffmpeg.exe -i "g:/nginx-1.8.0/html/1.mov" -y  -metadata title="这是视频标题"
	-metadata comment="这是视频信息!"   -c:v libx264  -preset superfast -x264opts
	keyint=25  -r 15 -b:v 500k -c:a aac -strict experimental -ar 44100 -ac 2  -b:a 6
	4k -f  mp4 "g:/nginx-1.8.0/html/2.mp4"
	 */

	/****
	 * 获取指定时间内的图片（width或height小于0时，使用默认宽高）
	 * 
	 * @param ffmpegPath    转码工具的存放路径
	 * @param viedoFilePath :视频路径
	 * @param picPath       :图片保存路径
	 * @param width         :图片长
	 * @param height        :图片宽
	 * @param hour          :指定时
	 * @param min           :指定分
	 * @param sec           :指定秒
	 */
	public static boolean getThumb(String ffmpegPath, String viedoFilePath, String picPath, int width, int height, int hour, int min, float sec) throws IOException, InterruptedException {
		if (width > 0 && height > 0) {
			new ProcessBuilder(ffmpegPath, "-y", "-i", viedoFilePath, "-vframes", "1", "-ss", hour + ":" + min + ":" + sec, "-f", "mjpeg", "-s", width + "*" + height, "-an", picPath).start();
		} else {
			new ProcessBuilder(ffmpegPath, "-y", "-i", viedoFilePath, "-vframes", "1", "-ss", hour + ":" + min + ":" + sec, "-f", "mjpeg", "-an", picPath).start();
		}
		return true;
	}

	/**
	 * 得到第一秒（也是第一帧）图片（width或height小于0时，使用默认宽高）
	 */
	public static boolean getThumbStart(String ffmpegPath, String viedoFilePath, String picPath, int width, int height) throws IOException, InterruptedException {
		return getThumb(ffmpegPath, viedoFilePath, picPath, width, height, 0, 0, 1);
	}

	/**
	 * 得到最后一秒（也是最后一帧）图片（width或height小于0时，使用默认宽高）
	 */
	public static boolean getThumbEnd(String ffmpegPath, String viedoFilePath, String picPath, int width, int height) throws IOException, InterruptedException {
		VideoInfo videoInfo = new VideoInfo(ffmpegPath);
		videoInfo.getInfo(viedoFilePath);
		logger.debug("videoInfo=" + videoInfo);
		return getThumb(ffmpegPath, viedoFilePath, picPath, width, height, videoInfo.getHours(), videoInfo.getMinutes(), videoInfo.getSeconds() - 0.2f);
	}

	public static class VideoInfo {
		//视频路径
		private String ffmpegApp;
		//视频时
		private int hours;
		//视频分
		private int minutes;
		//视频秒
		private float seconds;
		//视频width
		private int width;
		//视频height
		private int heigt;

		public VideoInfo() {
		}

		public VideoInfo(String ffmpegApp) {
			this.ffmpegApp = ffmpegApp;
		}

		@Override
		public String toString() {
			return "time: " + hours + ":" + minutes + ":" + seconds + ", width = " + width + ", height= " + heigt;
		}

		public void getInfo(String videoFilename) throws IOException, InterruptedException {
			String tmpFile = videoFilename + ".tmp.png";
			ProcessBuilder processBuilder = new ProcessBuilder(ffmpegApp, "-y", "-i", videoFilename, "-vframes", "1", "-ss", "0:0:0", "-an", "-vcodec", "png", "-f", "rawvideo", "-s", "100*100", tmpFile);

			Process process = processBuilder.start();

			InputStream stderr = process.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line;
			//打印 sb，获取更多信息。 如 bitrate、width、heigt
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			new File(tmpFile).delete();

			logger.debug("video info:\n" + sb);
			Pattern pattern = Pattern.compile("Duration: (.*?),");
			Matcher matcher = pattern.matcher(sb);

			if (matcher.find()) {
				String time = matcher.group(1);
				calcTime(time);
			}

			pattern = Pattern.compile("w:\\d+ h:\\d+");
			matcher = pattern.matcher(sb);

			if (matcher.find()) {
				String wh = matcher.group();
				//w:100 h:100
				String[] strs = wh.split("\\s+");
				if (strs != null && strs.length == 2) {
					width = Integer.parseInt(strs[0].split(":")[1]);
					heigt = Integer.parseInt(strs[1].split(":")[1]);
				}
			}

			process.waitFor();
			if (br != null) {
				br.close();
			}
			if (isr != null) {
				isr.close();
			}
			if (stderr != null) {
				stderr.close();
			}
		}

		private void calcTime(String timeStr) {
			String[] parts = timeStr.split(":");
			hours = Integer.parseInt(parts[0]);
			minutes = Integer.parseInt(parts[1]);
			seconds = Float.parseFloat(parts[2]);
		}

		public String getFfmpegApp() {
			return ffmpegApp;
		}

		public void setFfmpegApp(String ffmpegApp) {
			this.ffmpegApp = ffmpegApp;
		}

		public int getHours() {
			return hours;
		}

		public void setHours(int hours) {
			this.hours = hours;
		}

		public int getMinutes() {
			return minutes;
		}

		public void setMinutes(int minutes) {
			this.minutes = minutes;
		}

		public float getSeconds() {
			return seconds;
		}

		public void setSeconds(float seconds) {
			this.seconds = seconds;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeigt() {
			return heigt;
		}

		public void setHeigt(int heigt) {
			this.heigt = heigt;
		}

	}

	/**
	 * 获取指定视频的帧并保存为图片至指定目录
	 * 
	 * @param videofile 源视频文件路径
	 * @param framefile 截取帧的图片存放路径
	 */
	public static void fetchFrame(String videofile, String framefile) throws Exception {
		logger.debug("fetchFrame...videofile={},framefile={}", videofile, framefile);
		long start = System.currentTimeMillis();
		File targetFile = new File(framefile);
		FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile);
		ff.setFormat("mp4");
		ff.start();
		int lenght = ff.getLengthInFrames();
		int i = 0;
		Frame f = null;
		while (i < lenght) {
			// 过滤前5帧，避免出现全黑的图片，依自己情况而定
			f = ff.grabFrame();
			if (f != null) {
				if (i > 500 && f.image != null) {
					break;
				}
			}
			i++;
		}
		IplImage img = f.image;
		int owidth = img.width();
		int oheight = img.height();
		// 对截取的帧进行等比例缩放
		int width = 800;
		int height = (int) ((double) width / owidth * oheight);
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		bi.getGraphics().drawImage(f.image.getBufferedImage().getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
		ImageIO.write(bi, "jpg", targetFile);
		//ff.flush();
		ff.stop();
		logger.debug("cost {} milli seconds", System.currentTimeMillis() - start);
	}

}
