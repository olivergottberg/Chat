package observer;

/**
 * Subject interface
 *
 * @author olivergottberg
 */
public interface ISubject {
    void addObserver(IObserver observer);
    void removeObserver(IObserver observer);
    void notifyObservers(String message);
    void sendObserverErrorMessage(Exception e);
}
