/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;

/**
 * FXML Controller class
 *
 * @author saul
 */
public class RegisterUIController implements Initializable {

    @FXML
    private TextField Name_I;
    @FXML
    private TextField Password_i;
    @FXML
    private TextField Nick;
    @FXML
    private TextField email;
    @FXML
    private TextField surname;
    private ImageView ImageUser;
    @FXML
    private Button uploadbutton;
    @FXML
    private ImageView uploadimg;
   
    @FXML
    private Text mailError;
    @FXML
    private Text nickError;
    @FXML
    private Text pwdError;
    @FXML
    private ImageView DeletePhoto;
    private Image selectedImage;
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
   
     DeletePhoto.setVisible(false);
    }    
   
    @FXML
    public void volver(ActionEvent event) throws IOException{
        Stage stageActual = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stageActual.close();
        Parent LoginP= FXMLLoader.load(getClass().getResource("LoginUI.fxml"));
        Scene LoginScene = new Scene(LoginP);
        Stage stage = new Stage();
        stage.setScene(LoginScene);
        stage.setTitle("LogIn");
        stage.show();
    }
   
    private void exit(ActionEvent event){
        System.exit(0);
    }
   

    @FXML
    public void registrar(ActionEvent event) throws IOException, AcountDAOException {
        boolean test = validateNickname() && validatePassword() && validateEmail();
        String gmail = email.getText();
        String surname2 = surname.getText();
        String name = Name_I.getText();
        String login = Nick.getText();
        String Password = Password_i.getText();
        Image image = uploadimg.getImage();

        if (test && !name.isEmpty() && !surname2.isEmpty() && !gmail.isEmpty() && !login.isEmpty() && !Password.isEmpty() && image != null) {
            int month = LocalDate.now().getMonthValue();
            int day = LocalDate.now().getDayOfMonth();
            int year = LocalDate.now().getYear();
            LocalDate date = LocalDate.of(year, month, day);
            Acount.getInstance().registerUser(name, surname2, gmail, login, Password, image, date);

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Registration completed");
            alert.setHeaderText("Congratulations, your registration was successful!");
            alert.setContentText("Enjoy your new user!");
            alert.showAndWait();
            volver(event);
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Registration error");
            alert.setHeaderText("Fields are missing, please complete them");
            alert.setContentText("Please complete all fields to continue with the registration.");
            alert.showAndWait();
        }

       
   } 
    private boolean validatePassword() {
     
        String Password = Password_i.getText();
        if(Password.length() > 6 && Password.matches("[a-zA-Z0-9]+")){
            return true;
        }
        else{
            pwdError.setText("Password must be more than 6 characters long and only contain letters and numbers");
            return false;
        }
    }
    private boolean validateNickname() throws AcountDAOException, IOException {
       
        Acount acc = Acount.getInstance();
        String login = Nick.getText();
        if(login.contains(" ")){
        nickError.setText("Nickname cannot contain spaces");
            return false; 
    }
        if(acc.existsLogin(login)){
            nickError.setText("Nickname already taken");
            return false;  
        }
        if(login.isEmpty()){
          nickError.setText("A Nickname is required");
            return false;  
        }
            return true;
        
    }
    
    
    
     private boolean validateEmail() {
    
    Pattern p = Pattern.compile("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$");
    Matcher m = p.matcher(email.getText());
    if(m.matches()){
        return true;
    }
    else{
        mailError.setText("Format is incorrect: yourmail@anything.com");
            return false;
    }
    }

     private void showAlert(AlertType alertType, String message) {
         
        Alert alert = new Alert(alertType);
        alert.setTitle(alertType == AlertType.ERROR ? "Error" : "Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void uploadAction(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            selectedImage = new Image(selectedFile.toURI().toString());
            uploadimg.setImage(selectedImage);
            DeletePhoto.setVisible(true);
        }
    }

    @FXML
    private void DeleteAction(MouseEvent event) {
   
    Image image = new Image(getClass().getResourceAsStream("/Resources/OIP.png"));
    uploadimg.setImage(image);
    DeletePhoto.setVisible(false);
    }

    }