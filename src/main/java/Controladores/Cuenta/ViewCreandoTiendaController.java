package Controladores.Cuenta;

import Servicios.Vistas.CambiosVistas;
import Servicios.Datos.UsuarioActivo;
import Servicios.Datos.CrearTienda;
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
import java.sql.SQLException;
import java.util.Random;

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
    private ChoiceBox<String> boxCategoria;  // El ChoiceBox debe manejar Strings o lo que sea tu tipo de categoría

    @FXML
    private Button BtnCargarImagen;

    @FXML
    private ImageView imagenTienda;

    @FXML
    private Button BtnCrearTienda;

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private CrearTienda crearTiendaService = new CrearTienda();  // Servicio para manejar la creación de tiendas
    private File archivoImagen;

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());

        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        // Rellenar el ChoiceBox con las categorías de tienda
        boxCategoria.getItems().addAll("Electrónica", "Ropa y Moda", "Hogar y Jardín", "Salud y Belleza", "Deportes", "Juguetes", "Alimentos", "Automóviles", "Libros", "Mascotas");
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
        cambiarVista(BtnCompras, "/Vistas/PantallaCuenta/Compras/View-Compras.fxml");
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

    // Método para cargar la imagen de la tienda
    @FXML
    private void cargarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        archivoImagen = fileChooser.showOpenDialog(null);

        if (archivoImagen != null) {
            try {
                Image nuevaImagen = new Image(new FileInputStream(archivoImagen));
                imagenTienda.setImage(nuevaImagen);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para crear la tienda
    @FXML
    private void crearTienda(ActionEvent event) {
        try {
            // Generar idTienda aleatorio
            int idTienda = generarIdAleatorio();

            // Obtener el idUsuario del usuario activo
            int idUsuario = UsuarioActivo.getIdUsuario();  // Asumimos que tienes el id del usuario activo

            // Obtener los valores del formulario
            String nombre = nombreTienda.getText();
            String descripcion = descTienda.getText();
            String categoria = boxCategoria.getValue();  // Obtenemos la categoría seleccionada

            // Validar campos
            if (nombre.isEmpty() || descripcion.isEmpty() || categoria == null || archivoImagen == null) {
                mostrarAlerta("Error", "Por favor, complete todos los campos y seleccione una imagen.");
                return;
            }

            // Crear la tienda usando el servicio
            crearTiendaService.crearTienda(idTienda, nombre, descripcion, idUsuario, categoria, archivoImagen);

            // Mostrar mensaje de éxito
            mostrarAlerta("Éxito", "La tienda ha sido creada exitosamente.");
            // Redirigir o limpiar la vista

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Ocurrió un error al crear la tienda: " + e.getMessage());
        }
    }

    private int generarIdAleatorio() {
        Random random = new Random();
        return random.nextInt(900000) + 100000;  // Generar un idTienda entre 100000 y 999999
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
