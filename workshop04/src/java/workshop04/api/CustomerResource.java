package workshop04.api;

import java.sql.SQLException;
import java.util.Optional;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import workshop04.business.CustomerBean;
import workshop04.model.Customer;

@RequestScoped
@Path("/customer")
public class CustomerResource {
    
    @EJB private CustomerBean customerBean;
    
    //a must for JAX_RS & Async database entrance
    //Note: please create threadpool in admin console
    @Resource(lookup = "concurrent/mythreadpool")
    private ManagedScheduledExecutorService threadPool;
    
    // GET /customer/1
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{custId}")
    public Response findByCustomerId(@PathParam("custId") Integer custId) {
        
        Optional<Customer> opt = null;
        
        try {
            opt = customerBean.findByCustomerId(custId);
        } catch (SQLException ex) {
            JsonObject error = Json.createObjectBuilder()
                    .add("error", ex.getMessage())
                    .build();
            
            // Server Error 500
            return Response.serverError().entity(error).build();
        }
        
        if (!opt.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        //return the data as Json object
        return Response.ok(opt.get().toJson()).build();
    }
    
    // GET /customer/async/1
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("async/{custId}")
    public void findByAsyncCustomerId(
            @PathParam("custId") Integer custId,
            @Suspended AsyncResponse asyncResp) {
        
        findByAsyncCustomerIdRunnable runnable = 
                new findByAsyncCustomerIdRunnable(custId, customerBean, asyncResp);
        
        //execute the runnable in the threadpool
        threadPool.execute(runnable);
        
        System.out.println(">>> exiting findByAsyncCustomerId");
    }
}
