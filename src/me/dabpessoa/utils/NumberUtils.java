package me.dabpessoa.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * 
 * @author diego.pessoa
 *
 */
public class NumberUtils {

	public static String toCurrency(Double d, Integer qtd_decimals) {
	    if (d == null || "".equals(d) || "NaN".equals(d)) {
	        return null;
	    }
	    
	    DecimalFormat DF = new DecimalFormat();
	    
	    BigDecimal bd = new BigDecimal(d);
	    bd = bd.setScale(qtd_decimals, BigDecimal.ROUND_HALF_EVEN);
	    DecimalFormatSymbols symbols = DF.getDecimalFormatSymbols();
	    symbols.setGroupingSeparator('.');
	    String ret = DF.format(bd) + "";
	    
	    if (ret.indexOf(",") == -1) {
	    	String zeros = "";
		    for(int i = 0 ; i < qtd_decimals ; i++) {
		    	zeros += "0";
		    }
	    	
	        ret += ","+zeros;
	    } else {
	    
	    	while (ret.split(",")[1].length() != qtd_decimals) {
		    	ret += "0";
		    }
	    	
	    }
	    
	    return ret;
	    
	}
	
	public static String toFormattedCEP(String cep) {
		if (cep != null && cep.length() == 8) {
			return cep.toString().substring(0, 2) + "."
					+ cep.toString().substring(2, 5) + "-"
					+ cep.toString().substring(5, 8);
		} throw new RuntimeException("CEP inválido ou já formatado.");
	}
	
}
