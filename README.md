# StartupSales

Proyecto desarrollado para la asignatura de Fundamentos de Software.

Este proyecto está orientado a la creación de una plataforma de ventas en línea donde los usuarios pueden gestionar sus negocios, comprar y vender productos, y realizar varias acciones de administración. A continuación, se detalla la estructura del proyecto.

---

## Estructura del Proyecto

```plaintext
StartupSales
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── Controladores          # Contiene los controladores de las vistas de la aplicación
│   │   │   │   ├── Cuenta             # Controladores para la gestión de cuenta
│   │   │   │   ├── Compras            # Controladores para la vista de compras
│   │   │   │   ├── Tienda             # Controladores para la vista de tiendas y productos
│   │   │   ├── DB                     # Contiene la configuración de la base de datos y clases de conexión
│   │   │   ├── Main                   # Clase principal para la ejecución de la aplicación
│   │   │   ├── Modelos                # Clases que representan los modelos de negocio
│   │   │   ├── Repositorios           # Repositorios para la gestión de datos de las entidades
│   │   │   │   ├── Carrito            # Repositorios específicos para el carrito de compras
│   │   │   │   ├── Productos          # Repositorios para la gestión de productos
│   │   │   │   ├── Tienda             # Repositorios específicos de tiendas
│   │   │   ├── Servicios              # Servicios que contienen la lógica de negocio
│   │   │   │   ├── Carrito            # Servicios de lógica de negocio para el carrito de compras
│   │   │   │   ├── Datos              # Servicios para la gestión de datos de usuario
│   │   │   │   ├── Productos          # Servicios para la gestión de productos
│   │   │   │   ├── Tienda             # Servicios específicos para la gestión de tiendas
│   │   │   │   ├── Util               # Clases de utilidad para formateo y otras funciones auxiliares
│   │   ├── resources                  # Archivos de recursos (imágenes, configuraciones, etc.)
│   │   └── webapp                     # Archivos web (HTML, CSS, JavaScript) si se trata de una aplicación web
└── README.md                          # Descripción general del proyecto y estructura de carpetas
