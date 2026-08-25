package com.driversfiles.www.core.data;

import java.util.Random;

/**
 * Generates a unique 6 character code.
 * 
 * @author Ajit Kumar
 */
public class AccessCode {
	
    private static Random random = new Random();

    public static String generateCode() {

    	StringBuffer buf = new StringBuffer();
    	
   		String str = Integer.toString(random.nextInt() & 0xffffff, 16);
		
		while (str.length() < 6)
			str = "0" + str;
		
		buf.append(str);
    	
    	return buf.toString().toUpperCase();
    }


}
