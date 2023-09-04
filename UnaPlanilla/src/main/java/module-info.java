module unaplanilla {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    requires com.jfoenix;
    requires jakarta.xml.bind;
    requires jakarta.ws.rs;
    requires java.sql;
    requires java.base;
    requires jakarta.json;
    
    opens cr.ac.una.unaplanilla to javafx.fxml, javafx.graphics;
    opens cr.ac.una.unaplanilla.controller to javafx.fxml, javafx.graphics, com.jfoenix;
    
    exports cr.ac.una.unaplanilla.model;
}
