package swp.model;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 */
public class AuctionItem implements Serializable {

    private URI id;
    private String owner;
    private String name;
    private String description;
    private Integer minimumPrice;
    private Calendar endDate;
    private List<AuctionBid> bids = new ArrayList<AuctionBid>();

    public AuctionItem(
            URI id,
            String owner,
            String name,
            String description,
            Integer minimumPrice,
            Calendar endDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.minimumPrice = minimumPrice;
        this.endDate = endDate;
    }

    public void addBid(AuctionBid bid) {
        bids.add(bid);
    }

    public List<AuctionBid> getBids() {
        return bids;
    }
    
    public AuctionBid getWinnerBid(){
        return Collections.max(bids);
    }

    public String getDescription() {
        return description;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public URI getId() {
        return id;
    }

    public Integer getMinimumPrice() {
        return minimumPrice;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }
}
