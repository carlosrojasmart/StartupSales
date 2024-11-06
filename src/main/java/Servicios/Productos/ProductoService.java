package Servicios.Productos;

import Modelos.Producto;
import Repositorios.Productos.CrearProducto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ProductoService {

    private final CrearProducto crearProducto;

    public ProductoService(CrearProducto crearProducto) {
        this.crearProducto = crearProducto;
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
}
