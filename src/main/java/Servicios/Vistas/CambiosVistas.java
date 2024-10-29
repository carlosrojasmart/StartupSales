package Servicios.Vistas;

import Modelos.Tienda;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import DB.JDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CambiosVistas {

    public void cambiarVista(Stage stage, String rutaFXML) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(rutaFXML));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void cargarVista(Stage stage, String rutaFXML, String titulo, double ancho, double alto) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(rutaFXML));
            Scene scene = new Scene(root, ancho, alto);
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String terminoBusqueda;

    public static void setTerminoBusqueda(String termino) {
        terminoBusqueda = termino;
    }

    public static String getTerminoBusqueda() {
        return terminoBusqueda;
    }

    private static Tienda tiendaSeleccionada;

    public static void setTiendaSeleccionada(Tienda tienda) {
        tiendaSeleccionada = tienda;
    }

    public static Tienda getTiendaSeleccionada() {
        return tiendaSeleccionada;
    }

    public static void setIdTiendaSeleccionada(int idTienda) {
        if (tiendaSeleccionada == null) {
            tiendaSeleccionada = new Tienda();
        }
        tiendaSeleccionada.setIdTienda(idTienda);
    }

    public static int getIdTiendaSeleccionada() {
        return tiendaSeleccionada != null ? tiendaSeleccionada.getIdTienda() : -1;
    }

    public static boolean usuarioTieneCompras(int idUsuario) {
        String sql = "SELECT COUNT(*) AS total FROM Compra WHERE idUsuario = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int totalCompras = rs.getInt("total");
                return totalCompras > 0; // Retorna true si el usuario tiene al menos una compra
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al verificar las compras del usuario: " + e.getMessage());
        }

        return false;
    }

}
