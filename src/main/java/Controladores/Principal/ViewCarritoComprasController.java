package Controladores.Principal;

import DB.JDBC;
import Modelos.Producto;
import Servicios.Datos.MostrarCarrito;
import Servicios.Datos.UsuarioActivo;
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
import java.util.List;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.Locale;

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

        BigDecimal total = BigDecimal.ZERO;
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

            // Aquí corregimos para mostrar el precio unitario, NO multiplicado por la cantidad.
            Label precioProducto = new Label(FormatoUtil.formatearPrecio(producto.getPrecio()));
            precioProducto.setStyle("-fx-font-size: 14px;");

            Button btnEliminarProducto = new Button("Eliminar");
            btnEliminarProducto.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff;");
            btnEliminarProducto.setOnAction(event -> eliminarProductoDelCarrito(producto));

            hboxProducto.getChildren().addAll(imagenProducto, nombreProducto, spinnerCantidad, precioProducto, btnEliminarProducto);
            vboxProductos.getChildren().add(hboxProducto);

            // En este punto no sumamos el total, ya que debe calcularse solo en obtenerTotalCarrito().
        }

        // Llamamos a actualizarTotal() para recalcular después de cargar los productos
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
        mostrarCarrito.eliminarProductoDelCarrito(producto.getIdProducto(), UsuarioActivo.getIdCarrito());
        cargarProductosCarrito();
    }

    @FXML
    public void procesarCompra() {
        // Obtener el saldo actual y el total del carrito como BigDecimal
        BigDecimal saldoActual = UsuarioActivo.getSaldoActual();
        BigDecimal totalCarrito = obtenerTotalCarrito();  // Usamos el método correcto para obtener el total

        System.out.println("Saldo actual: " + saldoActual);
        System.out.println("Total del carrito: " + totalCarrito);

        // Verificar si el saldo es suficiente
        if (saldoActual.compareTo(totalCarrito) < 0) {
            lblAvisoSaldo.setText("Saldo Insuficiente");
        } else {
            // Descontar el total del carrito del saldo actual
            BigDecimal nuevoSaldo = saldoActual.subtract(totalCarrito);
            System.out.println("Nuevo saldo después de compra: " + nuevoSaldo);

            UsuarioActivo.setSaldoActual(nuevoSaldo);

            // Registrar la compra y actualizar el saldo
            registrarCompra(UsuarioActivo.getIdUsuario(), UsuarioActivo.getIdCarrito(), totalCarrito);
            actualizarSaldoUsuario(UsuarioActivo.getIdUsuario(), nuevoSaldo);

            // Mostrar éxito y vaciar el carrito
            lblAvisoSaldo.setText("Compra realizada con éxito");
            lblTotal.setText("Total: 0.00 COP");

            // Vaciar el carrito después de la compra
            mostrarCarrito.vaciarCarrito(UsuarioActivo.getIdCarrito());
            cargarProductosCarrito(); // Refrescar la vista del carrito vacío
        }
    }



    private BigDecimal obtenerTotalCarrito() {
        BigDecimal total = BigDecimal.ZERO;

        for (Node node : vboxProductos.getChildren()) {
            if (node instanceof HBox hbox) {
                // El precio del producto está en la posición 3 del HBox
                Label precioLabel = (Label) hbox.getChildren().get(3);
                // La cantidad seleccionada está en la posición 2 del HBox
                Spinner<Integer> spinnerCantidad = (Spinner<Integer>) hbox.getChildren().get(2);

                // Obtener el precio desde el label y convertirlo a BigDecimal
                String precioTexto = precioLabel.getText()
                        .replace("COP", "")
                        .replace(",", "")
                        .trim();

                try {
                    // Imprimir el valor original para depuración
                    System.out.println("Precio texto (original): " + precioTexto);

                    // Convertir el texto del precio en un BigDecimal
                    BigDecimal precio = new BigDecimal(precioTexto);

                    // Obtener la cantidad del spinner
                    BigDecimal cantidad = BigDecimal.valueOf(spinnerCantidad.getValue());

                    // Imprimir la cantidad para asegurarnos de que sea correcta
                    System.out.println("Cantidad del producto: " + cantidad);

                    // Multiplicar el precio por la cantidad para obtener el precio total del producto
                    BigDecimal precioTotalProducto = precio.multiply(cantidad);

                    // Añadir al total
                    total = total.add(precioTotalProducto);

                    // Imprimir el valor calculado de precio total para depuración
                    System.out.println("Precio calculado para el producto: " + precio + " x " + cantidad + " = " + precioTotalProducto);

                } catch (NumberFormatException e) {
                    System.out.println("Error al convertir el precio: " + precioTexto);
                }
            }
        }

        // Imprimir el total calculado antes de devolverlo
        System.out.println("Total calculado del carrito: " + total);

        return total.setScale(2, RoundingMode.HALF_UP);  // Asegurarse de que el total tenga dos decimales
    }



    public void registrarCompra(int idUsuario, int idCarrito, BigDecimal totalCompra) {
        String sqlInsertCompra = "INSERT INTO Compra (idUsuario, total_compra, fecha, hora) VALUES (?, ?, CURDATE(), CURTIME())";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sqlInsertCompra, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, idUsuario);
            pstmt.setBigDecimal(2, totalCompra.setScale(2, RoundingMode.HALF_UP));
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

    private void actualizarSaldoUsuario(int idUsuario, BigDecimal nuevoSaldo) {
        String sql = "UPDATE Usuario SET saldo_actual = ? WHERE idUsuario = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setBigDecimal(1, nuevoSaldo);
            pstmt.setInt(2, idUsuario);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
