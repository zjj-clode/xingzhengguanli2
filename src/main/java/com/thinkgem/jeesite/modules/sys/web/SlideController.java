package com.thinkgem.jeesite.modules.sys.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinkgem.jeesite.modules.sys.utils.SlideUtil;
import com.thinkgem.jeesite.modules.sys.utils.SlideUtil.SlideImages;
import com.thinkgem.jeesite.modules.sys.utils.WordImgUtil;
import com.thinkgem.jeesite.modules.sys.utils.WordImgUtil.WordPoint;

@Controller
@RequestMapping(value = { "${frontPath}/slide" })
public class SlideController {
	
	private static final String SESSION_KEY = "slide_images";
	
	/** 是否验证通过 */
	public static final String SESSION_KEY_SLIDE_VALID = "session_key_slide_valid";
	
	@RequestMapping("/init")
	@ResponseBody
	public int getSlideImages(HttpSession session, int width, int height) throws Exception {
		String dir = session.getServletContext().getRealPath("/static/verify/images");
		SlideImages slideImages = SlideUtil.getSlideImages(dir, width, height);
		session.setAttribute(SESSION_KEY, slideImages);
		return slideImages.getStartY();
	}
	
	@RequestMapping("/backImg")
	public void getBackImg(HttpSession session, ServletOutputStream servletOutputStream, HttpServletResponse response) throws Exception {
		SlideImages slideImages = (SlideImages) session.getAttribute(SESSION_KEY);
		if (slideImages != null) {
			byte[] bytes = FileUtils.readFileToByteArray(new File(slideImages.getBackImg()));
			response.setContentLength(bytes.length);
			IOUtils.write(bytes, servletOutputStream);
		}
	}
	
	@RequestMapping("/slideImg")
	public void getSlideImg(HttpSession session, ServletOutputStream servletOutputStream, HttpServletResponse response) throws Exception {
		SlideImages slideImages = (SlideImages) session.getAttribute(SESSION_KEY);
		if (slideImages != null) {
			byte[] bytes = FileUtils.readFileToByteArray(new File(slideImages.getSlideImg()));
			response.setContentLength(bytes.length);
			IOUtils.write(bytes, servletOutputStream);
		}
	}
	
	@RequestMapping("/check")
	@ResponseBody
	public Map<String, Object> validSlide(int startX, HttpSession session) {
		SlideImages slideImages = (SlideImages) session.getAttribute(SESSION_KEY);
		boolean valid = false;
		if (slideImages != null) {
			valid = SlideUtil.checkSlide(startX, slideImages.getStartX());
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("valid", valid);
		//
		session.setAttribute(SESSION_KEY_SLIDE_VALID, valid);
		//
		if (valid) {
			resultMap.put("realX", slideImages.getStartX());
		}
		return resultMap;
	}
	
	@RequestMapping("/wordInit")
	@ResponseBody
	public List<String> wordInit(HttpSession session, int totalCount, int checkCount, int width, int height) {
		List<String> words = new ArrayList<>();
		List<WordPoint> points = WordImgUtil.getRandomWords(totalCount, checkCount, width, height);
		session.setAttribute("wordPoints", points);
		for (int i = 0; i < checkCount; i++) {
			words.add(points.get(i).getWord() + "");
		}
		return words;
	}
	
	@RequestMapping("/wordImg")
	public void getWordImg(HttpSession session, HttpServletResponse response) throws IOException {
		String dir = session.getServletContext().getRealPath("/static/verify/images");
		@SuppressWarnings("unchecked")
		List<WordPoint> points = (List<WordPoint>) session.getAttribute("wordPoints");
		byte[] bytes = WordImgUtil.getWordImage(dir, points);
		response.setContentLength(bytes.length);
		IOUtils.write(bytes, response.getOutputStream());
	}
	
	@RequestMapping("/wordCheck")
	@ResponseBody
	public boolean checkPoints(HttpSession session, String points) throws Exception {
		ObjectMapper om = new ObjectMapper();
		JavaType javaType = om.getTypeFactory().constructParametricType(List.class, WordPoint.class);
		List<WordPoint> wordPoints = om.readValue(points, javaType);
		@SuppressWarnings("unchecked")
		List<WordPoint> serverPoints = (List<WordPoint>) session.getAttribute("wordPoints");
		boolean result = WordImgUtil.checkPoint(serverPoints, wordPoints);
		return result;
	}
	
}
