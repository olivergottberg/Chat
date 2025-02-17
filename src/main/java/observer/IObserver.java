package observer;

/**
 * Observer interface
 *
 * @author olivergottberg
 */
public interface IObserver {
    void update(String message);
    void receiveError(Exception e);
}
