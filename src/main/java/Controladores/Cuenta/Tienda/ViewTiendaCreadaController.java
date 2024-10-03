package Controladores.Cuenta.Tienda;

import DB.JDBC;
import Servicios.Datos.UsuarioActivo;
import Servicios.Vistas.CambiosVistas;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewTiendaCreadaController {
    @FXML
    private TextField buscarProductos;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnMiPerfil;

    @FXML
    private Button BtnCompras;

    @FXML
    private Button BtnFacturacion;

    @FXML
    private Button BtnVolverInicio;

    @FXML
    private Button BtnCrearTienda;

    @FXML
    private GridPane contenedorTiendas; // El GridPane para contener las tiendas

    @FXML
    private ScrollPane scrollPaneTiendas; // El ScrollPane para las tiendas


    private CambiosVistas cambiosVistas = new CambiosVistas();

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        // Cargar las tiendas del usuario
        cargarTiendasDelUsuario();
    }

    /// Método para cargar dinámicamente las tiendas del usuario
    private void cargarTiendasDelUsuario() {
        int idUsuario = UsuarioActivo.getIdUsuario(); // Obtener el ID del usuario activo

        String query = "SELECT nombre, imagenTienda FROM Tienda WHERE idUsuario = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement stmt = conexion.prepareStatement(query)) {

            stmt.setInt(1, idUsuario);
            ResultSet resultSet = stmt.executeQuery();

            int column = 0;
            int row = 0;

            while (resultSet.next()) {
                String nombreTienda = resultSet.getString("nombre");
                byte[] imagenBytes = resultSet.getBytes("imagenTienda");

                // Crear dinámicamente los elementos para cada tienda
                agregarTienda(nombreTienda, imagenBytes, row, column);

                // Avanzar a la siguiente columna
                column++;

                // Si ya hay 3 tiendas en la fila, pasar a la siguiente fila
                if (column == 3) {
                    column = 0;
                    row++;
                }
            }

            // Ajustar el tamaño del GridPane para asegurarse de que el ScrollPane funcione
            ajustarGridPane();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para agregar cada tienda al GridPane
    private void agregarTienda(String nombreTienda, byte[] imagenBytes, int row, int column) {
        // Crear un ImageView para la imagen de la tienda
        ImageView imagenTienda = new ImageView();
        imagenTienda.setFitHeight(150);
        imagenTienda.setFitWidth(150);
        imagenTienda.setPreserveRatio(false);

        // Convertir el byte[] de la base de datos a una imagen y asignarla al ImageView
        if (imagenBytes != null) {
            Image imagen = new Image(new ByteArrayInputStream(imagenBytes));
            imagenTienda.setImage(imagen);
        }

        // Crear un Label para el nombre de la tienda
        Label nombreTiendaLabel = new Label(nombreTienda);
        nombreTiendaLabel.setStyle("-fx-font-size: 15px; -fx-alignment: center;");

        // Crear un botón para ir a la tienda
        Button btnIrATienda = new Button("Ir a tienda");
        btnIrATienda.setStyle("-fx-background-color: #000000; -fx-text-fill: white; -fx-font-family: 'MS Sans Serif';");
        btnIrATienda.setOnAction(event -> {
            // Aquí agregas el evento que lleva a la tienda correspondiente
            System.out.println("Ir a tienda: " + nombreTienda);
        });

        // Crear un VBox para agrupar la imagen, el nombre y el botón
        VBox tiendaBox = new VBox(10);
        tiendaBox.setPrefSize(150, 200);
        tiendaBox.getChildren().addAll(nombreTiendaLabel, imagenTienda, btnIrATienda);
        tiendaBox.setStyle("-fx-alignment: center; -fx-padding: 10 20 20 20;");

        // Agregar la tienda al GridPane en la fila y columna correspondiente
        contenedorTiendas.add(tiendaBox, column, row);
    }

    // Método para ajustar el tamaño del GridPane según las tiendas agregadas
    private void ajustarGridPane() {
        contenedorTiendas.setPrefHeight(Region.USE_COMPUTED_SIZE);
        contenedorTiendas.setPrefWidth(Region.USE_COMPUTED_SIZE);
        scrollPaneTiendas.setContent(contenedorTiendas);  // Asegurarse de que el ScrollPane contenga el GridPane correctamente
    }

    @FXML
    public void mostrarCarrito() {cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");}

    @FXML
    public void mostrarMiPerfil() {cambiarVista(BtnMiPerfil, "/Vistas/PantallaCuenta/MiPerfil/View-MiPerfil.fxml");}

    @FXML
    public void mostrarCompras(ActionEvent event) {cambiarVista(BtnCompras, "/Vistas/PantallaCuenta/Compras/View-Compras.fxml");}

    @FXML
    public void mostrarFacturacion(ActionEvent event) {cambiarVista(BtnFacturacion, "/Vistas/PantallaCuenta/Facturacion/View-Facturacion.fxml");}

    @FXML
    public void mostrarInicio(ActionEvent event) {cambiarVista(BtnVolverInicio, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");}

    @FXML
    public void crearTienda(ActionEvent event) {cambiarVista(BtnCrearTienda, "/Vistas/PantallaCuenta/Tienda/View-CreandoTienda.fxml");}

    private void cambiarVista(Node nodo, String rutaFXML) {
        Stage stage = (Stage) nodo.getScene().getWindow();
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }
}
