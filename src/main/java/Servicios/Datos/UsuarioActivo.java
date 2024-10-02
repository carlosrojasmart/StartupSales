package Servicios.Datos;

public class UsuarioActivo {
    private static int idUsuario;
    private static String nombre;
    private static String correoElectronico;

    public static void setUsuarioActivo(int id, String nombreUsuario, String correo) {
        idUsuario = id;
        nombre = nombreUsuario;
        correoElectronico = correo;
    }

    public static int getIdUsuario() {
        return idUsuario;
    }

    public static String getNombre() {
        return nombre;
    }

    public static String getCorreoElectronico() {
        return correoElectronico;
    }
}
