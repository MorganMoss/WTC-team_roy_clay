package za.co.wethinkcode.server;

public class BadConfigurationException extends RuntimeException {
    public BadConfigurationException(String message) {
        super(message);
    }
}
