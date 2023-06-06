package blog.braindose.demo.threescale;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Random;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.netty.http.NettyHttpMessage;
import org.apache.camel.model.RouteDefinition;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import blog.braindose.demo.threescale.model.Loan;
import blog.braindose.demo.threescale.service.JsonMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import jakarta.ws.rs.InternalServerErrorException;

public class Routes extends RouteBuilder {

    private static Logger LOGGER = LoggerFactory.getLogger(Routes.class);

    private JsonMapper mapper = new JsonMapper();
    // @ConfigProperty(name = "quarkus.http.port")
    // String port;

    @Override
    public void configure() throws Exception {

        final RouteDefinition from;

        if (Files.exists(keystorePath())) {
            from = from(
                    "netty-http:proxy://0.0.0.0:8443?ssl=true&keyStoreFile=/tls/keystore.jks&passphrase=changeit&trustStoreFile=/tls/truststore.jks");
        } else {
            from = from("netty-http:proxy://0.0.0.0:8080");
        }

        from
                .log("Message received : ${body}")
                .process(this::enrich)
                .log("Enriched Body Message : ${body}")
                //.setHeader("CamelHttpMethod").simple("${headers." + Exchange.HTTP_METHOD + "}")
                //.setHeader("Content-Type").simple("${headers." + Exchange.CONTENT_TYPE + "}")
                .toD("netty-http:"
                        + "${headers." + Exchange.HTTP_SCHEME + "}://"
                        + "${headers." + Exchange.HTTP_HOST + "}:"
                        + "${headers." + Exchange.HTTP_PORT + "}"
                        + "${headers." + Exchange.HTTP_PATH + "}");

    }

    Path keystorePath() {
        return Path.of("/tls", "keystore.jks");
    }

    public void enrich(final Exchange exchange) throws StreamReadException, DatabindException, IOException {

        final NettyHttpMessage message = exchange.getIn(NettyHttpMessage.class);
        final FullHttpRequest request = message.getHttpRequest();
        final ByteBuf buf = request.content();
        final String string = buf.toString(StandardCharsets.UTF_8);

        LOGGER.debug("string = {}", string);

        ObjectMapper m = new ObjectMapper();
        Random ran = new Random();
        // Loan l1 = message.getBody(Loan.class); // ---- this also return null
        Loan loan = (Loan) m.readValue(string.getBytes(), Loan.class);

        if (loan != null) {
            LOGGER.debug("loan: {}", mapper.str(loan));
            loan.setCustomerName(loan.getFirstName() + " " + loan.getLastName());
            loan.setFirstName(null);
            loan.setLastName(null);
            loan.setCreditRating(ran.nextDouble(0.1, 1.00));
            LOGGER.debug("", mapper.str(loan));
        } else {
            LOGGER.debug("loan object is null");
            throw new InternalServerErrorException("The post body is empty or null. Expecting Loan details to be posted.");
        }

        // buf.resetWriterIndex();
        // ByteBufUtil.writeUtf8(buf, mapper.str(loan));
        message.setBody(mapper.str(loan).getBytes());   // need to set back into the body. 
    }

    /*
     * message.getBody() will not work and return null value
     * public void enrich(final Exchange exchange) {
     * Random ran = new Random();
     * final Message message = exchange.getIn();
     * byte[] body2 = (byte[]) message.getBody();
     * String body = null;
     * 
     * if (body2 != null){
     * body = new String(body2);
     * }
     * 
     * LOGGER.debug("body: {}", body);
     * Loan loan = message.getBody(Loan.class);
     * if (loan != null){
     * LOGGER.debug("loan: {}", mapper.str(loan));
     * loan.setCustomerName(loan.getFirstName() + "" + loan.getLastName());
     * loan.setFirstName(null);
     * loan.setLastName(null);
     * loan.setCreditRating(ran.nextDouble(0.1, 1.00));
     * message.setBody(loan);
     * }
     * else{
     * LOGGER.debug("loan object is null.");
     * }
     * 
     * 
     * }
     */

}
