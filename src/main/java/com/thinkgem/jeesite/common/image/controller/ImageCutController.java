package com.thinkgem.jeesite.common.image.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinkgem.jeesite.common.image.service.ImageCutService;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 在线图片裁剪(覆盖掉原图片)
 * 
 * @author 廖水平
 */
@Controller
@RequestMapping(value = "${adminPath}/common/image")
public class ImageCutController extends BaseController {

	@Autowired
	private ImageCutService imageScale;

	/**
	 * 裁剪页面<br>
	 * uploadNum=1&imgSrcPath=/user/images/201703/04130818wk59.jpg&zoomWidth=152&zoomHeight=152
	 */
	@RequestMapping("/imageAreaSelect")
	public String imageAreaSelect(String uploadBase, String imgSrcPath, Integer zoomWidth, Integer zoomHeight,
			Integer uploadNum, HttpServletRequest request, ModelMap model) {
		model.addAttribute("imgSrcPath", imgSrcPath);
		model.addAttribute("zoomWidth", zoomWidth);
		model.addAttribute("zoomHeight", zoomHeight);
		model.addAttribute("uploadNum", uploadNum);
		return "modules/common/image/imageAreaSelect";
	}

	/**
	 * 完成裁剪页面
	 */
	@RequestMapping("/imageCut")
	public String imageCut(String imgSrcPath, Integer imgTop, Integer imgLeft, Integer imgWidth, Integer imgHeight,
			Integer reMinWidth, Integer reMinHeight, Float imgScale, Integer uploadNum, HttpServletRequest request,
			ModelMap model) {
		try {
			if (imgWidth > 0) {
				String ctx = request.getContextPath();
				imgSrcPath = imgSrcPath.substring(ctx.length());
				File file = new File(request.getSession().getServletContext().getRealPath(imgSrcPath));
				imageScale.resizeFix(file, file, reMinWidth, reMinHeight, getLen(imgTop, imgScale),
						getLen(imgLeft, imgScale), getLen(imgWidth, imgScale), getLen(imgHeight, imgScale));
			} else {
				String ctx = request.getContextPath();
				imgSrcPath = imgSrcPath.substring(ctx.length());
				File file = new File(request.getSession().getServletContext().getRealPath(imgSrcPath));
				imageScale.resizeFix(file, file, reMinWidth, reMinHeight);
			}
			model.addAttribute("uploadNum", uploadNum);
		} catch (Exception e) {
			logger.error("cut image error", e);
			model.addAttribute("message", e.getMessage());
		}
		return "modules/common/image/imageCut";
	}

	private int getLen(int len, float imgScale) {
		return Math.round(len / imgScale);
	}

}
