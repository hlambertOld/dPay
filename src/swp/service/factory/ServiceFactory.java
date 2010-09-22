package swp.service.factory;

import swp.service.AuctionPaymentRequestService;
import swp.service.XACTAuctionPaymentRequestService;

public class ServiceFactory {
    
    private static ServiceFactory INSTANCE = new ServiceFactory();

    private AuctionPaymentRequestService paymentRequestService = null;

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return INSTANCE;
    }

    public Object clone() throws CloneNotSupportedException {
        super.clone();
        throw new CloneNotSupportedException();
    }

    public AuctionPaymentRequestService getPaymentRequestService() {
        if (paymentRequestService == null) {
            paymentRequestService = new XACTAuctionPaymentRequestService();
        }
        return paymentRequestService;
    }
}

