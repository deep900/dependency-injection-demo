/**
 * 
 */
package com.application.configuration;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.application.main.Application;
import com.application.service.ServiceImpl;

/**
 * @author pradheep
 *
 */
@RunWith(JUnit4.class)
public class ApplicationTest {

	@Test
	public void testA() {
		System.out.println("Testing bean injection.");
		String resourceName = ApplicationConstants.beanSourceFileName;
		InputStream inputStream = null;
		try {
			inputStream = Application.class.getClassLoader().getResource(resourceName).openStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BeanFactory beanFactory = BeanFactory.getInstance();
		beanFactory.initialize(inputStream);
		ServiceImpl serviceImpl = (ServiceImpl) beanFactory.getBeanByClass(com.application.service.ServiceImpl.class);
		Assert.assertNotNull(serviceImpl);
		Assert.assertEquals("Welcome to programming",serviceImpl.sayHello());
	}
}
