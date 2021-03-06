package si.eugo.stork;

import eu.stork.peps.auth.commons.PersonalAttribute;

import java.util.Map;


public class StorkUser {
	private String username;
	private String givenName;
	private String surname;
	private String eId;
	private String dateOfBirth;
    private String countryCodeOfBirth;
    private String nationalityCode;
    private String gender;
    private String canonicalResidenceAddress;
    private String textResidenceAddress;
    
    private Map<String, PersonalAttribute> storkAttrs;

    public StorkUser() {
    }

    public StorkUser(String givenName, String surname, String dateOfBirth, String countryCodeOfBirth, String nationalityCode, String gender, String canonicalResidenceAddress, String textResidenceAddress) {
        setStuff(givenName, surname, dateOfBirth, countryCodeOfBirth, nationalityCode, gender, canonicalResidenceAddress, textResidenceAddress);
    }

    public void setStuff(String givenName, String surname, String dateOfBirth, String countryCodeOfBirth, String nationalityCode, String gender, String canonicalResidenceAddress, String textResidenceAddress) {
        this.setGivenName(givenName);
        this.setSurname(surname);
        this.setDateOfBirth(dateOfBirth);
        this.setCountryCodeOfBirth(countryCodeOfBirth);
        this.setNationalityCode(nationalityCode);
        this.setGender(gender);
        this.setCanonicalResidenceAddress(canonicalResidenceAddress);
        this.setTextResidenceAddress(textResidenceAddress);
    }

    public Map<String, PersonalAttribute> getStorkAttrs() {
		return storkAttrs;
	}

	public void setStorkAttrs(Map<String, PersonalAttribute> storkAttrs) {
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

    public String getCountryCodeOfBirth() {
        return countryCodeOfBirth;
    }

    public void setCountryCodeOfBirth(String countryCodeOfBirth) {
        this.countryCodeOfBirth = countryCodeOfBirth;
    }

    public String getNationalityCode() {
        return nationalityCode;
    }

    public void setNationalityCode(String nationalityCode) {
        this.nationalityCode = nationalityCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCanonicalResidenceAddress() {
        return canonicalResidenceAddress;
    }

    public void setCanonicalResidenceAddress(String canonicalResidenceAddress) {
        this.canonicalResidenceAddress = canonicalResidenceAddress;
    }

    public String getTextResidenceAddress() {
        return textResidenceAddress;
    }

    public void setTextResidenceAddress(String textResidenceAddress) {
        this.textResidenceAddress = textResidenceAddress;
    }

    @Override
    public String toString() {
        return String.format("StorkUser{username='%s', givenName='%s', surname='%s', eId='%s', dateOfBirth='%s', countryCodeOfBirth='%s', nationalityCode='%s', gender='%s', canonicalResidenceAddress='%s', textResidenceAddress='%s'}",
                username, givenName, surname, eId, dateOfBirth, countryCodeOfBirth, nationalityCode, gender, canonicalResidenceAddress, textResidenceAddress);
    }
}
