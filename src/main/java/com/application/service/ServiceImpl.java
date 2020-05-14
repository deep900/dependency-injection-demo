package com.application.service;
import java.util.logging.Logger;

import com.application.bean.ServiceBean;
import com.application.configuration.BeanFactory;
import com.application.configuration.BeanFactoryAware;

/**
 * 
 */

/**
 * @author pradheep
 *
 */
public class ServiceImpl implements BeanFactoryAware{
	
	private final static Logger logger = Logger.getLogger(ServiceImpl.class.getName());

	private BeanFactory beanFactory;
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
	
	public String sayHello(){
		ServiceBean bean = (ServiceBean) beanFactory.getBeanByClass(ServiceBean.class);
		if(null != bean){
			bean.setName("Welcome to programming");
		}
		logger.info(bean.getName());
		return bean.getName();
	}
	
}
