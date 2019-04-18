package org.academiadecodigo.javabank.command;

import org.academiadecodigo.javabank.persistence.model.Customer;

import javax.validation.constraints.*;

/**
 * The {@link Customer} data transfer object
 */
public class CustomerDto {

    private Integer id;

    @NotNull(message = "First Name is Mandatory")
    @NotBlank(message = "First Name is Mandatory")
    @Size(min=3, max=64, message = "Size Must be Between 3 and 64")
    private String firstName;

    @NotNull(message = "Last Name is Mandatory")
    @NotBlank(message = "Last Name is Mandatory")
    @Size(min=3, max=64, message = "Size Must be Between 3 and 64")
    private String lastName;

    @NotNull(message = "E-Mail is Mandatory")
    @NotBlank(message = "E-Mail is Mandatory")
    @Email
    private String email;

    @Pattern(regexp = "(9[1236][0-9]) ?([0-9]{3}) ?([0-9]{3})", message = "Phone has Invalid Characters")
    @Size(min=9, max=16, message = "Size Must be Between 9 and 16")
    private String phone;

    /**
     * Gets the id of the customer dto
     *
     * @return the customer dto id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the customer dto
     *
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the first name of the customer dto
     *
     * @return the customer dto first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the customer dto
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the customer dto
     *
     * @return the customer dto last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the customer dto
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the email of the customer dto
     *
     * @return the customer dto email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the customer dto email
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone of the customer dto
     *
     * @return the customer dto phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the customer dto phone
     *
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "CustomerDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
