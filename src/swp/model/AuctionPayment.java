package swp.model;

import java.net.URI;
import java.net.URL;

public class AuctionPayment {
    
    private URL host;
    private URI id;  
    private String user;
    
    public AuctionPayment(URL host, URI id, String user){
        this.host = host;
        this.id = id;
        this.user = user;
    }

    public URL getHost() {
        return host;
    }

    public URI getId() {
        return id;
    }

    public String getUser() {
        return user;
    }
    
    public int hashCode() {
        int result;
        result = 29 * (id.hashCode() + host.hashCode());
        return result;
    }
    
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof AuctionPayment)) {
            return false;
        }
        AuctionPayment that = (AuctionPayment) other;
        return this.host.equals(that.host) && this.id.equals(that.id);
    }
    


}
