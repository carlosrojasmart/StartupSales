package Controladores.Cuenta.Tienda;

import Modelos.Producto;
import Modelos.Tienda;
import Servicios.Datos.MostrarCarrito;
import Servicios.Datos.MostrarProductos;
import Servicios.Datos.UsuarioActivo;
import Servicios.Vistas.CambiosVistas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class ViewTiendaAClienteController {

    @FXML
    private TextField buscarProductos;

    @FXML
    private TextField nombreTienda;

    @FXML
    private TextField categoriaTienda;

    @FXML
    private TextField DescTienda;

    @FXML
    private ImageView imagenTienda;

    @FXML
    private Button BtnVolverInicio;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnMiPerfil;

    @FXML
    private Button BtnCompras;

    @FXML
    private Button BtnFacturacion;

    @FXML
    private VBox vboxProductos; // VBox donde se agregarán dinámicamente los productos

    private MostrarProductos mostrarProductos = new MostrarProductos();

    @FXML
    private void initialize() {
        Tienda tienda = CambiosVistas.getTiendaSeleccionada();
        if (tienda != null) {
            cargarDatosTienda(tienda);
            cargarProductos(tienda);
        } else {
            System.out.println("No hay tienda seleccionada.");
        }

        // Hacer los TextFields no editables
        nombreTienda.setEditable(false);
        categoriaTienda.setEditable(false);
        DescTienda.setEditable(false);

        // Configurar el buscador de productos
        buscarProductos.setOnMouseClicked(event -> {
            // Limpiar el campo de búsqueda al hacer clic
            buscarProductos.clear();
        });

        // Realizar búsqueda cuando el usuario presione "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());

        // Configurar el evento del carrito
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());
    }

    private void cargarDatosTienda(Tienda tienda) {
        nombreTienda.setText(tienda.getNombre());
        categoriaTienda.setText(tienda.getCategoria());
        DescTienda.setText(tienda.getDescripcion());

        if (tienda.getImagen() != null) {
            Image image = new Image(new ByteArrayInputStream(tienda.getImagen()));
            imagenTienda.setImage(image);
        }
    }

    private void cargarProductos(Tienda tienda) {
        List<Producto> productos = mostrarProductos.obtenerProductosDeTienda(tienda.getIdTienda());
        vboxProductos.getChildren().clear();

        for (Producto producto : productos) {
            agregarProductoAVista(producto);
        }
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


    private void agregarProductoAVista(Producto producto) {
        HBox hboxProducto = new HBox(10);
        hboxProducto.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-alignment: CENTER_LEFT; -fx-border-color: #dddddd;");
        hboxProducto.setPrefWidth(600);

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
        nombreProducto.setStyle("-fx-font-size: 14px; -fx-padding: 0 10 0 10;");

        // Precio del producto
        Label precioProducto = new Label(String.format("$ %.2f", producto.getPrecio()));
        precioProducto.setStyle("-fx-font-size: 14px;");

        // Botón para agregar al carrito
        Button btnAgregarCarrito = new Button("Agregar al Carrito");
        btnAgregarCarrito.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
        btnAgregarCarrito.setOnAction(event -> agregarAlCarrito(producto));

        HBox.setMargin(hboxProducto, new Insets(5, 0, 10, 0)); // Ajustar el margen

        // Agregar los elementos al HBox
        hboxProducto.getChildren().addAll(imagenProducto, nombreProducto, precioProducto, btnAgregarCarrito);

        // Ajustar el margen del primer producto para reducir el espacio superior
        if (vboxProductos.getChildren().isEmpty()) {
            VBox.setMargin(hboxProducto, new Insets(1, 0, 5, 0)); // Menor margen superior si es el primero
        } else {
            VBox.setMargin(hboxProducto, new Insets(5, 0, 10, 0)); // Margen estándar para los demás productos
        }

        // Agregar el HBox al VBox de productos
        vboxProductos.getChildren().add(hboxProducto);
    }


    private void agregarAlCarrito(Producto producto) {
        int idCarrito = UsuarioActivo.getIdCarrito(); // Ahora obtienes el ID del carrito del usuario activo
        MostrarCarrito mostrarCarrito = new MostrarCarrito();

        // Establecer la cantidad antes de agregar al carrito
        producto.setCantidad(1); // O la cantidad deseada

        mostrarCarrito.agregarProductoAlCarrito(idCarrito, producto);
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
