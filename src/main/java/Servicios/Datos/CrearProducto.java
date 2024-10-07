package Servicios.Datos;

import Controladores.Cuenta.Tienda.ViewEditarProductoController;
import DB.JDBC;
import Modelos.Producto;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrearProducto {

    public boolean crearProducto(Producto producto, Stage stage) {
        // Generar un idProducto aleatorio antes de guardar
        producto.setIdProducto(generarIdProductoAleatorio());

        String sql = "INSERT INTO Producto (idProducto, nombre, precio, descripcion, stock, categoria, imagenProducto, idTienda) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, producto.getIdProducto());
            pstmt.setString(2, producto.getNombre());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setString(4, producto.getDescripcion());
            pstmt.setInt(5, producto.getStock());
            pstmt.setString(6, producto.getCategoria());
            pstmt.setBytes(7, producto.getImagenProducto());
            pstmt.setInt(8, producto.getIdTienda());

            int filasInsertadas = pstmt.executeUpdate();
            if (filasInsertadas > 0) {
                // Cambiar de vista a la tienda después de la creación exitosa
                irAVistaTienda(stage);
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void irAVistaTienda(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/PantallaCuenta/Tienda/View-MirarTienda.fxml"));
            Parent root = loader.load();

            // Cambiar la escena del stage a la vista de la tienda
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la vista de la tienda.");
        }
    }

    // Método para generar un idProducto aleatorio
    public int generarIdProductoAleatorio() {
        Random random = new Random();
        return random.nextInt(900000) + 100000; // Generar un idProducto entre 100000 y 999999
    }

    public List<Producto> obtenerProductosDeTienda(int idTienda) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM Producto WHERE idTienda = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idTienda);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(resultSet.getInt("idProducto"));
                producto.setNombre(resultSet.getString("nombre"));
                producto.setPrecio(resultSet.getDouble("precio"));
                producto.setDescripcion(resultSet.getString("descripcion"));
                producto.setStock(resultSet.getInt("stock"));
                producto.setCategoria(resultSet.getString("categoria"));
                producto.setImagenProducto(resultSet.getBytes("imagenProducto"));

                productos.add(producto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productos;
    }

    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE Producto SET nombre = ?, precio = ?, descripcion = ?, stock = ?, categoria = ?, imagenProducto = ? WHERE idProducto = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setDouble(2, producto.getPrecio());
            pstmt.setString(3, producto.getDescripcion());
            pstmt.setInt(4, producto.getStock());
            pstmt.setString(5, producto.getCategoria());
            pstmt.setBytes(6, producto.getImagenProducto());
            pstmt.setInt(7, producto.getIdProducto());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarProducto(int idProducto) {
        String sql = "DELETE FROM Producto WHERE idProducto = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idProducto);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void editarProducto(Stage stage, Producto producto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/PantallaCuenta/Tienda/View-EditarProducto.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la vista de edición
            ViewEditarProductoController controller = loader.getController();
            controller.setProductoSeleccionado(producto); // Pasar el producto al controlador de edición

            // Mostrar la nueva vista
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la vista de edición de producto.");
        }
    }

}
