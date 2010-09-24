package swp.web.exception;

public class AuctionNotExpiredException extends Exception{
    
    public AuctionNotExpiredException(){
        super();
    }
    
    public AuctionNotExpiredException(Throwable cause) {
        super(cause);
    }

    public AuctionNotExpiredException(String message) {
        super(message);
    }

    public AuctionNotExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

}
