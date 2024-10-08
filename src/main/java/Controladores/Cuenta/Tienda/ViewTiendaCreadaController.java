package Controladores.Cuenta.Tienda;

import DB.JDBC;
import Modelos.Tienda;
import Servicios.Datos.MostrarTiendas;
import Servicios.Datos.UsuarioActivo;
import Servicios.Vistas.CambiosVistas;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

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

        int idUsuario = obtenerIdUsuario(); // Aquí obtienes el id del usuario actual

        // Obtener la lista de tiendas desde la base de datos
        List<Tienda> tiendas = mostrarTiendas.obtenerTiendas(idUsuario);

        // Crear la vista de las tiendas y agregarla al ScrollPane
        VBox vistaTiendas = mostrarTiendas.crearVistaTiendas(tiendas);
        scrollPaneTiendas.setContent(vistaTiendas);

        //-------------------USO DE BARRA BUSQUEDA---------------------//
        buscarProductos.setOnMouseClicked(event -> {buscarProductos.clear();});
        // Realizar búsqueda cuando el usuario presione "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());
        // Configurar el evento del carrito
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());
        //--------------------------------------------------------------//

    }

    // Método simulado para cargar tiendas
    private List<Tienda> cargarTiendasDesdeBD() {
        // Lógica para obtener las tiendas de la base de datos
        return List.of(
                new Tienda(1, "Tienda 1", "Descripción de Tienda 1", "Electrónica", null),
                new Tienda(2, "Tienda 2", "Descripción de Tienda 2", "Ropa", null)
        );
    }

    // Método simulado para obtener el id del usuario actual
    private int obtenerIdUsuario() {
        return UsuarioActivo.getIdUsuario(); // Suponiendo que ya tienes una clase que gestiona el usuario activo
    }


    @FXML
    public void mostrarCarrito() {cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");}

    @FXML
    public void mostrarMiPerfil() {cambiarVista(BtnMiPerfil, "/Vistas/PantallaCuenta/MiPerfil/View-MiPerfil.fxml");}

    @FXML
    public void mostrarCompras(ActionEvent event) {cambiarVista(BtnCompras, "/Vistas/PantallaCuenta/Compras/View-Compras.fxml");}

    @FXML
    public void mostrarFacturacion(ActionEvent event) {cambiarVista(BtnFacturacion, "/Vistas/PantallaCuenta/Facturacion/View-Facturacion.fxml");}

    @FXML
    public void mostrarInicio(ActionEvent event) {cambiarVista(BtnVolverInicio, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");}

    @FXML
    public void crearTienda(ActionEvent event) {cambiarVista(BtnCrearTienda, "/Vistas/PantallaCuenta/Tienda/View-CreandoTienda.fxml");}

    private void cambiarVista(Node nodo, String rutaFXML) {
        Stage stage = (Stage) nodo.getScene().getWindow();
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }

    //------------USO DE BARRA BUSQUEDA-----------------//
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
