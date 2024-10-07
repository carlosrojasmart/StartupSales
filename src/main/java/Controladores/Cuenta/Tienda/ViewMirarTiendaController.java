package Controladores.Cuenta.Tienda;

import Modelos.Tienda;
import Servicios.Datos.MostrarTiendas;
import Servicios.Vistas.CambiosVistas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ViewMirarTiendaController {
    private static Tienda tiendaSeleccionada;

    @FXML
    private TextField buscarProductos;

    @FXML
    private TextField nombreTienda;

    @FXML
    private ChoiceBox<String> categoriaTienda;

    @FXML
    private TextField DescTienda;

    @FXML
    private ImageView imagenTienda;

    @FXML
    private Button BtnVolverInicio;

    @FXML
    private Button btnCargarImagen;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnVolverTiendas;

    @FXML
    private Button BtnCompras;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnMiPerfil;

    @FXML
    private Button BtnFacturacion;

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private MostrarTiendas mostrarTiendas = new MostrarTiendas();
    private File archivoImagen;

    // Setter estático para recibir la tienda seleccionada
    public static void setTiendaSeleccionada(Tienda tienda) {
        tiendaSeleccionada = tienda;
    }

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());
        cargarDatosTienda();
        configurarEventos();
    }

    public void cargarDatosTienda() {
        if (tiendaSeleccionada != null) {
            nombreTienda.setText(tiendaSeleccionada.getNombre());
            DescTienda.setText(tiendaSeleccionada.getDescripcion());

            // Cargar la imagen de la tienda
            if (tiendaSeleccionada.getImagen() != null) {
                Image image = new Image(new ByteArrayInputStream(tiendaSeleccionada.getImagen()));
                imagenTienda.setImage(image);
            }

            // Cargar las categorías y seleccionar la actual
            List<String> categorias = mostrarTiendas.obtenerCategorias();
            categoriaTienda.getItems().setAll(categorias);
            categoriaTienda.setValue(tiendaSeleccionada.getCategoria());
        }
    }

    private void configurarEventos() {
        btnCargarImagen.setOnAction(event -> cargarImagen());
        btnGuardar.setOnAction(event -> guardarCambios());
        btnVolverTiendas.setOnAction(event -> volverATiendas());
    }

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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void guardarCambios() {
        // Verificar si se realizaron cambios y actualizar la tienda
        boolean cambiosRealizados = false;

        if (!nombreTienda.getText().equals(tiendaSeleccionada.getNombre())) {
            tiendaSeleccionada.setNombre(nombreTienda.getText());
            cambiosRealizados = true;
        }

        if (!DescTienda.getText().equals(tiendaSeleccionada.getDescripcion())) {
            tiendaSeleccionada.setDescripcion(DescTienda.getText());
            cambiosRealizados = true;
        }

        if (!categoriaTienda.getValue().equals(tiendaSeleccionada.getCategoria())) {
            tiendaSeleccionada.setCategoria(categoriaTienda.getValue());
            cambiosRealizados = true;
        }

        if (archivoImagen != null) {
            try {
                tiendaSeleccionada.setImagen(new FileInputStream(archivoImagen).readAllBytes());
                cambiosRealizados = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Actualizar la tienda en la base de datos si hubo cambios
        if (cambiosRealizados) {
            mostrarTiendas.actualizarTienda(tiendaSeleccionada);
            System.out.println("Tienda actualizada correctamente.");
        } else {
            System.out.println("No se realizaron cambios.");
        }
    }

    private void volverATiendas() {
        cambiarVista(btnVolverTiendas, "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");
    }

    private void cambiarVista(Node nodo, String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();

            // Obtener el Stage actual desde el nodo fuente
            Stage stage = (Stage) nodo.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
}
