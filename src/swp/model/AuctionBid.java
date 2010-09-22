package swp.model;

import java.io.Serializable;

/**
 */
public class AuctionBid implements Serializable, Comparable<AuctionBid> {
    private String owner;
    private Integer price;

    public AuctionBid(String owner, Integer price) {
        this.owner = owner;
        this.price = price;
    }

    public String getOwner() {
        return owner;
    }

    public Integer getPrice() {
        return price;
    }

    @Override
    public int compareTo(AuctionBid other) {
        return this.price - other.price;
    }
}
