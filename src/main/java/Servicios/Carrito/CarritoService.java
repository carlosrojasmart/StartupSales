package Servicios.Carrito;

import Modelos.Producto;
import Repositorios.Carrito.MostrarCarrito;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class CarritoService {

    private final MostrarCarrito mostrarCarrito;

    public CarritoService(MostrarCarrito mostrarCarrito) {
        this.mostrarCarrito = mostrarCarrito;
    }

    public int obtenerIdCarrito(int idUsuario) {
        try {
            return mostrarCarrito.obtenerIdCarritoDesdeBD(idUsuario);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener el ID del carrito: " + e.getMessage());
            return -1;
        }
    }

    public void agregarProductoAlCarrito(int idCarrito, Producto producto) {
        try {
            if (mostrarCarrito.productoYaExisteEnCarrito(idCarrito, producto.getIdProducto())) {
                mostrarCarrito.actualizarCantidadProducto(idCarrito, producto.getIdProducto(), producto.getCantidad());
            } else {
                mostrarCarrito.insertarProductoEnCarrito(idCarrito, producto.getIdProducto(), producto.getCantidad());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al agregar o actualizar el producto en el carrito: " + e.getMessage());
        }
    }

    public List<Producto> obtenerProductosDeCarrito(int idCarrito) {
        try {
            return mostrarCarrito.obtenerProductosDeCarrito(idCarrito);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener los productos del carrito: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void actualizarCantidadProducto(int idProducto, int idCarrito, int nuevaCantidad) {
        try {
            mostrarCarrito.actualizarCantidadProducto(idCarrito, idProducto, nuevaCantidad);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al actualizar la cantidad del producto en el carrito: " + e.getMessage());
        }
    }

    public void eliminarProductoDelCarrito(int idProducto, int idCarrito) {
        try {
            mostrarCarrito.eliminarProductoDelCarrito(idProducto, idCarrito);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar el producto del carrito: " + e.getMessage());
        }
    }

    public void vaciarCarrito(int idCarrito) {
        try {
            mostrarCarrito.vaciarCarrito(idCarrito);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al vaciar el carrito: " + e.getMessage());
        }
    }

    public void procesarCompra(int idUsuario, int idCarrito, BigDecimal totalCompra) {
        try {
            int idCompra = mostrarCarrito.registrarCompra(idUsuario, totalCompra);
            mostrarCarrito.registrarProductosDeCompra(idCompra, idCarrito);
            mostrarCarrito.vaciarCarrito(idCarrito);
            System.out.println("Compra procesada exitosamente con ID de compra: " + idCompra);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al procesar la compra: " + e.getMessage());
        }
    }
}
