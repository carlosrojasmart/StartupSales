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
    private final MostrarProductos mostrarProductos; // Añadir MostrarProductos

    // Modificar el constructor para aceptar también MostrarProductos
    public ProductoService(CrearProducto crearProducto, MostrarProductos mostrarProductos) {
        this.crearProducto = crearProducto;
        this.mostrarProductos = mostrarProductos;
    }

    public boolean crearProducto(Producto producto, File archivoImagen) {
        try (FileInputStream fis = new FileInputStream(archivoImagen)) {
            crearProducto.setArchivoImagen(archivoImagen);
            return crearProducto.crearProducto(producto);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarProducto(Producto producto, File archivoImagen) {
        try (FileInputStream fis = archivoImagen != null ? new FileInputStream(archivoImagen) : null) {
            crearProducto.setArchivoImagen(archivoImagen);
            return crearProducto.actualizarProducto(producto);
        } catch (IOException e) {
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
