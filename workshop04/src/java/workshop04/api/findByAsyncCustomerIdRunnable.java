package workshop04.api;

import java.sql.SQLException;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import workshop04.business.CustomerBean;
import workshop04.model.Customer;


public class findByAsyncCustomerIdRunnable implements Runnable {

    private Integer custId;
    private CustomerBean customerBean;
    private AsyncResponse asyncResponse;
    
    public findByAsyncCustomerIdRunnable(Integer cid,
            CustomerBean cb,
            AsyncResponse ar) {
        custId = cid;
        customerBean = cb;
        asyncResponse = ar;
    }

    @Override
    public void run() {
        System.out.println(">>> running findByAsyncCustomerIdRunnable");
        Optional<Customer> opt = null;
        
        try {
            opt = customerBean.findByCustomerId(custId);
        } catch (SQLException ex) {
            JsonObject error = Json.createObjectBuilder()
                    .add("error", ex.getMessage())
                    .build();
            
            // Server Error 500
            asyncResponse.resume(Response.serverError().entity(error).build());
        }
        
        if (!opt.isPresent()) {
            asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
        }
        
        //return the data as Json object
        asyncResponse.resume(Response.ok(opt.get().toJson()).build());        
    }
    
    
}
