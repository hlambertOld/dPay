package swp.model;


/**
 * Class representing a completed Payment.
 */

public class AuctionPayment {

    private String buyer;
    private PaymentKey id;

    public AuctionPayment(PaymentKey id, String user) {
        this.id = id;
        this.buyer = user;
    }

    public PaymentKey getId() {
        return id;
    }

    public String getBuyer() {
        return buyer;
    }
}
