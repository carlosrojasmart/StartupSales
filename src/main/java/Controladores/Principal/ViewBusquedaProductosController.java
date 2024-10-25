package Controladores.Principal;

import Modelos.Producto;
import Modelos.Tienda;
import Servicios.Datos.BusquedaProductos;
import Servicios.Datos.MostrarCarrito;
import Servicios.Datos.UsuarioActivo;
import Servicios.Vistas.CambiosVistas;
import Servicios.Vistas.FormatoUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Servicios.Datos.MostrarTiendas;


import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
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
    private TextField FMin;

    @FXML
    private TextField FMax;

    @FXML
    private ChoiceBox<String> CbCategorias;

    @FXML
    private CheckBox CkTiendas;

    @FXML
    private CheckBox CkProductos;

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

        cargarCategorias();
    }

    private void cargarCategorias() {
        // Obtener las categorías desde el servicio
        List<String> categorias = mostrarTiendas.obtenerCategorias();

        // Agregar las categorías al ChoiceBox
        CbCategorias.getItems().clear();
        CbCategorias.getItems().addAll(categorias);
    }


    @FXML
    private void realizarBusqueda() {
        String terminoBusqueda = buscarProductos.getText().trim();
        String categoriaSeleccionada = CbCategorias.getSelectionModel().getSelectedItem();
        boolean tiendasSeleccionadas = CkTiendas.isSelected();
        boolean productosSeleccionados = CkProductos.isSelected();
        BigDecimal precioMin = FMin.getText().isEmpty() ? null : new BigDecimal(FMin.getText());
        BigDecimal precioMax = FMax.getText().isEmpty() ? null : new BigDecimal(FMax.getText());

        // Limpiar los resultados anteriores
        vboxProductos.getChildren().clear();

        // Mostrar tiendas si están seleccionadas
        if (tiendasSeleccionadas) {
            List<Tienda> tiendas = mostrarTiendas.buscarTiendasPorNombre(terminoBusqueda);
            if (!tiendas.isEmpty()) {
                Label labelTiendas = new Label("Tiendas:");
                labelTiendas.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                vboxProductos.getChildren().add(labelTiendas);
                mostrarResultadosTiendas(tiendas);
            }
        }

        // Mostrar productos si están seleccionados
        if (productosSeleccionados) {
            List<Producto> productos = busquedaProductos.buscarConFiltros(terminoBusqueda, categoriaSeleccionada, precioMin, precioMax);
            if (!productos.isEmpty()) {
                Label labelProductos = new Label("Productos:");
                labelProductos.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                vboxProductos.getChildren().add(labelProductos);
                mostrarResultadosBusqueda(productos);
            }
        }
    }




    private void mostrarResultadosBusqueda(List<Producto> productos) {
        vboxProductos.getChildren().clear();
        for (Producto producto : productos) {
            agregarProductoAVista(producto);
        }
    }

    private void mostrarResultadosTiendas(List<Tienda> tiendas) {
        vboxProductos.getChildren().clear();
        for (Tienda tienda : tiendas) {
            agregarTiendaAVista(tienda);
        }
    }

    private void agregarProductoAVista(Producto producto) {
        HBox hboxProducto = new HBox(10);
        hboxProducto.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-border-color: #dddddd;");
        hboxProducto.setPrefWidth(600);

        ImageView imagenProducto = new ImageView();
        imagenProducto.setFitHeight(80);
        imagenProducto.setFitWidth(80);
        if (producto.getImagenProducto() != null) {
            Image image = new Image(new ByteArrayInputStream(producto.getImagenProducto()));
            imagenProducto.setImage(image);
        }

        Label nombreProducto = new Label(producto.getNombre());
        nombreProducto.setStyle("-fx-font-size: 14px;");

        Label precioProducto = new Label(FormatoUtil.formatearPrecio(producto.getPrecio()));
        precioProducto.setStyle("-fx-font-size: 14px;");

        Button btnAgregarCarrito = new Button("Agregar al Carrito");
        btnAgregarCarrito.setOnAction(event -> agregarAlCarrito(producto));

        hboxProducto.getChildren().addAll(imagenProducto, nombreProducto, precioProducto, btnAgregarCarrito);
        vboxProductos.getChildren().add(hboxProducto);
    }

    private void agregarTiendaAVista(Tienda tienda) {
        HBox hboxTienda = new HBox(10);
        hboxTienda.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-border-color: #dddddd;");
        hboxTienda.setPrefWidth(600);

        ImageView imagenTienda = new ImageView();
        imagenTienda.setFitHeight(80);
        imagenTienda.setFitWidth(80);

        if (tienda.getImagen() != null) {
            Image image = new Image(new ByteArrayInputStream(tienda.getImagen()));
            imagenTienda.setImage(image);
            System.out.println("Imagen cargada para la tienda: " + tienda.getNombre());
        } else {
            System.out.println("No se encontró imagen para la tienda: " + tienda.getNombre());
        }

        Label nombreTienda = new Label(tienda.getNombre());
        nombreTienda.setStyle("-fx-font-size: 14px;");

        Button btnVerTienda = new Button("Ver Tienda");
        btnVerTienda.setOnAction(event -> irATienda(tienda.getIdTienda()));

        hboxTienda.getChildren().addAll(imagenTienda, nombreTienda, btnVerTienda);
        vboxProductos.getChildren().add(hboxTienda);
    }


    private void irATienda(int idTienda) {
        // Obtener la tienda por el ID
        Tienda tienda = mostrarTiendas.obtenerTiendaPorId(idTienda);

        // Verificar si la tienda fue encontrada
        if (tienda != null) {
            // Guardar la tienda seleccionada en CambiosVistas
            CambiosVistas.setTiendaSeleccionada(tienda);

            // Cambiar a la vista de la tienda para el cliente (View-TiendaACliente.fxml)
            cambiarVista(vboxProductos, "/Vistas/PantallaCuenta/Tienda/View-TiendaACliente.fxml");
        } else {
            System.out.println("Tienda no encontrada con el ID: " + idTienda);
        }
    }

    private void agregarAlCarrito(Producto producto) {
        int idCarrito = UsuarioActivo.getIdCarrito();
        if (idCarrito == -1) {
            System.out.println("Error: No se ha encontrado un carrito asociado para el usuario.");
            return;
        }

        MostrarCarrito mostrarCarrito = new MostrarCarrito();
        producto.setCantidad(1); // Cantidad inicial
        mostrarCarrito.agregarProductoAlCarrito(idCarrito, producto);
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
        CambiosVistas.setTerminoBusqueda(""); // Limpiar el término de búsqueda antes de cambiar a la vista del carrito
        cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");
    }

    @FXML
    public void mostrarMisTiendas(ActionEvent event) {
        cambiarVista(BtnMisTiendas, "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");
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
    public void volverInicio(ActionEvent event) {
        cambiarVista(btnVolverInicio, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");
    }
}
