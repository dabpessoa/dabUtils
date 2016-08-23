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

	public static final int BIGDECIMAL_DEFAULT_ROUND_MODE = BigDecimal.ROUND_HALF_EVEN;
	public static final int BIGDECIMAL_DEFAULT_NUMBER_OF_DECIMALS = 2;
	
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
	
	public static boolean isBigger(BigDecimal v1, BigDecimal v2, int quantidadeCasasDecimais) {
		v1 = changeNumberDecimals(v1, quantidadeCasasDecimais);
		v2 = changeNumberDecimals(v2, quantidadeCasasDecimais);
		return v1.compareTo(v2) > 0;
	}
	
	public static boolean isBigger(BigDecimal v1, BigDecimal v2) {
		return isBigger(v1, v2, BIGDECIMAL_DEFAULT_NUMBER_OF_DECIMALS);
	}
	
	public static boolean isSmaller(BigDecimal v1, BigDecimal v2, int quantidadeCasasDecimais) {
		v1 = changeNumberDecimals(v1, quantidadeCasasDecimais);
		v2 = changeNumberDecimals(v2, quantidadeCasasDecimais);
		return v1.compareTo(v2) < 0;
	}
	
	public static boolean isSmaller(BigDecimal v1, BigDecimal v2) {
		return isSmaller(v1, v2, BIGDECIMAL_DEFAULT_NUMBER_OF_DECIMALS);
	}
	
	public static boolean equals(BigDecimal v1, BigDecimal v2, int quantidadeCasasDecimais) {
		v1 = changeNumberDecimals(v1, quantidadeCasasDecimais);
		v2 = changeNumberDecimals(v2, quantidadeCasasDecimais);
		return v1.compareTo(v2) == 0;
	}
	
	public static boolean equals(BigDecimal v1, BigDecimal v2) {
		return equals(v1, v2, BIGDECIMAL_DEFAULT_NUMBER_OF_DECIMALS);
	}
	
	public static BigDecimal changeNumberDecimals(BigDecimal valor, int quantidadeCasasDecimais) {
		if (valor == null) return null;
		return valor.setScale(quantidadeCasasDecimais, BIGDECIMAL_DEFAULT_ROUND_MODE);
	}
	
}
