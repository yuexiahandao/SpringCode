/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility class for working with Strings that have placeholder values in them. A placeholder takes the form
 * {@code ${name}}. Using {@code PropertyPlaceholderHelper} these placeholders can be substituted for
 * user-supplied values. <p> Values for substitution can be supplied using a {@link Properties} instance or
 * using a {@link PlaceholderResolver}.
 *
 * 这是一个工具类，同有占位符的字符串进行操作。使用PropertyPlaceholderHelper，这些占位符可以被用户提供的值替换掉。
 * 被替换的值可以使用Properties的实例或者PlaceholderResolver来获取。
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 3.0
 */
public class PropertyPlaceholderHelper {

	private static final Log logger = LogFactory.getLog(PropertyPlaceholderHelper.class);

	private static final Map<String, String> wellKnownSimplePrefixes = new HashMap<String, String>(4);

	static {
		wellKnownSimplePrefixes.put("}", "{");
		wellKnownSimplePrefixes.put("]", "[");
		wellKnownSimplePrefixes.put(")", "(");
	}


	private final String placeholderPrefix;

	private final String placeholderSuffix;

	private final String simplePrefix;

	private final String valueSeparator;

	private final boolean ignoreUnresolvablePlaceholders;


	/**
	 * Creates a new {@code PropertyPlaceholderHelper} that uses the supplied prefix and suffix.
	 * Unresolvable placeholders are ignored.
	 * @param placeholderPrefix the prefix that denotes the start of a placeholder
	 * @param placeholderSuffix the suffix that denotes the end of a placeholder
	 */
	public PropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix) {
		this(placeholderPrefix, placeholderSuffix, null, true);
	}

	/**
	 * Creates a new {@code PropertyPlaceholderHelper} that uses the supplied prefix and suffix.
	 * @param placeholderPrefix the prefix that denotes the start of a placeholder
	 * @param placeholderSuffix the suffix that denotes the end of a placeholder
	 * @param valueSeparator the separating character between the placeholder variable
	 * and the associated default value, if any
	 * @param ignoreUnresolvablePlaceholders indicates whether unresolvable placeholders should
	 * be ignored ({@code true}) or cause an exception ({@code false})
	 */
	public PropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix,
			String valueSeparator, boolean ignoreUnresolvablePlaceholders) {

		Assert.notNull(placeholderPrefix, "'placeholderPrefix' must not be null");
		Assert.notNull(placeholderSuffix, "'placeholderSuffix' must not be null");
		this.placeholderPrefix = placeholderPrefix;
		this.placeholderSuffix = placeholderSuffix;
		String simplePrefixForSuffix = wellKnownSimplePrefixes.get(this.placeholderSuffix);
		if (simplePrefixForSuffix != null && this.placeholderPrefix.endsWith(simplePrefixForSuffix)) {
			this.simplePrefix = simplePrefixForSuffix;
		}
		else {
			this.simplePrefix = this.placeholderPrefix;
		}
		this.valueSeparator = valueSeparator;
		this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
	}


	/**
	 * Replaces all placeholders of format {@code ${name}} with the corresponding
	 * property from the supplied {@link Properties}.
	 * @param value the value containing the placeholders to be replaced
	 * @param properties the {@code Properties} to use for replacement
	 * @return the supplied value with placeholders replaced inline
	 */
	public String replacePlaceholders(String value, final Properties properties) {
		Assert.notNull(properties, "'properties' must not be null");
		return replacePlaceholders(value, new PlaceholderResolver() {
			@Override
			public String resolvePlaceholder(String placeholderName) {
				return properties.getProperty(placeholderName);
			}
		});
	}

	/**
	 * Replaces all placeholders of format {@code ${name}} with the value returned
	 * from the supplied {@link PlaceholderResolver}.
	 * @param value the value containing the placeholders to be replaced
	 * @param placeholderResolver the {@code PlaceholderResolver} to use for replacement
	 * @return the supplied value with placeholders replaced inline
	 *
	 * 又是一个包装函数，找到相关的值后，现在进行替换
	 */
	public String replacePlaceholders(String value, PlaceholderResolver placeholderResolver) {
		Assert.notNull(value, "'value' must not be null");
		return parseStringValue(value, placeholderResolver, new HashSet<String>());
	}

	protected String parseStringValue(
			String strVal, PlaceholderResolver placeholderResolver, Set<String> visitedPlaceholders) {

		// 创建一个StringBuilder类方便更改。
		StringBuilder result = new StringBuilder(strVal);

		// 找到第一个占位符的起点
		int startIndex = strVal.indexOf(this.placeholderPrefix);
		// 循环去找，直到找到所有的占位符
		while (startIndex != -1) {
			// 结尾的占位符
			int endIndex = findPlaceholderEndIndex(result, startIndex);
			if (endIndex != -1) {
				// 取得名称key
				String placeholder = result.substring(startIndex + this.placeholderPrefix.length(), endIndex);
				String originalPlaceholder = placeholder;
				// 将找到的占位符记录下来，防止出现循环，出现循环的时候报错。这里可以查看下面的又对这个方法进行了调用，
				// 这个时候这个检查才有用 ${${user.name}},你会发现当${user.name}='${user.name}'的时候循环依赖的问题就出现了。
				if (!visitedPlaceholders.add(originalPlaceholder)) {
					throw new IllegalArgumentException(
							"Circular placeholder reference '" + originalPlaceholder + "' in property definitions");
				}
				// Recursive invocation, parsing placeholders contained in the placeholder key.
				// 所以说是支持${${user.name}}的方式的
				placeholder = parseStringValue(placeholder, placeholderResolver, visitedPlaceholders);
				// Now obtain the value for the fully resolved key...
				// 找到value值，有两种模式
				String propVal = placeholderResolver.resolvePlaceholder(placeholder);
				if (propVal == null && this.valueSeparator != null) {
					// 我操，原来在placeholder中可以放置默认的设置。 这个特性很重要啊！
					int separatorIndex = placeholder.indexOf(this.valueSeparator);
					if (separatorIndex != -1) {
						String actualPlaceholder = placeholder.substring(0, separatorIndex);
						String defaultValue = placeholder.substring(separatorIndex + this.valueSeparator.length());
						propVal = placeholderResolver.resolvePlaceholder(actualPlaceholder);
						if (propVal == null) {
							propVal = defaultValue;
						}
					}
				}
				if (propVal != null) {
					// Recursive invocation, parsing placeholders contained in the
					// previously resolved placeholder value.
					// 对于值里面的占位符，再进行一次替换
					propVal = parseStringValue(propVal, placeholderResolver, visitedPlaceholders);
					// 将占位符的值替换掉
					result.replace(startIndex, endIndex + this.placeholderSuffix.length(), propVal);
					if (logger.isTraceEnabled()) {
						logger.trace("Resolved placeholder '" + placeholder + "'");
					}
					// 寻找下一个开始位置
					startIndex = result.indexOf(this.placeholderPrefix, startIndex + propVal.length());
				}
				else if (this.ignoreUnresolvablePlaceholders) {
					// Proceed with unprocessed value.
					// 没有的话跳过这个占位符，不做任何处理，这就是startIndex的作用吧！
					startIndex = result.indexOf(this.placeholderPrefix, endIndex + this.placeholderSuffix.length());
				}
				else {
					// 如果没有配置ignoreUnresolvablePlaceholders的话，那么就进行报错处理流程
					throw new IllegalArgumentException("Could not resolve placeholder '" +
							placeholder + "'" + " in string value \"" + strVal + "\"");
				}
				// 所有的嵌套占位符替换完成后删除这条占位符的记录。循环检查只在嵌套的占位符中进行。
				visitedPlaceholders.remove(originalPlaceholder);
			}
			else {
				// 没有找到结尾的占位符，退出循环
				startIndex = -1;
			}
		}

		return result.toString();
	}

	private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
		int index = startIndex + this.placeholderPrefix.length();
		int withinNestedPlaceholder = 0;
		while (index < buf.length()) {
			if (StringUtils.substringMatch(buf, index, this.placeholderSuffix)) {
				if (withinNestedPlaceholder > 0) {
					withinNestedPlaceholder--;
					index = index + this.placeholderSuffix.length();
				}
				else {
					return index;
				}
			}
			else if (StringUtils.substringMatch(buf, index, this.simplePrefix)) {
				withinNestedPlaceholder++;
				index = index + this.simplePrefix.length();
			}
			else {
				index++;
			}
		}
		return -1;
	}


	/**
	 * Strategy interface used to resolve replacement values for placeholders contained in Strings.
	 * 策略接口，用于解决字符串中占位符的替换。
	 */
	public static interface PlaceholderResolver {

		/**
		 * Resolve the supplied placeholder name to the replacement value.
		 * @param placeholderName the name of the placeholder to resolve
		 * @return the replacement value, or {@code null} if no replacement is to be made
		 */
		String resolvePlaceholder(String placeholderName);
	}

}
