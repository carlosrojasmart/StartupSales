package Controladores.Cuenta.Tienda;

import Controladores.Vistas.CambiosVistas;
import Modelos.UsuarioActivo;
import Repositorios.Tienda.CrearTienda;
import Repositorios.Tienda.MostrarTiendas;
import Servicios.Tienda.TiendaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
    private TiendaService tiendaService = new TiendaService(new MostrarTiendas(), new CrearTienda());  // Crear instancias de ambos repositorios
    private File archivoImagenSeleccionado; // Almacenará la imagen seleccionada para la tienda

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        boxCategoria.getItems().addAll("Electrónica", "Ropa y Moda", "Hogar y Jardín", "Salud y Belleza", "Deportes", "Juguetes", "Alimentos", "Automóviles", "Libros", "Mascotas");

        buscarProductos.setOnAction(event -> realizarBusqueda());
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

    @FXML
    private void cargarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        // Selecciona el archivo de imagen
        archivoImagenSeleccionado = fileChooser.showOpenDialog(null);

        if (archivoImagenSeleccionado != null) {
            try {
                // Convierte el archivo de imagen a Image para mostrar en el ImageView
                Image image = new Image(new FileInputStream(archivoImagenSeleccionado));
                imagenTienda.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error al cargar la imagen en el controlador.");
            }
        }
    }

    @FXML
    private void crearTienda(ActionEvent event) {
        String nombre = nombreTienda.getText();
        String descripcion = descTienda.getText();
        String categoria = boxCategoria.getValue();
        int idUsuario = UsuarioActivo.getIdUsuario();

        if (nombre.isEmpty() || descripcion.isEmpty() || categoria == null || archivoImagenSeleccionado == null) {
            System.out.println("Por favor, complete todos los campos.");
            return;
        }

        boolean exito = tiendaService.crearTienda(nombre, descripcion, idUsuario, categoria, archivoImagenSeleccionado);

        if (exito) {
            cambiarVista((Node) event.getSource(), "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");
            System.out.println("Tienda creada exitosamente.");
        } else {
            System.out.println("Error al crear la tienda.");
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
