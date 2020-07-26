package org.krytonspace.carrent.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.krytonspace.carrent.utils.ModelField;

import java.util.Date;

/**
 * Model class holding data for a client.
 */
public class ClientModel extends Model {
    @ModelField(name = "Last name")
    private String lastName;

    @ModelField(name = "First name")
    private String firstName;

    @ModelField(name = "Date of birth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date birthDate;

    @ModelField(name = "Address")
    private String address;

    @ModelField(name = "Postal code")
    private String postalCode;

    @ModelField(name = "City")
    private String city;

    @ModelField(name = "Licenses")
    private String licenses;

    @ModelField(name = "Mail address")
    private String emailAddress;

    @ModelField(name = "Phone number")
    private String phoneNumber;

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getLicenses() {
        return licenses;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLicenses(String licenses) {
        this.licenses = licenses;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
