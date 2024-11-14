package Controladores.Cuenta.Compras;

import Modelos.UsuarioActivo;
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

    @FXML
    private Button CompraAhora;

    //inicializamos CambioVistas
    private CambiosVistas cambiosVistas = new CambiosVistas();

    @FXML
    private void initialize() {
        // Limpiar el campo de búsqueda al hacer clic
        buscarProductos.setOnMouseClicked(event -> {buscarProductos.clear();});

        // Asignar la funcionalidad de búsqueda al presionar "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());

        //inicializa el boton de carrito
        carritoCompra.setOnMouseClicked(event -> {mostrarCarrito();});
    }

    @FXML
    private void realizarBusqueda() {
        //inicializa el terminoBusqueda
        String terminoBusqueda = buscarProductos.getText().trim();
        if (!terminoBusqueda.isEmpty()) {
            // Si no esta vacio almacena el termino el término de búsqueda para usarlo en la vista de búsqueda de productos
            CambiosVistas.setTerminoBusqueda(terminoBusqueda);

            // Cambiar a la vista de búsqueda de productos
            cambiarVista(buscarProductos, "/Vistas/PantallaPrincipal/View-BusquedaProductos.fxml");
        } else {
            // Si el termino de busqueda esta avcio imprime
            System.out.println("El término de búsqueda está vacío.");
        }
    }

    //Cambio de vista general
    private void cambiarVista(Node nodo, String rutaFXML) {
        //Inicializa el stage usando el nodo para cambio de vista
        Stage stage = (Stage) nodo.getScene().getWindow();
        //Usa CambiosVista para realizar el cambio usando el stage y la ruta del fxml
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }

    //Cambio de vista a tiendas
    @FXML
    public void mostrarMisTiendas(ActionEvent event) {
        // Verificar si el usuario es verdeor o no
        if (UsuarioActivo.isVendedor()) {
            // Si es vendedor va a la vista de tiena ya creada
            cambiarVista(BtnTienda, "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");
        } else {
            // Si no es vendedor va a la vista para crear tienda
            cambiarVista(BtnTienda, "/Vistas/PantallaCuenta/Tienda/View-CrearTienda.fxml");
        }
    }

    //Cambio de vista a Carrito
    @FXML
    public void mostrarCarrito() {cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");}

    //Cambio de vista a Perfil
    @FXML
    public void mostrarMiPerfil() {
        cambiarVista(BtnMiPerfil, "/Vistas/PantallaCuenta/MiPerfil/View-MiPerfil.fxml");
    }

    //Cambio de vista a Inicio
    @FXML
    public void CompraAhora(ActionEvent event) {cambiarVista(CompraAhora, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");}

    //Cambio de vista a facturacion
    @FXML
    public void mostrarFacturacion(ActionEvent event) {cambiarVista(BtnFacturacion, "/Vistas/PantallaCuenta/Facturacion/View-Facturacion.fxml");}

    //Cambio de vista a Inicio boton superior
    @FXML
    public void mostrarInicio(ActionEvent event) {cambiarVista(BtnVolverInicio, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");}

}
