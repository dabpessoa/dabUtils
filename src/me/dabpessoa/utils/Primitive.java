package me.dabpessoa.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by diego.pessoa on 21/02/2017.
 */
public final class Primitive {

	private static class Type {
		Class<?> primitiveType;
		Class<?> wrapperType;
		Object defaultValue;
		public Type(Class<?> primitiveType, Class<?> wrapperType, Object defaultValue) {
			this.primitiveType = primitiveType; 
			this.wrapperType = wrapperType; 
			this.defaultValue = defaultValue;
		}
	}
	
	private static final List<Type> PRIMITIVE_DEFAULT_VALUES = new ArrayList<Type>(8);
	static {
		add(new Type(Boolean.TYPE, Boolean.class, Boolean.valueOf(false)));
		add(new Type(Character.TYPE, Character.class, Character.valueOf('\0')));
		add(new Type(Byte.TYPE, Byte.class, Byte.valueOf((byte) 0)));
		add(new Type(Short.TYPE, Short.class, Short.valueOf((short) 0)));
		add(new Type(Integer.TYPE, Integer.class, Integer.valueOf(0)));
		add(new Type(Long.TYPE, Long.class, Long.valueOf(0L)));
		add(new Type(Float.TYPE, Float.class, Float.valueOf(0.0F)));
		add(new Type(Double.TYPE, Double.class, Double.valueOf(0.0D)));
	}
	
	private static <T> void add(Type type) {
		PRIMITIVE_DEFAULT_VALUES.add(type);
	}

	@SuppressWarnings("unchecked")
	public static <T> T defaultValue(Class<T> clazz) {
		Type type = findType(clazz);
		return (T) type.defaultValue;
	}
	
	public static boolean isWrapper(Class<?> clazz) {
		Type type = findType(clazz);
		if (type == null) return false;
		if (type.wrapperType == clazz) return true;
		return false;
	}
	
	public static boolean isPrimitive(Class<?> clazz) {
		return clazz.isPrimitive();
	}
	
	public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
		return isPrimitive(clazz) || isWrapper(clazz); 
	}
	
	public static boolean isPrimitiveOrWrapper(Object o) {
		if (o == null) return false;
		return isPrimitiveOrWrapper(o.getClass());
	}
	
	private static Type findType(Class<?> clazz) {
		Iterator<Type> it = PRIMITIVE_DEFAULT_VALUES.iterator();
		while (it.hasNext()) {
			Type type = it.next();
			if (type.wrapperType.equals(clazz) || type.primitiveType.equals(clazz)) return type;
		} return null;
	}
	
}