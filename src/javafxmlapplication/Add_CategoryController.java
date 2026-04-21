/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import model.Charge;

/**
 * FXML Controller class
 *
 * @author saul
 */
public class Add_CategoryController implements Initializable {

    @FXML
    private TableView<Category> table;
    @FXML
    private TableColumn<Category, String> Colum_name;
    @FXML
    private TableColumn<Category, String> Colum_Description;
    private Acount account;
    private ObservableList<Category> categories;
    @FXML
    private TextField FieldN;
    @FXML
    private TextField FieldD;
    private Cuenta_GastosController cuentaController;
    @FXML
    private Button btnDelete;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        FieldD.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Stage currentStage = (Stage) newScene.getWindow();
                if (currentStage != null) {
                    currentStage.setResizable(false);
                }
            }
        });
        try{
            account = Acount.getInstance();
        }catch ( AcountDAOException | IOException ex) {
            Logger.getLogger(Add_CategoryController.class.getName()).log(Level.SEVERE,null,ex);
        }
        Colum_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        Colum_Description.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        
        try{
            categories = FXCollections.observableArrayList(account.getUserCategories());
        }catch(AcountDAOException ex){
            Logger.getLogger(Add_CategoryController.class.getName()).log(Level.SEVERE,null,ex);
        }
        table.setItems(categories);
        try{
        table.getItems().addListener((ListChangeListener<Category>) change -> {
                try{
                    updateButtonState();
                
                }catch(AcountDAOException ex){}
            });
        
        updateButtonState();
        }catch(AcountDAOException e){e.printStackTrace();}
        
    }    

    @FXML
    private void addC(ActionEvent event)throws AcountDAOException,IOException {
        String nombre = FieldN.getText();
        String description = FieldD.getText();
        FieldN.clear();
        FieldD.clear();
        
        if (!nombre.isEmpty() && !nombre.isBlank() && !description.isEmpty() && !description.isBlank()){
        account.getLoggedUser();
        account.registerCategory(nombre,description);
        
        
       
        Alert alerta = new Alert(AlertType.WARNING);
        alerta.setTitle("Add Category");
        alerta.setContentText("Do you confirm adding the category " + nombre + "?");
        alerta.setHeaderText("Warning");
        alerta.showAndWait();
        initialize(null, null);
        }else{ 
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle("Error in the data field");
        alerta.setContentText("Please do not leave the fields empty or enter spaces.");
        alerta.showAndWait();
        }
    }
    
    private void updateButtonState() throws AcountDAOException{
        try {
        // Habilitar o deshabilitar el botón de borrar según si hay categorías o si la TableView está vacía
        btnDelete.setDisable(account.getUserCategories().isEmpty() || table.getItems().isEmpty());
    } catch (AcountDAOException e) {
        // Manejar la excepción, aquí puedes imprimir un mensaje de error o tomar otra acción apropiada
        e.printStackTrace();
    }
    }
     public void setCuentaController(Cuenta_GastosController cuentaController) {
        this.cuentaController = cuentaController;
    }
     
    @FXML
    private void DeleteC(ActionEvent event) throws AcountDAOException,IOException {
       account.getLoggedUser();
        Category res = table.getSelectionModel().getSelectedItem();
        
        if ( res != null ){
             Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete category");
            alert.setHeaderText("Message");
            alert.setContentText("Are you sure you want to delete this category?");
            alert.showAndWait();
            account.removeCategory(res);
            removeChargesByCategory(res);
            actualizarTablaCat();
            
            if (cuentaController != null) {
                cuentaController.actualizarTabla();
                cuentaController.initialize(null,null);
            }
        }else{
           Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle("Delete button error");
        alerta.setContentText("Please select a category");
        alerta.showAndWait();
        }
    }

    @FXML
    private void CloseC(ActionEvent event) throws IOException{
        
        try {
            // Cargar el nuevo FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Cuenta_Gastos.fxml"));
            Parent root = fxmlLoader.load();

            // Crear una nueva escena
            Scene scene = new Scene(root);

            // Obtener la ventana actual
            Stage currentStage = (Stage) FieldD.getScene().getWindow();

            // Crear una nueva ventana
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Charges view");
            newStage.show();

            // Cerrar la ventana actual
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void removeChargesByCategory(Category categoryToRemove) throws AcountDAOException {
     account.getLoggedUser();
    // Obtener la lista de cargos del usuario
    List<Charge> userCharges = account.getUserCharges();
    // Usar un Iterator para evitar problemas de ConcurrentModificationException
    Iterator<Charge> iterator = userCharges.iterator();
    while (iterator.hasNext()) {
        Charge charge = iterator.next();
        if (charge.getCategory().equals(categoryToRemove)) {
            iterator.remove();
        }
    }
}
    public void actualizarTablaCat() throws AcountDAOException{
        System.out.println("Actualizando tabla...");
        categories.setAll(account.getUserCategories());
        table.refresh();
        List<Category> userCharges = account.getUserCategories();
        System.out.println("Cargos del usuario: " + userCharges);

    }
    

}
