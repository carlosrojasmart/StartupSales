package Controladores.Cuenta.Tienda;

import Modelos.Producto;
import Modelos.Tienda;
import Servicios.Datos.MostrarProductos;
import Servicios.Datos.MostrarTiendas;
import Servicios.Vistas.CambiosVistas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    @FXML
    private Button btnAgregarProducto;

    @FXML
    private VBox vboxProductos; // VBox donde se agregarán dinámicamente los productos

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private MostrarTiendas mostrarTiendas = new MostrarTiendas();
    private MostrarProductos mostrarProductos = new MostrarProductos();
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
        cargarProductos();
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

    private void cargarProductos() {
        if (tiendaSeleccionada != null) {
            List<Producto> productos = mostrarProductos.obtenerProductosDeTienda(tiendaSeleccionada.getIdTienda());
            vboxProductos.getChildren().clear();

            for (Producto producto : productos) {
                agregarProductoAVista(producto);
            }
        }
    }

    private void agregarProductoAVista(Producto producto) {
        // Crear el HBox para el producto con un espaciado de 10 entre los elementos
        HBox hboxProducto = new HBox(10);
        hboxProducto.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-alignment: CENTER_LEFT; -fx-border-color: #dddddd;");
        hboxProducto.setPrefWidth(600); // Ajustar el ancho del HBox

        // Imagen del producto
        ImageView imagenProducto = new ImageView();
        imagenProducto.setFitHeight(80);
        imagenProducto.setFitWidth(80);
        if (producto.getImagenProducto() != null) {
            Image image = new Image(new ByteArrayInputStream(producto.getImagenProducto()));
            imagenProducto.setImage(image);
        }

        // Nombre del producto
        Label nombreProducto = new Label(producto.getNombre());
        nombreProducto.setStyle("-fx-font-size: 14px; -fx-padding: 0 10 0 10;"); // Añadir padding para separarlo de la imagen

        // Botón para editar el producto
        Button btnEditarProducto = new Button("Editar Producto");
        btnEditarProducto.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
        btnEditarProducto.setOnAction(event -> editarProducto(producto));

        // Agregar los elementos al HBox
        hboxProducto.getChildren().addAll(imagenProducto, nombreProducto, btnEditarProducto);

        // Ajustar margen del HBox dentro del VBox
        VBox.setMargin(hboxProducto, new Insets(5, 0, 10, 0)); // Espacio superior e inferior de 10px

        // Agregar el HBox al VBox de productos
        vboxProductos.getChildren().add(hboxProducto);
    }

    private void editarProducto(Producto producto) {
        // Aquí puedes cambiar la vista a la de edición de producto y pasar el producto seleccionado
        // usando un método estático o un patrón adecuado.
        System.out.println("Editar producto: " + producto.getNombre());
        // cambiosVistas.cambiarVista(...);
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

    public static Tienda getTiendaSeleccionada() {
        return tiendaSeleccionada;
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

    @FXML
    public void mostrarCrearProducto(ActionEvent event) {
        cambiarVista(btnAgregarProducto, "/Vistas/PantallaCuenta/Tienda/View-Producto.fxml");
    }
}