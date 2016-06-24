package me.dabpessoa.utils;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * Classe que possui m�todos auxiliares para localiza��o de valores via reflection em uma 
 * classe qualquer.
 * 
 * @author diego.pessoa [dabpessoa@gmail.com]
 * @since 17.06.2013
 *
 */
public class ReflectionUtils {
	
	public ReflectionUtils() {}

	public static List<Object> findFieldsValueByAnnotation(Object entity, Class<?> annotationClass) {
		List<Object> values = new ArrayList<Object>();
		List<Field> fields = findFieldsByAnnotation(entity.getClass(), annotationClass);
		if (fields != null) {
			for (Field f : fields) {
				values.add(findFieldValue(entity, f));
			}
			return values;
		} return null;
	}
	
	public static List<Object> findFieldsValue(Object entity) {
		List<Object> values = new ArrayList<Object>();
		Field fields[] = entity.getClass().getDeclaredFields();
		if (fields != null) {
			for (Field f : fields) {
				values.add(findFieldValue(entity, f));
			}
			return values;
		} return null;
	}
	
	public static Object findFieldValue(Object entity, Field field) {
		try {
			field.setAccessible(true);
			return field.get(entity);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setFieldValue(Object entity, Field field, Object value) throws IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);
		field.set(entity, value);
	}
	
	public static <T> Object findAnnotationAttributeValue (Class<T> clazz, Class<?> annotationClass, Field field, String annotationAttribute) {
		
		Annotation[] as = null;
		if (field != null) {
			as = field.getDeclaredAnnotations();
		} else if (clazz != null) {
			as = clazz.getDeclaredAnnotations();
		}
		
		if (as != null && as.length > 0) {
			for (Annotation a : as) {
				if (a.annotationType().getName().equalsIgnoreCase(annotationClass.getName())) {
					Method ms[] = a.annotationType().getMethods();
					for (Method m : ms) {
						if (m.getName().equalsIgnoreCase(annotationAttribute)) {
							try {
								return m.invoke(a);
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		return null;
		
	}
	
	public static <T> List<Field> findFieldsByAnnotation(Class<T> clazz, Class<?> annotationClass) {
		List<Field> fields = new ArrayList<Field>();
		Field[] fs = clazz.getDeclaredFields();
		for (Field f : fs) {
			Annotation[] as = f.getDeclaredAnnotations();
			for (Annotation a : as) {
				if (a.annotationType().getName().equalsIgnoreCase(annotationClass.getName())) {
					fields.add(f);
				}
			}
		}
		if (fields.isEmpty()) return null;
		return fields;
	}
	
	public static <T> List<Field> findFieldsByAnnotationAttributeValue(Class<T> clazz, Class<?> annotationClass, String attributeName, String... attributeValues) {
		List<Field> fields = new ArrayList<Field>();
		Field[] fs = clazz.getDeclaredFields();
		List<String> attribValues = Arrays.asList(attributeValues);
		for (Field f : fs) {
			Annotation[] as = f.getDeclaredAnnotations();
			for (Annotation a : as) {
				if (a.annotationType().getName().equalsIgnoreCase(annotationClass.getName())) {
					Method ms[] = a.annotationType().getMethods();
					for (Method m : ms) {
						if (m.getName().equalsIgnoreCase(attributeName)) {
							try {
								Object value = m.invoke(a);
								if (attribValues.contains(value)) {
									fields.add(f);
								}
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		if (fields.isEmpty()) return null;
		return fields;
	}
	
	public static <T> Field findFieldByName(Class<T> clazz, String fieldName) {
		Field[] fs = clazz.getDeclaredFields();
		for (Field f : fs) {
			if (f.getName().equalsIgnoreCase(fieldName)) {
				return f;
			}
			
		}
		return null;
	}
	
	public static <T> Field[] findFieldsByNames(Class<T> clazz, String[] fieldsName) {
		List<Field> fields = new ArrayList<Field>();
		for (String fieldName : fieldsName) {
			Field field = findFieldByName(clazz, fieldName);
			if (field != null) fields.add(field);
		}
		if (fields == null || fields.isEmpty()) return null;
		return fields.toArray(new Field[fields.size()]);
	}
	
	public static <T> List<String> findFieldsName(Class<T> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		List<String> nomes = new ArrayList<String>();
		for (Field field : fields) {
			nomes.add(field.getName());
		} return nomes;
	}
	
	public static <T, A> Annotation findAnnotation(Class<T> clazz, Class<A> annotation) {
		Field[] fs = clazz.getDeclaredFields();
		for (Field f : fs) {
			Annotation[] as = f.getDeclaredAnnotations();
			for (Annotation a : as) {
				if (a.annotationType().getName().equalsIgnoreCase(annotation.getClass().getName())) {
					return a;
				}
			}
		}
		return null;
	}
	
	@Target({ java.lang.annotation.ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Table {
		public abstract String name();

		public abstract String catalog();

		public abstract String schema();

		public abstract UniqueConstraint[] uniqueConstraints();
	}
	
	@Target({})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface UniqueConstraint {
		public abstract String name();

		public abstract String[] columnNames();
	}	
	
	public static <T> String findTableName(Class<T> clazz) {
		Object obj = findClassAnnotationAttributeValue(clazz, Table.class, "name");
		if (obj != null) return obj.toString();
		return null;
	}
	
	public static<T> String findSchemaName(Class<T> clazz) {
		Object obj = findClassAnnotationAttributeValue(clazz, Table.class, "schema");
		if (obj != null) return obj.toString();
		return null;
	}
	
	public static <T> Object findClassAnnotationAttributeValue(Class<T> clazz, Class<?> annotationClass, String attribute) {
		Annotation[] as = clazz.getDeclaredAnnotations();
		for (Annotation a : as) {
			if (a.annotationType().getName().equalsIgnoreCase(annotationClass.getName())) {
				Method ms[] = a.annotationType().getMethods();
				for (Method m : ms) {
					if (m.getName().equalsIgnoreCase(attribute)) {
						try {
							return m.invoke(a);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}
	
	public static <T> String findSchemaTableName(Class<T> clazz) {
		String tableName = findTableName(clazz);
		String schemaName = findSchemaName(clazz);
		if (tableName == null) tableName = Character.toLowerCase(clazz.getSimpleName().charAt(0)) + clazz.getSimpleName().substring(1);
		if (schemaName == null || schemaName.isEmpty()) return tableName;
		return schemaName + "." + tableName;
	}
	
	public static Object executeMethod(Object service, String methodName, Object... params) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> clazz = service.getClass();
		Class<?>[] classes = new Class<?>[params.length];
		for (int i = 0 ; i < classes.length ; i++) {
			classes[i] = params[i].getClass();
		}

		Method method = clazz.getDeclaredMethod(methodName, classes);
		Object result = method.invoke(service, params);
		return result;
	}
	
}
