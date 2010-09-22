package swp.web.exception;

public class AuctionDoesNotExistException extends Exception{
    
    public AuctionDoesNotExistException(){
        super();
    }
    
    public AuctionDoesNotExistException(Throwable cause) {
        super(cause);
    }

    public AuctionDoesNotExistException(String message) {
        super(message);
    }

    public AuctionDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

}
