package com.thinkgem.jeesite.modules.sys.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import com.thinkgem.jeesite.common.utils.FileUtils;

public class WordImgUtil {

	private static final String words = "天地玄黄宇宙洪荒日月盈昃辰宿列张寒来暑往秋收冬藏闰余成岁律吕调阳云腾致雨露结为霜金生丽水玉出昆冈剑号巨阙珠称夜光果珍李柰菜重芥姜海咸河淡鳞潜羽翔龙师火帝鸟官人皇始制文字乃服衣裳推位让国有虞陶唐吊民伐罪周发殷汤坐朝问道垂拱平章爱育黎首臣伏戎羌遐迩体率宾归王";

	private static final int MARGIN = 15; //文字离图片的边距
	private static final int DIFF = 20; //文字最小间距
	
	
	public static List<WordPoint> getRandomWords(int totalWords, int checkWords, int width, int height) {
		Random random = new Random();
		int size = words.length();
		List<Integer> wordIndexs = new ArrayList<>();
		List<WordPoint> wordPoints = new ArrayList<>();
		for (int i = 0; i < totalWords; i++) {
			int wordIndex = random.nextInt(size);
			if (!wordIndexs.contains(wordIndex)) {
				WordPoint wordPoint = new WordPoint();
				wordPoint.setWord(words.charAt(wordIndex));

				int randomMaxHeight = height - MARGIN * 2;
				int top = random.nextInt(randomMaxHeight);
				while (!checkValue(wordPoints, 1, top)) {
					top = random.nextInt(randomMaxHeight);
				}
				wordPoint.setTop(top + MARGIN);

				int randomMaxLeft = width - MARGIN * 2;
				int left = random.nextInt(randomMaxLeft);
				while (!checkValue(wordPoints, 1, top)) {
					left = random.nextInt(randomMaxLeft);
				}
				wordPoint.setLeft(left+ MARGIN);
				
				wordPoints.add(wordPoint);
			}
		}
		return wordPoints;
	}

	private static boolean checkValue(List<WordPoint> wordPoints, int type, int value) {
		for (WordPoint wordPoint : wordPoints) {
			int ev;
			if (type == 1) {
				ev = wordPoint.getTop();
			} else {
				ev = wordPoint.getLeft();
			}
			if (Math.abs(ev - value) < DIFF) {
				return false;
			}
		}
		return true;
	}

	public static byte[] getWordImage(String dir, List<WordPoint> wordPoints) throws IOException {
		File folder = new File(dir);
		File[] imgs = folder.listFiles();
		Random random = new Random();
		int index = random.nextInt(imgs.length);
		BufferedImage srcImage = ImageIO.read(imgs[index]); // 读入文件
		Graphics2D g = srcImage.createGraphics();

		Font f = new Font("Atlantic Inline", Font.BOLD, 28);
		g.setFont(f);
		//g.rotate(90 * Math.PI / 180);
		for(WordPoint wordPoint: wordPoints) {
			Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
			g.setColor(color);
			g.drawString(wordPoint.getWord()+"", wordPoint.getLeft(), wordPoint.getTop());
		}
		
		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		ImageIO.write(srcImage, "PNG", bas);
		return bas.toByteArray();
	}

	public static boolean checkPoint(List<WordPoint> allWordPoints, List<WordPoint> checkPoints) {
		WordPoint clientWp;
		WordPoint serverWp;
		int avaliableDiff = DIFF; //可接受差值
		for (int i = 0; i < checkPoints.size(); i++) {
			clientWp = checkPoints.get(i);
			serverWp = allWordPoints.get(i);
			int topDiff = clientWp.getTop() - serverWp.getTop();
			int leftDiff = clientWp.getLeft() - serverWp.getLeft();
			if (Math.abs(topDiff) > avaliableDiff || Math.abs(leftDiff) > avaliableDiff) {
				return false;
			}
		}
		return true;
	}

	public static class WordPoint implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		private char word;
		private int left;
		private int top;

		public char getWord() {
			return word;
		}

		public void setWord(char word) {
			this.word = word;
		}

		public int getLeft() {
			return left;
		}

		public void setLeft(int left) {
			this.left = left;
		}

		public int getTop() {
			return top;
		}

		public void setTop(int top) {
			this.top = top;
		}

		@Override
		public String toString() {
			return "WordPoint [word=" + word + ", left=" + left + ", top=" + top + "]";
		}

	}

	public static void main(String[] args) throws IOException {
		List<WordPoint> wordPoints = getRandomWords(5, 2, 400, 200);
		String dir = "D:\\projects\\zhaosheng\\src\\main\\webapp\\static\\verify\\images";
		byte[] bytes = getWordImage(dir, wordPoints);
		FileUtils.writeByteArrayToFile(new File("C:\\1.png"), bytes);
	}

}
