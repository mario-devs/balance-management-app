/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.User;

/**
 * FXML Controller class
 *
 * @author essie
 */
public class LoginUIController implements Initializable {

    @FXML
    private PasswordField textPassword;
    @FXML
    private TextField textUser;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
     // Quitar el MouseEvent y ponerlo ActionEvent
    @FXML
    private void registerclick(MouseEvent event) throws IOException {
        Stage stageActual = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stageActual.close();
        Parent LoginP=  FXMLLoader.load(getClass().getResource("RegisterUI.fxml"));
        Scene LoginScene = new Scene(LoginP);
        Stage stage = new Stage();
        stage.setMinWidth(LoginP.minWidth(-1));
        stage.setMinHeight(LoginP.minHeight(-1));
        stage.setScene(LoginScene);
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.setResizable(false);
        stage.setTitle("Create your new account");
        stage.show();
    }
     private void Cuenta_Gastos(Event event) throws IOException {
        Stage stageActual = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stageActual.close();
        Parent LoginP= FXMLLoader.load(getClass().getResource("Cuenta_Gastos.fxml"));
        Scene LoginScene = new Scene(LoginP);
        Stage stage = new Stage();
        stage.setMinWidth(LoginP.minWidth(-1));
        stage.setMinHeight(LoginP.minHeight(-1));
        stage.setTitle("Charges View");
        stage.setScene(LoginScene);
        stage.initModality(Modality.APPLICATION_MODAL); 
       
        stage.show();
        
    }
    //para cerrar todo, te lo expliqué en el controler de register
    private void exit(ActionEvent event){
        System.exit(0);
    }

    @FXML
    private void loginClick(ActionEvent event) throws AcountDAOException, IOException {
        String userName = textUser.getText();
        String pass = textPassword.getText();
        Acount acc = Acount.getInstance();
        if ( acc.logInUserByCredentials(userName, pass ) == false ){
        showAlert(Alert.AlertType.ERROR, "User or Password invalidated");
        }
        else{ Cuenta_Gastos(event); }
    }
    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(alertType == Alert.AlertType.ERROR ? "Error" : "Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void SpaceLogin(KeyEvent event) throws AcountDAOException, IOException{
        
        KeyCode tecla = event.getCode();
        if ( tecla == KeyCode.ENTER){
        String userName = textUser.getText();
        String pass = textPassword.getText();
        Acount acc = Acount.getInstance();
        if ( acc.logInUserByCredentials(userName, pass ) == false ){
        showAlert(Alert.AlertType.ERROR, "User or Password invalidated");
        }
        else{ Cuenta_Gastos(event); }
        }
    }

    @FXML
    private void tabBetweenFields(KeyEvent event) throws AcountDAOException, IOException {
        KeyCode key = event.getCode();
        if(key == KeyCode.TAB){
            if (textUser.isFocused()) {
                textPassword.requestFocus();
            } else if (textPassword.isFocused()) {
                textUser.requestFocus();  // Optional: this can be removed if you don't want to cycle back
            }
            
        }

    }

    
   
}
