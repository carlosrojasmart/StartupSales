package Servicios.Datos;

import java.math.BigDecimal;
import Modelos.UsuarioActivo;
import Repositorios.Datos.LoginRegister;

public class LoginRegisterService {

    private final LoginRegister loginRegister;

    public LoginRegisterService(LoginRegister loginRegister) {
        this.loginRegister = loginRegister;
    }

    public boolean handleLogin(String username, String password) {
        boolean loginExitoso = loginRegister.buscarUsuarioPorCorreo(username, password);
        if (loginExitoso) {
            System.out.println("Login exitoso.");
            return true;
        }
        System.out.println("Error en login: usuario o contraseña incorrectos.");
        return false;
    }

    public boolean registrarUsuario(String usuario, String correo, String contraseña, String telefono, String direccion) {
        String hashedPassword = loginRegister.hashPassword(contraseña);
        int idUsuario = loginRegister.insertarUsuario(usuario, direccion, correo, telefono, hashedPassword);
        if (idUsuario > 0) {
            int idCarrito = loginRegister.crearCarritoParaUsuario(idUsuario);
            UsuarioActivo.setUsuarioActivo(idUsuario, usuario, correo, false, idCarrito, BigDecimal.ZERO, BigDecimal.ZERO);
            System.out.println("Usuario registrado exitosamente.");
            return true;
        }
        System.out.println("Error al registrar usuario.");
        return false;
    }
}