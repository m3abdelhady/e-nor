package me.heuristic.references;

public class Porting {
	public static final String DEL_TSV = "TSV";
	public static final String REGEX_TSV = "\\t";
	public static final String DEL_CSVEXCEL = "CSVEXCEL";
	public static final String REGEX_CSVEXCEL = ",";
	public static final String DEL_COMMA = "COMMA";
	public static final String REGEX_COMMA = ",";
	public static final String DEL_SEMICOLON = "SEMICOLON";
	public static final String REGEX_SEMICOLON = ";";
	
	public static String getDelimiterRegEx(String delimiterName) {
		if (DEL_TSV.equalsIgnoreCase(delimiterName))
			return REGEX_TSV;
		else if (DEL_CSVEXCEL.equalsIgnoreCase(delimiterName))
			return REGEX_CSVEXCEL;
		else if (DEL_COMMA.equalsIgnoreCase(delimiterName))
			return REGEX_COMMA;
		else if (DEL_SEMICOLON.equalsIgnoreCase(delimiterName))
			return REGEX_SEMICOLON;
		else
			return "";
	}
}
