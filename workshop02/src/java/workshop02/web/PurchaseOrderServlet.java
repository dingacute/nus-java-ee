package workshop02.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import workshop02.model.PurchaseOrder;

//This program is to implement searching purchase order by customer id by NamedQuery
@WebServlet (urlPatterns = "/purchaseOrder")
public class PurchaseOrderServlet extends HttpServlet{
    
    //Get a reference to Entity Manager
    @PersistenceContext private EntityManager em;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        Integer custId = Integer.parseInt(req.getParameter("custId"));
        
        TypedQuery<PurchaseOrder> query = em.createNamedQuery(
                "PurchaseOrder.findByCustomerId", PurchaseOrder.class);
        //Set the parameter :custId
        query.setParameter("custId", custId);
        
        List<PurchaseOrder> result = query.getResultList();
        //LinkedHashMap sample
        LinkedHashMap<Integer, PurchaseOrder> map = new LinkedHashMap<>();
        for (PurchaseOrder ord : result) {
            map.put(ord.getOrderNum(), ord);
        }
        
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (PurchaseOrder po: result) {
            builder.add(po.toJson());
        }
        
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(MediaType.APPLICATION_JSON);
        try (PrintWriter pw = resp.getWriter()) {
            pw.print(builder.build().toString());
        }
        
    }
    
    
}
