package Controladores.Cuenta.Tienda;

import Servicios.Vistas.CambiosVistas;
import Servicios.Datos.UsuarioActivo;
import Servicios.Datos.CrearTienda;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ViewCreandoTiendaController {

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
    private TextField nombreTienda;

    @FXML
    private TextArea descTienda;

    @FXML
    private ChoiceBox<String> boxCategoria;

    @FXML
    private Button BtnCargarImagen;

    @FXML
    private ImageView imagenTienda;

    @FXML
    private Button BtnCrearTienda;

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private CrearTienda crearTienda = new CrearTienda();  // Instancia de CrearTienda

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());

        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        // Rellenar el ChoiceBox con las categorías de tienda
        boxCategoria.getItems().addAll("Electrónica", "Ropa y Moda", "Hogar y Jardín", "Salud y Belleza", "Deportes", "Juguetes", "Alimentos", "Automóviles", "Libros", "Mascotas");

        buscarProductos.setOnMouseClicked(event -> {buscarProductos.clear();});
        // Realizar búsqueda cuando el usuario presione "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());
        // Configurar el evento del carrito
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

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

    private void cambiarVista(Node nodo, String rutaFXML) {
        Stage stage = (Stage) nodo.getScene().getWindow();
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }

    // Llamar al método para cargar la imagen
    @FXML
    private void cargarImagen(ActionEvent event) {
        crearTienda.cargarImagen(imagenTienda);  // Pasar el ImageView para mostrar la imagen
    }

    // Llamar al método para crear la tienda
    @FXML
    private void crearTienda(ActionEvent event) {
        try {
            String nombre = nombreTienda.getText();
            String descripcion = descTienda.getText();
            String categoria = boxCategoria.getValue();

            // Obtener el idUsuario del usuario activo
            int idUsuario = UsuarioActivo.getIdUsuario();

            // Validar que todos los campos estén completos
            if (nombre.isEmpty() || descripcion.isEmpty() || categoria == null || crearTienda.getArchivoImagen() == null) {
                System.out.println("Por favor, complete todos los campos.");
                return;
            }

            // Pasar los parámetros al método (sin idTienda)
            crearTienda.crearTienda(nombre, descripcion, idUsuario, categoria, crearTienda.getArchivoImagen());

            // Cambiar a la vista de Tienda Creada después de crear la tienda
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            cambiosVistas.cambiarVista(stage, "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
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
