module my.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens Main to javafx.fxml;
    opens Controladores to javafx.fxml; // Asegúrate de abrir este paquete
    exports Main;
    exports Controladores; // Exporta el paquete si es necesario
}
