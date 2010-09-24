package swp.model;


public class BasicAuctionDetails {
   
    private PaymentKey id;
    private String itemName;
    private int price;

    public BasicAuctionDetails(PaymentKey id, String itemName, int price) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
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
