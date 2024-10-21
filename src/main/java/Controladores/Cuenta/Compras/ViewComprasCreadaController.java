package Controladores.Cuenta.Compras;

import DB.JDBC;
import Modelos.Compra;
import Servicios.Datos.UsuarioActivo;
import Servicios.Vistas.CambiosVistas;
import Servicios.Vistas.FormatoUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ViewComprasCreadaController {

    @FXML
    private TextField buscarProductos;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnVolverInicio;

    @FXML
    private Button BtnMiPerfil;

    @FXML
    private Button BtnTienda;

    @FXML
    private Button BtnFacturacion;

    @FXML
    private VBox vboxCompras;

    private final CambiosVistas cambiosVistas = new CambiosVistas();

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());
        buscarProductos.setOnAction(event -> realizarBusqueda());

        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        // Cargar las compras realizadas
        cargarCompras();
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

    @FXML
    public void mostrarCarrito() {
        cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");
    }

    @FXML
    public void mostrarMiPerfil() {
        cambiarVista(BtnMiPerfil, "/Vistas/PantallaCuenta/MiPerfil/View-MiPerfil.fxml");
    }

    @FXML
    public void mostrarMisTiendas(ActionEvent event) {
        if (UsuarioActivo.isVendedor()) {
            cambiarVista(BtnTienda, "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");
        } else {
            cambiarVista(BtnTienda, "/Vistas/PantallaCuenta/Tienda/View-CrearTienda.fxml");
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

    private void cambiarVista(Node nodo, String rutaFXML) {
        Stage stage = (Stage) nodo.getScene().getWindow();
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }

    private void cargarCompras() {
        List<Compra> compras = obtenerComprasUsuario(UsuarioActivo.getIdUsuario());
        vboxCompras.getChildren().clear();

        for (Compra compra : compras) {
            HBox hboxCompra = new HBox(10);
            hboxCompra.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-border-color: #dddddd;");
            hboxCompra.setPrefWidth(600);

            // Label para la fecha
            Label fechaLabel = new Label("Fecha: " + compra.getFecha());
            fechaLabel.setStyle("-fx-font-size: 14px;");

            // Label para la hora
            Label horaLabel = new Label("Hora: " + compra.getHora());
            horaLabel.setStyle("-fx-font-size: 14px;");

            // Label para el total de la compra
            Label totalCompra = new Label("Total: " + FormatoUtil.formatearPrecio(compra.getTotalCompra()));
            totalCompra.setStyle("-fx-font-size: 14px;");

            // Crear un VBox para los productos y añadir cada producto en una nueva línea con el prefijo "Productos:"
            VBox vboxProductos = new VBox(5); // Añadir un espacio de 5 entre cada producto
            Label productosTitulo = new Label("Productos:");
            productosTitulo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            vboxProductos.getChildren().add(productosTitulo);

            String[] productosArray = compra.getProductosResumen().split(", ");
            for (String producto : productosArray) {
                Label productoLabel = new Label(producto);
                productoLabel.setStyle("-fx-font-size: 14px;");
                vboxProductos.getChildren().add(productoLabel);
            }

            // Crear un VBox para organizar los elementos de fecha y hora juntos
            VBox vboxFechaHora = new VBox(5);
            vboxFechaHora.getChildren().addAll(fechaLabel, horaLabel);

            // Añadir la información a la HBox
            hboxCompra.getChildren().addAll(vboxFechaHora, totalCompra, vboxProductos);
            vboxCompras.getChildren().add(hboxCompra);
        }
    }


    private List<Compra> obtenerComprasUsuario(int idUsuario) {
        List<Compra> compras = new ArrayList<>();
        String sql = "SELECT c.idCompra, c.total_compra, c.fecha, c.hora, " +
                "GROUP_CONCAT(CONCAT(p.nombre, ' x', cp.cantidad) SEPARATOR ', ') AS productos " +
                "FROM Compra c " +
                "JOIN compra_producto cp ON c.idCompra = cp.idCompra " +
                "JOIN Producto p ON cp.idProducto = p.idProducto " +
                "WHERE c.idUsuario = ? " +
                "GROUP BY c.idCompra";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Compra compra = new Compra();
                compra.setIdCompra(rs.getInt("idCompra"));
                compra.setTotalCompra(rs.getDouble("total_compra"));
                compra.setFecha(rs.getDate("fecha").toLocalDate());
                compra.setHora(rs.getTime("hora").toLocalTime());

                // Obtener el resumen de productos con sus cantidades
                String productosResumen = rs.getString("productos");
                compra.setProductosResumen(productosResumen);

                compras.add(compra);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener las compras del usuario: " + e.getMessage());
        }

        return compras;
    }

}
