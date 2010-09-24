package swp.web;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import dk.brics.jwig.AuthorizationRequiredException;
import dk.brics.jwig.BadRequestException;
import dk.brics.jwig.User;
import dk.brics.jwig.WebApp;
import dk.brics.xact.XML;

public abstract class DPayAbstractApp extends WebApp{
    
    public User getUser() {
        User user = super.getUser();
        if (user == null) {
            throw new AuthorizationRequiredException("pay");
        }
        return user;
    }
    
    protected XML getHtmlWrapper() {
        return XML.parseTemplate("<html>" +
                "<head>" +
                "<title>" +
                "<[TITLE]>" +
                "</title>" +
                "</head>" +
                "<body>" +
                "<[BODY]>" +
                "</body>" +
                "</html>");
    }
    
    protected URL convertURL(String value, String parameterName){
        if(value == null){
            throw new BadRequestException("You need to provide the request parameter " + parameterName);
        }
        try {
            return new URL(value);
        } catch (MalformedURLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    
    protected URI convertURI(String value, String parameterName) {
        if(value == null){
            throw new BadRequestException("You need to provide the request parameter " + parameterName);
        }
        try {
            return new URI(value);
        } catch (URISyntaxException e) {
            throw new BadRequestException(e.getMessage());
        }  
    }
    

}
