package Servicios.Datos;

import java.math.BigDecimal;
import java.sql.Connection;
import Modelos.UsuarioActivo;
import Repositorios.Datos.LoginRegister;

public class LoginRegisterService {

    private final LoginRegister loginRegister;

    // Constructor que recibe una instancia de LoginRegister
    public LoginRegisterService(LoginRegister loginRegister) {
        this.loginRegister = loginRegister;
    }

    // metodo que maneja el proceso de login de un usuario
    public boolean handleLogin(String username, String password) {
        // Verifica si las credenciales son válidas llamando al repositorio
        boolean loginExitoso = loginRegister.buscarUsuarioPorCorreo(username, password);

        // Si el login es exitoso, imprime un mensaje y retorna verdadero
        if (loginExitoso) {
            System.out.println("Login exitoso.");
            return true;
        }

        // Si el login falla, imprime un mensaje de error y retorna falso
        System.out.println("Error en login: usuario o contraseña incorrectos.");
        return false;
    }

    // metodo que registra un nuevo usuario en el sistema
    public boolean registrarUsuario(String usuario, String correo, String contraseña, String telefono, String direccion) {
        // Hashea la contraseña proporcionada
        String hashedPassword = loginRegister.hashPassword(contraseña);

        // Inserta el nuevo usuario en la base de datos y obtiene su ID
        int idUsuario = loginRegister.insertarUsuario(usuario, direccion, correo, telefono, hashedPassword);

        // Verifica si el usuario fue insertado correctamente (ID > 0)
        if (idUsuario > 0) {
            // Crea un carrito para el nuevo usuario y obtiene su ID de carrito
            int idCarrito = loginRegister.crearCarritoParaUsuario(idUsuario);

            // Registra al usuario como activo en el sistema mediante `UsuarioActivo`
            UsuarioActivo.setUsuarioActivo(idUsuario, usuario, correo, false, idCarrito, BigDecimal.ZERO, BigDecimal.ZERO);

            // Imprime mensaje de éxito y retorna verdadero
            System.out.println("Usuario registrado exitosamente.");
            return true;
        }

        // Si el registro falla, imprime un mensaje de error y retorna falso
        System.out.println("Error al registrar usuario.");
        return false;
    }

}