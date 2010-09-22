package swp.service;

import java.net.URI;

import swp.model.AuctionItem;

public interface AuctionItemLoader {
    
    public AuctionItem load(URI id, String host);

}
