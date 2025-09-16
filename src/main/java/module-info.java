module org.example.parser {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    opens org.example.parser to javafx.fxml;
    exports org.example.parser;
}