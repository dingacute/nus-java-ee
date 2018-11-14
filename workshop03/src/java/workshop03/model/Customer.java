package workshop03.model;

import java.math.BigDecimal;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Customer {
    
    @Id @Column(name = "customer_id")
    private Integer customerId;
       
    private String name;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private String fax;  
    private String email;   
    
    @Column(name = "credit_limit")
    private Integer creditLimit;
    
    @ManyToOne
    @JoinColumn(name = "discount_code", 
            referencedColumnName = "discount_code")
    private DiscountCode discountCode;

    @OneToMany(mappedBy = "customer")
    private List<PurchaseOrder> purchaseOrders;
    
    public Integer getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAddressLine1() {
        return addressLine1;
    }
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }
    
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }
    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCreditLimit() {
        return creditLimit;
    }
    public void setCreditLimit(Integer creditLimit) {
        this.creditLimit = creditLimit;
    }
    
    public DiscountCode getDiscountCode() {
        return discountCode;
    }
    public void setDiscountCode(DiscountCode discountCode) {
        this.discountCode = discountCode;
    }

    public List<PurchaseOrder> getPurchaseOrders() {
        return purchaseOrders;
    }
    public void setPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }    
    
    public JsonObject toJson() {
        return (Json.createObjectBuilder()
                .add("customerId", customerId)
                .add("name", name)
                .add("addressline1", addressLine1)
                .add("addressline2", addressLine2)
                .add("city", city)
                .add("state", state)
                .add("zip", zip)
                .add("phone", phone)
                .add("fax", fax)
                .add("email", email)
                .add("discountCode", discountCode.getDicountCode().toString())
                .add("creditLimit", creditLimit)
                .build());
    }
}
