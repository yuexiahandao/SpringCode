/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.parsing.DefaultsDefinition;

/**
 * Simple JavaBean that holds the defaults specified at the {@code <beans>}
 * level in a standard Spring XML bean definition document:
 * {@code default-lazy-init}, {@code default-autowire}, etc.
 *
 * 简单的javaBean类，里面有xml beans的默认设置项。
 *
 * @author Juergen Hoeller
 * @since 2.0.2
 */
public class DocumentDefaultsDefinition implements DefaultsDefinition {

	private String lazyInit;

	/**
	 * 用于属性注入时的，数组注入
	 */
	private String merge;

	/**
	 * Spring能自动装配Bean与Bean之间的依赖关系，无需使用ref显示指定依赖Bean。
	 * 通过<beans.../>元素的default-autowire属性指定，也可以通过autowire属性指定。
	 * no:不使用自动装配，必须通过ref定义。
	 * byName:根据属性名自动装配
	 * byType:根据属性类型自动装配
	 * constructor:根据属性名自动装配,用构造函数
	 * autodetect:由beanFactory决定用byType或constructor，有默认构造函数，用byType；
	 * Spring启动时将自动搜索，自动装配。若不想自动装配，可使用autowire-candidate="false"
	 */
	private String autowire;

	/**
	 * 这个属性是对类的属性进行检查的标志
	 *
	 * 在Bean被创建时Bean的属性（property）如果在配置文件Bean的定义中没有进行初始化赋值，
	 * 默认情况下Spring对于没有进行初始化的属性（property）是不做检查的。
	 * 但是很多情况下会要求Bean特定的属性必须进行初始化赋值，在Spring2.x中通过在bean标签中使用dependency-check属性设定由Spring进行强制检查的方式。
	 * denpendency-check属性有四个值。 但是在3.0后此设置已经不建议用了。
	 * http://www.csdn123.com/html/exception/544/544707_544709_544712.htm
	 */
	private String dependencyCheck;

	/**
	 * 使用对bean名字进行模式匹配来对自动装配进行限制，其做法是在<beans/>元素的
	 * 'default-autowire-candidates' 属性中进行设置。可以使用通配符,如以'Repository'结尾的bean，
	 * 那么可以设置为"*Repository“。
	 */
	private String autowireCandidates;

	private String initMethod;

	private String destroyMethod;

	private Object source;


	/**
	 * Set the default lazy-init flag for the document that's currently parsed.
	 */
	public void setLazyInit(String lazyInit) {
		this.lazyInit = lazyInit;
	}

	/**
	 * Return the default lazy-init flag for the document that's currently parsed.
	 */
	public String getLazyInit() {
		return this.lazyInit;
	}

	/**
	 * Set the default merge setting for the document that's currently parsed.
	 */
	public void setMerge(String merge) {
		this.merge = merge;
	}

	/**
	 * Return the default merge setting for the document that's currently parsed.
	 */
	public String getMerge() {
		return this.merge;
	}

	/**
	 * Set the default autowire setting for the document that's currently parsed.
	 */
	public void setAutowire(String autowire) {
		this.autowire = autowire;
	}

	/**
	 * Return the default autowire setting for the document that's currently parsed.
	 */
	public String getAutowire() {
		return this.autowire;
	}

	/**
	 * Set the default dependency-check setting for the document that's currently parsed.
	 */
	public void setDependencyCheck(String dependencyCheck) {
		this.dependencyCheck = dependencyCheck;
	}

	/**
	 * Return the default dependency-check setting for the document that's currently parsed.
	 */
	public String getDependencyCheck() {
		return this.dependencyCheck;
	}

	/**
	 * Set the default autowire-candidate pattern for the document that's currently parsed.
	 * Also accepts a comma-separated list of patterns.
	 */
	public void setAutowireCandidates(String autowireCandidates) {
		this.autowireCandidates = autowireCandidates;
	}

	/**
	 * Return the default autowire-candidate pattern for the document that's currently parsed.
	 * May also return a comma-separated list of patterns.
	 */
	public String getAutowireCandidates() {
		return this.autowireCandidates;
	}

	/**
	 * Set the default init-method setting for the document that's currently parsed.
	 */
	public void setInitMethod(String initMethod) {
		this.initMethod = initMethod;
	}

	/**
	 * Return the default init-method setting for the document that's currently parsed.
	 */
	public String getInitMethod() {
		return this.initMethod;
	}

	/**
	 * Set the default destroy-method setting for the document that's currently parsed.
	 */
	public void setDestroyMethod(String destroyMethod) {
		this.destroyMethod = destroyMethod;
	}

	/**
	 * Return the default destroy-method setting for the document that's currently parsed.
	 */
	public String getDestroyMethod() {
		return this.destroyMethod;
	}

	/**
	 * Set the configuration source {@code Object} for this metadata element.
	 * <p>The exact type of the object will depend on the configuration mechanism used.
	 *
	 * 设置为这个元元素配置Source对象，确切的类型取决于使用的配置机制。
	 */
	public void setSource(Object source) {
		this.source = source;
	}

	@Override
	public Object getSource() {
		return this.source;
	}

}
