package swp.model;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class AuctionPaymentRequest implements Serializable {

    private PaymentKey id;
    private String itemName;
    private Date endDate;
    private String buyer;
    private int price;

    public AuctionPaymentRequest(PaymentKey id, String buyer, String itemName, Date endDate, int price) {
        this.buyer = buyer;
        this.endDate = endDate;
        this.id = id;
        this.itemName = itemName;
        this.price = price;
    }

    public String getBuyer() {
        return buyer;
    }

    public Date getEndDate() {
        return endDate;
    }

    public PaymentKey getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public int getPrice() {
        return price;
    }
}
