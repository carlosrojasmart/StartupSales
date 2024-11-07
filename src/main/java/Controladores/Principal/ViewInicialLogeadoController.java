package Controladores.Principal;

import Modelos.Producto;
import Modelos.UsuarioActivo;
import Controladores.Vistas.CambiosVistas;
import Repositorios.Carrito.MostrarCarrito;
import Repositorios.Productos.CrearProducto;
import Repositorios.Productos.MostrarProductos;
import Servicios.Carrito.CarritoService;
import Servicios.Productos.ProductoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.util.List;

public class ViewInicialLogeadoController {

    @FXML
    private TextField buscarProductos;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnComprasAr;

    @FXML
    private ImageView usuarioIcono;

    @FXML
    private HBox TopProductos;

    @FXML
    private Button BtnMisTiendas;

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private final ProductoService productoService = new ProductoService(new CrearProducto(), new MostrarProductos());
    private final CarritoService carritoService = new CarritoService(new MostrarCarrito(), productoService);

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());
        usuarioIcono.setOnMouseClicked(event -> mostrarMiPerfil());
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        cargarTopProductosMasVendidos();
    }

    private void cargarTopProductosMasVendidos() {

        List<Producto> topProductos = productoService.obtenerTopProductosMasVendidos();

        TopProductos.getChildren().clear(); // Limpiamos cualquier contenido anterior

        for (Producto producto : topProductos) {
            // Crear un VBox para cada producto
            VBox vboxProducto = new VBox(10);
            vboxProducto.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-alignment: CENTER;");
            vboxProducto.setPrefWidth(150);

            // Imagen del producto
            ImageView imagenProducto = new ImageView();
            imagenProducto.setFitHeight(80);
            imagenProducto.setFitWidth(80);
            if (producto.getImagenProducto() != null) {
                imagenProducto.setImage(new Image(new ByteArrayInputStream(producto.getImagenProducto())));
            }

            // Nombre del producto
            Label nombreProducto = new Label(producto.getNombre());
            nombreProducto.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

            // Precio del producto
            Label precioProducto = new Label("COP " + producto.getPrecio().toString());
            precioProducto.setStyle("-fx-font-size: 12px;");

            // Botón de agregar al carrito
            Button btnAgregarCarrito = new Button("Agregar al Carrito");
            btnAgregarCarrito.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
            btnAgregarCarrito.setOnAction(event -> agregarAlCarrito(producto));

            // Añadir componentes al VBox del producto
            vboxProducto.getChildren().addAll(imagenProducto, nombreProducto, precioProducto, btnAgregarCarrito);

            // Añadir VBox del producto al HBox TopProductos
            TopProductos.getChildren().add(vboxProducto);
        }
    }

    private void agregarAlCarrito(Producto producto) {
        int idCarrito = UsuarioActivo.getIdCarrito();
        producto.setCantidad(1);
        carritoService.agregarProductoAlCarrito(idCarrito, producto);
        System.out.println("Producto agregado al carrito: " + producto.getNombre());
    }



    private void cambiarVista(Node nodo, String rutaFXML) {
        Stage stage = (Stage) nodo.getScene().getWindow();
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }

    @FXML
    public void mostrarMiPerfil() {
        cambiarVista(usuarioIcono, "/Vistas/PantallaCuenta/MiPerfil/View-MiPerfil.fxml");
    }

    @FXML
    public void mostrarCarrito() {
        cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");
    }

    @FXML
    public void mostrarMisTiendas(ActionEvent event) {
        // Verificar si el usuario es vendedor
        if (UsuarioActivo.isVendedor()) {
            // Si es vendedor, ir a la vista de tienda ya creada
            cambiarVista(BtnMisTiendas, "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");
        } else {
            // Si no es vendedor, ir a la vista para crear la tienda
            cambiarVista(BtnMisTiendas, "/Vistas/PantallaCuenta/Tienda/View-CrearTienda.fxml");
        }
    }

    @FXML
    public void mostrarCompras(ActionEvent event) {
        if (CambiosVistas.usuarioTieneCompras(UsuarioActivo.getIdUsuario())) {
            cambiarVista(BtnComprasAr, "/Vistas/PantallaCuenta/Compras/View-ComprasCreada.fxml");
        } else {
            cambiarVista(BtnComprasAr, "/Vistas/PantallaCuenta/Compras/View-Compras.fxml");
        }
    }

    @FXML
    public void realizarBusqueda() {
        String terminoBusqueda = buscarProductos.getText().trim();

        if (!terminoBusqueda.isEmpty()) {
            // Almacenar el término de búsqueda para que esté disponible en la vista de búsqueda de productos
            CambiosVistas.setTerminoBusqueda(terminoBusqueda);

            // Cambiar a la vista de búsqueda de productos
            cambiarVista(buscarProductos, "/Vistas/PantallaPrincipal/View-BusquedaProductos.fxml");
        } else {
            System.out.println("El término de búsqueda está vacío.");
        }
    }
}
