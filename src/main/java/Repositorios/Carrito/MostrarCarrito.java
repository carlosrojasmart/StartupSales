package Repositorios.Carrito;

import DB.JDBC;
import Modelos.Producto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MostrarCarrito {

    // Metodo para obtener el ID del carrito de un usuario
    public int obtenerIdCarritoDesdeBD(int idUsuario) throws SQLException {
        String sql = "SELECT idCarrito FROM carrito WHERE idUsuario = ?"; //consulta sql para obtener el carrito de un un usuario
        try (Connection conexion = JDBC.ConectarBD();// el '?' es para dinamizar el resulatado
             PreparedStatement pstmt = conexion.prepareStatement(sql)) { //PreparedStament asegura la consulta sql
            pstmt.setInt(1, idUsuario); //obtiene la consulta en indice 1 para el usuario
            ResultSet rs = pstmt.executeQuery();//ejecuta la query
            if (rs.next()) {
                return rs.getInt("idCarrito"); // retorna la columna idCarrito del rs
            } else {
                System.out.println("No se encontró carrito para el usuario con id: " + idUsuario);
                return -1;
            }
        }
    }

    public boolean productoYaExisteEnCarrito(int idCarrito, int idProducto) throws SQLException {
        String verificarSql = "SELECT cantidad FROM carrito_producto WHERE idCarrito = ? AND idProducto = ?";//misma explicacion
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement verificarStmt = conexion.prepareStatement(verificarSql)) { //misma explicacion
            verificarStmt.setInt(1, idCarrito);//obtiene la consulta en indice 1 del idCarrito
            verificarStmt.setInt(2, idProducto);//obtiene la consulta en indice 2 del idProducto
            ResultSet rs = verificarStmt.executeQuery();//misma ejecucion de query
            return rs.next();//aqui rs avanza a la siguiente fila verificando si hay mas filas
        }
    }

    public void actualizarCantidadProducto(int idCarrito, int idProducto, int cantidad) throws SQLException {
        String actualizarSql = "UPDATE carrito_producto SET cantidad = cantidad + ? WHERE idCarrito = ? AND idProducto = ?"; //consuilta qque actualiza la cantidad de un producto
        try (Connection conexion = JDBC.ConectarBD();
            PreparedStatement actualizarStmt = conexion.prepareStatement(actualizarSql)) {
            actualizarStmt.setInt(1, cantidad);//obtiene consulta en el indice 1 de cantidad
            actualizarStmt.setInt(2, idCarrito);//misma funcion en indice 2 y en idCarrito
            actualizarStmt.setInt(3, idProducto);//misma funcion en indice 3 y en idProducto
            actualizarStmt.executeUpdate();//actualiza la bd
        }
    }

    public void insertarProductoEnCarrito(int idCarrito, int idProducto, int cantidad) throws SQLException {
        String insertarSql = "INSERT INTO carrito_producto (idCarrito, idProducto, cantidad) VALUES (?, ?, ?)"; //inserta en carrito_producto valores del parentesis
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement insertarStmt = conexion.prepareStatement(insertarSql)) {
            insertarStmt.setInt(1, idCarrito);//mismas funciones de obtener consultas
            insertarStmt.setInt(2, idProducto);//
            insertarStmt.setInt(3, cantidad);//
            insertarStmt.executeUpdate();
        }
    }

    public List<Producto> obtenerProductosDeCarrito(int idCarrito) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.idProducto, p.nombre, p.precio, p.imagenProducto, cp.cantidad " +
                "FROM Producto p " +
                "JOIN carrito_producto cp ON p.idProducto = cp.idProducto " +
                "WHERE cp.idCarrito = ?";//obtiene lso productos del carrito, utiizando el '?' para establecer el valor q se tiene que buscar
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idCarrito);//obtiene consulta en indice 1 de idCarrto
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Producto producto = new Producto();//instancia el un producto
                producto.setIdProducto(rs.getInt("idProducto"));//setea en idProducto los valores de la consulta
                producto.setNombre(rs.getString("nombre"));//setea en Nombre los valores de la consulta
                producto.setPrecio(rs.getBigDecimal("precio"));//setea en Precio los valores de la consulta
                producto.setImagenProducto(rs.getBytes("imagenProducto"));//setea en ImagenProducto los valores de la consulta
                producto.setCantidad(rs.getInt("cantidad"));//setea en Cantidad los valores de la consulta
                productos.add(producto);//se agrega en la lista productos lo que se seteo en producto
            }
        }
        return productos;
    }

    public void eliminarProductoDelCarrito(int idProducto, int idCarrito) throws SQLException {
        String sql = "DELETE FROM carrito_producto WHERE idCarrito = ? AND idProducto = ?";//elimina un producto del carrito segun el idCarrito
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idCarrito);
            pstmt.setInt(2, idProducto);
            pstmt.executeUpdate();
        }
    }

    public void vaciarCarrito(int idCarrito) throws SQLException {
        String sql = "DELETE FROM carrito_producto WHERE idCarrito = ?";//vacia todo el carrito
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idCarrito);
            pstmt.executeUpdate();
        }
    }

    public int registrarCompra(int idUsuario, BigDecimal totalCompra) throws SQLException {
        String sql = "INSERT INTO Compra (idUsuario, total_compra, fecha, hora) VALUES (?, ?, CURDATE(), CURTIME())";//inserta en Compra los parametros de la consulta
        try (Connection conexion = JDBC.ConectarBD();//se insertan parametros de fecha y tiempo actual (funciones sql CURDATE-TIME)
            PreparedStatement pstmt = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {//generated keys se utiliza para tener las claves que se generan en la BD
            pstmt.setInt(1, idUsuario);
            pstmt.setBigDecimal(2, totalCompra);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();//retorna las claves generadas
            if (rs.next()) {
                return rs.getInt(1); // ID de la compra generada
            } else {
                throw new SQLException("No se pudo obtener el ID de la compra generada.");
            }
        }
    }

    public void registrarProductosDeCompra(int idCompra, int idCarrito) throws SQLException {
        String sql = "INSERT INTO compra_producto (idCompra, idProducto, cantidad) " + //inserta parametros en compra_productos
                "SELECT ?, idProducto, cantidad FROM carrito_producto WHERE idCarrito = ?";//se asignan parametros y el '?' se asigna a idCompra siendo variable
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idCompra);
            pstmt.setInt(2, idCarrito);
            pstmt.executeUpdate();//ejectua y actualiza
        }
    }

    // Metodo en el repositorio MostrarCarrito para reducir stock
    public void reducirStockProductos(int idCarrito) throws SQLException {
        String sql = "UPDATE producto SET stock = stock - ? WHERE idProducto = ?";//actualiza el producto con el stock
        List<Producto> productos = obtenerProductosDeCarrito(idCarrito);

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            for (Producto producto : productos) {
                pstmt.setInt(1, producto.getCantidad()); // Cantidad del producto en el carrito
                pstmt.setInt(2, producto.getIdProducto()); // ID del producto
                pstmt.executeUpdate();
            }
        }
    }

}
