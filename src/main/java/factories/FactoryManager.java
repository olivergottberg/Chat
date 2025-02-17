package factories;

import interfaces.ICommunicationProtocolFactory;
import observer.IObserver;

/**
 * Factory manager class
 *
 * @author olivergottberg
 */
public class FactoryManager {

    /**
     * Creates factory based on requested type
     * @param protocolType The requested protocol type
     * @param observer observer
     * @return Factory
     */
    public static ICommunicationProtocolFactory getFactory (String protocolType, IObserver observer) {
        switch (protocolType) {
            case "UDP":
                return new UDPProtocolFactory(observer);
            case "WebSocket":
                return new WebSocketProtocolFactory(observer);
            default:
                throw new IllegalArgumentException("Unknown protocol");
        }
    }
}
