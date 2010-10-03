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

    private RemoteAuctionService remoteService = new XACTAuctionPaymentRequestService();
    private AuctionPaymentService paymentService = new LocalFileSystemAuctionPaymentService();

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return INSTANCE;
    }

    public Object clone() throws CloneNotSupportedException {
        super.clone();
        throw new CloneNotSupportedException();
    }

    public RemoteAuctionService getRemoteAuctionService() {
        return remoteService;
    }

    public AuctionPaymentService getPaymentService() {
        return paymentService;
    }
}

