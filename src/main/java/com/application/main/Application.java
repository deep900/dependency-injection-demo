/**
 * 
 */
package com.application.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import com.application.configuration.ApplicationConstants;
import com.application.configuration.BeanFactory;
import com.application.service.ServiceImpl;

/**
 * @author pradheep
 *
 */
public class Application {

	private final static Logger logger = Logger.getLogger(Application.class.getName());
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String resourceName = ApplicationConstants.beanSourceFileName;
		try {
			InputStream inputStream = Application.class.getClassLoader().getResource(resourceName).openStream();
			BeanFactory beanFactory = BeanFactory.getInstance();
			beanFactory.initialize(inputStream);
			logger.info("---------------------------------");
			logger.info("Dependency injection example");
			logger.info("---------------------------------");
			ServiceImpl serviceImpl = (ServiceImpl) beanFactory
					.getBeanByClass(com.application.service.ServiceImpl.class);
			serviceImpl.sayHello();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
