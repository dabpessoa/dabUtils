package me.dabpessoa.utils;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

	@SuppressWarnings("unchecked")
	public static <T,K> Map<T,K> create(Object... values) {
		Map<T,K> map = new HashMap<T, K>();
		int count = 0;
		while (count < values.length) {
			Object key = values[count++];
			Object value = values[count++];
			map.put((T)key, (K)value);
		}
		return map;
	}
	
	public static <T,K> Map<T,K> create(T[] keys, K[]values) {
		Map<T,K> map = new HashMap<T, K>();
		for(int i = 0 ; i < keys.length ; i++) {
			map.put(keys[i], values[i]);
		}
		return map;
	}
	
}
