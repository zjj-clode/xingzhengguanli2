package com.cloudinte.common.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;


/**
 * @Desc：word操作工具类
 * @Author：wtman
 * @Date：2017-7-24
 */
public class WordUtil {
   
/**
* @Desc：生成word文件
* @Author：wtman
* @Date：WordUtil
* @param dataMap word中需要展示的动态数据，用map集合来保存
* @param templateName word模板名称，例如：test.ftl
* @param fileName 生成的文件名称，例如：test.doc
*/
public static File createWord(Map<String,String> dataMap,String templateName,String fileName){
        try {
        //创建配置实例 
        Configuration configuration = new Configuration();
        
        //设置编码
            configuration.setDefaultEncoding("UTF-8");
            
            //ftl模板文件统一放至 templates.modules.akmi.table 包下面
            configuration.setClassForTemplateLoading(WordUtil.class,"/templates/modules/akmi/table/");
            
            //获取模板 
            Template template = configuration.getTemplate(templateName);
            
            //输出文件
            File outFile = new File(fileName);
            
/*            //如果输出目标文件夹不存在，则创建
            if (!outFile.getParentFile().exists()){
                outFile.getParentFile().mkdirs();
            }
*/            
            //将模板和数据模型合并生成文件 
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));


            //生成文件
            template.process(dataMap, out);
            
            //关闭流
            out.flush();
            out.close();
            return outFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
