package org.example.lr3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HelloController {

    @FXML
    private Button encryptStage;

    @FXML
    private Button decryptStage;

    //encrypt
    @FXML
    private TextArea inputTextEnc;

    @FXML
    private TextArea outputTextEnc;

    @FXML
    public TextArea outputTextEncLog;

    public TextArea getOutputTextEncLog(){
        return outputTextEncLog;
    }

    @FXML
    private TextField roundCountEnc;

    @FXML
    private TextField encryptKey;

    @FXML
    private Button encryptButton;

    @FXML
    private Label encryptfooterMessage;


    //decrypt
    @FXML
    public TextArea outputTextDecLog;

    public TextArea getOutputTextDecLog(){
        return outputTextDecLog;
    }

    @FXML
    private TextArea inputTextDec;

    @FXML
    private TextArea outputTextDec;

    @FXML
    private TextField roundCountDecrypt;
    @FXML
    private TextField decryptKey;

    @FXML
    private Button decryptButton;

    @FXML
    private Label decryptfooterMessage;


    @FXML
    private void handleEncryptButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("encrypt-view.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
        URL iconUrl = HelloApplication.class.getResource("pawprint.png");
        stage.getIcons().add(new Image(iconUrl.toExternalForm()));
        stage.setTitle("Шифрование: Сети Фейштеля");
        stage.show();
    }

    @FXML
    private void handleDecryptButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("decrypt-view.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
        URL iconUrl = HelloApplication.class.getResource("pawprint.png");
        stage.getIcons().add(new Image(iconUrl.toExternalForm()));
        stage.setTitle("Дешифрование: Сети Фейштеля");
        stage.show();
    }

    @FXML
    private void onEncryptButtonClick(){
        try {
            Integer roundCount = Integer.valueOf(roundCountEnc.getText());
            String data = inputTextEnc.getText();
            FeishtelCypher encrypter = new FeishtelCypher(encryptKey.getText(), getOutputTextEncLog());
            String encrypted = encrypter.encrypt(data, roundCount);
            outputTextEnc.setText(encrypted);
        } catch (Exception e) {
            encryptfooterMessage.setText("Ой произошла ошибочка! Посмотрите в консоли");
        }
    }

    @FXML
    private void onDecryptButtonClick(){
        try {
            Integer roundCount = Integer.valueOf(roundCountDecrypt.getText());
            String data = inputTextDec.getText();
            FeishtelCypher encrypter = new FeishtelCypher(decryptKey.getText(), getOutputTextDecLog());
            String decrypted = encrypter.decrypt(data, roundCount);
            outputTextDec.setText(decrypted);
        } catch (NumberFormatException e) {
            decryptfooterMessage.setText("Ой произошла ошибочка! Посмотрите в консоли");
        }
    }
}
