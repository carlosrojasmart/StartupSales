package Controladores.Principal;

import Modelos.Producto;
import Modelos.Tienda;
import Repositorios.Productos.BusquedaProductos;
import Servicios.Productos.BusquedaProductosService;
import Repositorios.Carrito.MostrarCarrito;
import Modelos.UsuarioActivo;
import Controladores.Vistas.CambiosVistas;
import Servicios.Carrito.CarritoService;
import Servicios.Util.FormatoUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Repositorios.Tienda.MostrarTiendas;

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
    private VBox vboxTiendas;
    @FXML
    private VBox vboxProductos;

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private BusquedaProductosService busquedaProductosService = new BusquedaProductosService(new BusquedaProductos());
    private MostrarTiendas mostrarTiendas = new MostrarTiendas();
    private CarritoService carritoService = new CarritoService(new MostrarCarrito());

    @FXML
    private void initialize() {
        vboxTiendas.setStyle("-fx-padding: 100 0 0 0;");
        vboxProductos.setStyle("-fx-padding: 20 0 0 0;");

        String terminoBusqueda = CambiosVistas.getTerminoBusqueda();
        if (terminoBusqueda != null && !terminoBusqueda.isEmpty()) {
            buscarProductos.setText(terminoBusqueda);
            realizarBusqueda();
        }

        usuarioIcono.setOnMouseClicked(event -> mostrarMiPerfil());
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        cargarCategorias();
    }

    private void cargarCategorias() {
        List<String> categorias = mostrarTiendas.obtenerCategorias();
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

        vboxTiendas.getChildren().clear();
        vboxProductos.getChildren().clear();

        if (tiendasSeleccionadas) {
            List<Tienda> tiendas = mostrarTiendas.buscarTiendasPorNombre(terminoBusqueda);
            if (categoriaSeleccionada != null && !categoriaSeleccionada.isEmpty()) {
                tiendas = tiendas.stream()
                        .filter(tienda -> tienda.getCategoria().equals(categoriaSeleccionada))
                        .toList();
            }
            if (!tiendas.isEmpty()) {
                Label labelTiendas = new Label("Tiendas:");
                labelTiendas.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                vboxTiendas.getChildren().add(labelTiendas);
                mostrarResultadosTiendas(tiendas);
            }
        }

        if (productosSeleccionados) {
            List<Producto> productos = busquedaProductosService.buscarConFiltros(terminoBusqueda, categoriaSeleccionada, precioMin, precioMax);
            if (!productos.isEmpty()) {
                Label labelProductos = new Label("Productos:");
                labelProductos.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                vboxProductos.getChildren().add(labelProductos);
                mostrarResultadosBusqueda(productos);
            }
        }
    }

    private void mostrarResultadosTiendas(List<Tienda> tiendas) {
        for (Tienda tienda : tiendas) {
            agregarTiendaAVista(tienda);
        }
    }

    private void mostrarResultadosBusqueda(List<Producto> productos) {
        for (Producto producto : productos) {
            agregarProductoAVista(producto);
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
        }

        Label nombreTienda = new Label(tienda.getNombre());
        nombreTienda.setStyle("-fx-font-size: 14px;");

        Button btnVerTienda = new Button("Ver Tienda");
        btnVerTienda.setOnAction(event -> irATienda(tienda.getIdTienda()));

        hboxTienda.getChildren().addAll(imagenTienda, nombreTienda, btnVerTienda);
        vboxProductos.getChildren().add(hboxTienda);
    }

    private void irATienda(int idTienda) {
        Tienda tienda = mostrarTiendas.obtenerTiendaPorId(idTienda);

        if (tienda != null) {
            CambiosVistas.setTiendaSeleccionada(tienda);
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
        producto.setCantidad(1);
        carritoService.agregarProductoAlCarrito(idCarrito, producto);
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
        CambiosVistas.setTerminoBusqueda("");
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
