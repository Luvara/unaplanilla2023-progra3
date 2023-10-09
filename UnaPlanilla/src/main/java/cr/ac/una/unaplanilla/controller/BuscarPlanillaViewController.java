package cr.ac.una.unaplanilla.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.unaplanilla.model.TipoPlanillaDto;
import cr.ac.una.unaplanilla.service.TipoPlanillaService;
import cr.ac.una.unaplanilla.util.FlowController;
import cr.ac.una.unaplanilla.util.Mensaje;
import cr.ac.una.unaplanilla.util.Respuesta;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author BiblioPZ UNA
 */
public class BuscarPlanillaViewController extends Controller implements Initializable {

    @FXML
    private JFXTextField txfCodigo;
    @FXML
    private JFXTextField txfDescripcion;
    @FXML
    private JFXTextField txfSalario;
    @FXML
    private JFXButton btnFiltrar;
    @FXML
    private TableView<TipoPlanillaDto> tbvPlanilla;
    @FXML
    private JFXButton btnAceptar;

    private Object seleccionado;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        anadirSlots();
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnFiltrar(ActionEvent event) {
        tbvPlanilla.getItems().clear();
        TipoPlanillaService service = new TipoPlanillaService();
        String codigo = txfCodigo.getText();
        String descripcion = txfDescripcion.getText();
        String planillaMes = txfSalario.getText();
        
        if (codigo.equals("")){
            codigo = "%";
        }
        if (descripcion.equals("")){
            descripcion = "%";
        }
        if (planillaMes.equals("")){
            planillaMes = "%";
        }

        Respuesta respuesta = service.getPlanillas(codigo, descripcion, planillaMes);

        if (respuesta.getEstado()) {
            ObservableList<TipoPlanillaDto> planillas = FXCollections.observableArrayList((List<TipoPlanillaDto>) respuesta.getResultado("TipoPlanilla"));
            tbvPlanilla.setItems(planillas);
            tbvPlanilla.refresh();
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Consultar planillas", getStage(), respuesta.getMensaje());
        }
    }

    @FXML
    private void onActionBtnAceptar(ActionEvent event) {
        seleccionado = tbvPlanilla.getSelectionModel().getSelectedItem();
        TiposPlanillaViewController tiposPlanilla = (TiposPlanillaViewController) FlowController.getInstance().getController("TiposPlanillaView");
        tiposPlanilla.bindBuscar();
        getStage().close();
        
    }

    public void anadirSlots() {

        tbvPlanilla.getItems().clear();
        tbvPlanilla.getColumns().clear();

        TableColumn<TipoPlanillaDto, String> tbcCodigo = new TableColumn<>("Codigo");
        tbcCodigo.setPrefWidth(100);
        tbcCodigo.setCellValueFactory(cd -> cd.getValue().codigo);
        TableColumn<TipoPlanillaDto, String> tbcDescripcion = new TableColumn<>("Descripcion");
        tbcDescripcion.setPrefWidth(200);
        tbcDescripcion.setCellValueFactory(cd -> cd.getValue().descripcion);
        TableColumn<TipoPlanillaDto, String> tbcPlaxmes = new TableColumn<>("Plan x mes");
        tbcPlaxmes.setPrefWidth(200);
        tbcPlaxmes.setCellValueFactory(cd -> cd.getValue().planillaPorMes);

        tbvPlanilla.getColumns().add(tbcCodigo);
        tbvPlanilla.getColumns().add(tbcDescripcion);
        tbvPlanilla.getColumns().add(tbcPlaxmes);
        tbvPlanilla.refresh();
    }
    
    public Object getSeleccionado() {
        return seleccionado;
    }
}
