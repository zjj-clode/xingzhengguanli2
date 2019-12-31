package com.cloudinte.modules.xingzhengguanli.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;

@SuppressWarnings("deprecation")
public class WordUtils {
	
	//配置信息,代码本身写的还是很可读的,就不过多注解了  
    private static Configuration configuration = null;  
    
    static {  
        configuration = new Configuration();  
        configuration.setDefaultEncoding("utf-8");  
   }  
  
    private WordUtils() {  
        throw new AssertionError();  
    }  
  
    public static void exportMillCertificateWord(HttpServletRequest request, HttpServletResponse response, 
    		Map<String, Object> map,String title,String ftlFile,String ftlPath) throws IOException {  
        
    	
        File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null;  
        
        String encodedFilename = null;
		String userAgent = request.getHeader("User-Agent");
		boolean ifIE = userAgent.contains("MSIE") || userAgent.contains("Trident");
		
        try {  
        	
        	if (ifIE) {
				encodedFilename = URLEncoder.encode(title, "UTF-8");
			} else {
				// 非IE浏览器的处理：
				encodedFilename = new String(title.getBytes("UTF-8"), "ISO-8859-1");
			}
        	
        	 String path = request.getSession().getServletContext().getRealPath("/"); 
			 String filePath = path  +"upload"+File.separator +"zc"+File.separator + encodedFilename + ".doc";
        	
            // 调用工具类的createDoc方法生成Word文档  
            file = createDoc(map,filePath,ftlFile,ftlPath);  
            fin = new FileInputStream(file);  
  
            response.setContentType("multipart/form-data");
            // 设置浏览器以下载的方式处理该文件名  
            String fileName = encodedFilename+".doc";  
            response.setHeader("Content-Disposition", "attachment;filename=".concat(fileName));  
  			//您在这里稍微注意一下,中文在火狐下会出现乱码的现象
            out = response.getOutputStream();  
            byte[] buffer = new byte[512];  // 缓冲区  
            int bytesToRead = -1;  
            // 通过循环将读入的Word文件的内容输出到浏览器中  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } finally {  
            if(fin != null) fin.close();  
            if(out != null) out.close();  
            if(file != null) file.delete(); // 删除临时文件  
        }  
    }  
  
    public static File createDoc(Map<?, ?> dataMap,String filePath,String ftlFile,String ftlPath) throws IOException {  
    	
    	//设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
		//这里我们的模板是放在template包下面
		configuration.setDirectoryForTemplateLoading(new File(ftlPath));
		
		Template freemarkerTemplate = configuration.getTemplate(ftlFile);  
        
        File f = new File(filePath);
       if(!f.exists()){
    	   f.getParentFile().mkdir();
    	   f.createNewFile();
       }
        try {  
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开  
        	FileOutputStream o = new FileOutputStream(f);
            Writer w = new OutputStreamWriter(o, "utf-8");  
            freemarkerTemplate.process(dataMap, w);  
            w.close();  
            o.flush();
            o.close();
        } catch (Exception ex) {  
            ex.printStackTrace();  
            throw new RuntimeException(ex);  
        }  
        return f;  
    }  

}
