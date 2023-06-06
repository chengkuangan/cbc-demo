package blog.braindose.demo.threescale.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Generate unique transaction id base on the transaction type
 */
@ApplicationScoped
public class TxnId {

    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
    
    /**
     * Generate unique transaction id in the format of TxnType-yyyyMMdd-HHmmssSSS-RNDNUM
     * @param type Transaction type. @see blog.braindose.paygate.util.TxnTypes
     * @return unique transaction id in String format.
     */
    public String id(){
        String gs = uniqueNumber();
        String ds = df.format(new Date());
        return ds + "-" + gs;
    }

    /**
     * Create 5 digit random number. 
     * Quarkus seems to not support/prefer Random
     * @return 5 digit random number
     */
    private String uniqueNumber(){
        String n = "";
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i=1; i<10; i++) {
            list.add(Integer.valueOf(i));
        }
        Collections.shuffle(list);
        for (int i=0; i<5; i++) {
            n += "" + list.get(i);
        }
        return n;
    }
    
}
