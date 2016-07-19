/*
 * Copyright 2002-2013 the original author or authors.
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

package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Simple interface for bean definition readers.
 * Specifies load methods with Resource and String location parameters.
 *
 * <p>Concrete bean definition readers can of course add additional
 * load and register methods for bean definitions, specific to
 * their bean definition format.
 * bean定义的阅读器当然可以为bean定义添加额外的加载和注册方法用来实现特定的bean定义格式。
 *
 * <p>Note that a bean definition reader does not have to implement
 * this interface. It only serves as suggestion for bean definition
 * readers that want to follow standard naming conventions.
 *
 * 注意所有的bean定义阅读器，并不是一定要实现这个接口，这只是一个建议接口，这样可以实现标准的名称转换。
 *
 * @author Juergen Hoeller
 * @since 1.1
 * @see org.springframework.core.io.Resource
 *
 * 主要定义资源文件读取并转换为BeanDefinition的各个功能。
 */
public interface BeanDefinitionReader {

	/**
	 * Return the bean factory to register the bean definitions with.
	 * <p>The factory is exposed through the BeanDefinitionRegistry interface,
	 * encapsulating the methods that are relevant for bean definition handling.
	 *
	 * 其实这个DefaultListableBeanFactory已经继承了这个BeanDefinitionRegistry，所以Reader中传进来的就是
	 * 各种BeanFactory，就是用于存放各种BeanDefinition（注册登记使用）
	 */
	BeanDefinitionRegistry getRegistry();

	/**
	 * Return the resource loader to use for resource locations.
	 * Can be checked for the <b>ResourcePatternResolver</b> interface and cast
	 * accordingly, for loading multiple resources for a given resource pattern.
	 * <p>Null suggests that absolute resource loading is not available
	 * for this bean definition reader.
	 * <p>This is mainly meant to be used for importing further resources
	 * from within a bean definition resource, for example via the "import"
	 * tag in XML bean definitions. It is recommended, however, to apply
	 * such imports relative to the defining resource; only explicit full
	 * resource locations will trigger absolute resource loading.
	 * <p>There is also a {@code loadBeanDefinitions(String)} method available,
	 * for loading bean definitions from a resource location (or location pattern).
	 * This is a convenience to avoid explicit ResourceLoader handling.
	 * @see #loadBeanDefinitions(String)
	 * @see org.springframework.core.io.support.ResourcePatternResolver
	 *
	 * 这里需要使用ResourceLoader来处理资源的加载，聚合使用。
	 */
	ResourceLoader getResourceLoader();

	/**
	 * Return the class loader to use for bean classes.
	 * <p>{@code null} suggests to not load bean classes eagerly
	 * but rather to just register bean definitions with class names,
	 * with the corresponding Classes to be resolved later (or never).
	 *
	 * 获取Bean需要的类加载器
	 */
	ClassLoader getBeanClassLoader();

	/**
	 * Return the BeanNameGenerator to use for anonymous beans
	 * (without explicit bean name specified).
	 * 如果没有指定bean的名称，那么需要使用BeanNameGenerator这个类来生成一个
	 * Bean的唯一名称。
	 */
	BeanNameGenerator getBeanNameGenerator();


	/**
	 * Load bean definitions from the specified resource.
	 * @param resource the resource descriptor
	 * @return the number of bean definitions found
	 * @throws BeanDefinitionStoreException in case of loading or parsing errors
	 *
	 * 这些重载方法是注册Beans的真正入口，哈哈，终于找到你了。
	 */
	int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException;

	/**
	 * Load bean definitions from the specified resources.
	 * @param resources the resource descriptors
	 * @return the number of bean definitions found
	 * @throws BeanDefinitionStoreException in case of loading or parsing errors
	 */
	int loadBeanDefinitions(Resource... resources) throws BeanDefinitionStoreException;

	/**
	 * Load bean definitions from the specified resource location.
	 * <p>The location can also be a location pattern, provided that the
	 * ResourceLoader of this bean definition reader is a ResourcePatternResolver.
	 * @param location the resource location, to be loaded with the ResourceLoader
	 * (or ResourcePatternResolver) of this bean definition reader
	 * @return the number of bean definitions found
	 * @throws BeanDefinitionStoreException in case of loading or parsing errors
	 * @see #getResourceLoader()
	 * @see #loadBeanDefinitions(org.springframework.core.io.Resource)
	 * @see #loadBeanDefinitions(org.springframework.core.io.Resource[])
	 */
	int loadBeanDefinitions(String location) throws BeanDefinitionStoreException;

	/**
	 * Load bean definitions from the specified resource locations.
	 * @param locations the resource locations, to be loaded with the ResourceLoader
	 * (or ResourcePatternResolver) of this bean definition reader
	 * @return the number of bean definitions found
	 * @throws BeanDefinitionStoreException in case of loading or parsing errors
	 */
	int loadBeanDefinitions(String... locations) throws BeanDefinitionStoreException;

}
