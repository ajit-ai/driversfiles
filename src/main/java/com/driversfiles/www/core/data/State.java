package com.driversfiles.www.core.data;

/**
 * Holds state information.
 * 
 * @author Ajit Kumar
 */
public enum State {

	ALABAMA("Alabama", "AL", Country.UNITED_STATES),
	ALASKA("Alaska", "AK", Country.UNITED_STATES),
	ARIZONA("Arizona", "AZ", Country.UNITED_STATES),
	ARKANSAS("Arkansas", "AR", Country.UNITED_STATES),
	CALIFORNIA("California", "CA", Country.UNITED_STATES),
	COLORADO("Colorado", "CO", Country.UNITED_STATES),
	CONNECTICUT("Connecticut", "CT", Country.UNITED_STATES),
	DELAWARE("Delaware", "DE", Country.UNITED_STATES),
	DISTRICT_OF_COLUMBIA("District Of Columbia", "DC", Country.UNITED_STATES),
	FLORIDA("Florida", "FL", Country.UNITED_STATES),
	GEORGIA("Georgia", "GA", Country.UNITED_STATES),
	HAWAII("Hawaii", "HI", Country.UNITED_STATES),
	IDAHO("Idaho", "ID", Country.UNITED_STATES),
	ILLINOIS("Illinois", "IL", Country.UNITED_STATES),
	INDIANA("Indiana", "IN", Country.UNITED_STATES),
	IOWA("Iowa", "IA", Country.UNITED_STATES),
	KANSAS("Kansas", "KS", Country.UNITED_STATES),
	KENTUCKY("Kentucky", "KY", Country.UNITED_STATES),
	LOUISIANA("Louisiana", "LA", Country.UNITED_STATES),
	MAINE("Maine", "ME", Country.UNITED_STATES),
	MARYLAND("Maryland", "MD", Country.UNITED_STATES),
	MASSACHUSETTS("Massachusetts", "MA", Country.UNITED_STATES),
	MICHIGAN("Michigan", "MI", Country.UNITED_STATES),
	MINNESOTA("Minnesota", "MN", Country.UNITED_STATES),
	MISSISSIPPI("Mississippi", "MS", Country.UNITED_STATES),
	MISSOURI("Missouri", "MO", Country.UNITED_STATES),
	MONTANA("Montana", "MT", Country.UNITED_STATES),
	NEBRASKA("Nebraska", "NE", Country.UNITED_STATES),
	NEVADA("Nevada", "NV", Country.UNITED_STATES),
	NEW_HAMPSHIRE("New Hampshire", "NH", Country.UNITED_STATES),
	NEW_JERSEY("New Jersey", "NJ", Country.UNITED_STATES),
	NEW_MEXICO("New Mexico", "NM", Country.UNITED_STATES),
	NEW_YORK("New York", "NY", Country.UNITED_STATES),
	NORTH_CAROLINA("North Carolina", "NC", Country.UNITED_STATES),
	NORTH_DAKOTA("North Dakota", "ND", Country.UNITED_STATES),
	OHIO("Ohio", "OH", Country.UNITED_STATES),
	OKLAHOMA("Oklahoma", "OK", Country.UNITED_STATES),
	OREGON("Oregon", "OR", Country.UNITED_STATES),
	PENNSYLVANIA("Pennsylvania", "PA", Country.UNITED_STATES),
	RHODE_ISLAND("Rhode Island", "RI", Country.UNITED_STATES),
	SOUTH_CAROLINA("South Carolina", "SC", Country.UNITED_STATES),
	SOUTH_DAKOTA("South Dakota", "SD", Country.UNITED_STATES),
	TENNESSEE("Tennessee", "TN", Country.UNITED_STATES),
	TEXAS("Texas", "TX", Country.UNITED_STATES),
	UTAH("Utah", "UT", Country.UNITED_STATES),
	VERMONT("Vermont", "VT", Country.UNITED_STATES),
	VIRGINIA("Virginia", "VA", Country.UNITED_STATES),
	WASHINGTON("Washington", "WA", Country.UNITED_STATES),
	WEST_VIRGINIA("West Virginia", "WV", Country.UNITED_STATES),
	WISCONSIN("Wisconsin", "WI", Country.UNITED_STATES),
	WYOMING("Wyoming", "WY", Country.UNITED_STATES);
	private String name;
	private String code;
	private Country country;

	private State(String name, String code, Country country) {
		this.name = name;
		this.code = code;
		this.country = country;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public Country getCountry() {
		return country;
	}

	@Override
	public String toString() {
		return code;
	}

	public static boolean isValid(String code) {
		for (State s : values()) {
			if (s.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}

	public static State getStateByCode(String code) {
		for (State s : values()) {
			if (s.getCode().equalsIgnoreCase(code)) {
				return s;
			}
		}
		return null;
	}

	public static State getStateByName(String name) {
		for (State s : values()) {
			if (s.getName().equalsIgnoreCase(name)) {
				return s;
			}
		}
		return null;
	}

	public static State getState(String state) {
		State st = null;
		if (state != null) {
			if (state.trim().length() == 2) {
				st = getStateByCode(state);
			}
		}

		if (st == null) {
			st = getStateByName(state);
		}

		return st;
	}

}
