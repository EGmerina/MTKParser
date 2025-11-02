module org.example.parser {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.feather;
    requires java.desktop;

    opens org.example.parser to javafx.fxml;
    exports org.example.parser;
    exports org.example.parser.utils;
    opens org.example.parser.utils to javafx.fxml;
}