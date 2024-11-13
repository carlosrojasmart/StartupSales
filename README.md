# StartupSales

Proyecto desarrollado para la asignatura de Fundamentos de Software.

Este proyecto está orientado a la creación de una apliacion de ventas realizada en Java JavaFX en esta aplicacion lo usuarios pueden vender, compras productos y realizar
diversas acciones de seguimiento ante sus ventas o compras.

---

## Estructura del Proyecto

```plaintext
StartupSales
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── Controladores          # Contiene los controladores de las vistas de la aplicación
│   │   │   │   ├── Cuenta             # Controladores para la gestión de cuenta
│   │   │   │   ├── Principal          # Controladores principales
│   │   │   │   ├── Sesion             # Controladores de sesion 
│   │   │   │   └── Vistas             # Controladores para Vistas
│   │   │   ├── DB                     # Contiene la configuración de la base de datos y clases de conexión
│   │   │   ├── Main                   # Clase principal para la ejecución de la aplicación
│   │   │   ├── Modelos                # Clases que representan los modelos de negocio
│   │   │   ├── Repositorios           # Repositorios para la gestión de datos de las entidades
│   │   │   │   ├── Carrito            # Repositorios para el carrito de compras
│   │   │   │   ├── Datos              # Repositorios gestion de datos general
│   │   │   │   ├── Perfil             # Repositorios para gestion de perfil usuario
│   │   │   │   ├── Productos          # Repositorios para tratado de productos
│   │   │   │   └── Tienda             # Repositorios mandejo de datos tiendas
│   │   │   └── Servicios              # Servicios que contienen la lógica de negocio
│   │   │       ├── Carrito            # Servicios de lógica de negocio para el carrito de compras
│   │   │       ├── Datos              # Servicios para la gestión de datos de usuario
│   │   │       ├── Productos          # Servicios para la gestión de los productos
│   │   │       ├── Tienda             # Servicios específicos para la gestión de tiendas
│   │   │       └── Util               # Clases de utilidad para formateo y otras funciones auxiliares
│   │   └── resources                  # Archivos de recursos
│   │       │   ├── Imagenes           # Recusos de imagenes usadas en aplicaciones
│   │       └── └── Vistas             # Vistas realizadas para aplicacion
│   └── test                           # Contiene diversos tests de pruebas realizados a la aplicacion 
└── README.md                          # Descripción general del proyecto y estructura de carpetas

