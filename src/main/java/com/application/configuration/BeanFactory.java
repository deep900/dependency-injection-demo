/**
 * 
 */
package com.application.configuration;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author pradheep
 *
 */
public class BeanFactory {

	private final static Logger logger = Logger.getLogger(BeanFactory.class.getName());

	private static BeanFactory beanFactory = new BeanFactory();

	private HashMap<String, Object> singletonBeans = new HashMap<String, Object>();

	private HashMap<String, List<Object>> prototypeBeans = new HashMap<String, List<Object>>();

	private static boolean isInitialized = false;

	private static BeanLoader beanLoader = new BeanLoader();

	private BeanFactory() {

	}

	private static void initializeBeans(InputStream beanDefinitionFileStream) throws FileNotFoundException {
		beanLoader.loadBeans(beanDefinitionFileStream);		
		logger.info("Loaded :" + beanLoader.getBeanDefinitionList().toString());
	}

	public synchronized void initialize(InputStream beanDefinitionFileStream) {
		try {
			if (!isInitialized) {
				initializeBeans(beanDefinitionFileStream);
				Iterator<BeanDefinition> beanIterator = beanLoader.getBeanDefinitionList().iterator();
				while (beanIterator.hasNext()) {
					BeanDefinition beanDefinition = beanIterator.next();
					Class classObj = null;
					try {
						classObj = Class.forName(beanDefinition.getClassName());
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
					if (beanDefinition.getScope().equalsIgnoreCase("singleton")) {
						try {
							getBean(classObj, beanDefinition.getScope());
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						}
					}
				}
			}
			isInitialized = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void setBeanFactoryContext(Object obj) {
		if (obj instanceof BeanFactoryAware) {
			BeanFactoryAware beanFactoryAware = (BeanFactoryAware) obj;
			beanFactoryAware.setBeanFactory(getInstance());
		}
	}

	public static BeanFactory getInstance() {
		return beanFactory;
	}
	
	public Object getBeanByClass(Class className){
		String key = className.getName();
		if(singletonBeans.containsKey(key)){
			return singletonBeans.get(key);
		}
		else{
			return getBean(className, "prototype");
		}
	}

	private Object getBean(Class className, String scope) throws IllegalArgumentException {
		logger.info("Loading :" + className.getName() + " With scope :" + scope);
		switch (scope.toLowerCase()) {
		case "singleton":
			return provideSingletonBean(className);
		case "proptotype":
			return providePrototypeBean(className);
		default:
			logger.info("Default scope taken as singleton");
			return provideSingletonBean(className);
		}
	}

	private Object provideSingletonBean(Class className) {
		if (singletonBeans.containsKey(className.getName())) {
			return singletonBeans.get(className.getName());
		} else {
			Object obj = null;
			try {
				obj = loadClassObject(className);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			if (null != obj) {
				singletonBeans.put(className.getName(), obj);
			}
			setBeanFactoryContext(obj);
			return obj;
		}
	}

	private Object providePrototypeBean(Class className) {
		logger.info("Providing a prototype bean of class :" + className.getName());
		String key = className.getName();
		Object obj = null;
		try {
			obj = loadClassObject(className);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (prototypeBeans.containsKey(key)) {
			List<Object> objectList = prototypeBeans.get(key);
			objectList.add(obj);
		} else {
			List<Object> objectList = new ArrayList<Object>();
			objectList.add(obj);
			prototypeBeans.put(key, objectList);
		}
		setBeanFactoryContext(obj);
		return obj;
	}

	private Object loadClassObject(Class className)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		logger.info("Loading the class :" + className.getName());
		return Class.forName(className.getName()).newInstance();
	}

	// TO be used later //
	private void destroyPrototypeObject(Object obj) {
		if (null == obj) {
			return;
		}
		String key = obj.getClass().getName();
		if (prototypeBeans.containsKey(key)) {
			List<Object> objectList = prototypeBeans.get(obj.getClass().getName());
			Iterator iter = objectList.iterator();
			Collection toBeRemoved = new ArrayList();
			while (iter.hasNext()) {
				Object x = iter.next();
				if (x.equals(obj)) {
					toBeRemoved.add(x);
				}
			}
			objectList.removeAll(toBeRemoved);
			System.gc();
		}
	}
}
