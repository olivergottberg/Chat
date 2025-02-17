package factories;

import communication.UDPChatCommunicator;
import interfaces.ICommunicationProtocol;
import interfaces.ICommunicationProtocolFactory;
import observer.IObserver;

/**
 * Factory for creating UDP communicator
 *
 * @author olivergottberg
 */
public class UDPProtocolFactory implements ICommunicationProtocolFactory {

    private IObserver observer;

    public UDPProtocolFactory(IObserver observer) {
        this.observer = observer;
    }

    /**
     * Creating the communication protocol
     * @return the communication protocol
     */
    @Override
    public ICommunicationProtocol createProtocol() {
        UDPChatCommunicator communicator = new UDPChatCommunicator();
        communicator.addObserver(observer);
        return communicator;
    }
}
