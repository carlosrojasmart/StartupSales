package Controladores.Principal;

import Modelos.Producto;
import Modelos.Tienda;
import Servicios.Datos.BusquedaProductos;
import Servicios.Vistas.CambiosVistas;
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
import Servicios.Datos.MostrarTiendas;


import java.io.ByteArrayInputStream;
import java.util.List;

public class ViewBusquedaProductosController {

    @FXML
    private TextField buscarProductos;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnComprasAr;

    @FXML
    private ImageView usuarioIcono;

    @FXML
    private Button BtnMisTiendas;

    @FXML
    private Button btnVolverInicio;

    @FXML
    private Label lblProductos;

    @FXML
    private VBox vboxProductos;

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private BusquedaProductos busquedaProductos = new BusquedaProductos();
    private MostrarTiendas mostrarTiendas = new MostrarTiendas();


    @FXML
    private void initialize() {
        // Obtener el término de búsqueda de la clase CambiosVistas
        String terminoBusqueda = CambiosVistas.getTerminoBusqueda();

        // Realizar la búsqueda automáticamente si hay un término
        if (terminoBusqueda != null && !terminoBusqueda.isEmpty()) {
            buscarProductos.setText(terminoBusqueda);
            realizarBusqueda();
        }

        usuarioIcono.setOnMouseClicked(event -> mostrarMiPerfil());
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());
    }

    @FXML
    private void realizarBusqueda() {
        String terminoBusqueda = buscarProductos.getText().trim();

        // Actualizar el texto de la etiqueta dependiendo del término de búsqueda
        if (terminoBusqueda.isEmpty()) {
            lblProductos.setText("Productos");
            vboxProductos.getChildren().clear();
            System.out.println("El término de búsqueda está vacío.");
            return;
        } else {
            lblProductos.setText(terminoBusqueda);
        }

        // Realizar la búsqueda y mostrar los resultados
        List<Producto> productos = busquedaProductos.buscarProductosPorNombre(terminoBusqueda);
        mostrarResultadosBusqueda(productos);
    }

    private void mostrarResultadosBusqueda(List<Producto> productos) {
        vboxProductos.getChildren().clear();
        for (Producto producto : productos) {
            agregarProductoAVista(producto);
        }
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

        // Botón para ir a la tienda
        Button btnIrATienda = new Button("Ir a Tienda");
        btnIrATienda.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
        btnIrATienda.setOnAction(event -> irATienda(producto.getIdTienda()));

        // Agregar los elementos al HBox
        hboxProducto.getChildren().addAll(imagenProducto, nombreProducto, precioProducto, btnAgregarCarrito, btnIrATienda);

        // Agregar el HBox al VBox de productos
        vboxProductos.getChildren().add(hboxProducto);
    }

    private void irATienda(int idTienda) {
        // Obtener la tienda por el ID
        Tienda tienda = mostrarTiendas.obtenerTiendaPorId(idTienda);

        // Verificar si la tienda fue encontrada
        if (tienda != null) {
            // Guardar la tienda seleccionada en CambiosVistas
            CambiosVistas.setTiendaSeleccionada(tienda);

            // Cambiar a la vista de la tienda para el cliente
            cambiarVista(vboxProductos, "/Vistas/PantallaCuenta/Tienda/View-MirarTienda.fxml");
        } else {
            System.out.println("Tienda no encontrada con el ID: " + idTienda);
        }
    }

    private void agregarAlCarrito(Producto producto) {
        // Aquí puedes implementar la lógica para agregar el producto al carrito.
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
        cambiarVista(BtnMisTiendas, "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");
    }

    @FXML
    public void mostrarCompras(ActionEvent event) {
        cambiarVista(BtnComprasAr, "/Vistas/PantallaCuenta/Compras/View-Compras.fxml");
    }

    @FXML
    public void volverInicio(ActionEvent event) {
        cambiarVista(btnVolverInicio, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");
    }
}
