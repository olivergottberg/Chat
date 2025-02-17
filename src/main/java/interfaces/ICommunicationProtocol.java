package interfaces;

/**
 * Interface for communication protocols
 *
 * @author olivergottberg
 */
public interface ICommunicationProtocol {
    void sendChat(String sender, String message) throws Exception;
    void startListen();
    void stopListen() throws Exception;

}
