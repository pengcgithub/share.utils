package com.share.export.freemark;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.IOException;

public class DocTemplateFactory {
   
	/** freemark模板配置 **/
	private static Configuration configuration = new Configuration();
	
	static {
		configuration.setDefaultEncoding("utf-8");
		configuration.setTemplateLoader(new ClassTemplateLoader(DocTemplateFactory.class, "/export/"));
	}
	
	public static Template getTemplate(String templateName) {
		try {
			return configuration.getTemplate(templateName);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}