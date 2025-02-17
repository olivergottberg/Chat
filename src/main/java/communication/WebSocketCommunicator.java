package communication;

import interfaces.ICommunicationProtocol;
import io.socket.client.IO;
import io.socket.client.Socket;
import observer.IObserver;
import observer.ISubject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * The communicator handles the network traffic between all chat clients.
 * Messages are sent and received via the WebSocket.
 * 
 * @author Martin Goblirsch
 * @author olivergottberg
 */
public class WebSocketCommunicator implements ICommunicationProtocol, ISubject {

    private final String WEB_SOCKET_ADDRESS = "https://work-58y8.onrender.com/";
    private Socket IOSocket;
    private List<IObserver> observers = new ArrayList<>();

    public WebSocketCommunicator() {

    }

    /**
     * Send the chat message to all clients.
     *
     * @param sender Name of the sender.
     * @param message Text message to send.
     * @throws java.lang.Exception
     */
    public void sendChat(String sender, String message) throws Exception {

        try {
            this.IOSocket.emit("message", sender + ": " + message);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Start to listen for messages from other clients.
     */
    public void startListen() {
        try {
            this.listenForMessages();
        } catch (Exception e) {
            sendObserverErrorMessage(e);
        }
    }
    
    public void stopListen() throws Exception {
        IOSocket.disconnect();
        IOSocket.close();
    }

    /**
     * Listen for messages from other clients.
     */
    private void listenForMessages() throws Exception {
        try {
            IOSocket = IO.socket(WEB_SOCKET_ADDRESS);

            IOSocket.on("message", (final Object... args) -> {
                String receivedMessage = args[0].toString();
                notifyObservers(receivedMessage);
            });
            IOSocket.connect();

        } catch (URISyntaxException e) {
            throw e;
        }
    }

    /**
     * Add new observer to the list
     * @param observer Observer to be added
     */
    @Override
    public void addObserver(IObserver observer) {
        observers.add(observer);
    }

    /**
     * Remove observer from the list
     * @param observer Observer to be removed
     */
    @Override
    public void removeObserver(IObserver observer) {
        observers.remove(observer);
    }

    /**
     * Update observers with new messages
     * @param message The message to transmit
     */
    @Override
    public void notifyObservers(String message) {
        for (IObserver observer : observers) {
            observer.update(message);
        }
    }

    /**
     * Update observers with error
     * @param e The error to transmit
     */
    @Override
    public void sendObserverErrorMessage (Exception e) {
        for (IObserver observer : observers) {
            observer.receiveError(e);
        }
    }
}
