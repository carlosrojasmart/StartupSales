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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import java.io.ByteArrayInputStream;
import java.sql.*;
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

    @FXML
    private Button btnContinuar;

    @FXML
    private Label lblAvisoSaldo;

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

            Spinner<Integer> spinnerCantidad = new Spinner<>();
            spinnerCantidad.setPrefWidth(55);
            spinnerCantidad.setMaxWidth(55);
            spinnerCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, producto.getCantidad()));
            spinnerCantidad.valueProperty().addListener((obs, oldValue, newValue) -> {
                mostrarCarrito.actualizarCantidadProducto(producto.getIdProducto(), UsuarioActivo.getIdCarrito(), newValue);
                actualizarTotal();
            });

            Label precioProducto = new Label(FormatoUtil.formatearPrecio(producto.getPrecio() * producto.getCantidad()));
            precioProducto.setStyle("-fx-font-size: 14px;");

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

                String precioTexto = precioLabel.getText()
                        .replace("$", "")
                        .replace("COP", "")
                        .replace(",", "")
                        .trim();

                try {
                    double precio = Double.parseDouble(precioTexto);
                    total += precio * spinnerCantidad.getValue();
                } catch (NumberFormatException e) {
                    System.out.println("Error al convertir el precio: " + precioTexto);
                }
            }
        }

        lblTotal.setText(String.format("Total: %.2f COP", total));
    }

    private void eliminarProductoDelCarrito(Producto producto) {
        mostrarCarrito.eliminarProductoDelCarrito(producto.getIdProducto(), UsuarioActivo.getIdCarrito());
        cargarProductosCarrito();
    }

    @FXML
    private void procesarCompra() {
        double saldoActual = UsuarioActivo.getSaldoActual(); // Obtiene el saldo actual del usuario
        double totalCarrito = obtenerTotalCarrito(); // Obtiene el total del carrito

        System.out.println("Saldo actual: " + saldoActual);
        System.out.println("Total del carrito: " + totalCarrito);

        if (saldoActual < totalCarrito) {
            lblAvisoSaldo.setText("Saldo Insuficiente");
        } else {
            // Descuenta el total del carrito del saldo actual del usuario
            double nuevoSaldo = saldoActual - totalCarrito;
            UsuarioActivo.setSaldoActual(nuevoSaldo);

            // Registra la compra en la base de datos
            registrarCompra(UsuarioActivo.getIdUsuario(), UsuarioActivo.getIdCarrito(), totalCarrito);

            // Actualiza el saldo del usuario en la base de datos
            actualizarSaldoUsuario(UsuarioActivo.getIdUsuario(), nuevoSaldo);

            lblAvisoSaldo.setText("Compra realizada con éxito");
            lblTotal.setText("Total: 0.00 COP");

            // Limpia el carrito después de la compra exitosa
            mostrarCarrito.vaciarCarrito(UsuarioActivo.getIdCarrito());
            cargarProductosCarrito(); // Refresca la vista del carrito para mostrarlo vacío
        }
    }

    private double obtenerTotalCarrito() {
        String totalTexto = lblTotal.getText().replace("Total: ", "").replace("COP", "").trim();

        try {
            // Cambia el formato para usar el separador de miles como coma y el punto como separador decimal
            NumberFormat format = NumberFormat.getInstance(Locale.forLanguageTag("es-CO")); // Esto ajusta para la región de Colombia
            Number number = format.parse(totalTexto);
            return number.doubleValue();
        } catch (ParseException e) {
            System.out.println("Error al convertir el total del carrito: " + totalTexto);
            e.printStackTrace();
            return 0.0;
        }
    }


    public void registrarCompra(int idUsuario, int idCarrito, double totalCompra) {
        String sqlInsertCompra = "INSERT INTO Compra (idUsuario, total_compra, fecha, hora) VALUES (?, ?, CURDATE(), CURTIME())";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sqlInsertCompra, Statement.RETURN_GENERATED_KEYS)) {
            // Asegúrate de que no estás multiplicando el total por 100
            pstmt.setInt(1, idUsuario);
            pstmt.setBigDecimal(2, BigDecimal.valueOf(totalCompra).setScale(2, RoundingMode.HALF_UP));
            int filasInsertadas = pstmt.executeUpdate();

            if (filasInsertadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int idCompra = rs.getInt(1);
                    registrarProductosDeCompra(idCompra, idCarrito);
                    System.out.println("Compra registrada con ID: " + idCompra + ", Total: " + totalCompra);
                }
            } else {
                System.out.println("No se pudo registrar la compra.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al registrar la compra: " + e.getMessage());
        }
    }


    private void registrarProductosDeCompra(int idCompra, int idCarrito) {
        String sqlInsertProductos = "INSERT INTO compra_producto (idCompra, idProducto, cantidad) " +
                "SELECT ?, idProducto, cantidad FROM carrito_producto WHERE idCarrito = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sqlInsertProductos)) {
            pstmt.setInt(1, idCompra);
            pstmt.setInt(2, idCarrito);
            int filasInsertadas = pstmt.executeUpdate();
            System.out.println("Productos de la compra registrados: " + filasInsertadas);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al registrar los productos de la compra: " + e.getMessage());
        }
    }

    public void actualizarSaldoUsuario(int idUsuario, double nuevoSaldo) {
        String sql = "UPDATE Usuario SET saldo_actual = ? WHERE idUsuario = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setDouble(1, nuevoSaldo);
            pstmt.setInt(2, idUsuario);
            int filasActualizadas = pstmt.executeUpdate();
            System.out.println("Saldo actualizado correctamente para el usuario " + idUsuario + ": " + filasActualizadas + " filas actualizadas.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al actualizar el saldo del usuario: " + e.getMessage());
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
        if (CambiosVistas.usuarioTieneCompras(UsuarioActivo.getIdUsuario())) {
            cambiarVista(BtnComprasAr, "/Vistas/PantallaCuenta/Compras/View-ComprasCreada.fxml");
        } else {
            cambiarVista(BtnComprasAr, "/Vistas/PantallaCuenta/Compras/View-Compras.fxml");
        }
    }

    @FXML
    public void mostrarCarrito() {
        CambiosVistas.setTerminoBusqueda("");
        cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");
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

}
