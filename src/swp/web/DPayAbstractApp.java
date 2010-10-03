package swp.web;

import dk.brics.jwig.AuthorizationRequiredException;
import dk.brics.jwig.BadRequestException;
import dk.brics.jwig.User;
import dk.brics.jwig.WebApp;
import dk.brics.xact.XML;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * An extension of the WebApp.
 * This class will be extended by all other WebApp's in the dPay service
 */

public abstract class DPayAbstractApp extends WebApp {

    public User getUser() {
        User user = super.getUser();
        if (user == null) {
            throw new AuthorizationRequiredException("pay");
        }
        return user;
    }

    /**
     * @return a simple HTML-template with a title and body gap
     */

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

    /**
     * Checking and converting a string to an URL
     *
     * @param value         the value to convert
     * @param parameterName the name of the value
     * @return an URL object corresponding to the value
     * @throws BadRequestException if the value is null or the value is malformed URL
     */

    protected URL convertURL(String value, String parameterName) {
        if (value == null) {
            throw new BadRequestException("You need to provide the request parameter " + parameterName);
        }
        try {
            if (!value.endsWith("/")) {
                value += "/";
            }
            return new URL(value);
        } catch (MalformedURLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    /**
     * Checking and converting a string to an URL
     *
     * @param value         the value to convert
     * @param parameterName the name of the value
     * @return an URL object corresponding to the value
     * @throws BadRequestException if the value is null or the value is malformed URI
     */

    protected String check(String value, String parameterName) {
        if (value == null) {
            throw new BadRequestException("You need to provide the request parameter " + parameterName);
        }
        return value;
    }


}
