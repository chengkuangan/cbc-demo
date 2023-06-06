package blog.braindose.demo.threescale.service;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import blog.braindose.demo.threescale.model.Loan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.ws.rs.InternalServerErrorException;

@ApplicationScoped
public class InputValidator {

    Validator validator;

    public InputValidator(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(InputValidator.class);

    public void validateEntity(Loan p) {

        Set<ConstraintViolation<Loan>> violations = validator.validate(p);
        String messages = "";
        if (!violations.isEmpty()) {
            for (ConstraintViolation<Loan> violation : violations) {
                String m = violation.getMessage();
                LOGGER.debug("Validation failed: {}", m);
                messages += m + ", ";
            }
            throw new InternalServerErrorException(
                    violations.size() > 1 ? "Multiple data validation errors : " + messages
                            : "Data validation error : " + messages);
        }
    }
}
