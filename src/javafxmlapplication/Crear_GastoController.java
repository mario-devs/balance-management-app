/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxmlapplication;

import java.io.File;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Acount;
import model.Category;
import javafxmlapplication.Cuenta_GastosController;
import model.AcountDAOException;
import model.Charge;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 *
 * @author saul
 */
public class Crear_GastoController implements Initializable {

    @FXML
    private Button BackLobby;
    @FXML
    private TextField nameG;
    @FXML
    private TextField costG;
    @FXML
    private TextField descriptionG;
    @FXML
    private Button addGFinalButton;
    @FXML
    private TextField unitsG;
    @FXML
    private DatePicker dateG;
    private Acount account2;
    private Cuenta_GastosController cuentaController;

    private static boolean pulsadoAdd = false;
    @FXML
    private ComboBox<String> categoriaNombre;
    @FXML
    private ImageView deletePhoto;
    @FXML
    private ImageView uploadimg;
    private Image selectedImage;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            account2 = Acount.getInstance();
            List<Category> categoriasUsuario = account2.getUserCategories();
            for (int i = 0; i < categoriasUsuario.size(); i++) {
                String categoria = categoriasUsuario.get(i).getName();
                categoriaNombre.getItems().add(categoria);

            }
            deletePhoto.setVisible(false);
            restrictToNumbersInt();
            restrictToLetters();
            restrictToLetters2();
            restrictToDecimalNumbers();

        } catch (AcountDAOException | IOException ex) {
            Logger.getLogger(Add_CategoryController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void BtnBack(MouseEvent event) throws IOException {
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageActual.close();
        Parent LoginP = FXMLLoader.load(getClass().getResource("Cuenta_Gastos.fxml"));
        Scene LoginScene = new Scene(LoginP);
        Stage stage = new Stage();
        stage.setMinWidth(LoginP.minWidth(-1));
        stage.setMinHeight(LoginP.minHeight(-1));
        stage.setScene(LoginScene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Charges view");
        stage.show();
    }

    public void setCuentaController(Cuenta_GastosController cuentaController) {
        this.cuentaController = cuentaController;
    }

    @FXML
    private void addGFinal(ActionEvent event) throws  AcountDAOException,IOException{
        String name = nameG.getText();
        String description = descriptionG.getText();
        String costText = costG.getText();
        String unitsText = unitsG.getText();
        Image scanImage = uploadimg.getImage();

        // Get the selected category from the combo box
        String selectedCategoryName = categoriaNombre.getValue();
        Category selectedCategory = null;
        List<Category> userCategories = account2.getUserCategories();
        for (Category category : userCategories) {
            if (category.getName().equals(selectedCategoryName)) {
                selectedCategory = category;
                break;
            }
        }

        // Check if any field is empty
        boolean fieldsNotEmpty = !name.isBlank() && !description.isBlank() && !costText.isBlank() && !unitsText.isBlank();
        boolean correctDataType = isNumeric(costText) && isNumeric(unitsText);

        if (fieldsNotEmpty && correctDataType) {
            double cost = Double.parseDouble(costText);
            int units = Integer.parseInt(unitsText);
            account2.registerCharge(name, description, cost, units, scanImage, dateG.getValue(), selectedCategory);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add Expense");
            alert.setHeaderText("Message");
            alert.setContentText("Expense " + name + " has been added successfully.");
            alert.showAndWait();
            volver(event);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error in data fields");
            alert.setContentText("Please do not leave any fields empty or enter spaces.");
            alert.showAndWait();
        }
    }

    private boolean isNumeric(String str) {
        if (str == null || str.isBlank()) {
            return false;
        }
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

     public static boolean pulsadoAdd(){
        return pulsadoAdd;
    }
    
    private void restrictToNumbersInt() {
        unitsG.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    unitsG.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
    
    private void restrictToDecimalNumbers() {
    costG.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                costG.setText(oldValue);
            }
        }
    });
}
    
    private void restrictToLetters() {
        descriptionG.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("[a-zA-Z]*")) {
                    descriptionG.setText(oldValue);
                }
            }
        });
    }
    private void restrictToLetters2() {
        nameG.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("[a-zA-Z]*")) {
                    nameG.setText(oldValue);
                }
            }
        });
    }
     
    @FXML
    private void deleteAction(MouseEvent event) {
        // Eliminar la imagen del ImageView
        
        Image image = new Image(getClass().getResourceAsStream("../Resources/OIP.png"));
        uploadimg.setImage(image);
        deletePhoto.setVisible(false);
        
    }
    
    

    @FXML
    private void uploadAction(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            selectedImage = new Image(selectedFile.toURI().toString());
            uploadimg.setImage(selectedImage);
            deletePhoto.setVisible(true);
        }
        
    }
    private void volver (Event event) throws IOException{
        Stage stageActual = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stageActual.close();
        Parent LoginP=  FXMLLoader.load(getClass().getResource("Cuenta_Gastos.fxml"));
        Scene LoginScene = new Scene(LoginP);
        Stage stage = new Stage();
        stage.setMinWidth(LoginP.minWidth(-1));
        stage.setMinHeight(LoginP.minHeight(-1));
        stage.setScene(LoginScene);
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.setResizable(false);
        stage.setTitle("Charges view");
        stage.show();
    }
}