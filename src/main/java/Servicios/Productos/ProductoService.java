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

    public ProductoService(CrearProducto crearProducto, MostrarProductos mostrarProductos) {
        this.crearProducto = crearProducto;
        this.mostrarProductos = mostrarProductos;
    }

    public boolean crearProducto(Producto producto, File archivoImagen) {
        try {
            crearProducto.setArchivoImagen(archivoImagen);  // Configura el archivo de imagen
            return crearProducto.crearProducto(producto);   // Llama a crearProducto en CrearProducto
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarProducto(Producto producto, File archivoImagen) {
        try {
            // Pasar el archivo de imagen directamente a actualizarProducto en CrearProducto
            return crearProducto.actualizarProducto(producto, archivoImagen);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarProducto(int idProducto) {
        return crearProducto.eliminarProducto(idProducto);
    }

    public List<Producto> obtenerTopProductosMasVendidos() {
        return mostrarProductos.obtenerTopProductosMasVendidos();
    }
}
