package org.springframework.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 读取配置在xml文件中的bean定义信息
 *
 * @author derekyi
 * @date 2020/11/26
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

	public static final String BEAN_ELEMENT = "bean";
	public static final String PROPERTY_ELEMENT = "property";
	public static final String ID_ATTRIBUTE = "id";
	public static final String NAME_ATTRIBUTE = "name";
	public static final String CLASS_ATTRIBUTE = "class";
	public static final String VALUE_ATTRIBUTE = "value";

	/**
	 * 属性的引用
	 */
	public static final String REF_ATTRIBUTE = "ref";
	public static final String INIT_METHOD_ATTRIBUTE = "init-method";
	public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
	public static final String SCOPE_ATTRIBUTE = "scope";
	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
		super(registry);
	}

	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
		super(registry, resourceLoader);
	}

	@Override
	public void loadBeanDefinitions(String location) throws BeansException {
		ResourceLoader resourceLoader = getResourceLoader();
		Resource resource = resourceLoader.getResource(location);
		loadBeanDefinitions(resource);
	}

	@Override
	public void loadBeanDefinitions(Resource resource) throws BeansException {
		try {
			InputStream inputStream = resource.getInputStream();
			try {
				doLoadBeanDefinitions(inputStream);
			} finally {
				inputStream.close();
			}
		} catch (IOException | DocumentException ex) {
			throw new BeansException("IOException parsing XML document from " + resource, ex);
		}
	}

	protected void doLoadBeanDefinitions(InputStream inputStream) throws DocumentException {
		// //该 Document 对象表示整个 XML 文档。
		// Document document = XmlUtil.readXML(inputStream);
		// // 调用 getDocumentElement() 方法获取 XML 文档的根元素。指<Beans>
		// Element root = document.getDocumentElement();
		// NodeList childNodes = root.getChildNodes();
		// for (int i = 0; i < childNodes.getLength(); i++) {
		// //如果 childNodes 是一个节点列表，并且 childNodes.item(i) 返回的是一个元素节点，
		// 	// 那么 childNodes.item(i) instanceof Element 的结果为 true，表示当前节点是一个元素节点。
		// 	// 否则，如果返回的是其他类型的节点（如文本节点或注释节点），结果将为 false。
		// 	if (childNodes.item(i) instanceof Element) {
		// 		//判断是否为bean元素
		// 		if (BEAN_ELEMENT.equals(((Element) childNodes.item(i)).getNodeName())) {
		// 			//解析bean标签
		// 			Element bean = (Element) childNodes.item(i);
		// 			String id = bean.getAttribute(ID_ATTRIBUTE);
		// 			String name = bean.getAttribute(NAME_ATTRIBUTE);
		// 			String className = bean.getAttribute(CLASS_ATTRIBUTE);
		// 			String initMethodName = bean.getAttribute(INIT_METHOD_ATTRIBUTE);
		// 			String destroyMethodName = bean.getAttribute(DESTROY_METHOD_ATTRIBUTE);
		// 			String beanScope = bean.attributeValue(SCOPE_ATTRIBUTE);
		// 			Class<?> clazz = null;
		// 			try {
		// 				//根据给定的类名 className 获取对应的 Class 对象
		// 				clazz = Class.forName(className);
		// 			} catch (ClassNotFoundException e) {
		// 				throw new BeansException("Cannot find class [" + className + "]");
		// 			}
		// 			//id优先于name
		// 			String beanName = StrUtil.isNotEmpty(id) ? id : name;
		// 			if (StrUtil.isEmpty(beanName)) {
		// 				//如果id和name都为空，将类名的第一个字母转为小写后作为bean的名称
		// 				beanName = StrUtil.lowerFirst(clazz.getSimpleName());
		// 			}
		//
		// 			BeanDefinition beanDefinition = new BeanDefinition(clazz);
		// 			beanDefinition.setInitMethodName(initMethodName);
		// 			beanDefinition.setDestroyMethodName(destroyMethodName);
		//
		// 			for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
		// 				if (bean.getChildNodes().item(j) instanceof Element) {
		// 					if (PROPERTY_ELEMENT.equals(((Element) bean.getChildNodes().item(j)).getNodeName())) {
		// 						//解析property标签
		// 						Element property = (Element) bean.getChildNodes().item(j);
		// 						String nameAttribute = property.getAttribute(NAME_ATTRIBUTE);
		// 						String valueAttribute = property.getAttribute(VALUE_ATTRIBUTE);
		// 						String refAttribute = property.getAttribute(REF_ATTRIBUTE);
		//
		// 						if (StrUtil.isEmpty(nameAttribute)) {
		// 							throw new BeansException("The name attribute cannot be null or empty");
		// 						}
		//
		// 						Object value = valueAttribute;
		// 						if (StrUtil.isNotEmpty(refAttribute)) {
		// 							value = new BeanReference(refAttribute);
		// 						}
		// 						PropertyValue propertyValue = new PropertyValue(nameAttribute, value);
		// 						beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
		// 					}
		// 				}
		// 			}
		// 			if (getRegistry().containsBeanDefinition(beanName)) {
		// 				//beanName不能重名
		// 				throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
		// 			}
		// 			//注册BeanDefinition
		// 			getRegistry().registerBeanDefinition(beanName, beanDefinition);
		// 		}
		// 	}
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);

		org.dom4j.Element beans = document.getRootElement();
		List<org.dom4j.Element> beanList = beans.elements(BEAN_ELEMENT);
		for (org.dom4j.Element bean : beanList) {
			String beanId = bean.attributeValue(ID_ATTRIBUTE);
			String beanName = bean.attributeValue(NAME_ATTRIBUTE);
			String className = bean.attributeValue(CLASS_ATTRIBUTE);
			String initMethodName = bean.attributeValue(INIT_METHOD_ATTRIBUTE);
			String destroyMethodName = bean.attributeValue(DESTROY_METHOD_ATTRIBUTE);
			String beanScope = bean.attributeValue(SCOPE_ATTRIBUTE);

			Class<?> clazz;
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new BeansException("Cannot find class [" + className + "]");
			}
			//id优先于name
			beanName = StrUtil.isNotEmpty(beanId) ? beanId : beanName;
			if (StrUtil.isEmpty(beanName)) {
				//如果id和name都为空，将类名的第一个字母转为小写后作为bean的名称
				beanName = StrUtil.lowerFirst(clazz.getSimpleName());
			}

			BeanDefinition beanDefinition = new BeanDefinition(clazz);
			beanDefinition.setInitMethodName(initMethodName);
			beanDefinition.setDestroyMethodName(destroyMethodName);
			if (StrUtil.isNotEmpty(beanScope)) {
				beanDefinition.setScope(beanScope);
			}

			List<org.dom4j.Element> propertyList = bean.elements(PROPERTY_ELEMENT);
			for (Element property : propertyList) {
				String propertyNameAttribute = property.attributeValue(NAME_ATTRIBUTE);
				String propertyValueAttribute = property.attributeValue(VALUE_ATTRIBUTE);
				String propertyRefAttribute = property.attributeValue(REF_ATTRIBUTE);

				if (StrUtil.isEmpty(propertyNameAttribute)) {
					throw new BeansException("The name attribute cannot be null or empty");
				}

				Object value = propertyValueAttribute;
				if (StrUtil.isNotEmpty(propertyRefAttribute)) {
					value = new BeanReference(propertyRefAttribute);
				}
				PropertyValue propertyValue = new PropertyValue(propertyNameAttribute, value);
				beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
			}
			if (getRegistry().containsBeanDefinition(beanName)) {
				//beanName不能重名
				throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
			}
			//注册BeanDefinition
			getRegistry().registerBeanDefinition(beanName, beanDefinition);
		}
		}
	}

