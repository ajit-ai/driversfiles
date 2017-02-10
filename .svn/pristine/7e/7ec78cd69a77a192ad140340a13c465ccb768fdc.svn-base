package com.driversfiles.www.spring;

import com.driversfiles.www.core.data.Country;
import com.driversfiles.www.core.data.Month;
import com.driversfiles.www.core.data.State;
import org.springframework.validation.Errors;

/**
 * This class provides additional validation methods on top of those provided by Spring.
 * 
 * @author Erik R. Jensen
 */
public class ValidationUtils extends org.springframework.validation.ValidationUtils {

	public static void rejectInvalidState(String stateCode, Errors errors, String field, String errorCode,
			Object[] errorArgs) {
		if (!State.isValid(stateCode)) {
			errors.rejectValue(field, errorCode, errorArgs, null);
		}
	}

	public static void rejectInvalidState(String stateCode, Errors errors, String field, String errorCode) {
		rejectInvalidState(stateCode, errors, field, errorCode, null);
	}

	public static void rejectInvalidCountry(String countryCode, Errors errors, String field, String errorCode,
			Object[] errorArgs) {
		if (!Country.isValid(countryCode)) {
			errors.rejectValue(field, errorCode, errorArgs, null);
		}
	}

	public static void rejectInvalidCountry(String countryCode, Errors errors, String field, String errorCode) {
		rejectInvalidCountry(countryCode, errors, field, errorCode, null);
	}

	public static void rejectInvalidMonth(String month, Errors errors, String field, String errorCode,
			Object[] errorArgs) {
		if (!Month.isValid(month)) {
			errors.rejectValue(field, errorCode, errorArgs, null);
		}
	}

	public static void rejectInvalidMonth(String month, Errors errors, String field, String errorCode) {
		rejectInvalidMonth(month, errors, field, errorCode, null);
	}
	
	public static void rejectInvalidYear(Integer year, Errors errors, String field, String errorCode, Integer firstValid, Object[] errorArgs) {
		
		if (firstValid == null)
			firstValid = 1900;
		
		if (year == null || year < firstValid) {
			errors.rejectValue(field, errorCode, errorArgs, null);
		}
	}
}
