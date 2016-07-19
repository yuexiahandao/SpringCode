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

package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

/**
 * Defines a factory which can return an Object instance
 * (possibly shared or independent) when invoked.
 * 定义了一个工厂,可以返回一个对象实例(可能共享或独立)，当调用时。
 *
 * <p>This interface is typically used to encapsulate a generic factory which
 * returns a new instance (prototype) of some target object on each invocation.
 * 通常使用这个接口来封装一个通用的工厂，返回一个新实例(原型)的目标对象在每次调用。
 *
 * <p>This interface is similar to {@link FactoryBean}, but implementations
 * of the latter are normally meant to be defined as SPI instances in a
 * {@link BeanFactory}, while implementations of this class are normally meant
 * to be fed as an API to other beans (through injection). As such, the
 * {@code getObject()} method has different exception handling behavior.
 *
 * 这个接口类似于FactoryBean,但是后者的实现通常意味着在BeanFactory中被定义为SPI实例,
 * 而实现这个类的通常的意思作为一个API提供其他的bean(通过注射)。因此,
 * getObject()方法有不同的异常处理的行为。
 *
 * @author Colin Sampaleanu
 * @since 1.0.2
 * @see FactoryBean
 */
public interface ObjectFactory<T> {

	/**
	 * Return an instance (possibly shared or independent)
	 * of the object managed by this factory.
	 * 返回(可能共享或独立)由这个工厂管理的一个实例对象。
	 * @return an instance of the bean (should never be {@code null})
	 * @throws BeansException in case of creation errors
	 */
	T getObject() throws BeansException;

}
