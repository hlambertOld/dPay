package swp.model;

import java.net.URI;
import java.net.URL;

/**
 * Identifier for a given auction
 */
public class PaymentKey {

    private URL host;
    private URI itemId;

    public PaymentKey(URL host, URI itemId) {
        this.host = host;
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof PaymentKey)) {
            return false;
        }
        PaymentKey that = (PaymentKey) other;
        return this.host.equals(that.host) && this.itemId.equals(that.itemId);
    }

    @Override
    public int hashCode() {
        int result;
        result = 29 * (host.hashCode() + itemId.hashCode());
        return result;
    }

    public URL getHost() {
        return host;
    }

    public URI getItemId() {
        return itemId;
    }
}
