package com.game.board.api.chutesladders.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ConfigUtility {
	@Autowired
	private Environment environment;

	public String getStringProperty(String pPropertyKey) {
		return environment.getProperty(pPropertyKey);
	}

	/**
	 * get a string property's value
	 * 
	 * @param propKey      property key
	 * @param defaultValue default value if the property is not found
	 * @return value
	 */
	public String getStringProperty(String propKey, String defaultValue) {
		String strProp = getStringProperty(propKey);
		if (strProp == null) {
			strProp = defaultValue;
		}
		return strProp;
	}

	/**
	 * internal recursive method to get string properties (array)
	 * 
	 * @param curResult current result
	 * @param paramName property key prefix
	 * @param i         current indice
	 * @return array of property's values
	 */
//	private List<String> getSystemStringProperties(List<String> curResult, String paramName, int i) {
//		String paramIValue = getStringProperty(paramName + "." + String.valueOf(i), null);
//		if (paramIValue == null) {
//			return curResult;
//		}
//		curResult.add(paramIValue);
//		return getSystemStringProperties(curResult, paramName, i + 1);
//	}

	/**
	 * get the values from a property key prefix
	 * 
	 * @param paramName property key prefix
	 * @return string array of values
	 */
//	public String[] getSystemStringProperties(String paramName) {
//		List<String> stringProperties = getSystemStringProperties(new ArrayList<String>(), paramName, 0);
//		return stringProperties.toArray(new String[stringProperties.size()]);
//	}

	public Integer getIntegerProperty(String pPropertyKey) {
		return environment.getProperty(pPropertyKey, Integer.class);
	}

}
