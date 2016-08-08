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

package org.springframework.beans;

/**
 * Interface to be implemented by bean metadata elements
 * that carry a configuration source object.
 *
 * 被用来实现bean元元素的接口，这些元元素保存着源对象的配置信息。
 *
 * BeanMetadataElement接口提供了一个getResource()方法,用来传输一个可配置的源对象。
 *
 * BeanMetadataAttributeAccessor接口既实现了BeanMetadataElement接口提供的
 * getResource()方法也提供了AttributeAccessorSupport 针对属性的增删改查，如上AttributeAccessor的方法。
 *
 * http://www.cnblogs.com/davidwang456/p/4192318.html
 *
 * @author Juergen Hoeller
 * @since 2.0
 */
public interface BeanMetadataElement {

	/**
	 * Return the configuration source {@code Object} for this metadata element
	 * (may be {@code null}).
	 */
	Object getSource();

}
