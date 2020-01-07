module Towers.of.Hanoi {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.lang3;

    opens view.automatic to javafx.fxml;
    opens view.manual to javafx.fxml;
    opens view.menu to javafx.fxml;
    opens application;
    opens model;

    exports view.automatic;
    exports view.manual;
    exports view.menu;
}