package blog.braindose.demo.threescale.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Loan {

    @NotNull(message = "Customer first name cannot be null")
    private String firstName;

    @NotNull(message = "Customer last name cannot be null")
    private String lastName;

    @NotNull(message = "Casa account number cannot be null")
    private String casa;

    @Positive(message = "Loan amount must be greater than zero")
    private double amount;
    
    public String customerName;

    public double creditRating;

    public Loan(){

    }

    public Loan(String firstName, String lastName, String casa, double amount){

        this.firstName = firstName;
        this.lastName = lastName;
        this.casa = casa;
        this.amount = amount;

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCasa() {
        return casa;
    }

    public void setCasa(String casa) {
        this.casa = casa;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(double creditRating) {
        this.creditRating = creditRating;
    }

    

}
