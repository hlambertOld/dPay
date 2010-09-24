package swp.service.factory;

import swp.service.AuctionPaymentService;
import swp.service.LocalFileSystemAuctionPaymentService;
import swp.service.RemoteAuctionService;
import swp.service.XACTAuctionPaymentRequestService;

/**
 * Thread-safe Singleton
 */

public class ServiceFactory {

    private static ServiceFactory INSTANCE = new ServiceFactory();

    private RemoteAuctionService remoteService = null;
    private AuctionPaymentService paymentService = null;

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return INSTANCE;
    }

    public Object clone() throws CloneNotSupportedException {
        super.clone();
        throw new CloneNotSupportedException();
    }

    public synchronized RemoteAuctionService getRemoteAuctionService() {
        if (remoteService == null) {
            remoteService = new XACTAuctionPaymentRequestService();
        }
        return remoteService;
    }

    public synchronized AuctionPaymentService getPaymentService() {
        if (paymentService == null) {
            paymentService = new LocalFileSystemAuctionPaymentService();
        }
        return paymentService;
    }
}

