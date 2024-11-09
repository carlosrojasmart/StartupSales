package Controladores.Cuenta.Facturacion;

import Servicios.Datos.UsuarioActivo;
import Servicios.Vistas.CambiosVistas;
import Servicios.Vistas.FormatoUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class ViewFacturacionController {

    @FXML
    private TextField buscarProductos;

    @FXML
    private Button BtnCompras;

    @FXML
    private Button BtnVolverInicio;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnMiPerfil;

    @FXML
    private Button BtnTienda;

    @FXML
    private Label lblSaldoActual;

    @FXML
    private Label lblSaldoPagar;

    @FXML
    private VBox vboxFacturas;

    private CambiosVistas cambiosVistas = new CambiosVistas();

    @FXML
    private void initialize() {
        // Configurar el buscador de productos
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());

        // Realizar búsqueda cuando el usuario presione "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());

        // Configurar el evento del carrito
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        // Cargar los saldos del usuario
        cargarSaldosUsuario();
    }

    private void cargarSaldosUsuario() {
        // Obtener los saldos directamente como BigDecimal
        BigDecimal saldoActual = UsuarioActivo.getSaldoActual();
        BigDecimal saldoPagar = UsuarioActivo.getSaldoPagar();

        // Formatear los valores para que se vean como moneda
        lblSaldoActual.setText(FormatoUtil.formatearPrecio(saldoActual));
        lblSaldoPagar.setText(FormatoUtil.formatearPrecio(saldoPagar));
    }

    @FXML
    private void realizarBusqueda() {
        String terminoBusqueda = buscarProductos.getText().trim();
        if (!terminoBusqueda.isEmpty()) {
            // Almacenar el término de búsqueda para la vista de búsqueda de productos
            CambiosVistas.setTerminoBusqueda(terminoBusqueda);

            // Cambiar a la vista de búsqueda de productos
            cambiarVista(buscarProductos, "/Vistas/PantallaPrincipal/View-BusquedaProductos.fxml");
        } else {
            System.out.println("El término de búsqueda está vacío.");
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
    public void mostrarCompras(ActionEvent event) {
        if (CambiosVistas.usuarioTieneCompras(UsuarioActivo.getIdUsuario())) {
            cambiarVista(BtnCompras, "/Vistas/PantallaCuenta/Compras/View-ComprasCreada.fxml");
        } else {
            cambiarVista(BtnCompras, "/Vistas/PantallaCuenta/Compras/View-Compras.fxml");
        }
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
    public void mostrarInicio(ActionEvent event) {
        cambiarVista(BtnVolverInicio, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");
    }

    private void cambiarVista(Node nodo, String rutaFXML) {
        Stage stage = (Stage) nodo.getScene().getWindow();
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }
}
