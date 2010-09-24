package swp.web.exception;

public class ItemURLReferenceException extends Exception{
    
    public ItemURLReferenceException(){
        super();
    }
    
    public ItemURLReferenceException(Throwable cause) {
        super(cause);
    }

    public ItemURLReferenceException(String message) {
        super(message);
    }

    public ItemURLReferenceException(String message, Throwable cause) {
        super(message, cause);
    }

}
