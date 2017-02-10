package com.driversfiles.www.hibernate;

import com.netradius.hibernate.support.HashedFieldUserType;

/**
 * Custom hibernate type for passwords.
 * 
 * @author Erik R. Jensen
 */
public class PasswordFieldUserType extends HashedFieldUserType {

	public PasswordFieldUserType() {
		init("SHA-256", "SUN", 30, "utf-8");
	}

}
