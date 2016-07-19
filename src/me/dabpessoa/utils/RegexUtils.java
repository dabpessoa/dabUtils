package me.dabpessoa.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author diego.pessoa
 *
 */
public class RegexUtils {
	
	public static List<String> findMatches(String regex, String value) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);

		List<String> matchers = new ArrayList<String>();
		while (matcher.find()) {
			matchers.add(matcher.group());
		}
		return matchers;
	}
	
	public static String removeMatches(String regex, String originalValue) {
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(originalValue);
		
		String value = originalValue;
		
		if (matcher.find()) {
			value = matcher.replaceAll("");
		}
		
		return value;
		
	}
	
	public static MatchResult createMatchResult(String regex, String originalValue) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(originalValue);
		return matcher.toMatchResult();
	}
	
	public static String replaceMatches(String regex, String originalValue, String... replacements) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(originalValue);
		
		String value = originalValue;

		int count = 0;
		while (matcher.find()) {
			if (count < replacements.length) {
				String replacement = replacements[count++];
				value = matcher.replaceFirst(replacement);
				matcher = pattern.matcher(value);
			}
		}
		
		return value;
	}
	
	public static String findGroup(String regex, String value, int groupNumber) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		
		if (matcher.find() && (groupNumber <= matcher.groupCount())) {
			return matcher.group(groupNumber);
		}
		
		return null;
	}
	
	public static String[] split(String regex, String value) {
		if (value == null) return null;
		return value.split(regex);
	}
	
	public static int getGroupCount(String regex, String value) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		return matcher.groupCount();
	}
	
	public static void main(String[] args) {
		List<String> matchers = RegexUtils.findMatches("(#\\{)((?is).*?)(\\})", "This order #{was} placed for Q#{T3 }000! OK?");
		
		for (String value : matchers) {
			System.out.println("Found: "+value);
		}
	}
	
}
