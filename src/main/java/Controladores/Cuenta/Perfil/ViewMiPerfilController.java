package Controladores.Cuenta.Perfil;

import Servicios.Vistas.CambiosVistas;
import Servicios.Datos.UsuarioActivo;
import Servicios.Perfil.ModificarPerfil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.FileInputStream;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import java.io.File;
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
        // Configuración del buscador y carrito
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        // Cargar datos del usuario en la vista
        String nombre = UsuarioActivo.getNombre();
        String correo = UsuarioActivo.getCorreoElectronico();
        nombreUsuario.setText(nombre);
        correoUsuario.setText(correo);

        try {
            // Cargar la imagen de perfil desde la base de datos
            Image imagen = modificarPerfil.cargarImagenPerfil();
            if (imagen != null) {
                imagenPerfil.setImage(imagen);
            }

            // Cargar los demás datos del usuario en los TextField
            cargarDatosUsuario();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarDatosUsuario() {
        try {
            // Obtener los datos actuales del usuario desde la base de datos
            String[] datosUsuario = modificarPerfil.obtenerDatosUsuario(UsuarioActivo.getIdUsuario());

            // Asignar los valores a los TextField correspondientes
            cambiarUsuario.setText(datosUsuario[0]);
            cambiarCorreo.setText(datosUsuario[1]);
            cambiarTelefono.setText(datosUsuario[2]);
            cambiarDireccion.setText(datosUsuario[3]);
            cambiarContraseña.setText(""); // Por seguridad, no mostrar la contraseña

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void guardarCambios() {
        try {
            // Obtener los datos ingresados por el usuario
            String nuevoUsuario = cambiarUsuario.getText();
            String nuevoCorreo = cambiarCorreo.getText();
            String nuevaContraseña = cambiarContraseña.getText();
            String nuevoTelefono = cambiarTelefono.getText();
            String nuevaDireccion = cambiarDireccion.getText();

            // Guardar los cambios en la base de datos
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
                // Cargar la imagen en el ImageView
                Image nuevaImagen = new Image(new FileInputStream(archivoSeleccionado));
                imagenPerfil.setImage(nuevaImagen);

                // Guardar la imagen en la base de datos usando ModificarPerfil
                modificarPerfil.guardarImagenPerfil(archivoSeleccionado);

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Métodos para cambiar las vistas
    @FXML
    public void mostrarCarrito() {
        cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");
    }

    @FXML
    public void mostrarCompras(ActionEvent event) {
        cambiarVista(BtnCompras, "/Vistas/PantallaCuenta/Compras/View-Compras.fxml");
    }

    @FXML
    public void mostrarMisTiendas(ActionEvent event) {
        // Verificar si el usuario es vendedor
        if (UsuarioActivo.isVendedor()) {
            // Si es vendedor, ir a la vista de tienda ya creada
            cambiarVista(BtnTienda, "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");
        } else {
            // Si no es vendedor, ir a la vista para crear la tienda
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
