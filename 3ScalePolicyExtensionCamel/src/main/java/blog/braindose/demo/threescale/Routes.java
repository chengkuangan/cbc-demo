package blog.braindose.demo.threescale;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import blog.braindose.demo.threescale.model.Loan;
import blog.braindose.demo.threescale.service.JsonMapper;


public class Routes extends RouteBuilder {

    private static Logger LOGGER = LoggerFactory.getLogger(Routes.class);

    JsonMapper mapper = new JsonMapper();
    // @ConfigProperty(name = "quarkus.http.port")
    // String port;

    @Override
    public void configure() throws Exception {
        
        final RouteDefinition from;

        // LOGGER.info("http port = {}", port);

        // port = (port == null || port.equals("")) ? "8080" : port;

        if (Files.exists(keystorePath())) {
            from = from("netty-http:proxy://0.0.0.0:8443?ssl=true&keyStoreFile=/tls/keystore.jks&passphrase=changeit&trustStoreFile=/tls/truststore.jks");
        } else {
            from = from("netty-http:proxy://0.0.0.0:8080");
        }

        from
            .log("Message received : ${body}")
            .process(Routes::enrich)
            .toD("netty-http:"
                + "${headers." + Exchange.HTTP_SCHEME + "}://" 
                + "${headers." + Exchange.HTTP_HOST + "}:"
                + "${headers." + Exchange.HTTP_PORT + "}"
                + "${headers." + Exchange.HTTP_PATH + "}")
            ;
        
    }

    Path keystorePath() {
        return Path.of("/tls", "keystore.jks");
    }

    public static void enrich(final Exchange exchange) { 
        Random ran = new Random();
        final Message message = exchange.getIn();
        Loan loan = message.getBody(Loan.class);
        loan.setCustomerName(loan.getFirstName() + "" + loan.getLastName());
        loan.setFirstName(null);
        loan.setLastName(null);
        loan.setCreditRating(ran.nextDouble(0.1, 1.00));
        message.setBody(loan);
    }

}
