package com.aspectsecurity.unittestsweb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

// Singleton
public class PropertiesHelper {
	private static final String SPRING_EXTERNAL_CONFIG = "/../../../classes/spring_external_config.properties";

	private static PropertiesHelper instance = null;
	private Properties props;

	public static PropertiesHelper getInstance() {
		if (instance == null) {
			instance = new PropertiesHelper();
		}
		return instance;
	}

	private PropertiesHelper() {
		this.props = new Properties();
		try {
			String path = System.getProperty("user.dir") + SPRING_EXTERNAL_CONFIG;
			File f = new File(path);
			if (f.canRead()) {
				FileInputStream fis = new FileInputStream(f);
				this.props.load(fis);	// load properties from prop file
			}
		} catch (NullPointerException | SecurityException | IOException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public String getProperty(String key) {
		return props.getProperty(key);	// can be null
	}
}
