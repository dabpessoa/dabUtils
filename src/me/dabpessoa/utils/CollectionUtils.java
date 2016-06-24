package me.dabpessoa.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public abstract class CollectionUtils {
	
	public static boolean isEmpty(Collection<?> collection) {
		return ((collection == null) || (collection.isEmpty()));
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return ((map == null) || (map.isEmpty()));
	}
	
	public static <T> boolean isEmpty(T[] array) {
		return ((array == null) || (array.length == 0));
	}
	
	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}
	
	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}
	
	public static <T> boolean isNotEmpty(T[] array) {
		return !isEmpty(array);
	}
	
	public static <T> List<T> arrayToList(T[] array) {
		List<T> list = new ArrayList<T>();
		if (array != null) {
			for (T element : array) {
				list.add(element);
			}
			return list;
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] listToArray(final List<T> list, Class<T> clazz) throws NoSuchMethodException, SecurityException {
		return list.toArray((T[])Array.newInstance(clazz, list.size()));
	}

	@SuppressWarnings("unchecked")
	public static <E> void mergeArrayIntoCollection(Object[] array, Collection<E> collection) {
		if (collection == null) {
			throw new IllegalArgumentException("Collection must not be null");
		}
		for (Object elem : array)
			collection.add((E)elem);
	}

	@SuppressWarnings("unchecked")
	public static <V> void mergePropertiesIntoMap(Properties props,
			Map<String, V> map) {
		if (map == null)
			throw new IllegalArgumentException("Map must not be null");
		Enumeration<String> en;
		if (props != null)
			for (en = (Enumeration<String>) props.propertyNames(); en.hasMoreElements();) {
				
				String key = en.nextElement();
				V value = (V) props.getProperty(key);
				if (value == null) {
					value = (V) props.get(key);
				}
				map.put(key, value);
			}
	}

	public static boolean contains(Iterator<?> iterator, Object element) {
		while ((iterator != null) && (iterator.hasNext())) {
			Object candidate = iterator.next();
			
			if (candidate == element) {
				return true;
			}
			if ((candidate == null) || (element == null)) {
				return false;
			}
			if (candidate.equals(element)) {
				return true;
			}
			
		}
		return false;
	}

	public static boolean contains(Enumeration<?> enumeration, Object element) {
		while ((enumeration != null) && (enumeration.hasMoreElements())) {
			Object candidate = enumeration.nextElement();
			if (candidate == element) {
				return true;
			}
			if ((candidate == null) || (element == null)) {
				return false;
			}
			if (candidate.equals(element)) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsInstance(Collection<?> collection,
			Object element) {
		Iterator<?> localIterator;
		if (collection != null) {
			for (localIterator = collection.iterator(); localIterator.hasNext();) {
				Object candidate = localIterator.next();
				if (candidate == element) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean containsAny(Collection<?> source,
			Collection<?> candidates) {
		if ((isEmpty(source)) || (isEmpty(candidates))) {
			return false;
		}
		for (Iterator<?> localIterator = candidates.iterator(); localIterator
				.hasNext();) {
			Object candidate = localIterator.next();
			if (source.contains(candidate)) {
				return true;
			}
		}
		return false;
	}

	public static boolean equals(Object[] array1, Object[] array2) {
		if (array1 == array2) {
			return true;
		}
		if ((array1 == null) || (array2 == null)) {
			return false;
		}
		if (array1.equals(array2)) {
			return true;
		}
		return Arrays.equals((Object[]) array1, (Object[]) array2);
	}
	
	public static <E> E findFirstMatch(Collection<?> source,
			Collection<E> candidates) {
		if ((isEmpty(source)) || (isEmpty(candidates))) {
			return null;
		}
		for (Iterator<E> localIterator = candidates.iterator(); localIterator.hasNext();) {
			E candidate = localIterator.next();
			if (source.contains(candidate)) {
				return candidate;
			}
		}
		return null;
	}

	public static <T> T findValueOfType(Collection<T> collection, Class<?> type) {
		if (isEmpty(collection)) {
			return null;
		}
		T value = null;
		for (Iterator<T> localIterator = collection.iterator(); localIterator
				.hasNext();) {
			T element = localIterator.next();
			if ((type == null) || (type.isInstance(element))) {
				if (value != null) {
					return null;
				}
				value = element;
			}
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public static <T> T findValueOfType(Collection<T> collection,
			Class<?>[] types) {
		if ((isEmpty(collection)) || (isEmpty(types))) {
			return null;
		}
		for (Class<?> type : types) {
			Object value = findValueOfType(collection, type);
			if (value != null) {
				return (T) value;
			}
		}
		return null;
	}

	public static boolean hasUniqueObject(Collection<?> collection) {
		if (isEmpty(collection)) {
			return false;
		}
		boolean hasCandidate = false;
		Object candidate = null;
		for (Iterator<?> localIterator = collection.iterator(); localIterator
				.hasNext();) {
			Object elem = localIterator.next();
			if (!(hasCandidate)) {
				hasCandidate = true;
				candidate = elem;
			} else if (candidate != elem) {
				return false;
			}
		}
		return true;
	}

	public static Class<?> findCommonElementType(Collection<?> collection) {
		if (isEmpty(collection)) {
			return null;
		}
		Class<?> candidate = null;
		for (Iterator<?> localIterator = collection.iterator(); localIterator
				.hasNext();) {
			Object val = localIterator.next();
			if (val != null) {
				if (candidate == null) {
					candidate = val.getClass();
				} else if (candidate != val.getClass()) {
					return null;
				}
			}
		}
		return candidate;
	}

	public static <A, E extends A> A[] toArray(Enumeration<E> enumeration,
			A[] array) {
		ArrayList<E> elements = new ArrayList<E>();
		while (enumeration.hasMoreElements()) {
			elements.add(enumeration.nextElement());
		}
		return elements.toArray(array);
	}

	public static <E> Iterator<E> toIterator(Enumeration<E> enumeration) {
		return new EnumerationIterator<E>(enumeration);
	}

	private static class EnumerationIterator<E> implements Iterator<E> {
		private Enumeration<E> enumeration;

		public EnumerationIterator(Enumeration<E> enumeration) {
			this.enumeration = enumeration;
		}

		public boolean hasNext() {
			return this.enumeration.hasMoreElements();
		}

		public E next() {
			return this.enumeration.nextElement();
		}

		public void remove() throws UnsupportedOperationException {
			throw new UnsupportedOperationException("Not supported");
		}
	}
	
}
