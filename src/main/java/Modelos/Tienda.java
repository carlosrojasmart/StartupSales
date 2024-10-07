package Modelos;

public class Tienda {
    private String nombre;
    private byte[] imagen;

    public Tienda(String nombre, byte[] imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public byte[] getImagen() {
        return imagen;
    }
}
