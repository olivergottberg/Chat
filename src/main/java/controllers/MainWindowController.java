package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import factories.FactoryManager;
import interfaces.ICommunicationProtocol;
import interfaces.ICommunicationProtocolFactory;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import observer.IObserver;
import observer.ISubject;

/**
 * FXML Controller class
 *
 * @author Martin Goblirsch
 * @author olivergottberg
 */
public class MainWindowController implements Initializable, IObserver {

    @FXML
    public TextArea txtAreaChat;
    @FXML
    public TextField txtMessage;
    @FXML
    public TextField txtName;
    @FXML
    public ToggleGroup gg;
    private ICommunicationProtocol _communicator;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Setting UDP as the default protocol
        ICommunicationProtocolFactory factory = FactoryManager.getFactory("UDP", this);
        setCommunicationProtocol(factory.createProtocol());

        _communicator.startListen();
        addRadioButtonListener();
    }

    /**
     * Setting the communication protocol to be used
     *
     * @param communicator The type of communicator to use
     */
    private void setCommunicationProtocol(ICommunicationProtocol communicator) {
        if (_communicator != null) {
            try {
                if (_communicator instanceof ISubject) {
                    ((ISubject) _communicator).removeObserver(this);
                }
                _communicator.stopListen();
            } catch (Exception e) {
                error(e);
            }
        }
        _communicator = communicator;
        _communicator.startListen();
    }

    /**
     * Receive message from user.
     *
     * @param message The received message.
     */
    public void receiveMessage(String message) {
        //Platform.runLater takes you back to the javaFX thread if needed.
        Platform.runLater(() -> {
            //Here is the code that actually runs when receivedMessage gets called
            txtAreaChat.setText(txtAreaChat.getText() + "\n" + message);
        });
    }

    /**
     * Inform the user that an error has occurred and exit the application.
     *
     * @param e
     */
    public void error(Exception e) {
        showAlert("An error has occured and the application will close: \n" + e.getMessage(), "Error Error!");
        System.exit(1);
    }

    /**
     * Adds listener for radio buttons in the view.
     */
    private void addRadioButtonListener() {
        gg.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) -> {
            RadioButton chk = (RadioButton) t1.getToggleGroup().getSelectedToggle(); // Cast object to radio button
            System.out.println("Selected Radio Button - " + chk.getText());

            handleRadioButtonChange(chk.getText());
        });
    }

    /**
     * Update protocol on radio button change
     *
     * @param protocolType The type of protocol to create
     */
    private void handleRadioButtonChange (String protocolType) {
        ICommunicationProtocolFactory factory = FactoryManager.getFactory(protocolType, this);
        setCommunicationProtocol(factory.createProtocol());
    }

    @FXML
    private void handleSendButton() {
        if (inputValid(txtName.getText(), txtMessage.getText())) {
            this.sendMessage(txtName.getText(), txtMessage.getText());
            txtMessage.setText("");
        }
    }

    private boolean inputValid(String name, String message) {
        if (name.length() == 0) {
            this.showAlert("Please write your name to use the chat", "Fail");
            return false;
        }
        if (message.length() == 0) {
            this.showAlert("Please write a real message.", "Fail");
            return false;
        }
        return true;
    }

    /**
     * Send current message to all users.
     */
    private void sendMessage(String name, String message) {
        try {
            _communicator.sendChat(name, message);
        } catch (Exception e) {
            this.error(e);
        }
    }

    private void showAlert(String message, String title) {
        //Platform.runLater takes you back to the javaFX thread if needed.
        Platform.runLater(() -> {
            //Here is the code that actually runs when receivedMessage gets called
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.setHeaderText(null);

            alert.showAndWait();
        });
    }

    /**
     * Update when a new message is received
     * @param message The message received
     */
    @Override
    public void update(String message) {
        receiveMessage(message);
    }

    /**
     * Call error method if an error is received
     * @param e The error received
     */
    @Override
    public void receiveError(Exception e) {
        error(e);
    }
}
