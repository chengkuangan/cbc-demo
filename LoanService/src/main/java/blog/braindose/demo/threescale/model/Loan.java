package blog.braindose.demo.threescale.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Loan {

    public String id;
    
    @NotNull(message = "Customer name cannot be null")
    public String customerName;

    @NotNull(message = "Casa account number cannot be null")
    public String casa;

    @Positive(message = "Loan amount must be greater than zero")
    public double amount;
    public double interest;
    public int tenure;
    @Positive(message = "Credit Rating must be greater than zero")
    @DecimalMin(value = "0.1", inclusive = true, message = "Credit Rating must be greater than 0.1" )
    @DecimalMax(value = "1.0", inclusive = true, message = "Credit Rating must be less than or equal 1.0" )
    public double creditRating;


    public Loan(){

    }

    public Loan(String customerName, String casa, double amount, double creditRating){

        this.customerName = customerName;
        this.casa = casa;
        this.amount = amount;
        this.creditRating = creditRating;

    }

    public Loan(String customerName, String casa, double amount){

        this.customerName = customerName;
        this.casa = casa;
        this.amount = amount;
    }

}
