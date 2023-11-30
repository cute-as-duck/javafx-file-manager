module fx.fmanager.javafxfilemanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens fx.filemanager to javafx.fxml;
    exports fx.filemanager;
}