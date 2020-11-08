package com.bootdo.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * StringUtil.java
 *
 * Copyright 1999, 2000, 2001 Jcorporate Ltd.
 */

/**
 * Utilities for manipulations of strings
 * 
 * @author Michael Nash
 * @version $Revision: 1.6 $ $Date: 2009/10/09 01:18:06 $
 */
public final class StringUtil {
	private static Logger logger = LoggerFactory.getLogger(StringUtil.class);
	
	private static final int TIME_OUT = 30000;

	private static final String thisClass = StringUtil.class + ".";

	public static final String EMPTY_STRING = "";

	/**
	 * Constructor
	 */
	public StringUtil() {
		super();
	} /* StringUtil() */

	/**
	 * Parses a boolean string to return a boolean value
	 * 
	 * @param theString
	 *            The string to parse
	 * @return True or False depending of the contents of the string.
	 */
	public static boolean toBoolean(String theString) {
		if (theString == null) {
			return false;
		}

		theString = theString.trim();

		if (theString.equalsIgnoreCase("y") || theString.equalsIgnoreCase("yes")
				|| theString.equalsIgnoreCase("true") || theString.equalsIgnoreCase("1")) {
			return true;
		}

		return false;
	}

	/**
	 * Make sure a string is not null. This is a convenience method, but it is
	 * not necessarily recommended if you do a lot of processing for theMessage
	 * parameter. It is recommended that you assertNotBlank, catch
	 * IllegalArgumentException and then throw an appropriate exception with
	 * your message.
	 * 
	 * @param theString
	 *            Any string, possibly null
	 * @param theMessage
	 *            The error message to use if the argument is not null.
	 */
	public static final void assertNotBlank(String theString, String theMessage) {
		if (theString == null) {
			throw new IllegalArgumentException("Null argument not allowed: " + theMessage);
		}
		if (theString.trim().equals(EMPTY_STRING)) {
			throw new IllegalArgumentException("Blank argument not allowed: " + theMessage);
		}
	} /* assertNotBlank(String, String) */

	/**
	 * Make sure a string is not null.
	 * 
	 * @param theString
	 *            Any string, possibly null
	 * @return An empty string if the original was null, else the original
	 */
	public static final String notNull(String theString) {
		if (theString == null) {
			return EMPTY_STRING;
		}

		return theString;
	} /* notNull(String) */

	/**
	 * Throws an exception if theString can't be mapped to a boolean value.
	 * 
	 * @param theString
	 *            the string to check
	 * @param theMessage
	 *            the message to have in the IllegalArgumentException if the
	 *            conditions aren't met.
	 */
	public static final void assertBoolean(String theString, String theMessage) {
		assertNotBlank(theString, theMessage);

		if (!(theString.equalsIgnoreCase("yes") || theString.equalsIgnoreCase("true")
				|| theString.equalsIgnoreCase("no") || theString.equalsIgnoreCase("false")
				|| theString.equalsIgnoreCase("y") || theString.equalsIgnoreCase("n"))) {
			throw new IllegalArgumentException(theMessage);
		}
	}

	/**
	 * This method is useful for creating lists that use letters instead of
	 * numbers, such as a, b, c, d...instead of 1, 2, 3, 4. Valid numbers are
	 * from 1 to 26, corresponding to the 26 letters of the alphabet. By
	 * default, the letter is returned as a lowercase, but if the boolean
	 * upperCaseFlag is true, the letter will be returned as an uppercase.
	 * Creation date: (5/11/00 12:52:23 PM)
	 * 
	 * @param number
	 *            The number to convert
	 * @param upperCaseFlag
	 *            True if you want the final data to be uppercase
	 * @return java.lang.String
	 * @throws Exception
	 */
	public static String numberToLetter(int number, boolean upperCaseFlag) throws Exception {

		// add nine to bring the numbers into the right range (in java, a= 10, z
		// = 35)
		if (number < 1 || number > 26) {
			throw new Exception("The number is out of the proper range (1 to "
					+ "26) to be converted to a letter.");
		}

		int modnumber = number + 9;
		char thechar = Character.forDigit(modnumber, 36);

		if (upperCaseFlag) {
			thechar = Character.toUpperCase(thechar);
		}

		return "" + thechar;
	} /* numberToLetter(int, boolean) */

	/**
	 * replace substrings within string.
	 * 
	 * @param s
	 *            The string to work with
	 * @param sub
	 *            The string to substitude the occurances of
	 * @param with
	 *            The string to replace with
	 * @return A processed java.util.String
	 */
	public static String replace(String s, String sub, String with) {
		int c = 0;
		int i = s.indexOf(sub, c);

		if (i == -1) {
			return s;
		}

		StringBuffer buf = new StringBuffer(s.length() + with.length());

		do {
			buf.append(s.substring(c, i));
			buf.append(with);
			c = i + sub.length();
		} while ((i = s.indexOf(sub, c)) != -1);

		if (c < s.length()) {
			buf.append(s.substring(c, s.length()));
		}

		return buf.toString();
	} /* replace(String, String, String) */

	/**
	 * Formats the string to an XML/XHTML escaped character. Useful for &'s, etc
	 * 
	 * @param s
	 *            the String to format
	 * @return The escaped formatted String.
	 * @see org.apache.xml.serialize.BaseMarkupSerializer for example of
	 *      original code.
	 */
	public static String xmlEscape(String s) {
		int length = s.length();
		StringBuffer fsb = new StringBuffer(length);

		for (int i = 0; i < length; i++) {
			fsb = printEscaped(s.charAt(i), fsb);
		}

		return fsb.toString();
	}

	/**
	 * Formats a particular character to something workable in xml Helper to
	 * xmlEscape()
	 * 
	 * @param ch
	 *            the character to print.
	 * @param fsb
	 *            The StringBuffer to add this to.
	 * @return a StringBuffer that is modified
	 */
	protected static StringBuffer printEscaped(char ch, StringBuffer fsb) {
		String charRef;

		// If there is a suitable entity reference for this
		// character, print it. The list of available entity
		// references is almost but not identical between
		// XML and HTML.
		charRef = getEntityRef(ch);

		if (charRef != null) {
			fsb.append('&');
			fsb.append(charRef);
			fsb.append(';');

			// ch<0xFF == isPrintable()
		}
		else if ((ch >= ' ' && ch < 0xFF && ch != 0xF7) || ch == '\n' || ch == '\r' || ch == '\t') {

			// If the character is not printable, print as character reference.
			// Non printables are below ASCII space but not tab or line
			// terminator, ASCII delete, or above a certain Unicode threshold.
			if (ch < 0x10000) {
				fsb.append(ch);
			}
			else {
				fsb.append((char) ((((int) ch - 0x10000) >> 10) + 0xd800));
				fsb.append((char) ((((int) ch - 0x10000) & 0x3ff) + 0xdc00));
			}
		}
		else {
			fsb.append("&#x");
			fsb.append(Integer.toHexString(ch));
			fsb.append(';');
		}

		return fsb;
	}

	/**
	 * Helper to xmlEscape()
	 * 
	 * @param ch
	 *            the character to escape
	 * @return A modified string representing the tanlation of the character or
	 *         null if there is no translation for it.
	 */
	protected static String getEntityRef(int ch) {

		// Encode special XML characters into the equivalent character
		// references.
		// These five are defined by default for all XML documents.
		switch (ch) {
		case '<':
			return "lt";

		case '>':
			return "gt";

		case '"':
			return "quot";

		case '\'':
			return "apos";

		case '&':
			return "amp";
		}

		return null;
	}

	/**
	 * HTML code for ellipses (3 dots, like '...' as one character) used for
	 * appending to a truncate() line if necessary
	 */
	public static String ELLIPSES = "&#133";

	/**
	 * truncate a string at the given length if necessary, adding an ellipses at
	 * the end if truncation occurred; uses ELLIPSES static String from this
	 * class
	 * 
	 * @param str
	 *            The string to process
	 * @param len
	 *            The maximum length to process the string to.
	 * @return the apopriately trimmed string.
	 */
	public static String truncate(String str, int len) {
		String result = str;
		if (str.length() > len) {
			result = str.substring(0, len) + ELLIPSES;
		}
		return result;
	}

	/**
	 * Map accent characters with equivalent without accent.
	 * <p/>
	 * <p/>
	 * $Date: 2008/06/05 01:27:05 $
	 * 
	 * @return Hashtable Character mapping table.
	 */
	public static Hashtable characterMap() {
		Hashtable characterMap = new Hashtable();

		characterMap.put(new Character('?'), new Character('a'));
		characterMap.put(new Character('?'), new Character('a'));
		characterMap.put(new Character('?'), new Character('a'));
		characterMap.put(new Character('?'), new Character('a'));
		characterMap.put(new Character('?'), new Character('A'));
		characterMap.put(new Character('?'), new Character('A'));
		characterMap.put(new Character('?'), new Character('A'));
		characterMap.put(new Character('?'), new Character('A'));
		characterMap.put(new Character('?'), new Character('e'));
		characterMap.put(new Character('?'), new Character('e'));
		characterMap.put(new Character('?'), new Character('e'));
		characterMap.put(new Character('?'), new Character('e'));
		characterMap.put(new Character('?'), new Character('E'));
		characterMap.put(new Character('?'), new Character('E'));
		characterMap.put(new Character('?'), new Character('E'));
		characterMap.put(new Character('?'), new Character('E'));
		characterMap.put(new Character('?'), new Character('i'));
		characterMap.put(new Character('?'), new Character('i'));
		characterMap.put(new Character('?'), new Character('I'));
		characterMap.put(new Character('?'), new Character('I'));
		characterMap.put(new Character('?'), new Character('o'));
		characterMap.put(new Character('?'), new Character('o'));
		characterMap.put(new Character('?'), new Character('O'));
		characterMap.put(new Character('?'), new Character('O'));
		characterMap.put(new Character('?'), new Character('u'));
		characterMap.put(new Character('?'), new Character('u'));
		characterMap.put(new Character('?'), new Character('U'));
		characterMap.put(new Character('?'), new Character('U'));
		characterMap.put(new Character('?'), new Character('c'));
		characterMap.put(new Character('?'), new Character('C'));

		return characterMap;
	}

	/**
	 * Remove from the parameter the accent characters and return the remain
	 * string or null if empty
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param s
	 *            the string to remove the accent characters
	 * @return String
	 */
	public static String removeAccents(String s) {
		String out = null;
		if (s != null) {
			Hashtable charRemove = characterMap();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < s.length(); i++) {
				Character c = new Character(s.charAt(i));
				if (charRemove.containsKey(c)) {
					c = (Character) charRemove.get(c);
				}
				sb.append(c.charValue());
			}
			out = sb.toString();
		}
		return out;
	}

	/**
	 * Replace all comma by dot
	 * <p/>
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param str
	 *            String to change
	 * @return String The result of substitution
	 */
	public static String convertCommaToDot(String str) {
		return str.replace(',', '.');
	}

	/**
	 * Compare 2 decimals string
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param decim1
	 *            First string to compare
	 * @param decim2
	 *            Second string to compare
	 * @return int return 1 if decim1 > decim2<BR>
	 *         retourne 0 if decim1 == decim2<BR>
	 *         return -1 if decim1 < decim2
	 * @throws ParseException
	 *             if wrong parameters.
	 */
	public static int compareDecimals(String decim1, String decim2) throws ParseException {
		BigDecimal dec1 = new BigDecimal(decim1);
		BigDecimal dec2 = new BigDecimal(decim2);
		return dec1.compareTo(dec2);
	}

	/**
	 * Compare 2 integers string.
	 * <p/>
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param int1
	 *            first string to compare
	 * @param int2
	 *            second string to compare
	 * @return int return 1 if decim1 > decim2<BR>
	 *         return 0 if decim1 == decim2<BR>
	 *         return -1 if decim1 < decim2
	 * @throws ParseException
	 *             if wrong parameters.
	 */
	public static int compareIntegers(String int1, String int2) throws ParseException {
		BigInteger dec1 = new BigInteger(int1);
		BigInteger dec2 = new BigInteger(int2);
		return dec1.compareTo(dec2);
	}

	/**
	 * Check if Empty $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param obj
	 *            to check
	 * @return boolean
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null || obj.toString().trim().equals(""))
			return true;
		return false;
	}

	public static boolean isNullEmpty(Object obj) {
		if (obj == null || obj.toString().trim().equals("")
				|| obj.toString().toLowerCase().trim().equals("null"))
			return true;
		return false;
	}

	/**
	 * Check if string is alphanumeric or not.
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param s
	 *            String to check.
	 * @return boolean true if alphanumeric, false if not.
	 */
	public static boolean isAlphaNumeric(String s) {
		return isAlphaNumeric(s, "");
	}

	/**
	 * Check if string is alphanumeric with addons chararcters or not.
	 * <p/>
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param str
	 *            string to check
	 * @param otherChars
	 *            extra characters to check with
	 * @return boolean true if parameter string contains only alpha numerics,<BR>
	 *         plus addons characters and false if not.
	 */
	public static boolean isAlphaNumeric(String str, String otherChars) {
		String alphaNum = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ otherChars;
		for (int i = 0; i < str.length(); i++) {
			if (alphaNum.indexOf(str.charAt(i)) == -1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check if decimal number
	 * <p/>
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param s
	 *            string to check.
	 * @return boolean true if the value is decimal number false if not
	 */
	public static boolean isDecimal(String s) {
		try {
			new BigDecimal(s);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * Check if string pass is a vlaid email address
	 * <p/>
	 * $Date: 2008/06/05 01:27:05 $
	 * 
	 * @param email
	 *            string to check
	 * @return int 0 if valid address, 1 more than 2 tokens (".", "@")<BR>
	 *         and 2 if the second token is not "." .
	 */
	public static int isEmail(String email) {
		StringTokenizer st = new StringTokenizer(email, "@");

		if (st.countTokens() != 2) {
			return 1;
		}

		st.nextToken();
		if (st.nextToken().indexOf(".") == -1) {
			return 2;
		}

		return 0;
	}

	/**
	 * Check if integer string
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param str
	 *            string to check
	 * @return boolean true if all characters is digit
	 */
	public static boolean isInteger(String str) {
		try {
			new BigInteger(str);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public static Integer null2AbsInteger(String str) {
		if (StringUtil.isEmpty(str))
			return 0;

		return Math.abs(Integer.valueOf(str));
	}

	/**
	 * Check if number $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param str
	 *            to check
	 * @return boolean
	 */
	public static boolean isNumber(String str) {
		try {
			double number = Double.parseDouble(str);
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Check French company siren number.
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param siren
	 *            the company number
	 * @return boolean true if ok.
	 */
	public static boolean isSiren(String siren) {
		StringBuffer temp = new StringBuffer("");
		int value = 0;

		// Check if numeric digit.
		try {
			Double.parseDouble(siren);
		}
		catch (NumberFormatException nbe) {
			return false;
		}

		// string length must be 9
		if (siren.length() != 9) {
			return false;
		}

		//
		for (int i = 0; i < 9; i++) {
			int n = Integer.parseInt(siren.substring(i, i + 1)) * (((i % 2) != 0) ? 2 : 1);
			temp.append(n);
		}

		// sum of all digits
		for (int i = 0; i < (temp.length()); i++) {
			value += Integer.parseInt(temp.substring(i, i + 1));
		}

		// value must divide by 10
		if ((value % 10) != 0) {
			return false;
		}

		return true;
	}

	/**
	 * Check French company siret number
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param siret
	 *            The siret number
	 * @return boolean true if equivalent to siret number false if not.
	 */
	public static boolean isSiret(String siret) {
		// Check the length
		if (siret.length() != 14) {
			return false;
		}

		//
		try {
			Double.parseDouble(siret);
			return isSiren(siret.substring(0, 9));
		}
		catch (NumberFormatException nfe) {
			return false;
		}
	}

	/**
	 * Return empty string if parameter is null
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param s
	 *            the parameter to check
	 * @return String return the parameter if empty or if the string == null
	 */
	public static String null2Empty(Object s) {
		if (s == null || s.toString().equalsIgnoreCase("null")) {
			return "";
		}
		else {
			return s.toString();
		}
	}

	public static String empty2Null(Object s) {
		if (isEmpty(s))
			return "null";
		else
			return String.valueOf(s);
	}

	public static String null2Zero(Object s) {
		if (s == null || (String.valueOf(s)).equalsIgnoreCase("null"))
			return "0";
		else
			return String.valueOf(s);
	}

	public static String null2Minus(Object s) {
		if (s == null || (String.valueOf(s)).equalsIgnoreCase("null"))
			return "-1";
		else
			return String.valueOf(s);
	}

	public static Integer nullToZero(Object s) {
		if (StringUtil.isEmpty(s) || (String.valueOf(s)).equalsIgnoreCase("null"))
			return 0;
		else
			return Integer.valueOf(s.toString());
	}

	/**
	 * @param s
	 * @return
	 */
	public static String null2Str(Object s) {
		if (s != null)
			return s.toString().trim();
		return "";
	}

	public static double toDouble(String str) {
		double lpResult = 0;

		try {
			lpResult = Double.parseDouble(null2Zero(str));
		}
		catch (NumberFormatException nfe) {
		}
		return lpResult;
	}

	/**
	 * Reformat string by converting "," by "."
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param str
	 *            string to reformat
	 * @return String return reformatted string.
	 */
	public static String reformatDecimalString(String str) {
		// replace all ',' by '.'
		str = str.replace(',', '.');
		try {
			Double.parseDouble(str);
			return str;
		}
		catch (NumberFormatException nbe) {
			return "";
		}
	}

	/**
	 * Replace all dots byt comma
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param str
	 *            string to change
	 * @return String reformatted
	 */
	public static String convertDotToComma(String str) {
		return str.replace('.', ',');
	}

	/**
	 * Replace all occurences of key by value in text.
	 * <p/>
	 * $Date: 2008/06/05 01:27:05 $
	 * 
	 * @param text
	 *            string in
	 * @param key
	 *            occurence to replace
	 * @param value
	 *            string to use
	 * @return String with the replace value
	 */
	public static String replaceAll(String text, String key, String value) {
		String buffer = text;
		if (buffer != null && key != null && value != null) {
			int length = key.length();
			for (int start = buffer.indexOf(key); start != -1; start = buffer.indexOf(key, start
					+ value.length())) {
				buffer = buffer.substring(0, start) + value + buffer.substring(start + length);
			}
		}
		return buffer;
	}

	/**
	 * Substituate once str1 by str2 in text Commentaire Anglais
	 * <p/>
	 * $Date: 2008/06/05 01:27:05 $
	 * 
	 * @param text
	 *            search and replace in
	 * @param str1
	 *            to search for
	 * @param str2
	 *            to replace with
	 * @return String replaced
	 */
	static public String replaceStringOnce(String text, String str1, String str2) {
		return replaceString(text, str1, str2, 1);
	}

	/**
	 * Substituate all occurence of str1 by str2 in text
	 * <p/>
	 * $Date: 2008/06/05 01:27:05 $
	 * 
	 * @param text
	 *            search and replace in
	 * @param str1
	 *            search for
	 * @param str2
	 *            replace with
	 * @return String with all values replaced
	 */
	static public String replaceString(String text, String str1, String str2) {
		return replaceString(text, str1, str2, -1);
	}

	/**
	 * Replace n occurences of str1 in text by str2. if n = -1 all occurrences
	 * are replaced
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param text
	 *            search and replace in
	 * @param str1
	 *            search for
	 * @param str2
	 *            replace with
	 * @param max
	 *            int values of occrrences to replace
	 * @return String replaced
	 */
	static public String replaceString(String text, String str1, String str2, int max) {
		if (text == null) {
			return null;
		}

		StringBuffer buffer = new StringBuffer(text.length());
		int start = 0;
		int end = 0;
		while ((end = text.indexOf(str1, start)) != -1) {
			buffer.append(text.substring(start, end)).append(str2);
			start = end + str1.length();
			if (--max == 0) {
				break;
			}
		}
		buffer.append(text.substring(start));
		return buffer.toString();
	}

	/**
	 * Convert string to list
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param s
	 *            String comma delimited string to format
	 * @return List
	 */
	public static List string2List(String s) {
		return string2List(s, ",");
	}

	/**
	 * Convert string to list using sep separator to divide $Date: 2008/06/05
	 * 01:27:05 $
	 * 
	 * @param s
	 *            String comma delimited string to format
	 * @param sep
	 *            a string containing the seprator characters
	 * @return List
	 */
	public static List string2List(String s, String sep) {
		return string2List(s, sep, s != null ? s.length() : Integer.MAX_VALUE);
	}

	/**
	 * Convert string to list using sep separator to divide
	 * 
	 * @param s
	 *            String comma delimited string to format
	 * @param sep
	 *            a string containing the seprator characters
	 * @param maxSize
	 *            the maximum size of the list
	 * @return List
	 */
	public static List string2List(String s, String sep, int maxSize) {
		List l = null;
		if (s != null) {
			l = new Vector();
			for (int i = 0; i < maxSize;) {
				int index = s.indexOf(sep, i);
				String token;
				if (index != -1) {
					token = s.substring(i, index);
				}
				else {
					token = s.substring(i);
				}
				if (token.length() > 0 && !token.equals(sep)) {
					l.add(token.trim());
				}
				i += token.length() + sep.length();
			}
		}
		return l;
	}

	/**
	 * convert the first character of string in lower case
	 * <p/>
	 * $Date: 2008/06/05 01:27:05 $
	 * 
	 * @param str
	 *            String to un-upper case the first character
	 * @return String
	 */
	static public String unUpperFirstChar(String str) {
		StringBuffer fsb = new StringBuffer();
		try {
			fsb.append(Character.toLowerCase(str.charAt(0)));
			fsb.append(str.substring(1));
			return fsb.toString();
		}
		finally {
		}
	}

	/**
	 * convert the first character of the string upper case
	 * <p/>
	 * $Date: 2008/06/05 01:27:05 $
	 * 
	 * @param str
	 *            String to make the first character upper.
	 * @return String
	 */
	static public String upperFirstChar(String str) {

		StringBuffer fsb = new StringBuffer();
		try {
			fsb.append(Character.toTitleCase(str.charAt(0)));
			fsb.append(str.substring(1));
			return fsb.toString();
		}
		finally {

		}

		// return "" + Character.toTitleCase(str.charAt(0)) + str.substring(1);
	}

	/**
	 * Repeat str n time to format another string.
	 * <p/>
	 * $Date: 2008/06/05 01:27:05 $
	 * 
	 * @param str
	 *            String to repeat
	 * @param n
	 *            int n repeat
	 * @return String
	 */
	static public String repeatString(String str, int n) {
		StringBuffer buffer = new StringBuffer();
		try {
			// StringBuffer is preallocated for 1K, so we may not
			// need to do any memory allocation here.
			int val = n * str.length();
			if (val > buffer.capacity()) {
				buffer.ensureCapacity(val);
			}
			// StringBuffer buffer = new StringBuffer(n * str.length());
			for (int i = 0; i < n; i++) {
				buffer.append(str);
			}
			return buffer.toString();
		}
		finally {
		}
	}

	/**
	 * enclosed the string with padding character. Space is padding character
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param str
	 *            String str string to center padding
	 * @param n
	 *            int n size of the new string
	 * @return String Result
	 */
	static public String centerPad(String str, int n) {
		return centerPad(str, n, " ");
	}

	/**
	 * Enclosed the string with padding character
	 * <p/>
	 * $Date: 2008/06/05 01:27:05 $
	 * 
	 * @param str
	 *            String str string to pad with
	 * @param n
	 *            int n size of the final string
	 * @param delim
	 *            String delim padding character
	 * @return String result of the center padding
	 */
	static public String centerPad(String str, int n, String delim) {
		int sz = str.length();
		int p = n - sz;
		if (p < 1) {
			return str;
		}
		str = leftPad(str, sz + p / 2, delim);
		str = rightPad(str, n, delim);
		return str;
	}

	/**
	 * Right padding with delimiter
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param str
	 *            String
	 * @param n
	 *            int size of the final string
	 * @param delim
	 *            padding character
	 * @return String padding string
	 */
	static public String rightPad(String str, int n, String delim) {
		int sz = str.length();
		n = (n - sz) / delim.length();
		if (n > 0) {
			str += repeatString(delim, n);
		}
		return str;
	}

	/**
	 * Right padding delimiter is space
	 * <p/>
	 * Dte: 2002/10/30 23:38:12 $
	 * 
	 * @param str
	 *            String
	 * @param n
	 *            int size of the new string
	 * @return String
	 */
	static public String rightPad(String str, int n) {
		return rightPad(str, n, " ");
	}

	/**
	 * Left padding padding character is space
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param str
	 *            String
	 * @param n
	 *            int size of the new string
	 * @return String
	 */
	static public String leftPad(String str, int n) {
		return leftPad(str, n, " ");
	}

	/**
	 * Left padding
	 * <p/>
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param str
	 *            String
	 * @param n
	 *            int size of the new string
	 * @param delim
	 *            padding character
	 * @return String result
	 */
	static public String leftPad(String str, int n, String delim) {
		int sz = str.length();
		n = (n - sz) / delim.length();
		if (n > 0) {
			str = repeatString(delim, n) + str;
		}
		return str;
	}

	/**
	 * Reverse the String.
	 * 
	 * @param str
	 *            the String to reverse.
	 * @return a reversed string
	 */
	static public String reverseString(String str) {
		StringBuffer fsb = new StringBuffer();
		try {
			fsb.append(str);
			return fsb.reverse().toString();
		}
		finally {

		}
	}

	/**
	 * Reverse the character case in the string
	 * <p/>
	 * <p/>
	 * $Date: 2008/06/05 01:27:05 $
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	static public String swapCase(String str) {
		int sz = str.length();
		StringBuffer buffer = new StringBuffer();
		// StringBuffer buffer = new StringBuffer(sz);
		try {
			if (sz > buffer.capacity()) {
				buffer.ensureCapacity(sz);
			}
			boolean whiteSpace = false;
			char ch = 0;
			char tmp = 0;
			for (int i = 0; i < sz; i++) {
				ch = str.charAt(i);
				if (Character.isUpperCase(ch)) {
					tmp = Character.toLowerCase(ch);
				}
				else if (Character.isTitleCase(ch)) {
					tmp = Character.toLowerCase(ch);
				}
				else if (Character.isLowerCase(ch)) {
					if (whiteSpace) {
						tmp = Character.toTitleCase(ch);
					}
					else {
						tmp = Character.toUpperCase(ch);
					}
				}
				buffer.append(tmp);
				whiteSpace = Character.isWhitespace(ch);
			}
			return buffer.toString();
		}
		finally {
		}
	}

	/**
	 * Create a random string $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param count
	 *            size of string.
	 * @return randomly generated string of size count
	 */
	static public String random(int count) {
		return random(count, false, false);
	}

	/**
	 * Create a random Ascii String $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param count
	 *            the size of the string
	 * @return randomly generated string of size count
	 */
	static public String randomAscii(int count) {
		return random(count, 32, 127, false, false);
	}

	/**
	 * Create a random character only string
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param count
	 *            size of string
	 * @return randomly generated string of size count
	 */
	static public String randomAlphabetic(int count) {
		return random(count, true, false);
	}

	/**
	 * Create a random alpha numeric string
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param count
	 *            the size of the string
	 * @return randomly generated string of size count
	 */
	static public String randomAlphanumeric(int count) {
		return random(count, true, true);
	}

	/**
	 * Create a random numeric string
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param count
	 *            the size of the final string
	 * @return randomly generated string of size count
	 */
	static public String randomNumeric(int count) {
		return random(count, false, true);
	}

	/**
	 * Create a random numeric string where you have control over size, and
	 * whether you want letters, numbers, or both.
	 * <p/>
	 * <p/>
	 * $Date: 2008/06/05 01:27:05 $
	 * 
	 * @param count
	 *            the size of the string
	 * @param letters
	 *            true if you want letters included
	 * @param numbers
	 *            true if you want numbers included
	 * @return randomly generated string of size count
	 */
	static public String random(int count, boolean letters, boolean numbers) {
		return random(count, 0, 0, letters, numbers);
	}

	/**
	 * Create a random numeric string where you have control over size, and
	 * whether you want letters, numbers, as well as ANSI minimum and maximum
	 * values of the characters.
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param count
	 *            the size of the string
	 * @param start
	 *            int minimum 'value' of the character
	 * @param end
	 *            maximum 'value' of the character
	 * @param letters
	 *            true if you want letters included
	 * @param numbers
	 *            true if you want numbers included
	 * @return randomly generated string of size count
	 */
	static public String random(int count, int start, int end, boolean letters, boolean numbers) {
		return random(count, start, end, letters, numbers, null);
	}

	/**
	 * Create a random numeric string where you have control over size, and
	 * whether you want letters, numbers, as well as ANSI minimum and maximum
	 * values of the characters.
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param count
	 *            the size of the string
	 * @param start
	 *            int minimum 'value' of the character
	 * @param end
	 *            maximum 'value' of the character
	 * @param letters
	 *            true if you want letters included
	 * @param numbers
	 *            true if you want numbers included
	 * @param set
	 *            the set of possible characters that you're willing to let the
	 *            string contain. may be null if all values are open.
	 * @return randomly generated string of size count
	 */
	static public String random(int count, int start, int end, boolean letters, boolean numbers,
			char[] set) {
		if ((start == 0) && (end == 0)) {
			end = (int) 'z';
			start = (int) ' ';
			if (!letters && !numbers) {
				start = 0;
				end = Integer.MAX_VALUE;
			}
		}
		Random rnd = new Random();
		// StringBuffer buffer = new StringBuffer();
		StringBuffer buffer = new StringBuffer();
		try {
			int gap = end - start;
			while (count-- != 0) {
				char ch;
				if (set == null) {
					ch = (char) (rnd.nextInt(gap) + start);
				}
				else {
					ch = set[rnd.nextInt(gap) + start];
				}
				if ((letters && numbers && Character.isLetterOrDigit(ch))
						|| (letters && Character.isLetter(ch))
						|| (numbers && Character.isDigit(ch)) || (!letters && !numbers)) {
					buffer.append(ch);
				}
				else {
					count++;
				}
			}
			return buffer.toString();
		}
		finally {
		}
	}

	/**
	 * Create a random string
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param count
	 *            the size of the string
	 * @param set
	 *            the set of characters that are allowed
	 * @return randomly generated string of size count
	 */
	static public String random(int count, String set) {
		return random(count, set.toCharArray());
	}

	/**
	 * Create a random string $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param count
	 *            the size of the string
	 * @param set
	 *            the set of characters that are allowed
	 * @return randomly generated string of size count
	 */
	static public String random(int count, char[] set) {
		return random(count, 0, set.length - 1, false, false, set);
	}

	/**
	 * return empty string the string is null
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param str
	 *            The string to split String
	 * @param lg
	 *            the length to subgstring
	 * @return a substring of parameter str.
	 */
	public static String substring(String str, int lg) {
		return substring(str, 0, lg);
	}

	/**
	 * return empty string the string is null
	 * <p/>
	 * $Date: 2009/10/09 01:18:06 $
	 * 
	 * @param str
	 *            The string to split String
	 * @param start
	 *            the location to start
	 * @param end
	 *            the end location of the substring
	 * @return a substring of parameter str.
	 */
	public static String substring(String str, int start, int end) {
		if (str == null || str.length() <= start) {
			return null;
		}
		else if (str.length() >= end) {
			return str.substring(start, end);
		}
		else {
			return str.substring(start);
		}
	}

	public static String getInString(Object object) {
		return getInString(object, ",");

	}

	public static String getInString(Object object, String delim) {
		StringBuffer bf = new StringBuffer();
		String str = object.toString();
		if (StringUtil.isEmpty(str) || str.indexOf(delim) == -1) {
			return "'" + StringUtil.null2Str(str) + "'";
		}
		String[] ips = str.split(delim);
		for (int i = 0; i < ips.length; i++) {
			String ip = ips[i];
			if (StringUtil.isEmpty(ip)) {
				continue;
			}
			if (i == ips.length - 1) {
				bf.append("'" + ip + "'");
			}
			else {
				bf.append("'" + ip + "',");
			}
		}
		return bf.toString();

	}

	public static void main(String[] args) {
		String[] ss = new String[] { "aa", "ss", "qq" };
		String dd = array2String(ss);
		System.out.println(dd);

	}

	public static String getFixSpace(int len) {
		String tmp = "";
		len = len < 0 ? 0 : len;
		for (int i = 0; i < len; i++) {
			tmp += "&nbsp";
		}
		return tmp;
	}

	public static String toFixLength(String str, int len) {
		String tmpStr;
		len = len < 0 ? 0 : len;
		tmpStr = str == null ? "" : str.trim();
		return (tmpStr + getFixSpace(len - tmpStr.length()));
	}

	/**
	 * 杞崲鏁板瓧瀛楃涓蹭负Double
	 * 
	 * @param str
	 * @return
	 */
	public static Double getDouble(String str) {
		if (null == str || "".equals(str.trim())) {
			return null;
		}
		else {
			return Double.valueOf(str);
		}
	}

	public static String list2String(List element) {
		return list2String(element, ",");
	}

	public static String list2String(List element, String delim) {

		String str = "";
		if (element == null || element.size() == 0)
			return str;

		for (int i = 0; i < element.size(); i++) {
			str = i == 0 ? element.get(i) + "" : str + delim + element.get(i);
		}
		return str;
	}

	public static String array2String(String[] strs, char delim) {
		if (strs == null) {
			return "";
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < strs.length; i++) {
			buf.append(i == 0 ? strs[i] : delim + strs[i]);
		}
		return buf.toString();
	}

	public static String array2String(String[] strs) {
		return array2String(strs, ';');
	}

	public static String[] listToStrArray(List element) {

		if (element == null || element.size() == 0)
			return null;

		Iterator it = element.iterator();
		String[] strArray = new String[element.size()];
		int i = 0;

		while (it.hasNext()) {
			strArray[i] = String.valueOf(it.next());
			i++;
		}
		return strArray;
	}

	public static String leftInclude(String str) {
		if (str == null || str.equals(""))
			return str;
		return str.trim() + "%";
	}

	public static String rightInclude(String str) {
		if (str == null || str.equals(""))
			return str;
		return "%" + str.trim();
	}

	public static String include(String str) {
		if (str == null || str.equals(""))
			return str;
		return "%" + str.trim() + "%";
	}

	/**
	 * 遍历HashMap
	 * 
	 * @param map
	 */
	public static String getHashMapContent(Map map) {
		StringBuffer result = new StringBuffer();
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			result.append(key).append(":").append(val).append("\n");
		}
		return result.toString();
	}

	/**
	 * 给定一个日期型字符串，返回加减n天后的日期型字符串
	 * 
	 * @param basicDate
	 * @param n
	 * @return
	 */
	public static String nDaysAfterOneDateString(String basicDate, int n) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date tmpDate = null;
		try {
			tmpDate = df.parse(basicDate);
		}
		catch (Exception e) {
			// 日期型字符串格式错误
		}
		long nDay = (tmpDate.getTime() / (24 * 60 * 60 * 1000) + 1 + n) * (24 * 60 * 60 * 1000);
		tmpDate.setTime(nDay);

		return df.format(tmpDate);
	}

	/**
	 * 获取网页内容
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String getURL(String url) {
		String s = "";
		try {
			URL myurl = new URL(url);
			URLConnection uc = myurl.openConnection();
			// uc
			// .addRequestProperty("User-Agent",
			// "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.1.8) Gecko/20100202 Firefox/3.5.8 GTB6");
			// uc.addRequestProperty("Accept",
			// "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			//
			// uc.addRequestProperty("Accept-Language", "en-us,en;q=0.5");
			// // conn.addRequestProperty("Accept-Encoding", "gzip,deflate");
			// uc.addRequestProperty("Accept-Charset",
			// "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
			// uc.addRequestProperty("Keep-Alive", "300");
			// uc.addRequestProperty("Proxy-Connection", "keep-alive");

			uc.setConnectTimeout(TIME_OUT);
			uc.setReadTimeout(TIME_OUT);
			DataInputStream dis = new DataInputStream(uc.getInputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(dis, "GBK"));
			String temp = "";
			while ((temp = br.readLine()) != null) {
				s += temp;
			}
		}
		catch (Exception e) {
			logger.info("获取URL出错[" + url + "]");
			// logger.error("获取URL出错[" + url + "]", e);
		}
		return s;
	}

	/*
	 * 可设置超时时间，单位秒
	 */
	public static String getURL(String url, int timeOut) {
		String s = "";
		try {
			logger.info("request url: " + url);
			URL myurl = new URL(url);
			URLConnection uc = myurl.openConnection();
			uc.setConnectTimeout(timeOut * 1000);
			uc.setReadTimeout(timeOut * 1000);
			DataInputStream dis = new DataInputStream(uc.getInputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			String temp = "";
			while ((temp = br.readLine()) != null) {
				s += temp;
			}
		}
		catch (Exception e) {
			logger.error("获取URL出错[" + url + "]", e);
		}
		return s;
	}

	/*
	 * 可设置超时时间，单位秒；可设置重试次数，默认3
	 */
	public static String getURL(String url, int timeOut, int retry) {
		String s = "";
		for (int i = 0; i < retry; i++) {
			s = getURL(url, timeOut);
			if (s.length() > 0) {
				break;
			}
		}
		return s;
	}

	/**
	 * 转码：utf-8转gbk
	 * 
	 * @param s
	 * @return
	 */
	public static String ex_ch(String s) {
		String temp = "";
		try {
			temp = new String(s.getBytes("utf-8"), "GBK");
		}
		catch (Exception e) {
			logger.error("", e);
		}
		return temp;
	}

	/**
	 * 转码：gbk转utf-8
	 * 
	 * @param s
	 * @return
	 */
	public static String ex_en(String s) {
		String temp = "";
		try {
			temp = new String(s.getBytes("GBK"), "utf-8");
		}
		catch (Exception e) {
			logger.error("", e);
		}
		return temp;
	}

	/**
	 * 判断ip
	 */
	public static boolean isIp(String param) {
		boolean flag = false;
		if (param.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 将长文本进行分割
	 * 
	 * @param text
	 * @param limit
	 * @param delim
	 * @return
	 */
	public static String splitByLongText(String text, int limit, String delim) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			buf.append(text.charAt(i));
			if ((i + 1) % limit == 0) {
				buf.append(delim);
			}
		}
		return buf.toString();
	}

	/**
	 * 将长文本进行分割
	 * 
	 * @param text
	 * @param limit
	 * @param delim
	 * @return
	 */
	public static String excelShell(String text, int limit, String delim) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			buf.append(text.charAt(i));
			if ((i + 1) % limit == 0) {
				buf.append(delim);
			}
		}
		return buf.toString();
	}

	/**
	 * 判断是是否linux环境
	 */
	public static boolean isLinux() {
		if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") == -1) {
			return true;
		}
		return false;
	}

	/**
	 * 获取本地Ip
	 */
	public static String getInetAddress() {

		try {
			InetAddress inet = InetAddress.getLocalHost();
			return inet.getHostAddress();
		}
		catch (UnknownHostException e) {
			logger.error("",e);
			return "";
		}
	}

	/**
	 * 从map获取字符串
	 */
	public static String getMap(Map map, String str, String dv) {
		if (str == null)
			return dv;
		str = str.trim();
		if (str.length() == 0)
			return dv;
		if (map == null)
			return dv;
		if (map.containsKey(str) == false)
			return dv;
		if (map.containsKey(str) == false)
			return dv;
		String s = "" + map.get(str);
		s = s.trim();
		if (s.length() == 0) {
			return dv;
		}
		return s;
	}

	public static String getMap(Map map, String str) {
		return getMap(map, str, "");
	}

	/**
	 * 从map获取整型
	 */
	public static int getMapInt(Map map, String str, int dv) {
		int value = 0;
		if (str == null)
			return dv;
		str = str.trim();
		if (str.length() == 0)
			return dv;
		if (map == null)
			return dv;
		if (map.containsKey(str) == false)
			return dv;
		String s = "" + map.get(str);
		s = s.trim();
		if (s.length() == 0)
			return dv;
		try {
			value = Integer.parseInt(s);
		}
		catch (Exception e) {
			return dv;
		}
		return value;
	}

	public static int getMapInt(Map map, String str) {
		return getMapInt(map, str, -1);
	}

	public static String getFontPath() {
		String path = getMap(getEnv(), "JAVA_HOME", "/usr/java/jdk1.6.0_20")
				+ "/jre/lib/fonts/SIMSUN.TTC";
		if (StringUtil.isLinux() == false) {// 如果是本地测试则改路径
			path = "C:/Windows/Fonts/SIMSUN.TTC";
		}
		logger.info("fontPath=" + path);
		return path;
	}

	public static String getCustomProperties(String key) {
		return getMap(getEnv(), key);
	}

	public static Map getEnv() {
		Map map = new HashMap();
		Process p = null;
		Runtime r = Runtime.getRuntime();
		String OS = System.getProperty("os.name").toLowerCase();
		try {
			if (OS.indexOf("windows 9") > -1) {
				p = r.exec("command.com /c set");
			}
			else if ((OS.indexOf("nt") > -1) || (OS.indexOf("windows 20") > -1)
					|| (OS.indexOf("windows xp") > -1)) {
				p = r.exec("cmd.exe /c set");
			}
			else {
				// Unix
				p = r.exec("env");
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				String[] str = line.split("=");
				if (str.length < 2) {
					continue;
				}
				map.put(str[0], str[1]);
			}
		}
		catch (Exception e) {
			logger.error("", e);
		}
		return map;
	}

	/**
	 * 根据url获取频道
	 * 
	 * @param url
	 * @return
	 */
	public static String getChannel(String url) {
		String channel = "";
		int startIndex = url.indexOf("http://");
		if (startIndex != -1) {
			channel = url.substring(7);
		}
		else {
			channel = url;
		}
		int endIndex = channel.indexOf("/");
		if (endIndex != -1) {
			channel = channel.substring(0, endIndex);
		}
		return channel;
	}

	/**
	 * 获取执行shell的结果
	 * 
	 * @param cmd
	 * @return
	 */
	public static String runShell4Result(String cmd) {
		logger.info(cmd);
		if (!isLinux()) {
			return "";
		}
		StringBuffer buf = new StringBuffer();
		Process p = null;
		BufferedReader in = null;
		InputStreamReader isr = null;
		InputStream is = null;

		try {
			p = Runtime.getRuntime().exec(cmd);
			is = p.getInputStream();
			isr = new InputStreamReader(is);
			in = new BufferedReader(isr);
			String line = null;
			while ((is.read()) != -1) {
				int all = is.available();
				byte[] b = new byte[all];
				is.read(b);
				line = new String(b, "UTF-8");
				buf.append(line).append("\n");
			}
			p.waitFor();

		}
		catch (Exception e) {
			logger.error("", e);
			buf.append("run shell error");

		}
		finally {
			try {

				if (is != null)
					is.close();

				if (isr != null)
					isr.close();

				if (in != null)
					in.close();

				if (p != null)
					p.destroy();

			}
			catch (Exception e) {
			}
		}
		logger.info(buf.toString());
		return buf.toString();

	}

	public static String date2String(Date date, String formatString) {
		SimpleDateFormat df = new SimpleDateFormat(formatString);
		return df.format(date);
	}

	/**
	 * 将对象转换成js数组，判断对象是否集合
	 * 
	 * @param object
	 * @return
	 */
	public static String Object2JsArray(Object object) {
		StringBuilder arrayStr = new StringBuilder();
		arrayStr.append("[");
		if (object instanceof Collection) {
			for (Object each : (Collection) object) {
				arrayStr.append("\"").append(String.valueOf(each)).append("\", ");
			}
		}
		else if (object instanceof Object[]) {
			for (Object each : (Object[]) object) {
				arrayStr.append("\"").append(String.valueOf(each)).append("\", ");
			}
		}
		else if (object != null) {
			arrayStr.append("\"").append(String.valueOf(object)).append("\"");
		}
		else {
			arrayStr.append("\"\"");
		}
		arrayStr.append("]");
		return arrayStr.toString();
	}

	public static Set array2Set(Object[] array) {
		if (array == null) {
			return null;
		}
		Set set = new LinkedHashSet();
		for (Object obj : array) {
			set.add(obj);
		}
		return set;
	}

	public static Set string2Set(String str) {
		return string2Set(str, ';');
	}

	public static Set string2Set(String str, char deli) {
		if (str == null) {
			return null;
		}
		Set set = new LinkedHashSet();
		for (String s : str.split(String.valueOf(deli))) {
			set.add(s);
		}
		return set;
	}
	
	public static String encoderUTF8(String s){
		
		return s.replaceAll("&", "&amp;");
	}
	
	public static int[] splitToInt(String s, String deli) {
		if(s == null || s.trim().length() == 0)
			return null;
		String[] arr = StringUtils.split(s, deli);
		int[] iarr = new int[arr == null ? 0 : arr.length];
		for(int i=0;arr != null && i<arr.length;i++) {
			try {
				iarr[i] = Integer.parseInt(arr[i]);
			} catch(Exception e){}
		}
		return iarr;
	}
}
