package org.example.util;

public interface ObjectMapper {
	void setDateFormat(String dateFormat);
	void setDecimalFormat(String decimalFormat);
	String writeValuesAsString(Object obj);
}
