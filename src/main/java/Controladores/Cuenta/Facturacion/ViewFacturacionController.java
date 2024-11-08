package Controladores.Cuenta.Facturacion;

import Modelos.UsuarioActivo;
import Repositorios.Datos.SaldoUsuario;
import Controladores.Vistas.CambiosVistas;
import Servicios.Util.FormatoUtil;
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
    private final SaldoUsuario saldoUsuarioRepo = new SaldoUsuario();


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
        lblSaldoActual.setText(FormatoUtil.formatearPrecio(UsuarioActivo.getSaldoActual()));
        lblSaldoPagar.setText(FormatoUtil.formatearPrecio(UsuarioActivo.getSaldoPagar()));
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
    private void procesarCompra() {
        // Lógica para procesar la compra, restando del saldo actual en la vista y en UsuarioActivo
        BigDecimal nuevoSaldo = UsuarioActivo.getSaldoActual().subtract(UsuarioActivo.getSaldoPagar());

        // Actualiza el saldo en UsuarioActivo
        UsuarioActivo.setSaldoActual(nuevoSaldo);

        // Actualiza el saldo en la base de datos
        guardarSaldoEnBaseDeDatos();

        // Actualizar la vista
        cargarSaldosUsuario();
    }

    private void guardarSaldoEnBaseDeDatos() {
        saldoUsuarioRepo.actualizarSaldo(UsuarioActivo.getIdUsuario(), UsuarioActivo.getSaldoActual());
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
