package swp.model;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 */
public class AuctionPaymentRequest implements Serializable {

    private URL host;
    private URI id;
    private String itemName;
    private Date endDate;
    private String buyer;
    private int price;

    public AuctionPaymentRequest(
            URL host,
            URI id,
            String itemName,
            Date endDate, 
            String buyer,
            int price) {
        this.id = id;
        this.host = host;
        this.itemName = itemName;
        this.endDate = endDate;
        this.buyer = buyer;
        this.price = price;
    }

    public Date getEndDate() {
        return endDate;
    }

    public URI getId() {
        return id;
    }
    
    public URL getHost(){
        return host;
    }

    public String getItemName() {
        return itemName;
    }

    public String getBuyer() {
        return buyer;
    }
    
    public int getPrice(){
        return price;
    }
    
    public String valueOf(){
        return buyer;
    }
}
