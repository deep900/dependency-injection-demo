/**
 * 
 */
package com.application.configuration;

/**
 * @author pradheep
 *
 */
public class BeanDefinition {
	
	private String className;
	
	private String scope;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@Override
	public String toString() {
		return "BeanDefinition [" + (className != null ? "className=" + className + ", " : "")
				+ (scope != null ? "scope=" + scope : "") + "]";
	}
}
