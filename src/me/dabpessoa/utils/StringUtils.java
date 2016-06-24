package me.dabpessoa.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public abstract class StringUtils {
	
	public static boolean hasLength(CharSequence string) {
		return (isNotNull(string) && (string.length() > 0));
	}
	
	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}

	public static boolean hasText(CharSequence string) {
		if (!(hasLength(string))) {
			return false;
		}
		int strLen = string.length();
		for (int i = 0; i < strLen; ++i) {
			if (!(isWhitespace(string.charAt(i)))) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasText(String string) {
		return hasText((CharSequence)string);
	}
	
	public static boolean isEmpty(String string) {
		if(!hasLength(string)) {
			return true;
		}
		return string.isEmpty();
	}
	
	public static boolean isNull(String string) {
		return string == null;
	}
	
	public static boolean isNotNull(String string) {
		return isNotNull((CharSequence) string);
	}
	
	public static boolean isNotNull(CharSequence string) {
		return string != null;
	}
	
	public static boolean isNotEmpty(String string) {
		return !isEmpty(string);
	}

	public static boolean containsWhitespace(CharSequence str) {
		if (!(hasLength(str))) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; ++i) {
			if (isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsWhitespace(String str) {
		return containsWhitespace((CharSequence) str);
	}

	public static String trimWhitespace(String str) {
		if (!(hasLength(str))) {
			return str;
		}
		return str.trim();
	}

	public static String trimAllWhitespace(String str) {
		if (!(hasLength(str))) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		int index = 0;
		while (sb.length() > index) {
			if (isWhitespace(sb.charAt(index))) {
				sb.deleteCharAt(index);
			} else {
				++index;
			}
		}
		return sb.toString();
	}

	public static String trimLeadingWhitespace(String str) {
		if (!(hasLength(str))) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		while ((sb.length() > 0) && (isWhitespace(sb.charAt(0)))) {
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}

	public static String trimTrailingWhitespace(String str) {
		if (!(hasLength(str))) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		while ((sb.length() > 0)
				&& (isWhitespace(sb.charAt(sb.length() - 1)))) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	public static String trimLeadingCharacter(String str, char leadingCharacter) {
		if (!(hasLength(str))) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		while ((sb.length() > 0) && (sb.charAt(0) == leadingCharacter)) {
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}

	public static String trimTrailingCharacter(String str,
			char trailingCharacter) {
		if (!(hasLength(str))) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		while ((sb.length() > 0)
				&& (sb.charAt(sb.length() - 1) == trailingCharacter)) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	public static boolean startsWithIgnoreCase(String str, String prefix) {
		if ((str == null) || (prefix == null)) {
			return false;
		}
		if (str.startsWith(prefix)) {
			return true;
		}
		if (str.length() < prefix.length()) {
			return false;
		}
		String lcStr = str.substring(0, prefix.length()).toLowerCase();
		String lcPrefix = prefix.toLowerCase();
		return lcStr.equals(lcPrefix);
	}

	public static boolean endsWithIgnoreCase(String string, String suffix) {
		if ((string == null) || (suffix == null)) {
			return false;
		}
		if (string.endsWith(suffix)) {
			return true;
		}
		if (string.length() < suffix.length()) {
			return false;
		}

		String lcStr = string.substring(string.length() - suffix.length())
				.toLowerCase();
		String lcSuffix = suffix.toLowerCase();
		return lcStr.equals(lcSuffix);
	}

	public static boolean substringMatch(CharSequence string, int index, CharSequence substring) {
		for (int j = 0; j < substring.length(); ++j) {
			int i = index + j;
			if ((i >= string.length()) || (string.charAt(i) != substring.charAt(j))) {
				return false;
			}
		}
		return true;
	}

	public static int countOccurrencesOf(String string, String sub) {
		if ((string == null) || (sub == null) || (string.length() == 0)
				|| (sub.length() == 0)) {
			return 0;
		}
		int count = 0;
		int pos = 0;
		int idx;
		while ((idx = string.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}

	public static String replace(String inString, String oldPattern, String newPattern) {
		if ((!(hasLength(inString))) || (!(hasLength(oldPattern))) || (newPattern == null)) {
			return inString;
		}
		StringBuilder sb = new StringBuilder();
		int pos = 0;
		int index = inString.indexOf(oldPattern);

		int patLen = oldPattern.length();
		while (index >= 0) {
			sb.append(inString.substring(pos, index));
			sb.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sb.append(inString.substring(pos));

		return sb.toString();
	}

	public static String delete(String inString, String pattern) {
		return replace(inString, pattern, "");
	}

	public static String deleteAny(String inString, String charsToDelete) {
		if ((!(hasLength(inString))) || (!(hasLength(charsToDelete)))) {
			return inString;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < inString.length(); ++i) {
			char c = inString.charAt(i);
			if (charsToDelete.indexOf(c) == -1) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String wrap(String string, String symbol) {
		return ((string != null) ? (symbol + string + symbol) : null);
	}
	
	public static String quote(String string) {
		return wrap(string, "'");
	}
	
	public static String doubleQuote(String string) {
		return wrap(string, "\"");
	}

	public static Object quoteIfString(Object obj) {
		return ((obj instanceof String) ? quote((String) obj) : obj);
	}
	
	public static String capitalize(String string) {
		return changeAllFirstCharacters(string, true);
	}
	
	public static String uncapitalize(String string) {
		return changeAllFirstCharacters(string, false); 
	}
	
	public static String changeAllFirstCharacters(String string, boolean capitalize) {
		if (!hasLength(string)) {
			return string;
		}
		
		for (int i = 0 ; i < string.length() ; i++) {
			if ((i == 0 && !isWhitespace(string.charAt(i))) ||
					(i > 0 && isWhitespace(string.charAt(i-1)) && !isWhitespace(string.charAt(i)))) {
				if (capitalize) string = string.substring(0, i) + Character.toUpperCase(string.charAt(i)) + string.substring(i+1);
				else string = string.substring(0, i) + Character.toLowerCase(string.charAt(i)) + string.substring(i+1);
			}
		}
		return string;
	}
	
	public static String toUpperCase(String string) {
		if (!hasLength(string)) {
			return string;
		}
		return string.toUpperCase();
	}
	
	public static String toLowerCase(String string) {
		if(!hasLength(string)) {
			return string;
		}
		return string.toLowerCase();
	}
	
	public static boolean isWhitespace(Character c) {
		if (c != null) {
			return Character.isWhitespace(c);
		} 
		return false;
	}
	
	public static String capitalizeFirstCharacter(String string) {
		return changeFirstCharacterCase(string, true);
	}

	public static String uncapitalizeFirstCharacter(String string) {
		return changeFirstCharacterCase(string, false);
	}

	private static String changeFirstCharacterCase(String string, boolean capitalize) {
		if (!hasLength(string)) {
			return string;
		}
		StringBuilder sb = new StringBuilder(string.length());
		if (capitalize) {
			sb.append(Character.toUpperCase(string.charAt(0)));
		} else {
			sb.append(Character.toLowerCase(string.charAt(0)));
		}
		sb.append(string.substring(1));
		return sb.toString();
	}

	public static Locale parseLocaleString(String localeString) {
		String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
		String language = (parts.length > 0) ? parts[0] : "";
		String country = (parts.length > 1) ? parts[1] : "";
		String variant = "";
		if (parts.length >= 2) {
			int endIndexOfCountryCode = localeString.indexOf(country)
					+ country.length();

			variant = trimLeadingWhitespace(localeString
					.substring(endIndexOfCountryCode));
			if (variant.startsWith("_")) {
				variant = trimLeadingCharacter(variant, '_');
			}
		}
		return ((language.length() > 0) ? new Locale(language, country, variant)
				: null);
	}

	public static String toLanguageTag(Locale locale) {
		return locale.getLanguage()
				+ ((hasText(locale.getCountry())) ? "-" + locale.getCountry()
						: "");
	}

	public static String[] addStringToArray(String[] array, String str) {
		if ((array == null) || (array.length == 0)) {
			return new String[] { str };
		}
		String[] newArr = new String[array.length + 1];
		System.arraycopy(array, 0, newArr, 0, array.length);
		newArr[array.length] = str;
		return newArr;
	}

	public static String[] concatenateStringArrays(String[] array1, String[] array2) {
		if ((array1 == null) || (array1.length == 0)) {
			return array2;
		}
		if ((array2 == null) || (array2.length == 0)) {
			return array1;
		}
		String[] newArr = new String[array1.length + array2.length];
		System.arraycopy(array1, 0, newArr, 0, array1.length);
		System.arraycopy(array2, 0, newArr, array1.length, array2.length);
		return newArr;
	}

	public static String[] sortStringArray(String[] array) {
		if ((array == null) || (array.length == 0)) {
			return new String[0];
		}
		Arrays.sort(array);
		return array;
	}

	public static String[] toStringArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return ((String[]) collection.toArray(new String[collection.size()]));
	}

	public static String[] toStringArray(Enumeration<String> enumeration) {
		if (enumeration == null) {
			return null;
		}
		List<String> list = (List<String>) Collections.list(enumeration);
		return ((String[]) list.toArray(new String[list.size()]));
	}

	public static String[] trimArrayElements(String[] array) {
		if ((array == null) || (array.length == 0)) {
			return new String[0];
		}
		String[] result = new String[array.length];
		for (int i = 0; i < array.length; ++i) {
			String element = array[i];
			result[i] = ((element != null) ? element.trim() : null);
		}
		return result;
	}

	public static String[] removeDuplicateStrings(String[] array) {
		if ((array == null) || (array.length == 0)) {
			return array;
		}
		Set<String> set = new TreeSet<String>();
		for (String element : array) {
			set.add(element);
		}
		return toStringArray(set);
	}

	public static String[] splitFirstOccurrence(String toSplit, String delimiter) {
		if ((!(hasLength(toSplit))) || (!(hasLength(delimiter)))) {
			return null;
		}
		int offset = toSplit.indexOf(delimiter);
		if (offset < 0) {
			return null;
		}
		String beforeDelimiter = toSplit.substring(0, offset);
		String afterDelimiter = toSplit.substring(offset + delimiter.length());
		return new String[] { beforeDelimiter, afterDelimiter };
	}

	public static Properties splitArrayElementsIntoProperties(String[] array,
			String delimiter) {
		return splitArrayElementsIntoProperties(array, delimiter, null);
	}

	public static Properties splitArrayElementsIntoProperties(String[] array,
			String delimiter, String charsToDelete) {
		if ((array == null) || (array.length == 0)) {
			return null;
		}
		Properties result = new Properties();
		for (String element : array) {
			if (charsToDelete != null) {
				element = deleteAny(element, charsToDelete);
			}
			String[] splittedElement = splitFirstOccurrence(element, delimiter);
			if (splittedElement == null) {
				continue;
			}
			result.setProperty(splittedElement[0].trim(),
					splittedElement[1].trim());
		}
		return result;
	}

	public static String[] tokenizeToStringArray(String string, String delimiters) {
		return tokenizeToStringArray(string, delimiters, true, true);
	}

	public static String[] tokenizeToStringArray(String string, String delimiters,
			boolean trimTokens, boolean ignoreEmptyTokens) {
		if (string == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(string, delimiters);
		List<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if ((!(ignoreEmptyTokens)) || (token.length() > 0)) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}

	public static String[] delimitedListToStringArray(String string,
			String delimiter) {
		return delimitedListToStringArray(string, delimiter, null);
	}

	public static String[] delimitedListToStringArray(String string, String delimiter, String charsToDelete) {
		if (string == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] { string };
		}
		List<String> result = new ArrayList<String>();
		if ("".equals(delimiter)) {
			for (int i = 0; i < string.length(); ++i)
				result.add(deleteAny(string.substring(i, i + 1), charsToDelete));
		} else {
			int pos = 0;
			int delPos;
			while ((delPos = string.indexOf(delimiter, pos)) != -1) {
				result.add(deleteAny(string.substring(pos, delPos), charsToDelete));
				pos = delPos + delimiter.length();
			}
			if ((string.length() > 0) && (pos <= string.length())) {
				result.add(deleteAny(string.substring(pos), charsToDelete));
			}
		}
		return toStringArray(result);
	}

	public static String[] commaDelimitedListToStringArray(String string) {
		return delimitedListToStringArray(string, ",");
	}

	public static Set<String> commaDelimitedListToSet(String string) {
		Set<String> set = new TreeSet<String>();
		String[] tokens = commaDelimitedListToStringArray(string);
		for (String token : tokens) {
			set.add(token);
		}
		return set;
	}

	public static String collectionToDelimitedString(Collection<?> coll,
			String delim, String prefix, String suffix) {
		if (coll == null || coll.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	public static String collectionToDelimitedString(Collection<?> coll,
			String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
	}

	public static String collectionToCommaDelimitedString(Collection<?> coll) {
		return collectionToDelimitedString(coll, ",");
	}
	
	public static String reverse(String string) {
		if (!hasLength(string)) {
			return string;
		}
		return new StringBuffer(string).reverse().toString();
	}
	
}
