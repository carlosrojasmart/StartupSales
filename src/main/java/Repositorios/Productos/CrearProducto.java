package Repositorios.Productos;

import DB.JDBC;
import Modelos.Producto;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrearProducto {

    private File archivoImagen;  //declara un objeto de tipo File que almacenará la imagen del producto

    public void setArchivoImagen(File archivoImagen) {
        this.archivoImagen = archivoImagen;//establece el archivo de imagen al producto
    }

    public boolean crearProducto(Producto producto) {
        //consulta para insertar un nuevo producto en la base de datos
        String sql = "INSERT INTO Producto (nombre, precio, descripcion, stock, categoria, imagenProducto, idTienda) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql);
             FileInputStream fis = archivoImagen != null ? new FileInputStream(archivoImagen) : null) {//si hay una imagen se obtiene como flujo de entrada

            //establece los parámetros de la consulta
            pstmt.setString(1, producto.getNombre());//asigna el nombre del producto al primer parámetro
            pstmt.setBigDecimal(2, producto.getPrecio());//asigna el precio del producto al segundo parámetro
            pstmt.setString(3, producto.getDescripcion());//asigna la descripción del producto al tercer parámetro
            pstmt.setInt(4, producto.getStock());//asigna el stock del producto al cuarto parámetro
            pstmt.setString(5, producto.getCategoria());//asigna la categoría del producto al quinto parámetro

            //si hay una imagen (archivoImagen no es null), la asigna al parámetro correspondiente en la consulta
            if (fis != null) {
                pstmt.setBinaryStream(6, fis, (int) archivoImagen.length());//establece el flujo binario de la imagen en el parámetro 6
            } else {
                pstmt.setNull(6, java.sql.Types.BLOB);//si no hay imagen, asigna un valor nulo al parámetro de la imagen.
            }//BLOB = almacena grandes cantidades de datos binarios

            pstmt.setInt(7, producto.getIdTienda());//asigna el ID de la tienda al parámetro 7

            return pstmt.executeUpdate() > 0; //ejecuta la consulta de inserción y devuelve true si se insertó correctamente
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;  //si hay error retorna false.
        }
    }

    public boolean actualizarProducto(Producto producto, File archivoImagen) {
        //onsulta para actualizar un producto. Si se pasa una imagen, se actualizará también.
        String sql = archivoImagen != null ?
                "UPDATE Producto SET nombre = ?, precio = ?, descripcion = ?, stock = ?, categoria = ?, imagenProducto = ? WHERE idProducto = ?" :
                "UPDATE Producto SET nombre = ?, precio = ?, descripcion = ?, stock = ?, categoria = ? WHERE idProducto = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            //establece los parámetros de la consulta
            pstmt.setString(1, producto.getNombre());//asigna el nombre del producto
            pstmt.setBigDecimal(2, producto.getPrecio());//asigna el precio del producto
            pstmt.setString(3, producto.getDescripcion());//asigna la descripción del producto
            pstmt.setInt(4, producto.getStock());//signa el stock del producto
            pstmt.setString(5, producto.getCategoria());//asigna la categoría del product

            if (archivoImagen != null) {//si se pasa una imagen, se actualizará también en la base de datos
                try (FileInputStream fis = new FileInputStream(archivoImagen)) {//crea un flujo de entrada para la imagen
                    pstmt.setBinaryStream(6, fis, (int) archivoImagen.length());//asigna la imagen al parámetro 6
                    pstmt.setInt(7, producto.getIdProducto());//establece el ID del producto para la actualización
                }
            } else {
                pstmt.setInt(6, producto.getIdProducto());//si no se pasa imagen, solo se actualiza el producto sin la imagen.
            }

            return pstmt.executeUpdate() > 0;//ejecuta la consulta de actualización y devuelve true si fue exitosa
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;  // si hay error rtorna false
        }
    }

    public boolean eliminarProducto(int idProducto) {
        //consultas para eliminar el producto del carrito y de la tabla de productos
        String sqlEliminarDeCarrito = "DELETE FROM carrito_producto WHERE idProducto = ?";
        String sqlEliminarProducto = "DELETE FROM Producto WHERE idProducto = ?";

        try (Connection conexion = JDBC.ConectarBD()) {
            //elimina el producto del carrito de compras
            try (PreparedStatement pstmtCarrito = conexion.prepareStatement(sqlEliminarDeCarrito)) {
                pstmtCarrito.setInt(1, idProducto);//establece el ID del producto para eliminarlo del carrito
                pstmtCarrito.executeUpdate();
            }
            //elimina el producto de la tabla Producto
            try (PreparedStatement pstmtProducto = conexion.prepareStatement(sqlEliminarProducto)) {
                pstmtProducto.setInt(1, idProducto);//establece el ID del producto para eliminarlo
                return pstmtProducto.executeUpdate() > 0;//eecuta la eliminación del producto y devuelve true si fue exitosa
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
