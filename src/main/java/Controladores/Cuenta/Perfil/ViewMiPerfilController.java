package Controladores.Cuenta.Perfil;

import Controladores.Vistas.CambiosVistas;
import Modelos.UsuarioActivo;
import Repositorios.Perfil.ModificarPerfil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

public class ViewMiPerfilController {

    @FXML
    private TextField buscarProductos;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnVolverInicio;

    @FXML
    private Button BtnCompras;

    @FXML
    private Button BtnTienda;

    @FXML
    private Button BtnFacturacion;

    @FXML
    private ImageView imagenPerfil;

    @FXML
    private Label nombreUsuario;

    @FXML
    private Label correoUsuario;

    @FXML
    private Button btnCambiarImagen;

    @FXML
    private TextField cambiarUsuario;

    @FXML
    private TextField cambiarCorreo;

    @FXML
    private TextField cambiarContraseña;

    @FXML
    private TextField cambiarTelefono;

    @FXML
    private TextField cambiarDireccion;

    @FXML
    private Button btnGuardarCambio;

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private ModificarPerfil modificarPerfil = new ModificarPerfil();

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        // Configuración de la acción de búsqueda
        buscarProductos.setOnAction(event -> realizarBusqueda());

        nombreUsuario.setText(UsuarioActivo.getNombre());
        correoUsuario.setText(UsuarioActivo.getCorreoElectronico());

        try {
            cargarImagenPerfil();
            cargarDatosUsuario();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarImagenPerfil() throws SQLException, IOException {
        byte[] imageBytes = modificarPerfil.cargarImagenPerfil();
        if (imageBytes != null) {
            Image imagen = new Image(new ByteArrayInputStream(imageBytes));
            imagenPerfil.setImage(imagen);
        }
    }

    private void cargarDatosUsuario() {
        try {
            String[] datosUsuario = modificarPerfil.obtenerDatosUsuario(UsuarioActivo.getIdUsuario());
            cambiarUsuario.setText(datosUsuario[0]);
            cambiarCorreo.setText(datosUsuario[1]);
            cambiarTelefono.setText(datosUsuario[2]);
            cambiarDireccion.setText(datosUsuario[3]);
            cambiarContraseña.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void realizarBusqueda() {
        String terminoBusqueda = buscarProductos.getText().trim();
        if (!terminoBusqueda.isEmpty()) {
            // Asigna el término de búsqueda para ser usado en la vista de resultados
            CambiosVistas.setTerminoBusqueda(terminoBusqueda);
            cambiarVista(buscarProductos, "/Vistas/PantallaPrincipal/View-BusquedaProductos.fxml");
        } else {
            System.out.println("El término de búsqueda está vacío.");
        }
    }

    @FXML
    public void guardarCambios() {
        try {
            String nuevoUsuario = cambiarUsuario.getText();
            String nuevoCorreo = cambiarCorreo.getText();
            String nuevaContraseña = cambiarContraseña.getText();
            String nuevoTelefono = cambiarTelefono.getText();
            String nuevaDireccion = cambiarDireccion.getText();

            modificarPerfil.guardarCambiosPerfil(UsuarioActivo.getIdUsuario(), nuevoUsuario, nuevoCorreo, nuevaContraseña, nuevoTelefono, nuevaDireccion);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cambiarImagenPerfil() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File archivoSeleccionado = fileChooser.showOpenDialog(null);

        if (archivoSeleccionado != null) {
            try {
                Image nuevaImagen = new Image(new FileInputStream(archivoSeleccionado));
                imagenPerfil.setImage(nuevaImagen);
                modificarPerfil.guardarImagenPerfil(archivoSeleccionado);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void mostrarCarrito() {
        cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");
    }

    @FXML
    public void mostrarCompras(ActionEvent event) {
        if (CambiosVistas.usuarioTieneCompras(UsuarioActivo.getIdUsuario())) {
            cambiarVista(BtnCompras, "/Vistas/PantallaCuenta/Compras/View-ComprasCreada.fxml");
        } else {
            cambiarVista(BtnCompras, "/Vistas/PantallaCuenta/Compras/View-Compras.fxml");
        }
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
}
