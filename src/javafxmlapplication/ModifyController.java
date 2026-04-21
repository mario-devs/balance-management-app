package javafxmlapplication;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.User;

public class ModifyController implements Initializable {
    @FXML
    private TextField Name_I;
    @FXML
    private TextField Password_i;
    @FXML
    private TextField email;
    @FXML
    private TextField surname;
    @FXML
    private ImageView uploadimg;
    @FXML
    private Button uploadbutton;
    @FXML
    private Text mailError;
    @FXML
    private Text nickError;
    @FXML
    private Text pwdError;
    @FXML
    private ImageView DeletePhoto;

    private User user;
    private Acount account;
    private Image selectedImage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            account = Acount.getInstance();
            user = account.getLoggedUser();
            Name_I.setText(user.getName());
            surname.setText(user.getSurname());
            Password_i.setText(user.getPassword());
            email.setText(user.getEmail());
            uploadimg.setImage(user.getImage());
        } catch (AcountDAOException | IOException ex) {
            Logger.getLogger(Add_CategoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void volver(Event event) throws IOException {
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageActual.close();
        Parent LoginP = FXMLLoader.load(getClass().getResource("Cuenta_Gastos.fxml"));
        Scene LoginScene = new Scene(LoginP);
        Stage stage = new Stage();
        stage.setScene(LoginScene);
        stage.show();
    }

    private boolean validatePassword() {
        String Password = Password_i.getText();
        if (Password.length() > 6 && Password.matches("[a-zA-Z0-9]+")) {
            return true;
        } else {
            pwdError.setText("Password must be more than 6 characters long and only contain letters and numbers");
            return false;
        }
    }

    private boolean validateEmail() {
        Pattern p = Pattern.compile("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$");
        Matcher m = p.matcher(email.getText());
        if (m.matches()) {
            return true;
        } else {
            mailError.setText("Format is incorrect: yourmail@anything.com");
            return false;
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(alertType == Alert.AlertType.ERROR ? "Error" : "Information");
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
        }
    }

    @FXML
    private void DeleteAction(MouseEvent event) {
      
        Image defaultImage = new Image(getClass().getResourceAsStream("/Resources/OIP.png"));
        uploadimg.setImage(defaultImage);
        selectedImage = defaultImage; 
        DeletePhoto.setVisible(true);
    }


    @FXML
private void Save(MouseEvent event) throws IOException, AcountDAOException {
    String name1 = Name_I.getText();
    String surname1 = surname.getText();
    String pass1 = Password_i.getText();
    String email1 = email.getText();

    boolean isUpdated = false;

    if (!name1.equals(user.getName())) {
        user.setName(name1);
        isUpdated = true;
    }
    if (!surname1.equals(user.getSurname())) {
        user.setSurname(surname1);
        isUpdated = true;
    }
    if (!pass1.equals(user.getPassword()) && validatePassword()) {
        user.setPassword(pass1);
        isUpdated = true;
    }
    if (!email1.equals(user.getEmail()) && validateEmail()) {
        user.setEmail(email1);
        isUpdated = true;
    }

 
    if (selectedImage != null && !selectedImage.equals(user.getImage())) {
        user.setImage(selectedImage);
        isUpdated = true;
    } else if (selectedImage == null && user.getImage() == null) {
      
        Image defaultImage = new Image(getClass().getResourceAsStream("/Resources/OIP.png"));
        user.setImage(defaultImage);
        isUpdated = true;
    }

    if (isUpdated) {
        showAlert(Alert.AlertType.INFORMATION, "All changes were made correctly");
        volver(event);
    } else {
        showAlert(Alert.AlertType.INFORMATION, "No changes were made");
    }
}

}



