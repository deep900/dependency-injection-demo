/**
 * 
 */
package com.application.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author pradheep
 *
 */
public class BeanLoader extends DefaultHandler {

	private final Logger logger = Logger.getLogger(BeanLoader.class.getName());

	private List<BeanDefinition> beanDefinitionList = new ArrayList<BeanDefinition>();
	
	private BeanDefinition beanDefinition = null;

	public void loadBeans(InputStream inputStream) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = saxParserFactory.newSAXParser();			
			saxParser.parse(inputStream, this);			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}	

	@Override
	public void startElement(String uri, String localName, String elementName, Attributes attributes) throws SAXException {
		logger.info(elementName);
		if (elementName.equalsIgnoreCase("bean")) {
			beanDefinition = new BeanDefinition();
			String className = attributes.getValue("className");
			String scope = attributes.getValue("scope");
			beanDefinition.setClassName(className);
			beanDefinition.setScope(scope);
			beanDefinitionList.add(beanDefinition);
		}
	}

	public List<BeanDefinition> getBeanDefinitionList() {
		return beanDefinitionList;
	}
}
