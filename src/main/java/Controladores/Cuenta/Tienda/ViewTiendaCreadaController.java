package Controladores.Cuenta.Tienda;

import Modelos.Tienda;
import Repositorios.Tienda.MostrarTiendas;
import Repositorios.Datos.UsuarioActivo;
import Controladores.Vistas.CambiosVistas;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;

import java.io.ByteArrayInputStream;
import java.util.List;

public class ViewTiendaCreadaController {
    @FXML
    private TextField buscarProductos;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnMiPerfil;

    @FXML
    private Button BtnCompras;

    @FXML
    private Button BtnFacturacion;

    @FXML
    private Button BtnVolverInicio;

    @FXML
    private Button BtnCrearTienda;

    @FXML
    private ScrollPane scrollPaneTiendas;

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private MostrarTiendas mostrarTiendas = new MostrarTiendas();

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        int idUsuario = obtenerIdUsuario();

        // Obtener la lista de tiendas y crear la vista
        List<Tienda> tiendas = mostrarTiendas.obtenerTiendas(idUsuario);
        VBox vistaTiendas = crearVistaTiendas(tiendas);
        scrollPaneTiendas.setContent(vistaTiendas);

        buscarProductos.setOnAction(event -> realizarBusqueda());
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());
    }

    private VBox crearVistaTiendas(List<Tienda> tiendas) {
        VBox contenedorTiendas = new VBox();
        contenedorTiendas.setSpacing(15);

        for (Tienda tienda : tiendas) {
            HBox tiendaBox = new HBox();
            tiendaBox.setSpacing(15);
            tiendaBox.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #DDDDDD; -fx-border-radius: 5; -fx-background-radius: 5;");

            ImageView imagenTienda = new ImageView();
            imagenTienda.setFitHeight(100);
            imagenTienda.setFitWidth(100);
            if (tienda.getImagen() != null) {
                Image image = new Image(new ByteArrayInputStream(tienda.getImagen()));
                imagenTienda.setImage(image);
            }

            Label nombreTienda = new Label(tienda.getNombre());
            nombreTienda.setStyle("-fx-font-family: 'MS Reference Sans Serif'; -fx-font-size: 14px;");

            Button irATienda = new Button("Ir a tienda");
            irATienda.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff; -fx-font-family: 'MS Reference Sans Serif'; -fx-font-size: 14px;");
            irATienda.setOnAction(event -> irATienda(tienda, event));

            tiendaBox.getChildren().addAll(imagenTienda, nombreTienda, irATienda);
            contenedorTiendas.getChildren().add(tiendaBox);
        }

        return contenedorTiendas;
    }

    private void irATienda(Tienda tienda, ActionEvent event) {
        ViewMirarTiendaController.setTiendaSeleccionada(tienda);
        cambiarVista((Node) event.getSource(), "/Vistas/PantallaCuenta/Tienda/View-MirarTienda.fxml");
    }

    private int obtenerIdUsuario() {
        return UsuarioActivo.getIdUsuario();
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
    public void mostrarCompras(ActionEvent event) {
        if (CambiosVistas.usuarioTieneCompras(UsuarioActivo.getIdUsuario())) {
            cambiarVista(BtnCompras, "/Vistas/PantallaCuenta/Compras/View-ComprasCreada.fxml");
        } else {
            cambiarVista(BtnCompras, "/Vistas/PantallaCuenta/Compras/View-Compras.fxml");
        }
    }

    @FXML
    public void mostrarFacturacion(ActionEvent event) {
        cambiarVista(BtnFacturacion, "/Vistas/PantallaCuenta/Facturacion/View-Facturacion.fxml");
    }

    @FXML
    public void mostrarInicio(ActionEvent event) {
        cambiarVista(BtnVolverInicio, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");
    }

    @FXML
    public void crearTienda(ActionEvent event) {
        cambiarVista(BtnCrearTienda, "/Vistas/PantallaCuenta/Tienda/View-CreandoTienda.fxml");
    }

    private void cambiarVista(Node nodo, String rutaFXML) {
        Stage stage = (Stage) nodo.getScene().getWindow();
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }

    private void realizarBusqueda() {
        String terminoBusqueda = buscarProductos.getText().trim();
        if (!terminoBusqueda.isEmpty()) {
            CambiosVistas.setTerminoBusqueda(terminoBusqueda);
            cambiarVista(buscarProductos, "/Vistas/PantallaPrincipal/View-BusquedaProductos.fxml");
        } else {
            System.out.println("El término de búsqueda está vacío.");
        }
    }
}
