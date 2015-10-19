package com.naivebayes.common.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.naivebayes.model.Document;

public class TextTokenizer {

	/**
	 * Preprocess the text by removing punctuation, duplicate spaces and
	 * lowercasing it.
	 * 
	 * @param text
	 * @return
	 */
	/*
	 * public static String preprocess(String text) { return
	 * text.replaceAll("\\p{P}", " ").replaceAll("\\s+", " ").replaceAll(",",
	 * " ").toLowerCase(Locale.getDefault()); }
	 */

	public static String preprocess(String text) {
		return text.replaceAll("\\p{P}", " ").replaceAll(",", " ")
				.toLowerCase(Locale.getDefault());
	}

	/**
	 * A simple method to extract the keywords from the text. For real world
	 * applications it is necessary to extract also keyword combinations.
	 * 
	 * @param text
	 * @return
	 */
	/*
	 * public static String[] extractKeywords(String text) { return
	 * text.split(" "); }
	 */

	public String[] extractKeywords1(String text) {

		StringBuffer str = new StringBuffer(text);
		String[] result = new String[6];
		StringBuffer EMPTY = new StringBuffer("");

		int count = 0;
		while (str.indexOf(",") > -1) {
			result[count] = str.substring(0, str.indexOf(","));
			String temp = str.toString();
			str = EMPTY;
			str = new StringBuffer(temp.substring(str.indexOf(",")));

		}
		return text.split(",");
	}

	public String[] extractKeywords(String text) {

		String[] test = text.split(",");
		return test;
	}
	
	public String[] extractKeywordsFast(String text) {

		
		char[] test_length = text.toCharArray();
		int length = 0;
		for(char c : test_length)
		{
			if(c==',') length++;
		}
		
		test_length = null;
		String[] test = new String[length];
		
		
		String result = text.intern();
		int index,count = 0;
		while((index = result.indexOf(","))>-1)
		{
			test[count++]=result.substring(0,index).intern();
			result =  result.substring(index+1).intern();
		}
		return test;
	}

	/**
	 * Counts the number of occurrences of the keywords inside the text.
	 * 
	 * @param keywordArray
	 * @return
	 */
	public Map<String, Integer> getKeywordCounts(String[] keywordArray) {
		Map<String, Integer> counts = new HashMap<>();

		// Map<String, int> map =
		// Arrays.asList(keywordArray).parallelStream().collect(Collectors.toMap(p
		// -> p, this.get(p)));

		Integer counter;
		long totalLength = keywordArray.length;

		for (int i = 1; i < totalLength; ++i) {
			counter = counts.get(keywordArray[i].intern());
			if (counter == null) {
				counter = 0;
			}
			counts.put(keywordArray[i].intern(), ++counter); // increase counter for the
													// keyword
		}

		return counts;
	}

	/**
	 * Counts the number of occurrences of the keywords inside the text.
	 * 
	 * @param keywordArray
	 * @return
	 */
	public static Map<String, Integer> getCounts(String[] keywordArray) {
		Map<String, Integer> counts = new HashMap<>();

		Integer counter;
		long totalLength = keywordArray.length;
		for (int i = 0; i < totalLength; ++i) {
			counter = counts.get(keywordArray[i]);
			if (counter == null) {
				counter = 0;
			}
			counts.put(keywordArray[i], ++counter); // increase counter for the
													// keyword
		}

		return counts;
	}

	/**
	 * Tokenizes the document and returns a Document Object.
	 * 
	 * @param text
	 * @return
	 */
	public Document tokenize(String text) {
		// String preprocessedText = preprocess(text);
		String[] keywordArray = this.extractKeywordsFast(text);

		Document doc = new Document();
		doc.tokens = this.getKeywordCounts(keywordArray);
		keywordArray = null;
		return doc;
	}
}
