/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp228finaltest2021;

/**
 *
 * @author faiaz
 */
public class Student {
    
    private final String studentID;
	private final String firstName;
	private final String lastName;
	private final String address;
        private final String city;
        private final String province;
	private final String postalCode;
	private final String phoneNumber;
	
	
	public Student( String studentID, String firstName, String lastName, String address, 
			String city, String postalCode, String province, String phoneNumber) {
		this.studentID = studentID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
                this.city = city;
		this.postalCode = postalCode;
		this.province = province;
		this.phoneNumber = phoneNumber;
		
	}

	public String getUserName() {
		return studentID;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getAddress() {
		return address;
	}
        public String getCity() {
		return city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getProvince() {
		return province;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	
	

}

