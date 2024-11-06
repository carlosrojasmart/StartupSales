package Controladores.Principal;

import Modelos.Producto;
import Repositorios.Carrito.MostrarCarrito;
import Modelos.UsuarioActivo;
import Controladores.Vistas.CambiosVistas;
import Servicios.Carrito.CarritoService;
import Servicios.Util.FormatoUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.io.ByteArrayInputStream;

public class ViewCarritoComprasController {

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
    private VBox vboxProductos;

    @FXML
    private Label lblTotal;

    @FXML
    private Button btnContinuar;

    @FXML
    private Label lblAvisoSaldo;

    private final CarritoService carritoService = new CarritoService(new MostrarCarrito());
    private final CambiosVistas cambiosVistas = new CambiosVistas();

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());
        usuarioIcono.setOnMouseClicked(event -> mostrarMiPerfil());

        cargarProductosCarrito();

        String terminoBusqueda = CambiosVistas.getTerminoBusqueda();
        if (terminoBusqueda != null && !terminoBusqueda.isEmpty()) {
            buscarProductos.setText(terminoBusqueda);
            realizarBusqueda();
        }

        buscarProductos.setOnAction(event -> realizarBusqueda());
    }

    @FXML
    private void realizarBusqueda() {
        String terminoBusqueda = buscarProductos.getText().trim();
        if (!terminoBusqueda.isEmpty()) {
            CambiosVistas.setTerminoBusqueda(terminoBusqueda);
            cambiarVista(buscarProductos, "/Vistas/PantallaPrincipal/View-BusquedaProductos.fxml");
        } else {
            System.out.println("El término de búsqueda está vacío.");
        }
    }

    private void cargarProductosCarrito() {
        List<Producto> productos = carritoService.obtenerProductosDeCarrito(UsuarioActivo.getIdCarrito());
        vboxProductos.getChildren().clear();

        for (Producto producto : productos) {
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

            Spinner<Integer> spinnerCantidad = new Spinner<>();
            spinnerCantidad.setPrefWidth(55);
            spinnerCantidad.setMaxWidth(55);
            spinnerCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, producto.getCantidad()));
            spinnerCantidad.valueProperty().addListener((obs, oldValue, newValue) -> {
                carritoService.actualizarCantidadProducto(producto.getIdProducto(), UsuarioActivo.getIdCarrito(), newValue);
                actualizarTotal();
            });

            Label precioProducto = new Label(FormatoUtil.formatearPrecio(producto.getPrecio()));
            precioProducto.setStyle("-fx-font-size: 14px;");

            Button btnEliminarProducto = new Button("Eliminar");
            btnEliminarProducto.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff;");
            btnEliminarProducto.setOnAction(event -> eliminarProductoDelCarrito(producto));

            hboxProducto.getChildren().addAll(imagenProducto, nombreProducto, spinnerCantidad, precioProducto, btnEliminarProducto);
            vboxProductos.getChildren().add(hboxProducto);
        }

        actualizarTotal();
    }

    private void actualizarTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (Node node : vboxProductos.getChildren()) {
            if (node instanceof HBox hbox) {
                Label precioLabel = (Label) hbox.getChildren().get(3);
                Spinner<Integer> spinnerCantidad = (Spinner<Integer>) hbox.getChildren().get(2);

                String precioTexto = precioLabel.getText()
                        .replace("COP", "")
                        .replace(",", "")
                        .trim();

                try {
                    BigDecimal precio = new BigDecimal(precioTexto);
                    total = total.add(precio.multiply(BigDecimal.valueOf(spinnerCantidad.getValue())));
                } catch (NumberFormatException e) {
                    System.out.println("Error al convertir el precio: " + precioTexto);
                }
            }
        }

        lblTotal.setText("Total: " + FormatoUtil.formatearPrecio(total));
    }

    private void eliminarProductoDelCarrito(Producto producto) {
        carritoService.eliminarProductoDelCarrito(producto.getIdProducto(), UsuarioActivo.getIdCarrito());
        cargarProductosCarrito();
    }

    @FXML
    public void procesarCompra() {
        BigDecimal saldoActual = UsuarioActivo.getSaldoActual();
        BigDecimal totalCarrito = obtenerTotalCarrito();

        if (saldoActual.compareTo(totalCarrito) < 0) {
            lblAvisoSaldo.setText("Saldo Insuficiente");
        } else {
            BigDecimal nuevoSaldo = saldoActual.subtract(totalCarrito);
            UsuarioActivo.setSaldoActual(nuevoSaldo);

            carritoService.procesarCompra(UsuarioActivo.getIdUsuario(), UsuarioActivo.getIdCarrito(), totalCarrito);

            lblAvisoSaldo.setText("Compra realizada con éxito");
            lblTotal.setText("Total: 0.00 COP");

            carritoService.vaciarCarrito(UsuarioActivo.getIdCarrito());
            cargarProductosCarrito();
        }
    }

    private BigDecimal obtenerTotalCarrito() {
        BigDecimal total = BigDecimal.ZERO;

        for (Node node : vboxProductos.getChildren()) {
            if (node instanceof HBox hbox) {
                Label precioLabel = (Label) hbox.getChildren().get(3);
                Spinner<Integer> spinnerCantidad = (Spinner<Integer>) hbox.getChildren().get(2);

                String precioTexto = precioLabel.getText()
                        .replace("COP", "")
                        .replace(",", "")
                        .trim();

                try {
                    BigDecimal precio = new BigDecimal(precioTexto);
                    total = total.add(precio.multiply(BigDecimal.valueOf(spinnerCantidad.getValue())));
                } catch (NumberFormatException e) {
                    System.out.println("Error al convertir el precio: " + precioTexto);
                }
            }
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    @FXML
    public void mostrarMiPerfil() {
        cambiarVista(usuarioIcono, "/Vistas/PantallaCuenta/MiPerfil/View-MiPerfil.fxml");
    }

    @FXML
    public void mostrarMisTiendas(ActionEvent event) {
        if (UsuarioActivo.isVendedor()) {
            cambiarVista(BtnMisTiendas, "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");
        } else {
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

    private void cambiarVista(Node nodo, String rutaFXML) {
        Platform.runLater(() -> {
            if (nodo.getScene() != null && nodo.getScene().getWindow() != null) {
                Stage stage = (Stage) nodo.getScene().getWindow();
                cambiosVistas.cambiarVista(stage, rutaFXML);
            } else {
                System.out.println("El nodo aún no está asociado a un Stage.");
            }
        });
    }
}
