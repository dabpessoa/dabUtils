package me.dabpessoa.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ReflectionUtils {

	private ReflectionUtils() {
		// Do nothing. Constutor protegito para evitar instanciação de classe utilitária.
	}

	public static boolean isNotEmpty(Object object, String... fieldsNameExcluidos) {
		return !isEmpty(object, fieldsNameExcluidos);
	}

	public static boolean isEmpty(Object object, String... fieldsNameExcluidos) {
		return isEmpty(object, true, fieldsNameExcluidos);
	}

	public static boolean isEmpty(Object object, boolean jumpStaticFields, String... fieldsNameExcluidos) {
		if (object == null) {
			return true;
		}

		Field[] fields = findFieldsArray(object.getClass(), jumpStaticFields);
		for (Field field : fields) {
			boolean isExcludedField = fieldsNameExcluidos != null && Arrays.stream(fieldsNameExcluidos).anyMatch(n -> n != null && !n.isEmpty() && field.getName().equals(n));
			boolean isStaticField = Modifier.isStatic(field.getModifiers());

			if ((jumpStaticFields && isStaticField) || (isExcludedField)) {
				continue;
			}

			Object value = findFieldValue(object, field);

			boolean isEmptyValue = checkIfIsEmptyValue(value);
			if (!isEmptyValue) {
				return false;
			}
		}

		return true;
	}

	public static boolean checkIfIsEmptyValue(Object value) {
		if (value == null) {
			return true;
		}

		Class<?> clazz = value.getClass();
		boolean isDefaultObjects = clazz.isEnum()
				|| clazz.equals(BigDecimal.class)
				|| clazz.equals(LocalDate.class)
				|| clazz.equals(LocalDateTime.class)
				|| clazz.equals(Date.class)
				|| clazz.equals(BigInteger.class)
				|| Number.class.isAssignableFrom(clazz);
		if (isDefaultObjects) {
			return false;
		}

		if (Primitive.isPrimitiveOrWrapperWithValueDistinctFromDefault(value)) {
			return false;
		}

		// É um objeto instanciado. Verificar recursivamente se este objeto também é vazio.
		if ((value instanceof String) && !value.toString().isEmpty()) {
			return false;
		}

		if (value.getClass().isArray()) {
			return ((Object[]) value).length == 0;
		}

		if (value instanceof List) {
			return ((List<?>) value).isEmpty();
		}

		return isEmpty(value);
	}

	public static Field[] findFieldsArray(Class<?> klass) {
		return findFieldsArray(klass, true);
	}

	public static Field[] findFieldsArray(Class<?> klass, boolean ignoreStatics) {
		return findFieldsArray(klass, ignoreStatics, true);
	}


	public static Field[] findFieldsArray(Class<?> klass, boolean ignoreStatics, boolean includeSuperClassFields) {
		List<Field> fields = findFields(klass, ignoreStatics, includeSuperClassFields);
		return fields.toArray(new Field[] {});
	}

	public static List<Field> findFields(Class<?> klass) {
		return findFields(klass, true, true);
	}

	public static List<Field> findFields(Class<?> klass, boolean ignoreStatics) {
		return findFields(klass, ignoreStatics, true);
	}

	public static List<Field> findFields(Class<?> klass, boolean ignoreStatics, boolean includeSuperClassFields) {
		List<Field> fields = new ArrayList<>();
		fields.addAll(Arrays.asList(klass.getDeclaredFields()));
		if (includeSuperClassFields &&  klass.getSuperclass() != null) {
			fields.addAll(findFields(klass.getSuperclass(), ignoreStatics, includeSuperClassFields));
		}

		return fields.stream().filter(f -> {
			if (!ignoreStatics) {
				return true;
			}

			return !Modifier.isStatic(f.getModifiers());
		}).collect(Collectors.toList());
	}

	public static Field findFirstFieldByType(Object obj, Class<?> clazz) {
		if (obj == null || clazz == null) {
			return null;
		}

		List<Field> fields = findFields(obj.getClass(), true, false);
		for (Field field : fields) {
			if (field.getType() == clazz) {
				return field;
			}
		}

		return null;
	}

	public static <T> List<Field> findFieldsByAnnotations(Class<T> clazz, Class<?>... annotationClasses) {
		List<Field> fields = new ArrayList<>();

		if (annotationClasses == null) {
			Collections.addAll(fields, clazz.getDeclaredFields());
			return fields;
		}

		for (int i = 0; i < annotationClasses.length; ++i) {
			List<Field> foundedFields = findFieldsByAnnotation(clazz, annotationClasses[i]);

			Iterator<?> it = foundedFields.iterator();
			while (it.hasNext()) {
				Field findedField = (Field) it.next();
				if (!fields.contains(findedField)) {
					fields.add(findedField);
				}
			}
		}

		return fields;
	}

	public static Field findField(Class<?> clazz, String name) {
		return findField(clazz, name, null);
	}

	public static Field findField(Class<?> clazz, String name, Class<?> type) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.isTrue(name != null || type != null, "Either name or type of the field must be specified");

		Class<?> searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			for (Field field : fields) {
				if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	public static Method findMethod(Class<?> clazz, String name) {
		return findMethodByParamTypes(clazz, name);
	}

	public static Method findMethodByParamTypes(Class<?> clazz, String name, Class<?>... paramTypes) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(name, "Method name must not be null");
		Class<?> searchType = clazz;
		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
			for (Method method : methods) {
				if (name.equals(method.getName())
						&& (paramTypes == null || paramTypes.length == 0 || Arrays.equals(paramTypes, method.getParameterTypes()))) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	public static Object invokeMethod(Method method, Object target) {
		return invokeMethod(method, target, (Object[]) null);
	}

	public static Object invokeMethod(Method method, Object target, Object... args) {
		try {
			if (args != null) {
				return method.invoke(target, args);
			} else {
				return method.invoke(target);
			}

		} catch (Exception ex) {
			handleReflectionException(ex);
		}
		throw new IllegalStateException("Should never get here");
	}

	public static Object invokeMethodByName(String methodName, Object target) {
		return invokeMethodByName(methodName, target, new Object());
	}

	public static Object invokeMethodByName(String methodName, Object target, Object... args) {
		Method method = findMethod(target.getClass(), methodName);
		if (method == null) {
			return null;
		}

		return invokeMethod(method, target, args);
	}

	public static void handleReflectionException(Exception ex) {
		if (ex instanceof NoSuchMethodException) {
			throw new IllegalStateException("Method not found: " + ex.getMessage());
		}
		if (ex instanceof IllegalAccessException) {
			throw new IllegalStateException("Could not access method: " + ex.getMessage());
		}
		if (ex instanceof InvocationTargetException) {
			handleInvocationTargetException((InvocationTargetException) ex);
		}
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		throw new RuntimeException(ex);
	}

	public static void handleInvocationTargetException(InvocationTargetException ex) {
		rethrowRuntimeException(ex.getTargetException());
	}

	public static void rethrowRuntimeException(Throwable ex) {
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		if (ex instanceof Error) {
			throw (Error) ex;
		}
		throw new RuntimeException(ex);
	}

	public static Object findFirstFieldValueByAnnotation(Object entity, Class<?> annotationClass) {
		List<Object> values = findFieldsValueByAnnotation(entity, annotationClass);
		return !values.isEmpty() ? values.get(0) : null;
	}

	public static List<Object> findFieldsValueByAnnotation(Object entity, Class<?> annotationClass) {
		List<Object> values = new ArrayList<>();
		List<Field> fields = findFieldsByAnnotation(entity.getClass(), annotationClass);

		Iterator<?> it = fields.iterator();
		while (it.hasNext()) {
			Field f = (Field) it.next();
			values.add(findFieldValue(entity, f));
		}

		return values;
	}

	public static Object[] findFieldsValueArrayExcludingAnnotation(Object obj, Class<?> annotationClass) {
		List<Object> list =  findFieldsValueExcludingAnnotation(obj, annotationClass);
		return list.isEmpty() ? new Object[] {} : list.toArray(new Object[list.size()]);
	}

	public static List<Object> findFieldsValueExcludingAnnotation(Object entity, Class<?> annotationClass) {
		List<Object> values = new ArrayList<>();
		List<Field> fields = findFieldsExcludingAnnotation(entity.getClass(), annotationClass);

		Iterator<?> it = fields.iterator();
		while (it.hasNext()) {
			Field f = (Field) it.next();
			values.add(findFieldValue(entity, f));
		}

		return values;
	}

	public static Object[] findFieldsValueArray(Object obj) {
		return findFieldsValueArray(obj, true, true);
	}

	public static Object[] findFieldsValueArray(Object obj, boolean ignoreStatics) {
		return findFieldsValueArray(obj, ignoreStatics, true);
	}

	public static Object[] findFieldsValueArray(Object obj, boolean ignoreStatics, boolean includeSuperClassFields) {
		List<Object> fieldsValue = findFieldsValue(obj, ignoreStatics, includeSuperClassFields);
		return fieldsValue.isEmpty() ? new Object[] {} : fieldsValue.toArray(new Object[fieldsValue.size()]);
	}

	public static List<Object> findFieldsValue(Object obj) {
		return findFieldsValue(obj, true, true);
	}

	public static List<Object> findFieldsValue(Object obj, boolean ignoreStatics) {
		return findFieldsValue(obj, ignoreStatics, true);
	}

	public static List<Object> findFieldsValue(Object obj, boolean ignoreStatics, boolean includeSuperClassFields) {
		if (obj == null) {
			return Collections.emptyList();
		}

		List<Object> values = new ArrayList<>();
		Field[] fields = findFieldsArray(obj.getClass(), ignoreStatics, includeSuperClassFields);
		if (fields == null) {
			return Collections.emptyList();
		}

		for (int i = 0; i < fields.length; ++i) {
			Field f = fields[i];
			values.add(findFieldValue(obj, f));
		}

		return values;
	}

	public static Object findFieldValue(Object object, Field field) {
		if (field == null) {
			return null;
		}

		try {

			if (!Modifier.isPublic(field.getModifiers())) {
				Method getMethod = findGetMethodForField(field, object);
				if (getMethod != null) {
					return invokeMethod(getMethod, object);
				}
			}

			return field.get(object);
		} catch (IllegalAccessException e ) {
			// Access to private field... do nothing...
		} catch (SecurityException | IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	private static Method findGetMethodForField(Field field, Object target) {
		Method getMethod = null;

		String methodName = Optional.ofNullable(field.getName()).map(n -> n.substring(0, 1).toUpperCase() + (n.length() > 1 ? n.substring(1) : "")).map(n -> "get" + n).orElse(null);
		if (methodName != null && !methodName.trim().isEmpty()) {
			getMethod = findMethod(target.getClass(), methodName);
		}

		return getMethod;
	}

	private static Method findSetMethodForField(Field field, Object target) {
		Method getMethod = null;

		Class<?> setMethodType = field.getType();

		String methodName = Optional.ofNullable(field.getName()).map(n -> n.substring(0, 1).toUpperCase() + (n.length() > 1 ? n.substring(1) : "")).map(n -> "set" + n).orElse(null);
		if (methodName != null && !methodName.trim().isEmpty()) {
			getMethod = findMethodByParamTypes(target.getClass(), methodName, setMethodType);
		}

		return getMethod;
	}

	public static Object findFieldValue(Object entity, String fieldName) {
		Field field = findFieldByName(entity.getClass(), fieldName);
		if (field == null) {
			return null;
		}

		return findFieldValue(entity, field);
	}

	public static void setFieldValue(Object object, Field field, Object value) throws IllegalArgumentException {
		if (field == null) {
			return;
		}

		if (!Modifier.isPublic(field.getModifiers())) {
			Method setMethod = findSetMethodForField(field, object);
			if (setMethod != null) {
				value = verifyFieldValueTransformation(field, value);
				invokeMethod(setMethod, object, value);
			}
		} else {
			throw new RuntimeException("O campo " + field.getName() + " deve ter um método público do tipo SET.");
		}
	}

	private static Object verifyFieldValueTransformation(Field field, Object value) {
		Class<?> setMethodType = field.getType();
		Class<?> valueType = value.getClass();

		if (setMethodType != valueType) {
			try {
				value = basicFieldTypeTransform(value, field);
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException("O tipo do parâmetro do método SET é diferente do tipo do valor passado por parâmetro. setMethodType: " +
						setMethodType + ", valueType: " + valueType + ", fieldName: " + Optional.ofNullable(field).map(Field::getName).orElse("[não informado]"));
			}
		}
		return value;
	}

	public static <T> Object findAnnotationAttributeValue(Class<T> clazz, Class<?> annotationClass, Field field, String annotationAttribute) {
		Annotation[] annotations = field != null ? field.getDeclaredAnnotations() : null;
		if (annotations == null) {
			annotations = clazz != null ? clazz.getDeclaredAnnotations() : null;
		}

		if (annotations != null && annotations.length > 0) {
			for (int i = 0; i < annotations.length; ++i) {
				Annotation annotation = annotations[i];
				if (annotation.annotationType().getName().equalsIgnoreCase(annotationClass.getName())) {
					return invokeAnnotationAttributeMethod(annotation, annotationAttribute);
				}
			}
		}

		return null;
	}

	public static <T> Object findAnnotationAttributeValueInClass(Class<T> clazz, Class<?> annotationClass, String annotationAttribute) {
		return findAnnotationAttributeValue(clazz, annotationClass, null, annotationAttribute);
	}

	private static Object invokeAnnotationAttributeMethod(Annotation annotation, String annotationAttribute) {
		if (annotation == null) {
			return false;
		}

		Method[] methods = annotation.annotationType().getMethods();
		if (methods == null) {
			return false;
		}

		for (int i = 0; i < methods.length; ++i) {
			Method method = methods[i];
			if (method.getName().equalsIgnoreCase(annotationAttribute)) {
				try {
					return method.invoke(annotation);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					log.error(e.getMessage(), e);
				}
			}
		}

		return null;
	}

	public static Method findAnnotationMethod(Annotation annotation, String methodName) {
		if (annotation == null || methodName == null) {
			return null;
		}

		Method[] methods = annotation.annotationType().getMethods();
		if (methods == null) {
			return null;
		}

		for (int i = 0; i < methods.length; ++i) {
			Method method = methods[i];
			if (method.getName().equalsIgnoreCase(methodName)) {
				return method;
			}
		}

		return null;
	}

	public static <T> Field findFirstFieldByAnnotation(Class<T> clazz, Class<?> annotationClass) {
		List<Field> fields = findFieldsByAnnotation(clazz, annotationClass);
		return !fields.isEmpty() ? fields.get(0) : null;
	}

	public static <T> List<Field> findFieldsByAnnotation(Class<T> clazz, Class<?> annotationClass) {
		List<Field> fieldsFound = new ArrayList<>();

		List<Field> allFields = findFields(clazz, false, true);
		allFields.forEach(f -> {
			Annotation[] as = f.getDeclaredAnnotations();

			for (int i = 0; i < as.length; ++i) {
				Annotation a = as[i];
				if (a.annotationType().getName().equalsIgnoreCase(annotationClass.getName())) {
					fieldsFound.add(f);
				}
			}
		});

		return fieldsFound;
	}

	public static <T> List<Field> findFieldsExcludingAnnotation(Class<T> clazz, Class<?> annotationClass) {
		List<Field> allFields = findFields(clazz, false, true);
		Iterator<Field> it = allFields.iterator();

		while (it.hasNext()) {
			Field field = it.next();

			Annotation[] as = field.getDeclaredAnnotations();
			for (int i = 0; i < as.length; ++i) {
				Annotation a = as[i];
				if (a.annotationType().getName().equalsIgnoreCase(annotationClass.getName())) {
					it.remove();
				}
			}
		}

		return allFields;
	}

	public static <T> List<Field> findFieldsByAnnotationAttributeValue(Class<T> clazz, Class<?> annotationClass, String attributeName, String... attributeValues) {
		List<Field> fields = new ArrayList<>();
		Field[] fs = clazz.getDeclaredFields();

		for (int i = 0; i < fs.length; ++i) {
			Field field = fs[i];
			Annotation[] as = field.getDeclaredAnnotations();

			for (int j = 0; j < as.length; ++j) {
				Annotation a = as[j];
				if (a.annotationType().getName().equalsIgnoreCase(annotationClass.getName())
						&& (checkAnnotationAttributeValue(a, attributeName, attributeValues))) {
					fields.add(field);

				}
			}
		}

		return fields;
	}

	public static <T> Field findFirstFieldByAnnotationAttributeValue(Class<T> clazz, Class<?> annotationClass, String attributeName, String... attributeValues) {
		List<Field> fs = findFields(clazz);

		for (int i = 0; i < fs.size(); ++i) {
			Field field = fs.get(i);
			Annotation[] as = field.getDeclaredAnnotations();

			for (int j = 0; j < as.length; ++j) {
				Annotation a = as[j];
				if (a.annotationType().getName().equalsIgnoreCase(annotationClass.getName())
						&& (checkAnnotationAttributeValue(a, attributeName, attributeValues))) {
					return field;
				}
			}
		}

		return null;
	}

	private static boolean checkAnnotationAttributeValue(Annotation annotation, String attributeName, String... attributeValues) {
		if (annotation == null) {
			return false;
		}

		Method[] methods = annotation.annotationType().getMethods();
		if (methods == null) {
			return false;
		}

		List<String> attribValues = Arrays.asList(attributeValues != null ? attributeValues : new String[] {});

		for (int i = 0; i < methods.length; ++i) {
			Method m = methods[i];
			if (m.getName().equalsIgnoreCase(attributeName)) {
				try {
					Object value = m.invoke(annotation);
					boolean isTrue = (value.getClass().isArray() && Arrays.stream(((Object[]) value)).anyMatch(attribValues::contains)) || attribValues.contains(value);
					if (isTrue) {
						return true;
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					log.error(e.getMessage(), e);
				}
			}
		}

		return false;
	}

	public static <T> Field findFieldByName(Class<T> clazz, String fieldName) {
		if (clazz == null) {
			return null;
		}

		Class<?> superClass = clazz;

		do {
			Field[] fields = superClass.getDeclaredFields();

			for (int i = 0; i < fields.length; i++) {
				Field f = fields[i];
				if (f.getName().equalsIgnoreCase(fieldName)) {
					return f;
				}
			}

			superClass = superClass.getSuperclass();
		} while (superClass != null);

		return null;
	}

	public static <T> Field[] findFieldsByNames(Class<T> clazz, String[] fieldsName) {
		List<Field> fields = new ArrayList<>();

		for (int i = 0; i < fieldsName.length; i++) {
			String fieldName = fieldsName[i];
			Field field = findFieldByName(clazz, fieldName);
			if (field != null) {
				fields.add(field);
			}
		}

		if (!fields.isEmpty()) {
			return fields.toArray(new Field[fields.size()]);
		} else {
			return new Field[] {};
		}
	}

	public static <T> List<String> findFieldsName(Class<T> clazz) {
		return findFieldsName(clazz, false);
	}

	public static <T> List<String> findFieldsName(Class<T> clazz, boolean ignoreStatics) {
		Field[] fields = findFieldsArray(clazz, ignoreStatics);
		List<String> nomes = new ArrayList<>();

		for (int i = 0; i < fields.length; ++i) {
			Field field = fields[i];
			nomes.add(field.getName());
		}

		return nomes;
	}

	public static <T, A> Annotation findAnnotation(Class<T> clazz, Class<A> annotation) {
		Field[] fs = clazz.getDeclaredFields();
		Field[] var3 = fs;
		int var4 = fs.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			Field f = var3[var5];
			Annotation[] as = f.getDeclaredAnnotations();
			Annotation[] var8 = as;
			int var9 = as.length;

			for (int var10 = 0; var10 < var9; ++var10) {
				Annotation a = var8[var10];
				if (a.annotationType().getName().equalsIgnoreCase(annotation.getClass().getName())) {
					return a;
				}
			}
		}

		return null;
	}

	public static boolean existsFieldAnnotation(Field field, Class<?> annotationClass) {
		Annotation annotation = findFieldAnnotation(field, annotationClass);
		return annotation != null;
	}

	public static Annotation findFieldAnnotation(Field field, Class<?> annotationClass) {
		Annotation[] annotations = field.getDeclaredAnnotations();

		for (int i = 0; i < annotations.length; i++) {
			Annotation annotation = annotations[i];
			if (annotation.annotationType().getName().equalsIgnoreCase(annotationClass.getName())) {
				return annotation;
			}
		}

		return null;
	}

	public static Annotation findMethodAnnotation(Method method, Class<?> annotationClass) {
		Annotation[] annotations = method.getDeclaredAnnotations();

		for (int i = 0; i < annotations.length; i++) {
			Annotation annotation = annotations[i];
			if (annotation.annotationType().getName().equalsIgnoreCase(annotationClass.getName())) {
				return annotation;
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T findMethodAnnotationAttributeValue(Method method, Class<?> annotationClass, String attributeName) {
		Annotation annotation = findMethodAnnotation(method, annotationClass);
		return (T) invokeAnnotationAttributeMethod(annotation, attributeName);
	}

	public static <T> Object findClassAnnotationAttributeValue(Class<T> clazz, Class<?> annotationClass, String attribute) {
		Annotation[] as = clazz.getDeclaredAnnotations();
		int annotationsSize = as.length;

		for (int i = 0; i < annotationsSize; ++i) {
			Annotation a = as[i];
			if (a.annotationType().getName().equalsIgnoreCase(annotationClass.getName())) {
				Method[] ms = a.annotationType().getMethods();
				int methodsSize = ms.length;

				for (int j = 0; j < methodsSize; ++j) {
					Method m = ms[j];
					if (m.getName().equalsIgnoreCase(attribute)) {
						try {
							return m.invoke(a);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							log.error(e.getMessage(), e);
						}
					}
				}
			}
		}

		return null;
	}

	public static Object executeMethod(Object service, String methodName, Object... params) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> clazz = service.getClass();
		Class<?>[] classes = new Class[params.length];

		for (int i = 0; i < classes.length; ++i) {
			classes[i] = params[i].getClass();
		}

		Method method = clazz.getDeclaredMethod(methodName, classes);
		return method.invoke(service, params);
	}

	public static List<String> getValidFields(List<String> fields, Class<?> clazz) {
		if (fields == null) {
			return Collections.emptyList();
		}

		Field[] foundFields = ReflectionUtils.findFieldsByNames(clazz, fields.toArray(new String[fields.size()]));
		if (foundFields == null) {
			return Collections.emptyList();
		}

		return Arrays.stream(foundFields).map(Field::getName).collect(Collectors.toList());
	}

	public static <T> T instantiateClass(Class<T> clazz) {
		T obj;
		try {
			obj = clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
			throw new RuntimeException("Erro ao instanciar classe: " + clazz);
		}
		return obj;
	}

	public static Object basicFieldTypeTransform(Object value, Field field) {
		Type fieldType = field.getType();
		String fieldName = field.getName();

		if (value != null) {
			try {
				if (value.getClass().isArray() || value instanceof Collection<?> || value instanceof Map<?, ?>) {
					return value;
				}

				value = convertValueByType(value, fieldType);

			} catch (NumberFormatException e) {
				throw new RuntimeException("Não foi possível setar o valor: " +
						value + ", no campo: " + fieldName + ", do tipo: " + fieldType + ", da classe: " + field.getDeclaringClass() + ". Erro de conversão de tipo.", e);
			}
		}

		return value;
	}

	public static List<String> pesquisaNomesDeCamposNaoExistentesNaClasse(List<String> camposStringList, Class<?> clazz) {
		List<String> camposValidosList = getValidFields(camposStringList, clazz);

		List<String> differences = camposStringList != null ? new ArrayList<>(camposStringList) : new ArrayList<>();
		camposValidosList.forEach(differences::remove);

		return differences;
	}

	public static Object convertValueByType(Object value, Type fieldType) {
		String myValue = value.toString();
		myValue = myValue.trim();

		if (Integer.class.equals(fieldType)) {
			value = Double.valueOf(myValue).intValue();
		} else if (String.class.equals(fieldType)) {
			value = myValue;
		} else if (BigDecimal.class.equals(fieldType)) {
			value = new BigDecimal(myValue);
		} else if (Long.class.equals(fieldType)) {
			value = Long.parseLong(myValue);
		} else if (Byte.class.equals(fieldType)) {
			value = Byte.parseByte(myValue);
		} else if (Short.class.equals(fieldType)) {
			value = Short.parseShort(myValue);
		} else if (Double.class.equals(fieldType)) {
			value = Double.parseDouble(myValue);
		} else if (Float.class.equals(fieldType)) {
			value = Float.parseFloat(myValue);
		} else if (BigInteger.class.equals(fieldType)) {
			value = new BigInteger(myValue);
		} else if (LocalDateTime.class.equals(fieldType) && (value instanceof Timestamp)) {
			value = Timestamp.valueOf(myValue).toLocalDateTime();
		} else if (LocalDate.class.equals(fieldType) && (value instanceof Timestamp)) {
			value = Timestamp.valueOf(myValue).toLocalDateTime().toLocalDate();
		} else if (LocalTime.class.equals(fieldType) && (value instanceof Timestamp)) {
			value = Timestamp.valueOf(myValue).toLocalDateTime().toLocalTime();
		}

		return value;
	}

	public static Map<String, String> toMap(Object obj) {
		if (obj == null) {
			return new HashMap<>();
		}

		Map<String, String> map = new HashMap<>();

		Field[] fields = findFieldsArray(obj.getClass(), true, true);
		if (fields != null) {
			for (Field field : fields) {
				String fieldName = field.getName();
				Object fieldValue = ReflectionUtils.findFieldValue(obj, field);

				if (!ReflectionUtils.checkIfIsEmptyValue(fieldValue)) {
					map.put(fieldName, fieldValue.toString());
				}
			}
		}

		return map;
	}

	public static void validaCamposExistentesDaClasse(List<String> camposValidarList, Class<?> clazz) {
		validaCamposExistentesDaClasse(camposValidarList, null, clazz, null);
	}

	public static void validaCamposExistentesDaClasse(List<String> camposValidarList, Class<?> clazz, String errorMessage) {
		validaCamposExistentesDaClasse(camposValidarList, null, clazz, errorMessage);
	}

	public static void validaCamposExistentesDaClasse(List<String> camposValidarList, List<String> camposIgnorarList, Class<?> clazz) {
		validaCamposExistentesDaClasse(camposValidarList, camposIgnorarList, clazz, null);
	}

	public static void validaCamposExistentesDaClasse(List<String> camposValidarList, List<String> camposIgnorarList, Class<?> clazz, String errorMessage) {
		List<String> camposInvalidos = ReflectionUtils.pesquisaNomesDeCamposNaoExistentesNaClasse(camposValidarList, clazz);

		if (!CollectionUtils.isEmpty(camposIgnorarList)) {
			camposIgnorarList.forEach(camposInvalidos::remove);
		}

		if (!CollectionUtils.isEmpty(camposInvalidos)) {
			if (!StringUtils.hasText(errorMessage)) {
				errorMessage = "Os seguintes campos passados por parâmetro são inválidos: %s";
			}

			throw new RuntimeException(String.format(errorMessage, camposInvalidos.stream().collect(Collectors.joining(",", "[", "]"))));
		}
	}

	public static void copySameFieldNames(Object objOrigem, Object objDestino) {
		if (objOrigem == null || objDestino == null) {
			return;
		}

		List<String> nomeCamposObjetoOrigemList = ReflectionUtils.findFieldsName(objOrigem.getClass(), true);
		List<String> nomeCamposValidosObjetoDestinoList = getValidFields(nomeCamposObjetoOrigemList, objDestino.getClass());

		for (String fieldName : nomeCamposValidosObjetoDestinoList) {
			Object origemFieldValue = findFieldValue(objOrigem, fieldName);

			Field field = findField(objDestino.getClass(), fieldName);
			if (field != null) {
				origemFieldValue = basicFieldTypeTransform(origemFieldValue, field);
				if (origemFieldValue != null) {
					setFieldValue(objDestino, field, origemFieldValue);
				}
			}
		}
	}

}
