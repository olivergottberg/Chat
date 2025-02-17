package factories;

import communication.WebSocketCommunicator;
import interfaces.ICommunicationProtocol;
import interfaces.ICommunicationProtocolFactory;
import observer.IObserver;

/**
 * Factory for creating WebSocket communicator
 *
 * @author olivergottberg
 */
public class WebSocketProtocolFactory implements ICommunicationProtocolFactory {

    private IObserver observer;

    public WebSocketProtocolFactory(IObserver observer) {
        this.observer = observer;
    }

    /**
     * Creating the communication protocol
     * @return the communication protocol
     */
    @Override
    public ICommunicationProtocol createProtocol() {
        WebSocketCommunicator communicator = new WebSocketCommunicator();
        communicator.addObserver(observer);
        return communicator;
    }
}
