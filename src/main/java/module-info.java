module com.vsu.ru.test_1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires vecmath;
    requires java.desktop;

    opens com.vsu.ru.cg to javafx.fxml;
    exports com.vsu.ru.cg;
}