package si.eugo.stork;

import java.util.HashMap;

import eu.stork.peps.auth.commons.PersonalAttribute;

public class StorkUser {
	private String username;
	private String givenName;
	private String surname;
	private String eId;
	private String dateOfBirth;
	HashMap<String, PersonalAttribute> storkAttrs;

	public HashMap<String, PersonalAttribute> getStorkAttrs() {
		return storkAttrs;
	}

	public void setStorkAttrs(HashMap<String, PersonalAttribute> storkAttrs) {
		this.storkAttrs = storkAttrs;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String geteId() {
		return eId;
	}

	public void seteId(String eId) {
		this.eId = eId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public StorkUser(String username) {
		this.username = username;
	}
	
	
}
