package communication;

import controllers.MainWindowController;
import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.URISyntaxException;

/**
 * The communicator handles the network traffic between all chat clients.
 * Messages are sent and received via the WebSocket.
 * 
 * WE USE WebSocket INSTEAD OF UDP SINCE WE HAVING TROUBLE SOVLING UDP MULTI-CAST ON SOME COMPUTERS.
 * THIS IS A QUICK FIX TO GET THE CHAT WORKING.
 * ALTHO NOT AS FUN, IT HAS NO REAL IMPACT ON THE ASSIGNMENT.
 * 
 * @author Martin Goblirsch
 */
public class UDPChatCommunicator {

    private final String WEB_SOCKET_ADDRESS = "https://laboration52.onrender.com/";
    private Socket IOSocket;
    private MainWindowController _chat = null;

    public UDPChatCommunicator(MainWindowController chat) {
        this._chat = chat;
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
            _chat.error(e);
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
                _chat.receiveMessage(args[0].toString());
            });
            IOSocket.connect();

        } catch (URISyntaxException e) {
            throw e;
        }
    }

}
