package com.share.export.freemark;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

import java.io.*;
import java.util.Map;

public class DocumentHandler {

	private static final String EXPORT_PATH = "/export";

	private String template = null;

	private Configuration configuration = null;

	public DocumentHandler() {
		configuration = new Configuration(new Version(2, 3, 23));
		configuration.setDefaultEncoding("UTF-8");
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void createDoc(Map<String, Object> dataMap, String fileName)
			throws UnsupportedEncodingException, FileNotFoundException {
		configuration.setClassForTemplateLoading(this.getClass(), EXPORT_PATH);
		Template t = null;
		File outFile = new File(fileName);
		FileOutputStream fos = new FileOutputStream(outFile);
		Writer out = null;
		out = new OutputStreamWriter(fos, "UTF-8");
		try {
			t = configuration.getTemplate(template + ".ftl");
			t.process(dataMap, out);
			out.flush();
		} catch (Exception e) {
			if (outFile.exists())
				outFile.delete();
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
