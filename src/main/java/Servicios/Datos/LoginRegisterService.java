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

    // Sobrecarga del método handleLogin que permite el login sin una conexión explícita
    public boolean handleLogin(String username, String password) {
        // Llama al método `handleLogin` con `null` para conexiónH2, indicando que se usará la base de datos principal
        return handleLogin(username, password, null);
    }

    // Método de login que acepta una conexión específica para usar H2 o base de datos de producción
    public boolean handleLogin(String username, String password, Connection conexionH2) {
        // Verifica si usar la conexión H2 o la base de datos principal según el parámetro de conexión
        boolean loginExitoso = (conexionH2 == null)
                ? loginRegister.buscarUsuarioPorCorreo(username, password) // Usa base de datos de producción
                : loginRegister.buscarUsuarioPorCorreoH2(username, password, conexionH2); // Usa base de datos H2

        // Imprime un mensaje y retorna verdadero si el login es exitoso
        if (loginExitoso) {
            System.out.println("Login exitoso.");
            return true;
        }

        // Si el login falla, imprime un mensaje de error y retorna falso
        System.out.println("Error en login: usuario o contraseña incorrectos.");
        return false;
    }

    // Registra un nuevo usuario en el sistema
    public boolean registrarUsuario(String usuario, String correo, String contraseña, String telefono, String direccion) {
        // Hashea la contraseña proporcionada para almacenamiento seguro
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
