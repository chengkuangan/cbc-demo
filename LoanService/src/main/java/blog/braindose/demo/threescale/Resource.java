package blog.braindose.demo.threescale;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import blog.braindose.demo.threescale.model.Loan;
import blog.braindose.demo.threescale.service.InputValidator;
import blog.braindose.demo.threescale.service.JsonMapper;
import blog.braindose.demo.threescale.service.TxnId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/loan")
public class Resource {

    private static Logger LOGGER = LoggerFactory.getLogger(Resource.class);

    @Inject
    JsonMapper mapper;

    @Inject
    TxnId txnId;

    @Inject
    InputValidator validator;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Loan loan) {
        LOGGER.info("Submitted Loan details: {}", mapper.str(loan));
        validator.validateEntity(loan);
        loan = randomLoanInfo(loan);
        LOGGER.info("Calculated Loan details: {}", mapper.str(loan));
        return Response.created(null).entity(loan).build();
    }

    private Loan randomLoanInfo(Loan loan) {
        Random ran = new Random();
        loan.id = txnId.id();
        loan.interest = ran.nextDouble(0.03, 0.10);
        int maxTenure = (int) (loan.creditRating * 35);
        loan.tenure = ran.nextInt(0, maxTenure);
        loan.tenure = maxTenure;
        return loan;
    }

}
