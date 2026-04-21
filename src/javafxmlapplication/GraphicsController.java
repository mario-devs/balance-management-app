package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.Charge;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class GraphicsController implements Initializable {

    @FXML
    private BarChart<String, Double> grafica;
    @FXML
    private CategoryAxis meses;
    @FXML
    private NumberAxis gastos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        graficaInvoker();
    }

    private Map<String, Double> gastoMensual() throws AcountDAOException, IOException {
        Map<String, Double> gastoMes = new HashMap<>();

        List<Charge> userCharges = Acount.getInstance().getUserCharges();

        for (Charge cargo : userCharges) {
            Month mesCargo = cargo.getDate().getMonth();
            String nombreMes = mesCargo.toString();
            double montoCargo = cargo.getCost();

            gastoMes.put(nombreMes, gastoMes.getOrDefault(nombreMes, 0.0) + montoCargo);
        }

        return gastoMes;
    }

    private void graficaInvoker() {
        try {
            Map<String, Double> gastoxMes= gastoMensual();

            XYChart.Series<String, Double> series = new XYChart.Series<>();
            

            Random rand = new Random();

            gastoxMes.forEach((mes, gasto) -> {
                XYChart.Data<String, Double> data = new XYChart.Data<>(mes, gasto);

               
                Color color = Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
                String rgb = String.format("%d, %d, %d", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));

                
                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        newNode.setStyle("-fx-bar-fill: rgb(" + rgb + ");");
                    }
                });

                series.getData().add(data);
            });   
            grafica.getData().clear();
            grafica.getData().add(series);
        } catch (AcountDAOException | IOException ex) {
            ex.printStackTrace();
            
        }
    }

    @FXML
    private void goBack(MouseEvent event) {
        try {
            Stage stageActual = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stageActual.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Cuenta_Gastos.fxml"));
            Parent root = loader.load();

            
            Node sourceNode = (Node) event.getSource();
            // Obtenemos el Stage asociado al nodo raíz
            Stage stage = (Stage) sourceNode.getScene().getWindow();

            Scene scene = new Scene(root);

           
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            
            System.err.println("Error al cargar el recurso FXML.");
        }
    }
    @FXML
    private void generarReportePDF() {
        try {
            Map<String, Double> totalGastosPorMes = gastoMensual();

            // Crear un nuevo documento PDF
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            // Crear un flujo de contenido para la página
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Agregar título
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.newLineAtOffset(100, 750);
            contentStream.showText("Annual Expense Report");
            contentStream.endText();

            // Agregar contenido
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 13);
            contentStream.newLineAtOffset(100, 700);

            for (Map.Entry<String, Double> entry : totalGastosPorMes.entrySet()) {
                contentStream.showText("Month: " + entry.getKey() + " - Expense: " + String.format("%.2f", entry.getValue()) + "€ ");
                contentStream.newLineAtOffset(0, -15);
            }

            contentStream.endText();
            contentStream.close();

            // Guardar el documento en un archivo
            document.save("AnnualExpensesReport.pdf");
            document.close();

            System.out.println("PDF succesfully generated! Go to project root folder to find it");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while generating PDF .");
        } catch (AcountDAOException ex) {
            Logger.getLogger(GraphicsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}


