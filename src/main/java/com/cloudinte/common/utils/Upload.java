package com.cloudinte.common.utils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@SuppressWarnings("serial")
public class Upload extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			this.doPost(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	@RequiresPermissions("common:pic:edit")
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String filename = "";
		String photo = "/photo/";
		String savePath = this.getServletConfig().getServletContext().getRealPath("") + photo;
		File f1 = new File(savePath);

		if (!f1.exists()) {
			f1.mkdirs();
		}

		DiskFileItemFactory fac = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");

		List<FileItem> fileList = null;
		try {
			fileList = upload.parseRequest(request);
		} catch (FileUploadException ex) {
			return;
		}

		Iterator<FileItem> it = fileList.iterator();

		String name = "";

		String extName = "";

		while (it.hasNext()) {

			FileItem item = it.next();

			if (!item.isFormField()) {

				name = item.getName();
				if (name == null || name.trim().equals("")) {
					continue;
				}

				//扩展名格式：

				if (name.lastIndexOf(".") >= 0) {
					extName = name.substring(name.lastIndexOf("."));
				}

				if (extName == null || !(extName.toLowerCase().equals(".jpg") || extName.toLowerCase().equals(".gif") || extName.toLowerCase().equals(".png"))) {
					continue;
				}

				File file = null;
				do {

					name = UUID.randomUUID().toString();
					//name=DateUtil.getNowDateString();
					filename = savePath + name + extName;
					file = new File(filename);
				} while (file.exists());

				File saveFile = new File(filename);

				try {
					item.write(saveFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		response.getWriter().print(request.getContextPath() + photo + name + extName);

	}

}