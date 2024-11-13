package Controladores.Cuenta.Compras;

import Modelos.Compra;
import Modelos.UsuarioActivo;
import Controladores.Vistas.CambiosVistas;
import Repositorios.Compras.ComprasRepo;
import Servicios.Compras.ComprasService;
import Servicios.Util.FormatoUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.List;

public class ViewComprasCreadaController {

    @FXML
    private TextField buscarProductos;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnVolverInicio;

    @FXML
    private Button BtnMiPerfil;

    @FXML
    private Button BtnTienda;

    @FXML
    private Button BtnFacturacion;

    @FXML
    private VBox vboxCompras;

    private final CambiosVistas cambiosVistas = new CambiosVistas();
    private final ComprasService comprasService;

    // Constructor para inicializar el servicio de compras
    public ViewComprasCreadaController() {
        this.comprasService = new ComprasService(new ComprasRepo());
    }

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());
        buscarProductos.setOnAction(event -> realizarBusqueda());
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());
        cargarCompras();
    }

    @FXML
    private void realizarBusqueda() {
        String terminoBusqueda = buscarProductos.getText().trim();
        if (!terminoBusqueda.isEmpty()) {
            CambiosVistas.setTerminoBusqueda(terminoBusqueda);
            cambiarVista(buscarProductos, "/Vistas/PantallaPrincipal/View-BusquedaProductos.fxml");
        } else {
            System.out.println("El término de búsqueda está vacío.");
        }
    }

    private void cargarCompras() {
        List<Compra> compras = comprasService.obtenerComprasUsuario(UsuarioActivo.getIdUsuario());
        Collections.reverse(compras);
        vboxCompras.getChildren().clear();

        for (Compra compra : compras) {
            HBox hboxCompra = new HBox(10);
            hboxCompra.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-border-color: #dddddd;");
            hboxCompra.setPrefWidth(600);

            Label fechaLabel = new Label("Fecha: " + compra.getFecha());
            fechaLabel.setStyle("-fx-font-size: 14px;");

            Label horaLabel = new Label("Hora: " + compra.getHora());
            horaLabel.setStyle("-fx-font-size: 14px;");

            Label totalCompra = new Label("Total: " + FormatoUtil.formatearPrecio(compra.getTotalCompra()));
            totalCompra.setStyle("-fx-font-size: 14px;");

            VBox vboxProductos = new VBox(5);
            Label productosTitulo = new Label("Productos:");
            productosTitulo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            vboxProductos.getChildren().add(productosTitulo);

            String[] productosArray = compra.getProductosResumen().split(", ");
            for (String producto : productosArray) {
                Label productoLabel = new Label(producto);
                productoLabel.setStyle("-fx-font-size: 14px;");
                vboxProductos.getChildren().add(productoLabel);
            }

            VBox vboxFechaHora = new VBox(5);
            vboxFechaHora.getChildren().addAll(fechaLabel, horaLabel);

            hboxCompra.getChildren().addAll(vboxFechaHora, totalCompra, vboxProductos);
            vboxCompras.getChildren().add(hboxCompra);
        }
    }

    private void cambiarVista(Node nodo, String rutaFXML) {
        Stage stage = (Stage) nodo.getScene().getWindow();
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }

    @FXML
    public void mostrarMisTiendas(ActionEvent event) {
        if (UsuarioActivo.isVendedor()) {
            cambiarVista(BtnTienda, "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");
        } else {
            cambiarVista(BtnTienda, "/Vistas/PantallaCuenta/Tienda/View-CrearTienda.fxml");
        }
    }

    @FXML
    public void mostrarCarrito() {
        cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");
    }

    @FXML
    public void mostrarMiPerfil() {
        cambiarVista(BtnMiPerfil, "/Vistas/PantallaCuenta/MiPerfil/View-MiPerfil.fxml");
    }

    @FXML
    public void mostrarFacturacion(ActionEvent event) {
        cambiarVista(BtnFacturacion, "/Vistas/PantallaCuenta/Facturacion/View-Facturacion.fxml");
    }

    @FXML
    public void mostrarInicio(ActionEvent event) {
        cambiarVista(BtnVolverInicio, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");
    }
}
