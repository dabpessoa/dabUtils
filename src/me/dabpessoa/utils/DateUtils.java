package me.dabpessoa.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

	private static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";
	private static final String DEFAULT_DATETIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
	
	public static Integer  getAnoCorrente(){
		GregorianCalendar DataAtual = new GregorianCalendar();
		return DataAtual.get(GregorianCalendar.YEAR);
	}
	
	public static Date dataAtual() {
		return new Date();
	}
	
	public static Date addDaysToCurrent(int quantidadeDias){
		GregorianCalendar dataAtual = new GregorianCalendar();
		dataAtual.add( GregorianCalendar.DATE , quantidadeDias);		
		return dataAtual.getTime();
	}
	
	public static boolean after(Date d1, Date d2, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			d1 = sdf.parse(sdf.format(d1));
			d2 = sdf.parse(sdf.format(d2));
			return d1.compareTo(d2) > 0;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean after(Date d1, Date d2) {
		return after(d1, d2, "dd/MM/yyyy");
	}
	
	public static boolean before(Date d1, Date d2, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			d1 = sdf.parse(sdf.format(d1));
			d2 = sdf.parse(sdf.format(d2));
			return d1.compareTo(d2) < 0;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean before(Date d1, Date d2) {
		return before(d1, d2, "dd/MM/yyyy");
	}
	
	public static boolean beforeOrEquals(Date d1, Date d2) {
		return before(d1, d2) || equals(d1, d2);
	}
	
	public static boolean afterOrEquals(Date d1, Date d2) {
		return after(d1, d2) || equals(d1, d2);
	}
	
	public static boolean equals(Date d1, Date d2, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			d1 = sdf.parse(sdf.format(d1));
			d2 = sdf.parse(sdf.format(d2));
			return d1.compareTo(d2) == 0;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static Date add(Date date, int field, int amount){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(field, amount);
		return c.getTime();
	}
	
	public static boolean equals(Date d1, Date d2) {
		return equals(d1, d2, "dd/MM/yyyy");
	}
	
	public static boolean between(Date data, Date dataInicio, Date dataFim){
		if(!before(data, dataInicio) && !after(data,dataFim)){
			return true;
		}
		return false;
	}
	
	public static String format(Date date) {
		return format(date, DEFAULT_DATE_PATTERN);
	}
	
	public static String formatDateTime(Date date) {
		return format(date, DEFAULT_DATETIME_PATTERN);
	}
	
	public static String format(Date date, String pattern){
		return new SimpleDateFormat(pattern).format(date);
	}
	
	public static Calendar parseDateToCalendar(Date date){
		if(date==null)
			return  null;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
	
	public static Date parse(String date, String pattern){
		try {
			return new SimpleDateFormat(pattern).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Date parse(String date) {
		return parse(date, DEFAULT_DATE_PATTERN);
	}
	
}