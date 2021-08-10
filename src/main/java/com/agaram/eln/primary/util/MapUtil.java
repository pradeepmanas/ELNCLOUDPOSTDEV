package com.agaram.eln.primary.util;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * This component class holds method to access deeply nested map object
 * @author ATE128
 *
 */
@Component
public class MapUtil {
	
	/**
	 * This method is used to access deeply nested map object.
	 * @param map [Map] deeply nested map object holding data
	 * @param keys [List] list of strings in the order to access
	 * @param <T> generic type of data
	 * @return retrieved data from deeply nested object
	 */
	@SuppressWarnings("unchecked")
	public <T> T getNestedValue(Map<?, ?> map, List<String> keys) {
        Object value = map;
        for (String key : keys) {
            value = ((Map<?, ?>) value).get(key);
        }
        return (T) value;
    }
}
