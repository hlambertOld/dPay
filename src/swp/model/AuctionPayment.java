package swp.model;


/**
 * Class representing a completed Payment.
 */

public class AuctionPayment {

    private String user;
    private PaymentKey id;

    public AuctionPayment(PaymentKey id, String user) {
        this.id = id;
        this.user = user;
    }

    public PaymentKey getId() {
        return id;
    }

    public String getUser() {
        return user;
    }
}
