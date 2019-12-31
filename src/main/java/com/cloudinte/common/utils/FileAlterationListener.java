package com.cloudinte.common.utils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.cms.service.ArticleService;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;

/**
 * 索引目录监听器。
 * <p>
 * <b>当索引目录被删除时能自动重建索引。</b>
 * </p>
 *
 * @author lsp
 *
 */
@Component
public class FileAlterationListener implements InitializingBean, DisposableBean {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;


	@Autowired
	private ArticleService articleService;
	
	private FileAlterationMonitor fileAlterationMonitor;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					// 1分钟后开始监听
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//
				String cmsLuceneDirectory = SettingsUtils.getSysConfig("lucene_index_dir_cms", "/lucene/index/cms");
				cmsLuceneDirectory = LuceneUtils.getRealPath(cmsLuceneDirectory);
				String os = System.getProperty("os.name");
				if (!StringUtils.isBlank(os) && os.toLowerCase().contains("windows")) {
					cmsLuceneDirectory = cmsLuceneDirectory.replace("/", "\\");
				}
				final String cmsDir = cmsLuceneDirectory;
				// 观察者
				File observerCmsDir = new File(cmsDir).getParentFile();
				FileAlterationObserver cmsDirObserver = new FileAlterationObserver(observerCmsDir);
				logger.debug("监听目录变化： {}", observerCmsDir.getAbsolutePath());
				// 添加监听器
				cmsDirObserver.addListener(new FileAlterationListenerAdaptor() {
					// 监听目录删除事件
					@Override
					public void onDirectoryDelete(File directory) {
						logger.debug("onDirectoryDelete ... directory = {}", directory.getAbsolutePath());
						// 检查索引目录是否还存在
						boolean cmsDirExists = new File(cmsDir).exists();
						logger.debug("cmsDirExists = {}", cmsDirExists);
						// 如果不存在就重新建立对应的索引
						if (!cmsDirExists) {
							logger.debug("建立cms索引 。。。");
							articleService.reIndex();
						}
					}
				});
				
				
				// 创建文件变化监听器，并开始监控。
				try {
					fileAlterationMonitor = new FileAlterationMonitor(TimeUnit.SECONDS.toMillis(5), cmsDirObserver);
					fileAlterationMonitor.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	
	@Override
	public void destroy() throws Exception {
		fileAlterationMonitor.stop();
	}
}
