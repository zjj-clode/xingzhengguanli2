package com.thinkgem.jeesite.common.image.service;

import java.awt.Color;
import java.io.File;

import magick.Magick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.thinkgem.jeesite.common.image.utils.AverageImageScale;
import com.thinkgem.jeesite.common.image.utils.MagickImageScale;

/**
 * @author 廖水平
 */
@Component
public class ImageCutService {

	private static final Logger log = LoggerFactory.getLogger(ImageCutService.class);

	/**
	 * 裁剪图片
	 */
	public void resizeFix(File srcFile, File destFile, int boxWidth, int boxHeight) throws Exception {
		if (isMagick) {
			MagickImageScale.resizeFix(srcFile, destFile, boxWidth, boxHeight);
		} else {
			AverageImageScale.resizeFix(srcFile, destFile, boxWidth, boxHeight);
		}
	}

	/**
	 * 裁剪图片
	 */
	public void resizeFix(File srcFile, File destFile, int boxWidth, int boxHeight, int cutTop, int cutLeft,
			int cutWidth, int catHeight) throws Exception {
		if (isMagick) {
			MagickImageScale.resizeFix(srcFile, destFile, boxWidth, boxHeight, cutTop, cutLeft, cutWidth, catHeight);
		} else {
			AverageImageScale.resizeFix(srcFile, destFile, boxWidth, boxHeight, cutTop, cutLeft, cutWidth, catHeight);
		}
	}

	/**
	 * 添加水印
	 */
	public void imageMark(File srcFile, File destFile, int minWidth, int minHeight, int pos, int offsetX, int offsetY,
			String text, Color color, int size, int alpha) throws Exception {
		if (isMagick) {
			MagickImageScale.imageMark(srcFile, destFile, minWidth, minHeight, pos, offsetX, offsetY, text, color,
					size, alpha);
		} else {
			AverageImageScale.imageMark(srcFile, destFile, minWidth, minHeight, pos, offsetX, offsetY, text, color,
					size, alpha);
		}
	}

	/**
	 * 添加水印
	 */
	public void imageMark(File srcFile, File destFile, int minWidth, int minHeight, int pos, int offsetX, int offsetY,
			File markFile) throws Exception {
		if (isMagick) {
			MagickImageScale.imageMark(srcFile, destFile, minWidth, minHeight, pos, offsetX, offsetY, markFile);
		} else {
			AverageImageScale.imageMark(srcFile, destFile, minWidth, minHeight, pos, offsetX, offsetY, markFile);
		}
	}

	/**
	 * 检查操作系统上是否安装了magick
	 */
	public void init() {
		if (tryMagick) {
			try {
				System.setProperty("jmagick.systemclassloader", "no");
				new Magick();
				log.info("using jmagick");
				isMagick = true;
			} catch (Throwable e) {
				log.warn("load jmagick fail, use java image scale. message:{}", e.getMessage());
				isMagick = false;
			}
		} else {
			log.info("jmagick is disabled.");
			isMagick = false;
		}
	}

	/**
	 * true:magick方式,false:java方式
	 */
	private boolean isMagick = false;
	private boolean tryMagick = true;

	public void setTryMagick(boolean tryMagick) {
		this.tryMagick = tryMagick;
	}

}
