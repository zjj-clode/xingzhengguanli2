package com.thinkgem.jeesite.common.image.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkgem.jeesite.common.image.utils.ImageUtils.Position;

/**
 * @author 廖水平
 */
public class AverageImageScale {
	private static Logger logger = LoggerFactory.getLogger(AverageImageScale.class);

	/**
	 * 缩小图片
	 * 
	 * @param srcFile
	 *            原图片
	 * @param destFile
	 *            目标图片
	 * @param boxWidth
	 *            缩略图最大宽度
	 * @param boxHeight
	 *            缩略图最大高度
	 * @throws IOException
	 */
	public static void resizeFix(File srcFile, File destFile, int boxWidth, int boxHeight) throws IOException {
		BufferedImage srcImgBuff = ImageIO.read(srcFile);
		int width = srcImgBuff.getWidth();
		int height = srcImgBuff.getHeight();
		if (width <= boxWidth && height <= boxHeight) {
			FileUtils.copyFile(srcFile, destFile);
			return;
		}
		int zoomWidth;
		int zoomHeight;
		if ((float) width / height > (float) boxWidth / boxHeight) {
			zoomWidth = boxWidth;
			zoomHeight = Math.round((float) boxWidth * height / width);
		} else {
			zoomWidth = Math.round((float) boxHeight * width / height);
			zoomHeight = boxHeight;
		}
		BufferedImage imgBuff = scaleImage(srcImgBuff, width, height, zoomWidth, zoomHeight);
		writeFile(imgBuff, destFile);
	}

	/**
	 * 裁剪并压缩
	 * 
	 * @param srcFile
	 *            原文件
	 * @param destFile
	 *            目标文件
	 * @param boxWidth
	 *            缩略图最大宽度
	 * @param boxHeight
	 *            缩略图最大高度
	 * @param cutTop
	 *            裁剪TOP
	 * @param cutLeft
	 *            裁剪LEFT
	 * @param cutWidth
	 *            裁剪宽度
	 * @param catHeight
	 *            裁剪高度
	 * @throws IOException
	 */
	public static void resizeFix(File srcFile, File destFile, int boxWidth, int boxHeight, int cutTop, int cutLeft, int cutWidth, int catHeight) throws IOException {
		BufferedImage srcImgBuff = ImageIO.read(srcFile);
		srcImgBuff = srcImgBuff.getSubimage(cutTop, cutLeft, cutWidth, catHeight);
		int width = srcImgBuff.getWidth();
		int height = srcImgBuff.getHeight();
		if (width <= boxWidth && height <= boxHeight) {
			writeFile(srcImgBuff, destFile);
			return;
		}
		int zoomWidth;
		int zoomHeight;
		if ((float) width / height > (float) boxWidth / boxHeight) {
			zoomWidth = boxWidth;
			zoomHeight = Math.round((float) boxWidth * height / width);
		} else {
			zoomWidth = Math.round((float) boxHeight * width / height);
			zoomHeight = boxHeight;
		}
		BufferedImage imgBuff = scaleImage(srcImgBuff, width, height, zoomWidth, zoomHeight);
		writeFile(imgBuff, destFile);
	}

	public static void writeFile(BufferedImage imgBuf, File destFile) throws IOException {
		File parent = destFile.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		ImageIO.write(imgBuf, "jpeg", destFile);
	}

	/**
	 * 添加文字水印
	 * 
	 * @param srcFile
	 *            源图片文件。需要加水印的图片文件。
	 * @param destFile
	 *            目标图片。加水印后保存的文件。如果和源图片文件一致，则覆盖源图片文件。
	 * @param minWidth
	 *            需要加水印的最小宽度，如果源图片宽度小于该宽度，则不加水印。
	 * @param minHeight
	 *            需要加水印的最小高度，如果源图片高度小于该高度，则不加水印。
	 * @param pos
	 *            水印位置 1-9，其他值为随机。1：左上；2：右上；3：左下；4：右下；5：中央。6:中上；7：中下；8：中左；9中右
	 * @param offsetX
	 *            加水印的位置的偏移量x。负数表示往左偏移。
	 * @param offsetY
	 *            加水印的位置的偏移量y。负数表示往上偏移。
	 * @param text
	 *            水印文字。多行文字需要加换行标记\n
	 * @param color
	 *            水印颜色
	 * @param size
	 *            水印字体大小
	 * @param alpha
	 *            透明度。0-100，越小越透明。
	 * @throws IOException
	 */
	public static void imageMark(File srcFile, File destFile, int minWidth, int minHeight, int pos, int offsetX, int offsetY, String text, Color color, int size, int alpha) throws IOException {
		BufferedImage imgBuff = ImageIO.read(srcFile);
		int width = imgBuff.getWidth();
		int height = imgBuff.getHeight();
		if (width <= minWidth || height <= minHeight) {
			imgBuff = null;
			if (!srcFile.equals(destFile)) {
				FileUtils.copyFile(srcFile, destFile);
			}
		} else {
			imageMark(imgBuff, width, height, pos, offsetX, offsetY, text, color, size, alpha);
			writeFile(imgBuff, destFile);
			imgBuff = null;
		}
	}

	/**
	 * 添加图片水印
	 * 
	 * @param srcFile
	 *            源图片文件。需要加水印的图片文件。
	 * @param destFile
	 *            目标图片。加水印后保存的文件。如果和源图片文件一致，则覆盖源图片文件。
	 * @param minWidth
	 *            需要加水印的最小宽度，如果源图片宽度小于该宽度，则不加水印。
	 * @param minHeight
	 *            需要加水印的最小高度，如果源图片高度小于该高度，则不加水印。
	 * @param pos
	 *            水印位置 1-9，其他值为随机。1：左上；2：右上；3：左下；4：右下；5：中央。6:中上；7：中下；8：中左；9中右
	 * @param offsetX
	 *            加水印的位置的偏移量x。负数表示往左偏移。
	 * @param offsetY
	 *            加水印的位置的偏移量y。负数表示往上偏移。
	 * @param markFile
	 *            水印图片
	 * @throws IOException
	 */
	public static void imageMark(File srcFile, File destFile, int minWidth, int minHeight, int pos, int offsetX, int offsetY, File markFile) throws IOException {
		BufferedImage imgBuff = ImageIO.read(srcFile);
		int width = imgBuff.getWidth();
		int height = imgBuff.getHeight();
		if (width <= minWidth || height <= minHeight) {
			imgBuff = null;
			if (!srcFile.equals(destFile)) {
				FileUtils.copyFile(srcFile, destFile);
			}
		} else {
			imageMark(imgBuff, width, height, pos, offsetX, offsetY, markFile);
			writeFile(imgBuff, destFile);
			imgBuff = null;
		}

	}

	public static void imageMark(File srcFile, File destFile, String text, int pos) throws IOException {
		// 初始字体大小
		int siz = 50;
		// 获取图片宽度
		BufferedImage srcImgBuff = ImageIO.read(srcFile);
		int width = srcImgBuff.getWidth();
		// 获取宽度最大字符串，需要根据字体大小计算
		String maxLenStr = null;
		int maxWidth = 0;
		// 多行以\n分割
		String[] textItems = text.split("\n");
		// 从里面找出最长的一行
		for (String textItem : textItems) {
			int w = AverageImageScale.getStringWidth(textItem, new Font(null, Font.PLAIN, siz));
			if (maxWidth < w) {
				maxWidth = w;
				maxLenStr = textItem;
			}
		}
		// 边框距离，默认2个字大小
		int margin = 2 * siz;
		// 调整字体大小，按图片宽度
		while (maxWidth > width * 0.3 + margin) {
			siz -= 5; // 每次字号缩小5
			if (siz < 15) {
				break;
			}
			margin = 2 * siz;
			maxWidth = AverageImageScale.getStringWidth(maxLenStr, new Font(null, Font.PLAIN, siz));
			logger.debug("图片宽度={}，左或右边距={}，字体大小={}，文字最大宽度={}", width, margin, siz, maxWidth);
		}

		int minWidth = 200;
		int minHeight = 200;
		Color color = Color.RED;
		int alpha = 100;

		switch (pos) {
			case 1:
				// 左上
				AverageImageScale.imageMark(srcFile, destFile, minWidth, minHeight, 1, margin, margin, text, color, siz, alpha);
				break;
			case 2:
				// 右上
				AverageImageScale.imageMark(srcFile, destFile, minWidth, minHeight, 2, -margin - maxWidth, margin, text, color, siz, alpha);
				break;
			case 3:
				// 左下
				AverageImageScale.imageMark(srcFile, destFile, minWidth, minHeight, 3, margin, -margin, text, color, siz, alpha);
				break;
			case 4:
				// 右下
				AverageImageScale.imageMark(srcFile, destFile, minWidth, minHeight, 4, -margin - maxWidth, -margin, text, color, siz, alpha);
				break;
			case 5:
				// 中央
				AverageImageScale.imageMark(srcFile, destFile, minWidth, minHeight, 5, (-margin - maxWidth) / 2, -margin / 2, text, color, siz, alpha);
				break;
			case 6:
				// 中上
				AverageImageScale.imageMark(srcFile, destFile, minWidth, minHeight, 6, (-margin - maxWidth) / 2, margin, text, color, siz, alpha);
				break;
			case 7:
				// 中下
				AverageImageScale.imageMark(srcFile, destFile, minWidth, minHeight, 7, (-margin - maxWidth) / 2, -margin, text, color, siz, alpha);
				break;
			case 8:
				// 中左
				AverageImageScale.imageMark(srcFile, destFile, minWidth, minHeight, 8, margin, -margin / 2, text, color, siz, alpha);
				break;
			case 9:
				// 中右
				AverageImageScale.imageMark(srcFile, destFile, minWidth, minHeight, 9, -margin - maxWidth, -margin / 2, text, color, siz, alpha);
				break;
			default:
				// 中央
				AverageImageScale.imageMark(srcFile, destFile, minWidth, minHeight, 5, (-margin - maxWidth) / 2, -margin / 2, text, color, siz, alpha);
				break;
		}

	}

	/**
	 * 添加文字水印
	 * 
	 * @param imgBuff
	 *            原图片
	 * @param width
	 *            原图宽度
	 * @param height
	 *            原图高度
	 * @param pos
	 *            水印位置 1-9，其他值为随机。1：左上；2：右上；3：左下；4：右下；5：中央。6:中上；7：中下；8：中左；9中右
	 * @param offsetX
	 *            加水印的位置的偏移量x。负数表示往左偏移。
	 * @param offsetY
	 *            加水印的位置的偏移量y。负数表示往上偏移。
	 * @param text
	 *            水印文字。多行文字需要加换行标记\n
	 * @param color
	 *            水印颜色
	 * @param size
	 *            文字大小
	 * @param alpha
	 *            透明度。0-100。越小越透明。
	 * @throws IOException
	 */
	private static void imageMark(BufferedImage imgBuff, int width, int height, int pos, int offsetX, int offsetY, String text, Color color, int size, int alpha) throws IOException {
		Position p = ImageUtils.markPosition(width, height, pos, offsetX, offsetY);
		Graphics2D g = imgBuff.createGraphics();
		g.setColor(color);
		g.setFont(new Font(null, Font.PLAIN, size));
		AlphaComposite a = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float) alpha / 100);
		g.setComposite(a);
		//g.drawString(text, p.getX(), p.getY());

		// 分成多行
		String[] textes = text.split("\n");
		int py;
		//
		switch (pos) { // 1：左上；2：右上；3：左下；4：右下；5：中央。6:中上；7：中下；8：中左；9中右
			case 1:
				py = p.getY(); // 开始写的位置
				for (int i = 0; i < textes.length; i++) {
					int y = (int) (py + i * size * 1.5); // height  1.5em
					g.drawString(textes[i], p.getX(), y);
				}
				break;
			case 2:
				py = p.getY();
				for (int i = 0; i < textes.length; i++) {
					int y = (int) (py + i * size * 1.5);
					g.drawString(textes[i], p.getX(), y);
				}
				break;
			case 3:
				py = p.getY() - textes.length * size;
				for (int i = 0; i < textes.length; i++) {
					int y = (int) (py + i * size * 1.5);
					g.drawString(textes[i], p.getX(), y);
				}
				break;
			case 4:
				py = p.getY() - textes.length * size;
				for (int i = 0; i < textes.length; i++) {
					int y = (int) (py + i * size * 1.5);
					g.drawString(textes[i], p.getX(), y);
				}
				break;
			case 5:
				py = p.getY() - textes.length * size / 2;
				for (int i = 0; i < textes.length; i++) {
					int y = (int) (py + i * size * 1.5);
					g.drawString(textes[i], p.getX(), y);
				}
				break;
			case 6:
				py = p.getY();
				for (int i = 0; i < textes.length; i++) {
					int y = (int) (py + i * size * 1.5);
					g.drawString(textes[i], p.getX(), y);
				}
				break;
			case 7:
				py = p.getY() - textes.length * size;
				for (int i = 0; i < textes.length; i++) {
					int y = (int) (py + i * size * 1.5);
					g.drawString(textes[i], p.getX(), y);
				}
				break;
			case 8:
				py = p.getY() - textes.length * size / 2;
				for (int i = 0; i < textes.length; i++) {
					int y = (int) (py + i * size * 1.5);
					g.drawString(textes[i], p.getX(), y);
				}
				break;
			case 9:
				py = p.getY() - textes.length * size / 2;
				for (int i = 0; i < textes.length; i++) {
					int y = (int) (py + i * size * 1.5);
					g.drawString(textes[i], p.getX(), y);
				}
				break;

			default:
				break;
		}

		//
		g.dispose();
	}

	private static void imageMark(BufferedImage imgBuff, int width, int height, int pos, int offsetX, int offsetY, File markFile) throws IOException {
		Position p = ImageUtils.markPosition(width, height, pos, offsetX, offsetY);
		Graphics2D g = imgBuff.createGraphics();
		AlphaComposite a = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP);
		g.setComposite(a);
		g.drawImage(ImageIO.read(markFile), p.getX(), p.getY(), null);
		g.dispose();
	}

	private static BufferedImage scaleImage(BufferedImage srcImgBuff, int width, int height, int zoomWidth, int zoomHeight) {
		int[] colorArray = srcImgBuff.getRGB(0, 0, width, height, null, 0, width);
		BufferedImage outBuff = new BufferedImage(zoomWidth, zoomHeight, BufferedImage.TYPE_INT_RGB);
		// 宽缩小的倍数
		float wScale = (float) width / zoomWidth;
		int wScaleInt = (int) (wScale + 0.5);
		// 高缩小的倍数
		float hScale = (float) height / zoomHeight;
		int hScaleInt = (int) (hScale + 0.5);
		int area = wScaleInt * hScaleInt;
		int x0, x1, y0, y1;
		int color;
		long red, green, blue;
		int x, y, i, j;
		for (y = 0; y < zoomHeight; y++) {
			// 得到原图高的Y坐标
			y0 = (int) (y * hScale);
			y1 = y0 + hScaleInt;
			for (x = 0; x < zoomWidth; x++) {
				x0 = (int) (x * wScale);
				x1 = x0 + wScaleInt;
				red = green = blue = 0;
				for (i = x0; i < x1; i++) {
					for (j = y0; j < y1; j++) {
						color = colorArray[width * j + i];
						red += getRedValue(color);
						green += getGreenValue(color);
						blue += getBlueValue(color);
					}
				}
				outBuff.setRGB(x, y, comRGB((int) (red / area), (int) (green / area), (int) (blue / area)));
			}
		}
		return outBuff;
	}

	private static int getRedValue(int rgbValue) {
		return (rgbValue & 0x00ff0000) >> 16;
	}

	private static int getGreenValue(int rgbValue) {
		return (rgbValue & 0x0000ff00) >> 8;
	}

	private static int getBlueValue(int rgbValue) {
		return rgbValue & 0x000000ff;
	}

	private static int comRGB(int redValue, int greenValue, int blueValue) {
		return (redValue << 16) + (greenValue << 8) + blueValue;
	}

	public static int getStringHeight(String str, Font font) {
		if (str == null || str.isEmpty() || font == null) {
			return 0;
		}
		return (int) font.getStringBounds(str, new FontRenderContext(new AffineTransform(), true, true)).getHeight();
	}

	public static int getStringWidth(String str, Font font) {
		if (str == null || str.isEmpty() || font == null) {
			return 0;
		}
		return (int) font.getStringBounds(str, new FontRenderContext(new AffineTransform(), true, true)).getWidth();
	}

	public static void main(String[] args) throws Exception {
		/*long time = System.currentTimeMillis();
		AverageImageScale.resizeFix(new File("test/com/jeecms/common/util/1.bmp"), new File("test/com/jeecms/common/util/1-n-2.bmp"), 310, 310, 50, 50, 320, 320);
		time = System.currentTimeMillis() - time;
		//System.out.println("resize2 img in " + time + "ms");*/

		// 怎么使用最大的字体写下所有的字。
		// 只考虑中左位置

		File srcFile = new File("C:\\Users\\pc\\Pictures\\s_1704251445507514677.jpg");
		String text = "经度：106.334\n纬度：36.331\n手机：13211122321\n时间：2017-11-12 11:33\n身份证：430232198412120972";
		for (int i = 1; i <= 9; i++) {
			File destFile = new File("C:\\Users\\pc\\Pictures\\s_1704251445507514677_" + i + ".jpg");
			imageMark(srcFile, destFile, text, i);
		}

	}

}
