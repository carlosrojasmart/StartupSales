package Servicios.Productos;

import Modelos.Producto;
import Repositorios.Productos.CrearProducto;
import Repositorios.Productos.MostrarProductos;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ProductoService {

    private final CrearProducto crearProducto;
    private final MostrarProductos mostrarProductos;

    // Constructor que inicializa los repositorios CrearProducto y MostrarProductos
    public ProductoService(CrearProducto crearProducto, MostrarProductos mostrarProductos) {
        this.crearProducto = crearProducto;
        this.mostrarProductos = mostrarProductos;
    }

    // Método para crear un nuevo producto, opcionalmente con una imagen
    public boolean crearProducto(Producto producto, File archivoImagen) {
        try {
            // Configura el archivo de imagen en el repositorio CrearProducto
            crearProducto.setArchivoImagen(archivoImagen);
            // Llama a crearProducto en el repositorio para almacenar el producto en la base de datos
            return crearProducto.crearProducto(producto);
        } catch (Exception e) {
            // Maneja excepciones e imprime la traza en caso de error
            e.printStackTrace();
            return false; // Retorna falso en caso de error
        }
    }

    // Método para actualizar un producto existente con la opción de agregar una nueva imagen
    public boolean actualizarProducto(Producto producto, File archivoImagen) {
        try {
            // Llama a actualizarProducto en CrearProducto y pasa el archivo de imagen
            return crearProducto.actualizarProducto(producto, archivoImagen);
        } catch (Exception e) {
            // Maneja excepciones e imprime la traza en caso de error
            e.printStackTrace();
            return false; // Retorna falso en caso de error
        }
    }

    // Método para eliminar un producto por su ID
    public boolean eliminarProducto(int idProducto) {
        // Llama a eliminarProducto en CrearProducto para borrar el producto en la base de datos
        return crearProducto.eliminarProducto(idProducto);
    }

    // Método para obtener una lista de los productos más vendidos
    public List<Producto> obtenerTopProductosMasVendidos() {
        // Llama al repositorio MostrarProductos para obtener los productos más vendidos
        return mostrarProductos.obtenerTopProductosMasVendidos();
    }
}
