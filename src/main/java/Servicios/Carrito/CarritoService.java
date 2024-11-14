package Servicios.Carrito;

import Modelos.Producto;
import Repositorios.Carrito.MostrarCarrito;
import Servicios.Productos.ProductoService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class CarritoService {

    private final MostrarCarrito mostrarCarrito;
    private final ProductoService productoService;

    // Se crea un constructor que acepta MostrarCarrito y ProductoService
    public CarritoService(MostrarCarrito mostrarCarrito, ProductoService productoService) {
        this.mostrarCarrito = mostrarCarrito;
        this.productoService = productoService;
    }

    // Metodo que obtiene el ID del carrito para un usuario dado
    public int obtenerIdCarrito(int idUsuario) {
        try {
            // Llama a la base de datos para obtener el ID del carrito del usuario
            return mostrarCarrito.obtenerIdCarritoDesdeBD(idUsuario);
        } catch (SQLException e) {
            // Maneja una excepción en caso de error en la consulta SQL
            e.printStackTrace();
            System.out.println("Error al obtener el ID del carrito: " + e.getMessage());
            return -1; // Retorna -1 en caso de error
        }
    }

    // metodo que agrega un producto al carrito o actualiza su cantidad si ya existe
    public void agregarProductoAlCarrito(int idCarrito, Producto producto) {
        try {
            // Verifica si el producto ya está en el carrito
            if (mostrarCarrito.productoYaExisteEnCarrito(idCarrito, producto.getIdProducto())) {
                // Si existe, actualiza la cantidad
                mostrarCarrito.actualizarCantidadProducto(idCarrito, producto.getIdProducto(), producto.getCantidad());
            } else {
                // Si no existe, lo inserta en el carrito
                mostrarCarrito.insertarProductoEnCarrito(idCarrito, producto.getIdProducto(), producto.getCantidad());
            }
        } catch (SQLException e) {
            // Maneja una excepción en caso de error en la base de datos
            e.printStackTrace();
            System.out.println("Error al agregar o actualizar el producto en el carrito: " + e.getMessage());
        }
    }

    // Obtiene la lista de productos en el carrito de un usuario
    public List<Producto> obtenerProductosDeCarrito(int idCarrito) {
        try {
            // Llama a la base de datos para obtener los productos en el carrito
            return mostrarCarrito.obtenerProductosDeCarrito(idCarrito);
        } catch (SQLException e) {
            // Maneja una excepción en caso de error en la consulta SQL
            e.printStackTrace();
            System.out.println("Error al obtener los productos del carrito: " + e.getMessage());
            return Collections.emptyList(); // Retorna una lista vacía en caso de error
        }
    }

    // metodo que actualiza la cantidad de un producto específico en el carrito
    public void actualizarCantidadProducto(int idProducto, int idCarrito, int nuevaCantidad) {
        try {
            // Llama a la base de datos para actualizar la cantidad del producto
            mostrarCarrito.actualizarCantidadProducto(idCarrito, idProducto, nuevaCantidad);
        } catch (SQLException e) {
            // Maneja una excepción en caso de error en la base de datos
            e.printStackTrace();
            System.out.println("Error al actualizar la cantidad del producto en el carrito: " + e.getMessage());
        }
    }

    // metodo que elimina un producto específico del carrito
    public void eliminarProductoDelCarrito(int idProducto, int idCarrito) {
        try {
            // Llama a la base de datos para eliminar el producto del carrito
            mostrarCarrito.eliminarProductoDelCarrito(idProducto, idCarrito);
        } catch (SQLException e) {
            // Maneja una excepción en caso de error en la base de datos
            e.printStackTrace();
            System.out.println("Error al eliminar el producto del carrito: " + e.getMessage());
        }
    }

    // metodo que vacía el carrito eliminando todos los productos
    public void vaciarCarrito(int idCarrito) {
        try {
            // Llama a la base de datos para eliminar todos los productos del carrito
            mostrarCarrito.vaciarCarrito(idCarrito);
        } catch (SQLException e) {
            // Maneja una excepción en caso de error en la base de datos
            e.printStackTrace();
            System.out.println("Error al vaciar el carrito: " + e.getMessage());
        }
    }

    // Procesa la compra de los productos en el carrito
    public void procesarCompra(int idUsuario, int idCarrito, BigDecimal totalCompra) {
        try {
            // Registra la compra en la base de datos y obtiene el ID de la compra
            int idCompra = mostrarCarrito.registrarCompra(idUsuario, totalCompra);
            // Registra los productos asociados con la compra
            mostrarCarrito.registrarProductosDeCompra(idCompra, idCarrito);
            // Reduce el stock de los productos comprados
            mostrarCarrito.reducirStockProductos(idCarrito);
            // Vacía el carrito después de la compra
            mostrarCarrito.vaciarCarrito(idCarrito);
            // Imprime mensaje de éxito de la compra
            System.out.println("Compra procesada exitosamente con ID de compra: " + idCompra);
        } catch (SQLException e) {
            // Maneja una excepción en caso de error en la base de datos
            e.printStackTrace();
            System.out.println("Error al procesar la compra: " + e.getMessage());
        }
    }

}
