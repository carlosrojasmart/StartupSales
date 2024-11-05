package Servicios.Datos;

import DB.JDBC;
import Modelos.Tienda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MostrarTiendas {

    // Método para obtener las tiendas desde la base de datos
    public List<Tienda> obtenerTiendas(int idUsuario) {
        List<Tienda> tiendas = new ArrayList<>();
        String sql = "SELECT idTienda, nombre, descripcion, categoria, imagenTienda FROM Tienda WHERE idUsuario = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int idTienda = rs.getInt("idTienda");
                    String nombre = rs.getString("nombre");
                    String descripcion = rs.getString("descripcion");
                    String categoria = rs.getString("categoria");
                    byte[] imagen = rs.getBytes("imagenTienda");
                    tiendas.add(new Tienda(idTienda, nombre, descripcion, categoria, imagen));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tiendas;
    }

    // Método adicional para obtener una tienda por su ID
    public Tienda obtenerTiendaPorId(int idTienda) {
        Tienda tienda = null;
        String sql = "SELECT * FROM Tienda WHERE idTienda = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idTienda);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                tienda = new Tienda();
                tienda.setIdTienda(resultSet.getInt("idTienda"));
                tienda.setNombre(resultSet.getString("nombre"));
                tienda.setDescripcion(resultSet.getString("descripcion"));
                tienda.setCategoria(resultSet.getString("categoria"));
                tienda.setImagen(resultSet.getBytes("imagenTienda"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tienda;
    }

    public boolean eliminarTienda(int idTienda) {
        String eliminarProductosSQL = "DELETE FROM Producto WHERE idTienda = ?";
        String eliminarTiendaSQL = "DELETE FROM Tienda WHERE idTienda = ?";

        try (Connection conexion = JDBC.ConectarBD()) {
            // Primero, elimina todos los productos asociados a la tienda
            try (PreparedStatement pstmtProductos = conexion.prepareStatement(eliminarProductosSQL)) {
                pstmtProductos.setInt(1, idTienda);
                pstmtProductos.executeUpdate();
            }

            // Luego, elimina la tienda
            try (PreparedStatement pstmtTienda = conexion.prepareStatement(eliminarTiendaSQL)) {
                pstmtTienda.setInt(1, idTienda);
                return pstmtTienda.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Tienda> buscarTiendasPorNombre(String nombreTienda) {
        List<Tienda> tiendas = new ArrayList<>();
        String sql = "SELECT * FROM Tienda WHERE nombre LIKE ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nombreTienda + "%");
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                Tienda tienda = new Tienda();
                tienda.setIdTienda(resultSet.getInt("idTienda"));
                tienda.setNombre(resultSet.getString("nombre"));
                tienda.setDescripcion(resultSet.getString("descripcion"));
                tienda.setCategoria(resultSet.getString("categoria"));

                // Verificar si el campo imagen no es null antes de asignarlo
                byte[] imagen = resultSet.getBytes("imagenTienda");
                if (imagen != null) {
                    tienda.setImagen(imagen);
                }

                tiendas.add(tienda);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tiendas;
    }

    public void actualizarTienda(Tienda tienda) {
        String sql = "UPDATE Tienda SET nombre = ?, descripcion = ?, categoria = ?, imagenTienda = ? WHERE idTienda = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, tienda.getNombre());
            pstmt.setString(2, tienda.getDescripcion());
            pstmt.setString(3, tienda.getCategoria());
            pstmt.setBytes(4, tienda.getImagen());
            pstmt.setInt(5, tienda.getIdTienda());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> obtenerCategorias() {
        List<String> categorias = new ArrayList<>();
        categorias.add("Electrónica");
        categorias.add("Ropa y Moda");
        categorias.add("Hogar y Jardín");
        categorias.add("Salud y Belleza");
        categorias.add("Deportes");
        categorias.add("Juguetes");
        categorias.add("Alimentos");
        categorias.add("Automóviles");
        categorias.add("Libros");
        categorias.add("Mascotas");
        return categorias;
    }

}
