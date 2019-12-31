package com.thinkgem.jeesite.modules.sys.utils;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import com.thinkgem.jeesite.common.utils.FileUtils;

public class SlideUtil {

	public static class SlideImages implements Serializable {

		private static final long serialVersionUID = 1L;

		private String backImg;
		private String slideImg;
		private int startX;
		private int startY;

		public String getBackImg() {
			return backImg;
		}

		public void setBackImg(String backImg) {
			this.backImg = backImg;
		}

		public String getSlideImg() {
			return slideImg;
		}

		public void setSlideImg(String slideImg) {
			this.slideImg = slideImg;
		}

		public int getStartX() {
			return startX;
		}

		public void setStartX(int startX) {
			this.startX = startX;
		}

		public int getStartY() {
			return startY;
		}

		public void setStartY(int startY) {
			this.startY = startY;
		}

	}

	public static boolean checkSlide(int startX, int sessionX) {
		int offset = sessionX - startX;
		int num = 5;
		if (offset >= -num && offset <= num) {
			return true;
		}
		return false;
	}

	/**
	 * 1.根据截取的图形的大小，计算出两个嵌套矩形的颜色填充 2.在将左边边的大半圆置为透明色，将上方、右边大半圆的填充对应颜色
	 * 
	 * 
	 * @param width
	 * @param height
	 * @param startX
	 * @param startY
	 * @param cutWidth
	 * @param cutHeight
	 * @param r
	 * @return
	 */
	private static int[][] getMarkPoints(int width, int height, int startX, int startY, int cutWidth, int cutHeight,
			int r) {
		int[][] data = new int[height][width];

		int endX = startX + cutWidth - r - r/4*3;
		int endY = startY + cutHeight;

		int newStartY = startY + r + r /4*3;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (y >= newStartY && y < endY && x >= startX && x < endX) {
					if(Math.abs(y - newStartY) < 2) {
						data[y][x] = Math.abs(y - newStartY) + 3;
					} else if(Math.abs(y - endY) < 5) {
						data[y][x] = Math.abs(y - endY) + 1;
					} else if(Math.abs(x - startX) < 2) {
						data[y][x] = Math.abs(x - startX) + 3;
					} else if(Math.abs(x - endX) < 4){
						data[y][x] = Math.abs(x - endX) + 3;
					} else {
						data[y][x] = 1;
					}
				} else {
					data[y][x] = 0;
				}
			}
		}
		
		//计算上边的大半圆
		int c = (int)Math.pow(r,2);
		int topCenterX = startX + (endX-startX)/2;
		int topCenterY = startY + r;
		for(int y = topCenterY - r; y < topCenterY + r; y++) {
			for(int x = topCenterX - r ; x < topCenterX + r; x++) {
				int a = (int)Math.abs(Math.pow(x - topCenterX,2));
				int b = (int)Math.abs(Math.pow(y - topCenterY,2));
				int total = a + b;
				int diff = c - total;
				if(total < c) {
					if(diff < 25 && y < topCenterY) {
						data[y][x] = 3;
					} else {
						data[y][x] = 1;
					}
				}
			}
		}
		//计算右边的大半圆
		int rightCenterX = endX + r/4*3;
		int rightCenterY = newStartY + (endY-newStartY)/2;
		for(int y = rightCenterY - r; y < rightCenterY + r; y++) {
			for(int x = rightCenterX - r ; x < rightCenterX + r; x++) {
				int a = (int)Math.abs(Math.pow(x - rightCenterX,2));
				int b = (int)Math.abs(Math.pow(y - rightCenterY,2));
				int total = a + b;
				int diff = c - total;
				if(total < c) {
					if(diff < 25 && x > rightCenterX) {
						data[y][x] = 3;
					} else {
						data[y][x] = 1;
					}
				}
			}
		}
		//扣掉左边的大半圆
		int leftCenterX = startX + r/4*3;
		int leftCenterY = newStartY + (endY-newStartY)/2;
		for(int y = leftCenterY - r; y < leftCenterY + r; y++) {
			for(int x = leftCenterX - r ; x < leftCenterX + r; x++) {
				int a = (int)Math.abs(Math.pow(x - leftCenterX,2));
				int b = (int)Math.abs(Math.pow(y - leftCenterY,2));
				if(a + b < c) {
					data[y][x] = 0;
				}
			}
		}
		return data;
	}

	private static SlideImages getSlideImages(File imgFile, int slideWidth, int slideHeight) throws Exception {
		BufferedImage srcImage = ImageIO.read(imgFile); // 读入文件

		int width = srcImage.getWidth();
		int height = srcImage.getHeight();

		BufferedImage backImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		BufferedImage slideImg = new BufferedImage(slideWidth, slideHeight, BufferedImage.TYPE_INT_ARGB);

		Random random = new Random();
		// 随机起始位置
		int startX = random.nextInt(width - slideWidth * 2) + slideWidth;
		int startY = random.nextInt(height - slideHeight);

		int radius = slideWidth > slideHeight ? slideHeight / 6 : slideWidth / 6; //以宽高的1/4为半径
		int[][] markPoints = getMarkPoints(width, height, startX, startY, slideWidth, slideHeight, radius);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = srcImage.getRGB(x, y);
				backImg.setRGB(x, y, rgb);
				int markValue = markPoints[y][x];
				if (markValue > 0) {
					//slideImg.setRGB(x - startX, y - startY, rgb);
					int slideRgb;
					int opacity;
					
					switch (markValue) {
						case 1: opacity = 0xA6; slideRgb = rgb; break ;
						case 2: opacity = 0x40; slideRgb = 0x8cFFFFFF; break ;
						case 3: opacity = 0x59; slideRgb = 0x73FFFFFF; break ;
						case 4: opacity = 0x73; slideRgb = 0x59FFFFFF; break ;
						case 5: opacity = 0x8c; slideRgb = 0x40FFFFFF; break ;
						default:opacity = 0xA6; slideRgb = rgb;
					}
					
					int r = (0xff & rgb);
					int g = (0xff & (rgb >> 8));
					int b = (0xff & (rgb >> 16));
					int newRgb = r + (g << 8) + (b << 16) + (opacity << 24);
					backImg.setRGB(x, y, newRgb);

					slideImg.setRGB(x - startX, y - startY, slideRgb);
					
				}
			}
		}

		String uuid = UUID.randomUUID().toString();
		File backFile = new File(FileUtils.getTempDirectory(), uuid + "-back.png");
		File slideFile = new File(FileUtils.getTempDirectory(), uuid + "-slide.png");
		
		BufferedImage slideImg1 = new BufferedImage(slideWidth, slideHeight, BufferedImage.TYPE_INT_ARGB);
		simpleBlur(slideImg, slideImg1);
		ImageIO.write(slideImg1, "PNG", slideFile);
		ImageIO.write(backImg, "PNG", backFile);

		SlideImages slideImages = new SlideImages();
		slideImages.setBackImg(backFile.getAbsolutePath());
		slideImages.setSlideImg(slideFile.getAbsolutePath());
		slideImages.setStartX(startX);
		slideImages.setStartY(startY);
		return slideImages;
	}

	public static SlideImages getSlideImages(String dir, int slideWidth, int slideHeight) throws Exception {
		File folder = new File(dir);
		File[] imgs = folder.listFiles();
		Random random = new Random();
		int index = random.nextInt(imgs.length);
		return getSlideImages(imgs[index], slideWidth, slideHeight);
	}

	public static ConvolveOp getGaussianBlurFilter(int radius, boolean horizontal) {
		if (radius < 1) {
			throw new IllegalArgumentException("Radius must be >= 1");
		}

		int size = radius * 2 + 1;
		float[] data = new float[size];

		float sigma = radius / 3.0f;
		float twoSigmaSquare = 2.0f * sigma * sigma;
		float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
		float total = 0.0f;

		for (int i = -radius; i <= radius; i++) {
			float distance = i * i;
			int index = i + radius;
			data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
			total += data[index];
		}

		for (int i = 0; i < data.length; i++) {
			data[i] /= total;
		}

		Kernel kernel = null;
		if (horizontal) {
			kernel = new Kernel(size, 1, data);
		} else {
			kernel = new Kernel(1, size, data);
		}
		return new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
	}

	public static void simpleBlur(BufferedImage src, BufferedImage dest) {
		BufferedImageOp op = getGaussianBlurFilter(2, false);
		op.filter(src, dest);
	}

	public static byte[] fromBufferedImage2(BufferedImage img, String imagType) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// 得到指定Format图片的writer
		Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(imagType);
		ImageWriter writer = (ImageWriter) iter.next();

		// 得到指定writer的输出参数设置(ImageWriteParam )
		ImageWriteParam iwp = writer.getDefaultWriteParam();
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // 设置可否压缩
		iwp.setCompressionQuality(1f); // 设置压缩质量参数

		iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);

		ColorModel colorModel = ColorModel.getRGBdefault();
		// 指定压缩时使用的色彩模式
		iwp.setDestinationType(
				new javax.imageio.ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));

		writer.setOutput(ImageIO.createImageOutputStream(bos));
		IIOImage iIamge = new IIOImage(img, null, null);
		writer.write(null, iIamge, iwp);
		byte[] d = bos.toByteArray();
		FileUtils.writeByteArrayToFile(new File("11.png"), d);
		return d;
	}

	public static void main(String[] args) throws Exception {
		String dir = "D:\\projects\\zhaosheng\\src\\main\\webapp\\static\\verify\\images";
		SlideImages slideImages = getSlideImages(dir, 50, 50);
		//System.out.println(slideImages.getBackImg());
		//System.out.println(slideImages.getSlideImg());
		//System.out.println(slideImages.getStartX());
		//System.out.println(slideImages.getStartY());
	}

}
