package Controladores.Cuenta.Compras;

import Repositorios.Datos.UsuarioActivo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import Controladores.Vistas.CambiosVistas;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ViewComprasController {

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

    private CambiosVistas cambiosVistas = new CambiosVistas();

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> {
            // Limpiar el campo de búsqueda al hacer clic
            buscarProductos.clear();
        });

        // Asignar la funcionalidad de búsqueda al presionar "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());

        carritoCompra.setOnMouseClicked(event -> {
            mostrarCarrito();
        });
    }

    @FXML
    private void realizarBusqueda() {
        String terminoBusqueda = buscarProductos.getText().trim();
        if (!terminoBusqueda.isEmpty()) {
            // Almacenar el término de búsqueda para usarlo en la vista de búsqueda de productos
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
    public void mostrarMisTiendas(ActionEvent event) {
        // Verificar si el usuario es vendedor
        if (UsuarioActivo.isVendedor()) {
            // Si es vendedor, ir a la vista de tienda ya creada
            cambiarVista(BtnTienda, "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");
        } else {
            // Si no es vendedor, ir a la vista para crear la tienda
            cambiarVista(BtnTienda, "/Vistas/PantallaCuenta/Tienda/View-CrearTienda.fxml");
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

    private void cambiarVista(Node nodo, String rutaFXML) {
        Stage stage = (Stage) nodo.getScene().getWindow();
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }

}
