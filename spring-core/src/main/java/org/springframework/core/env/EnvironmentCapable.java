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

package org.springframework.core.env;

/**
 * Interface indicating a component that contains and exposes an {@link Environment} reference.
 *
 * 代表包含或者暴露Environment引用的接口！
 *
 * <p>All Spring application contexts are EnvironmentCapable, and the interface is used primarily
 * for performing {@code instanceof} checks in framework methods that accept BeanFactory
 * instances that may or may not actually be ApplicationContext instances in order to interact
 * with the environment if indeed it is available.
 * 所有的SpringContext的实现类都是包含这个接口的。
 *
 * <p>As mentioned, {@link org.springframework.context.ApplicationContext ApplicationContext}
 * extends EnvironmentCapable, and thus exposes a {@link #getEnvironment()} method; however,
 * {@link org.springframework.context.ConfigurableApplicationContext ConfigurableApplicationContext}
 * redefines {@link org.springframework.context.ConfigurableApplicationContext#getEnvironment
 * getEnvironment()} and narrows the signature to return a {@link ConfigurableEnvironment}.
 * The effect is that an Environment object is 'read-only' until it is being accessed from
 * a ConfigurableApplicationContext, at which point it too may be configured.
 *
 * 所有的ApplicationContext都会重写getEnvironment，并且返回ConfigurableEnvironment。
 *
 * @author Chris Beams
 * @since 3.1
 * @see Environment
 * @see ConfigurableEnvironment
 * @see org.springframework.context.ConfigurableApplicationContext#getEnvironment()
 *
 * 定义获取Environment方法。
 */
public interface EnvironmentCapable {

	/**
	 * Return the {@link Environment} associated with this component.
	 *
	 * 获取Environment的信息
	 */
	Environment getEnvironment();

}
