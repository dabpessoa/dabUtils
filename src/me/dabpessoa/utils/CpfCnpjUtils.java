package me.dabpessoa.utils;

import java.text.ParseException;
import java.util.Arrays;


/**
 * Classe utilit�ri com as regras de valida��o de cpf/cnpj's.
 * @author Jos� Rom�rio Viana Filho.
 */
public final class CpfCnpjUtils {
	
    /** Quantidade de n�meros em um documento cpf. */
    public static final int CPF_DOC_LENGTH = 11;
    /** Quantidade de n�meros em um documento cnpj. */
    public static final int CNPJ_DOC_LENGTH = 14;
    /** Caracteres utilizados na formata��o de cpf's e cnpj's. */
    public static final char[] DOC_FORMATING_CHARACTERS = new char[] {' ', '.', '-', '/'};

    /** M�scara de cpf. */
    private static final String CPF_MASK = "###.###.###-##";

    /** M�scara de cnpj. */
    private static final String CNPJ_MASK = "##.###.###/####-##";

    /**
     * Construtor default protegido pois � uma Classe utilit�ria.
     */
    private CpfCnpjUtils() {

    }

    /**
     * Formata um cpf/cnpj.<BR>
     * String com tamanho 14 ser�o consideras cnpj e as demais cpf. Caso ocorra algum erro de parser, o cpf
     * sem formata��o ser� retornado.
     * @param unformattedCpfCnpj Cpf/Cnjp sem caracteres de formata��o.
     * @return cpf/cnpj formatado ou valor original em caso de erro de parser.
     */
    public static String formatCpfCnpj(final String unformattedCpfCnpj) {
        if (unformattedCpfCnpj.length() == CpfCnpjUtils.CNPJ_DOC_LENGTH) {
            return CpfCnpjUtils.formatCnpj(unformattedCpfCnpj);
        }
        return CpfCnpjUtils.formatCpf(unformattedCpfCnpj);
    }

    /**
     * Formata um cpf.<BR>
     * Caso ocorra algum erro de parser, o cpf sem formata��o ser� retornado.
     * @param unformattedCpf Cpf sem caracteres de formata��o.
     * @return cpf formatado ou valor original em caso de erro de parser.
     */
    public static String formatCpf(final String unformattedCpf) {
        try {
            return CpfCnpjUtils.formatString(lPad(unformattedCpf, CpfCnpjUtils.CPF_DOC_LENGTH, '0'), CpfCnpjUtils.CPF_MASK);
        } catch (ParseException e) {
            return unformattedCpf;
        }
    }

    /**
     * Formata um cpf.<BR>
     * Caso ocorra algum erro de parser, o cpf sem formata��o ser� retornado.
     * @param unformattedCnpj Cnpj sem caracteres de formata��o.
     * @return cpf formatado ou valor original em caso de erro de parser.
     */
    public static String formatCnpj(final String unformattedCnpj) {
        try {
            return CpfCnpjUtils.formatString(lPad(unformattedCnpj, CpfCnpjUtils.CNPJ_DOC_LENGTH, '0'), CpfCnpjUtils.CNPJ_MASK);
        } catch (ParseException e) {
            return unformattedCnpj;
        }
    }

    /**
     * Valida se um cpf ou um cnpj � v�lido. O tamanho do n�mero ser� utilizado para determinar o tipo do
     * documento. Este m�todo ignora caracteres de formata��o dos documentos, portando a string passada pode
     * conter ambos: somente n�meros ou o n�meros com a formata��o padr�o do documento.
     * @param cpfCnpj N�mero de cpf ou cnpj.
     * @return <code>true</code> se o n�mero for um cpf ou cnpj v�lido, <code>false</code> caso contr�rio.
     */
    public static boolean checkCPFCNPJ(String cpfCnpj) {
    	if (isCPFLength(cpfCnpj)) {
    		return checkCPF(cpfCnpj);
    	} else if(isCNPJLength(cpfCnpj)) {
    		return checkCNPJ(cpfCnpj);
    	} else return false;
    }
    
    public static boolean isCPFLength(String value) {
    	if (value == null) return false;
    	value = removeCharacters(value, CpfCnpjUtils.DOC_FORMATING_CHARACTERS);
    	return value.length() == CpfCnpjUtils.CPF_DOC_LENGTH; 
    }
    
    public static boolean isCNPJLength(String value) {
    	if (value == null) return false;
    	value = removeCharacters(value, CpfCnpjUtils.DOC_FORMATING_CHARACTERS);
    	return value.length() == CpfCnpjUtils.CNPJ_DOC_LENGTH; 
    }

    /**
     * Valida se um cpf � v�lido. O tamanho do n�mero ser� utilizado para determinar o tipo do documento. Este
     * m�todo ignora caracteres de formata��o dos documentos, portando a string passada pode conter ambos:
     * somente n�meros ou o n�meros com a formata��o padr�o do documento.
     * @param cpf N�mero de cpf.
     * @return <code>true</code> se o cpf for v�lido, <code>false</code> caso contr�rio.
     */
    public static boolean checkCPF(String cpf) {
        if (cpf != null) {
            cpf = removeCharacters(cpf, CpfCnpjUtils.DOC_FORMATING_CHARACTERS);

            if (cpf.length() == CpfCnpjUtils.CPF_DOC_LENGTH && toLong(cpf) != null) {
                if (CpfCnpjUtils.isCPF(cpf)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Valida se um cnpj � v�lido. O tamanho do n�mero ser� utilizado para determinar o tipo do documento.
     * Este m�todo ignora caracteres de formata��o dos documentos, portando a string passada pode conter
     * ambos: somente n�meros ou o n�meros com a formata��o padr�o do documento.
     * @param cnpj N�mero de cnpj.
     * @return <code>true</code> se o cnpj for v�lido, <code>false</code> caso contr�rio.
     */
    public static boolean checkCNPJ(String cnpj) {
        if (cnpj != null) {
            cnpj = removeCharacters(cnpj, CpfCnpjUtils.DOC_FORMATING_CHARACTERS);

            if (cnpj.length() == CpfCnpjUtils.CNPJ_DOC_LENGTH && toLong(cnpj) != null) {
                if (CpfCnpjUtils.isCNPJ(cnpj)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Verifica se o n�mero passado � um cpf.
     * @param cpf N�mero do cpf.
     * @return <code>true</code> se o n�mero for um cpf v�lido, <code>false</code> caso contr�rio.
     */
    private static boolean isCPF(String cpf) {
        if (cpf.length() != CpfCnpjUtils.CPF_DOC_LENGTH) {
            return false;
        }

        String rcpf1 = cpf.substring(0, 9);
        String rcpf2 = cpf.substring(9);

        int d1 = 0;
        for (int i = 0; i < 9; i++) {
            d1 += new Integer(rcpf1.substring(i, i + 1)).intValue() * (10 - i);
        }

        d1 = 11 - (d1 % 11);

        if (d1 > 9) {
            d1 = 0;
        }

        if (new Integer(rcpf2.substring(0, 1)).intValue() != d1) {
            return false;
        }

        d1 *= 2;
        for (int i = 0; i < 9; i++) {
            d1 += new Integer(rcpf1.substring(i, i + 1) + "").intValue() * (11 - i);
        }

        d1 = 11 - (d1 % 11);
        if (d1 > 9) {
            d1 = 0;
        }

        if (new Integer(rcpf2.substring(1, 2) + "").intValue() != d1) {
            return false;
        }

        // Verifica se o cpf cont�m todos os d�gitos iguais.
        if (toLong(cpf) - toLong(fill(cpf.charAt(0), cpf.length())) == 0) {
            return false;
        }

        return true;
    }

    /**
     * Verifica se o n�mero passado � um cnpj.
     * @param cnpj N�mero do cnpj.
     * @return <code>true</code> se o n�mero for um cnpj v�lido, <code>false</code> caso contr�rio.
     */
    private static boolean isCNPJ(String cnpj) {
        StringBuffer cnpjTMP = new StringBuffer();
        char ch;
        for (int x = 1; x <= cnpj.length(); x++) {
            ch = cnpj.charAt(x - 1);
            cnpjTMP.append(ch);
        }

        cnpj = cnpjTMP.toString();

        if (cnpj.length() != CpfCnpjUtils.CNPJ_DOC_LENGTH) {
            return false;
        } else if (cnpj.equals("00000000000000")) {
            return false;
        } else {
            int Numero[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            Numero[1] = (new Integer(cnpj.substring(0, 1)).intValue());
            Numero[2] = (new Integer(cnpj.substring(1, 2)).intValue());
            Numero[3] = (new Integer(cnpj.substring(2, 3)).intValue());
            Numero[4] = (new Integer(cnpj.substring(3, 4)).intValue());
            Numero[5] = (new Integer(cnpj.substring(4, 5)).intValue());
            Numero[6] = (new Integer(cnpj.substring(5, 6)).intValue());
            Numero[7] = (new Integer(cnpj.substring(6, 7)).intValue());
            Numero[8] = (new Integer(cnpj.substring(7, 8)).intValue());
            Numero[9] = (new Integer(cnpj.substring(8, 9)).intValue());
            Numero[10] = (new Integer(cnpj.substring(9, 10)).intValue());
            Numero[11] = (new Integer(cnpj.substring(10, 11)).intValue());
            Numero[12] = (new Integer(cnpj.substring(11, 12)).intValue());
            Numero[13] = (new Integer(cnpj.substring(12, 13)).intValue());
            Numero[14] = (new Integer(cnpj.substring(13, 14)).intValue());

            int soma = Numero[1] * 5 + Numero[2] * 4 + Numero[3] * 3 + Numero[4] * 2 + Numero[5] * 9 + Numero[6] * 8 + Numero[7] * 7 + Numero[8] * 6 + Numero[9] * 5 + Numero[10] * 4 + Numero[11] * 3 + Numero[12] * 2;

            soma = soma - (11 * ((soma / 11)));
            int resultado1, resultado2;
            if (soma == 0 || soma == 1) {
                resultado1 = 0;
            } else {
                resultado1 = 11 - soma;
            }
            if (resultado1 == Numero[13]) {
                soma = Numero[1] * 6 + Numero[2] * 5 + Numero[3] * 4 + Numero[4] * 3 + Numero[5] * 2 + Numero[6] * 9 + Numero[7] * 8 + Numero[8] * 7 + Numero[9] * 6 + Numero[10] * 5 + Numero[11] * 4 + Numero[12] * 3 + Numero[13] * 2;
                soma = soma - (11 * ((soma / 11)));
                if (soma == 0 || soma == 1) {
                    resultado2 = 0;
                } else {
                    resultado2 = 11 - soma;
                }
                if (resultado2 == Numero[14]) {
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    /**
     * Formata uma string.
     * @param value String a ser formatado.
     * @param mask M�scara.
     * @return Valor formatado.
     * @throws ParseException Caso ocorra um erro ao fazer o parser.
     */
    private static String formatString(String value, String mask) throws ParseException {
        javax.swing.text.MaskFormatter mf = new javax.swing.text.MaskFormatter(mask);
        mf.setValueContainsLiteralCharacters(false);
        return mf.valueToString(value);
    }
    
    private static String lPad(String str, int length, char completingChar) {
        str = (str == null ? "" : str);
        int strLength = str.length();
        if (strLength < length) {
            char[] chars = new char[length];
            Arrays.fill(chars, 0, length - strLength, completingChar);
            if (strLength > 0) {
                str.getChars(0, strLength, chars, length - strLength);
            }
            return new String(chars);
        }
        return str;
    }
    
    private static String removeCharacters(final String str, char... characters) {
        StringBuffer ret = new StringBuffer(str);

        /* varrer o texto, procurando caracteres */
        int pos = -1;
        for (char character : characters) {
            while((pos = ret.indexOf(String.valueOf(character))) != -1) {
                ret.deleteCharAt(pos);
            }
        }
        return ret.toString();
    }
    
    private static Long toLong(String value) {
        try {
            return Long.valueOf(value.trim());
        } catch(Exception e) {
            return null;
        }
    }
    
    private static String fill(char ch, int length) {
        char[] chars = new char[length];
        Arrays.fill(chars, 0, chars.length, ch);
        return new String(chars);
    }
    
}
