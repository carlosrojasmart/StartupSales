package Controladores.Principal;

import DB.JDBC;
import Modelos.Producto;
import Servicios.Datos.MostrarCarrito;
import Servicios.Datos.UsuarioActivo;
import Servicios.Vistas.BusquedaUtil;
import Servicios.Vistas.CambiosVistas;
import Servicios.Vistas.FormatoUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static Servicios.Vistas.BusquedaUtil.realizarBusqueda;


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

    private final MostrarCarrito mostrarCarrito = new MostrarCarrito();
    private final CambiosVistas cambiosVistas = new CambiosVistas();

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());
        usuarioIcono.setOnMouseClicked(event -> mostrarMiPerfil());

        // Cargar los productos del carrito
        cargarProductosCarrito();

        // Obtener el término de búsqueda de la clase CambiosVistas
        String terminoBusqueda = CambiosVistas.getTerminoBusqueda();
        if (terminoBusqueda != null && !terminoBusqueda.isEmpty()) {
            buscarProductos.setText(terminoBusqueda);
            realizarBusqueda(); // Realiza la búsqueda si el término no está vacío
        }

        // Asignar la funcionalidad de búsqueda cuando se presiona "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());
    }

    @FXML
    private void realizarBusqueda() {
        String terminoBusqueda = buscarProductos.getText().trim();

        if (!terminoBusqueda.isEmpty()) {
            // Almacenar el término de búsqueda para la vista de búsqueda de productos
            CambiosVistas.setTerminoBusqueda(terminoBusqueda);

            // Cambiar a la vista de búsqueda de productos
            cambiarVista(buscarProductos, "/Vistas/PantallaPrincipal/View-BusquedaProductos.fxml");
        } else {
            System.out.println("El término de búsqueda está vacío.");
        }
    }

    private void cargarProductosCarrito() {
        List<Producto> productos = mostrarCarrito.obtenerProductosDeCarrito(UsuarioActivo.getIdCarrito());
        vboxProductos.getChildren().clear();

        double total = 0;

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

            // Spinner ajustado para ser más estrecho
            Spinner<Integer> spinnerCantidad = new Spinner<>();
            spinnerCantidad.setPrefWidth(55); // Ajustar el ancho del Spinner
            spinnerCantidad.setMaxWidth(55);  // Limitar el ancho máximo del Spinner
            spinnerCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, producto.getCantidad()));
            spinnerCantidad.valueProperty().addListener((obs, oldValue, newValue) -> {
                mostrarCarrito.actualizarCantidadProducto(producto.getIdProducto(), UsuarioActivo.getIdCarrito(), newValue);
                actualizarTotal();
            });

            // Formatear el precio del producto usando COP
            Label precioProducto = new Label(FormatoUtil.formatearPrecio(producto.getPrecio() * producto.getCantidad()));
            precioProducto.setStyle("-fx-font-size: 14px;");

            // Botón con estilo restaurado
            Button btnEliminarProducto = new Button("Eliminar");
            btnEliminarProducto.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff;");
            btnEliminarProducto.setOnAction(event -> eliminarProductoDelCarrito(producto));

            hboxProducto.getChildren().addAll(imagenProducto, nombreProducto, spinnerCantidad, precioProducto, btnEliminarProducto);
            vboxProductos.getChildren().add(hboxProducto);

            total += producto.getPrecio() * producto.getCantidad();
        }

        lblTotal.setText("Total: " + FormatoUtil.formatearPrecio(total));
    }

    private void actualizarTotal() {
        double total = 0;

        for (Node node : vboxProductos.getChildren()) {
            if (node instanceof HBox hbox) {
                Label precioLabel = (Label) hbox.getChildren().get(3);
                Spinner<Integer> spinnerCantidad = (Spinner<Integer>) hbox.getChildren().get(2);

                // Reemplazar caracteres innecesarios y limpiar el texto del precio
                String precioTexto = precioLabel.getText()
                        .replace("$", "") // Eliminar el símbolo de moneda
                        .replace("COP", "") // Eliminar cualquier referencia a "COP" si es que la tienes
                        .replace(",", "") // Eliminar comas (si las hay)
                        .trim(); // Eliminar espacios en blanco

                try {
                    double precio = Double.parseDouble(precioTexto);
                    total += precio * spinnerCantidad.getValue();
                } catch (NumberFormatException e) {
                    System.out.println("Error al convertir el precio: " + precioTexto);
                    e.printStackTrace();
                }
            }
        }

        lblTotal.setText(String.format("Total: %.2f COP", total));
    }

    private void eliminarProductoDelCarrito(Producto producto) {
        mostrarCarrito.eliminarProductoDelCarrito(producto.getIdProducto(), UsuarioActivo.getIdCarrito());
        cargarProductosCarrito();
    }

    private void cambiarVista(Node nodo, String rutaFXML) {
        try {
            Platform.runLater(() -> {
                if (nodo.getScene() != null && nodo.getScene().getWindow() != null) {
                    Stage stage = (Stage) nodo.getScene().getWindow();
                    cambiosVistas.cambiarVista(stage, rutaFXML);
                } else {
                    System.out.println("El nodo aún no está asociado a un Stage.");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cambiar de vista: " + e.getMessage());
        }
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
        cambiarVista(BtnComprasAr, "/Vistas/PantallaCuenta/Compras/View-Compras.fxml");
    }

    @FXML
    public void mostrarCarrito() {
        CambiosVistas.setTerminoBusqueda("");
        cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");
    }

}
