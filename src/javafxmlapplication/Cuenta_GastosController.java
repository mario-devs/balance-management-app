package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import model.Charge;

public class Cuenta_GastosController implements Initializable {
    
     
    @FXML
    private TableView<Charge> table;
    @FXML
    private TableColumn<Charge, Integer> UnitsCol;
    @FXML
    private TableColumn<Charge, String> NameCol;
    @FXML
    private TableColumn<Charge, Double> CostCol;
    @FXML
    private TableColumn<Charge, LocalDate> DateCol;
    @FXML
    private TableColumn<Charge, String> DescriptionCol;
    @FXML
    private TableColumn<Charge, Category> CategoryCol;

    private ObservableList<Charge> misCargos;
    Acount user1;
    
    public Cuenta_GastosController cuentaObj;
    @FXML
    private Button addEButton;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button addCategoryButton;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'Cuenta_Gastos.fxml'.";
    
        if (table != null) {
            table.sceneProperty().addListener((observable, oldScene, newScene) -> {
                if (newScene != null) {
                    Stage currentStage = (Stage) newScene.getWindow();
                    if (currentStage != null) {
                        currentStage.setResizable(false);
                    }
                }
            });
        } else {
            // Manejar el caso donde 'table' es null, si es necesario.
            System.out.println("Table is null, cannot add scene listener.");
        }
        try {
            int index = 0;
            Image imgnull = null;
            user1 = Acount.getInstance();
            misCargos = FXCollections.observableArrayList(user1.getUserCharges());
            table.setItems(misCargos);
            // Configurar las columnas usando PropertyValueFactory
            UnitsCol.setCellValueFactory(new PropertyValueFactory<>("units"));
            NameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            CostCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
            DateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            DescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            CategoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
            CategoryCol.setCellFactory((c)-> new Celda());
            
            UnitsCol.setResizable(true);
            NameCol.setResizable(true);
            CostCol.setResizable(true);
            DateCol.setResizable(true);
            DescriptionCol.setResizable(true);
            CategoryCol.setResizable(true);

            
        } catch (AcountDAOException ex) {
            Logger.getLogger(Cuenta_GastosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Cuenta_GastosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
        table.getItems().addListener((ListChangeListener<Charge>) change -> {
                try{
                    updateButtonState();
                
                }catch(AcountDAOException ex){}
            });
        
        updateButtonState();
        }catch(AcountDAOException e){e.printStackTrace();}
            
    }
    public void actualizarTabla() throws AcountDAOException{
        misCargos.setAll(user1.getUserCharges());
        table.refresh();
    }

    @FXML
    private void addGasto(ActionEvent event) throws IOException {
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageActual.close();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Crear_Gasto.fxml"));
        Parent root = loader.load();
        
        Crear_GastoController crearGastoController = loader.getController();
        crearGastoController.setCuentaController(this);
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setMinWidth(root.minWidth(-1));
        stage.setMinHeight(root.minHeight(-1));
        stage.setTitle("Add Charge");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        
    
    }
    
    @FXML
    private void showBarChart(ActionEvent event) {
        try {
            Parent LoginP= FXMLLoader.load(getClass().getResource("Graphics.fxml"));
        Scene LoginScene = new Scene(LoginP);
        Stage stage = new Stage();
        stage.setMinWidth(LoginP.minWidth(-1));
        stage.setMinHeight(LoginP.minHeight(-1));
        stage.setTitle("Monthly Report");
        stage.setScene(LoginScene);
        stage.initModality(Modality.APPLICATION_MODAL); 
       
        stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Cuenta_GastosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void updateButtonState() throws AcountDAOException{
        try {
        // Habilitar o deshabilitar el botón de borrar según si hay categorías o si la TableView está vacía
        btnEliminar.setDisable(user1.getUserCategories().isEmpty() || table.getItems().isEmpty());
        addEButton.setDisable(user1.getUserCategories().isEmpty());
    } catch (AcountDAOException e) {
        // Manejar la excepción, aquí puedes imprimir un mensaje de error o tomar otra acción apropiada
        e.printStackTrace();
    }
    }
    
    @FXML
    private void removeG(ActionEvent event) throws AcountDAOException,IOException{
        Charge selectedPerson = table.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
             Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete expense");
            alert.setHeaderText("Message");
            alert.setContentText("Are you sure you want to delete this expense?");
            alert.showAndWait();
              user1.removeCharge(selectedPerson);
              actualizarTabla();
        }
    }
    @FXML
    private void ADD_Category(ActionEvent event) throws IOException {
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageActual.close();
        Parent LoginP= FXMLLoader.load(getClass().getResource("Add_Category.fxml"));
        Scene LoginScene = new Scene(LoginP);
        Stage stage = new Stage();
        stage.setMinWidth(LoginP.minWidth(-1));
        stage.setMinHeight(LoginP.minHeight(-1));
        stage.setTitle("Add Category");
        stage.setScene(LoginScene);
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.show();

    }
    
    
    @FXML
    private void Modify(ActionEvent event) throws IOException{
        try {
       
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Modify.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage currentStage = (Stage) table.getScene().getWindow();
   
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Modify your account");
            newStage.show();

         
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logOut(ActionEvent event) {
         try {
       
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginUI.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage currentStage = (Stage) table.getScene().getWindow();
   
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("LogIn");
            newStage.show();

         
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Celda extends TableCell<Charge, Category>{

        @Override
        protected void updateItem(Category t,  boolean b){
            super.updateItem(t,b);
            if(b||t== null){
                setText(null);
            }else{
                setText(t.getName());
            }
            
        }
    }
    


    
}   
