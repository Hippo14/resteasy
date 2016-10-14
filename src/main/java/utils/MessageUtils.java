package utils;

/**
 * Created by MSI on 2016-10-14.
 */
public class MessageUtils {

    public static String getMessage(String ex) {
        String message = ex.toString();
        if(message.contains(":"))
            message = message.substring(message.lastIndexOf(": "), message.length());
        return message;
    }

}
