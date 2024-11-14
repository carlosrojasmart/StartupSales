package Repositorios.Tienda;

import DB.JDBC;
import Modelos.Tienda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MostrarTiendas {

    // Metodo para obtener las tiendas de un usuario desde la base de datos
    public List<Tienda> obtenerTiendas(int idUsuario) {
        List<Tienda> tiendas = new ArrayList<>();
        //consulta para obtener datos de la Tienda de un usuario
        String sql = "SELECT idTienda, nombre, descripcion, categoria, imagenTienda FROM Tienda WHERE idUsuario = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);//establece el parámetro de la consulta en el indice 1
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {//iterar sobre los resultados
                    int idTienda = rs.getInt("idTienda");
                    String nombre = rs.getString("nombre");
                    String descripcion = rs.getString("descripcion");
                    String categoria = rs.getString("categoria");
                    byte[] imagen = rs.getBytes("imagenTienda");//obtener la imagen de la tienda
                    tiendas.add(new Tienda(idTienda, nombre, descripcion, categoria, imagen));//añadir la tienda a la lista
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tiendas;
    }

    // Metodo adicional para obtener una tienda por su ID
    public Tienda obtenerTiendaPorId(int idTienda) {
        Tienda tienda = null;//inicializar la variable tienda como null
        //consulta para obtener una tienda por su ID
        String sql = "SELECT * FROM Tienda WHERE idTienda = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idTienda);//establecer el parámetro del ID de la tienda en la consulta
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {//verificar si la consulta devuelve algún registro
                //si se encuentra la tienda, crear una nueva instancia de Tienda
                tienda = new Tienda();
                //asigna los valores de la tienda desde el ResultSet a la nueva instancia de Tienda
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


    // Metodo para eliminar una tienda, eliminando primero los productos asociados
    public boolean eliminarTienda(int idTienda) {
        String eliminarProductosSQL = "DELETE FROM Producto WHERE idTienda = ?";//consulta para eliminar los productos
        String eliminarTiendaSQL = "DELETE FROM Tienda WHERE idTienda = ?";//consulta para eliminar la tienda

        try (Connection conexion = JDBC.ConectarBD()) {
            //primero, eliminar todos los productos de la tienda
            try (PreparedStatement pstmtProductos = conexion.prepareStatement(eliminarProductosSQL)) {
                pstmtProductos.setInt(1, idTienda);//establecer el parámetro
                pstmtProductos.executeUpdate();
            }
            //luego, eliminar la tienda
            try (PreparedStatement pstmtTienda = conexion.prepareStatement(eliminarTiendaSQL)) {
                pstmtTienda.setInt(1, idTienda);//establecer el parámetro
                return pstmtTienda.executeUpdate() > 0;//si se eliminó la tienda, retorna true
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para buscar tiendas por nombre
    public List<Tienda> buscarTiendasPorNombre(String nombreTienda) {
        List<Tienda> tiendas = new ArrayList<>(); //lista para almacenar las tiendas encontradas
        String sql = "SELECT * FROM Tienda WHERE nombre LIKE ?"; //consulta para buscar tiendas en la que el nombre sea el buscado

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nombreTienda + "%"); //establecer el parámetro de búsqueda
            ResultSet resultSet = pstmt.executeQuery();
            //itera sobre los resultados
            while (resultSet.next()) {
                Tienda tienda = new Tienda(); // Crear una nueva instancia de Tienda
                tienda.setIdTienda(resultSet.getInt("idTienda"));//establecer el ID de la tienda
                tienda.setNombre(resultSet.getString("nombre"));//establecer el nombre de la tienda
                tienda.setDescripcion(resultSet.getString("descripcion"));//establecer la descripción de la tienda
                tienda.setCategoria(resultSet.getString("categoria"));//establecer la categoría de la tienda
                //verificar si el campo imagen no es null antes de asignarlo
                byte[] imagen = resultSet.getBytes("imagenTienda");
                if (imagen != null) {
                    tienda.setImagen(imagen);//asignar la imagen de la tienda si existe
                }

                tiendas.add(tienda); //añadir la tienda a la lista
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tiendas;
    }

    // Metodo para actualizar una tienda
    public void actualizarTienda(Tienda tienda) {
        String sql = "UPDATE Tienda SET nombre = ?, descripcion = ?, categoria = ?, imagenTienda = ? WHERE idTienda = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            //etablecer los parámetros de la tienda que se actualizarán
            pstmt.setString(1, tienda.getNombre());
            pstmt.setString(2, tienda.getDescripcion());
            pstmt.setString(3, tienda.getCategoria());
            pstmt.setBytes(4, tienda.getImagen());//esstablecer la imagen de la tienda
            pstmt.setInt(5, tienda.getIdTienda());//establecer el ID de la tienda que se va a actualizar

            pstmt.executeUpdate(); // Ejecutar la actualización

        } catch (Exception e) {
            e.printStackTrace(); // Imprimir el error si ocurre una excepción
        }
    }

    // Metodo para obtener la lista de categorías disponibles para las tiendas
    public List<String> obtenerCategorias() {
        List<String> categorias = new ArrayList<>();//lista para almacenar las categorías
        //añadir categorías yaupredefinidas
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
        return categorias; //retorna la lista de categorías disponibles
    }

    // Metodo para obtener las tiendas destacadas basadas en las ventas
    public List<Tienda> obtenerTiendasDestacadas() {
        List<Tienda> tiendasDestacadas = new ArrayList<>(); //lista para almacenar las tiendas destacadas
        String sql = """
    SELECT t.idTienda, t.nombre, t.descripcion, t.categoria, t.imagenTienda, SUM(cp.cantidad) as totalVentas
    FROM Tienda t
    JOIN Producto p ON t.idTienda = p.idTienda
    JOIN compra_producto cp ON p.idProducto = cp.idProducto
    GROUP BY t.idTienda
    ORDER BY totalVentas DESC
    LIMIT 5
    """;//consulta para obtener las 5 tiendas con más ventas

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            //iterar sobre los resultados
            while (rs.next()) {
                Tienda tienda = new Tienda();//creauna nueva instancia de Tienda
                tienda.setIdTienda(rs.getInt("idTienda"));//establecer el ID de la tienda
                tienda.setNombre(rs.getString("nombre"));//establecer el nombre de la tienda
                tienda.setDescripcion(rs.getString("descripcion"));//establecer la descripción de la tienda
                tienda.setCategoria(rs.getString("categoria"));//establecer la categoría de la tienda
                tienda.setImagen(rs.getBytes("imagenTienda"));//establecer la imagen de la tienda
                tiendasDestacadas.add(tienda);//añade la tienda destacada a la lista
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tiendasDestacadas;
    }
}
