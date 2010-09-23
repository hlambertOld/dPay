package swp.web.exception;

public class AuctionPaymentExistException extends Exception{
    
    public AuctionPaymentExistException(){
        super();
    }
    
    public AuctionPaymentExistException(Throwable cause) {
        super(cause);
    }

    public AuctionPaymentExistException(String message) {
        super(message);
    }

    public AuctionPaymentExistException(String message, Throwable cause) {
        super(message, cause);
    }

}
