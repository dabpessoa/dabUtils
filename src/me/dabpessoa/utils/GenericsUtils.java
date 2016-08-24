package me.dabpessoa.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericsUtils {

	public static Class<?> discoverClass(Class<?> clazz, int parameterPosition) {
		final Type type = clazz.getGenericSuperclass();
		ParameterizedType parameterizedType;
		try {
			parameterizedType = (ParameterizedType) type;
		} catch (ClassCastException e) {
			parameterizedType = (ParameterizedType) ((Class<?>) type)
					.getGenericSuperclass();
		}
		return (Class<?>) parameterizedType.getActualTypeArguments()[parameterPosition];
	}

}
