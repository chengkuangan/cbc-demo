package blog.braindose.demo.threescale.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JsonMapper {
    
    //private static final Logger LOGGER = LoggerFactory.getLogger(JsonMapper.class);
    
    private ObjectMapper mapper;

    public JsonMapper(){
        mapper = new ObjectMapper();
    }
    
    public String str(Object o){
        
        String jstr = null;
        //LOGGER.debug("Converting object to JSON String");
        try {
            jstr = mapper.writeValueAsString(o);
            //LOGGER.debug("JSON String: {}", jstr);
        } catch (JsonProcessingException e) {
            
        }
        
        return jstr;
    }
}
