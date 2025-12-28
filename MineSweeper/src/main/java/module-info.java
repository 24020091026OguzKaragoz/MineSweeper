module org.t3tracon.minesweeper {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires javafx.media;

    opens org.t3tracon.minesweeper to javafx.fxml;
    exports org.t3tracon.minesweeper;
}