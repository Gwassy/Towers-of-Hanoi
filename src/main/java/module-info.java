module Towers.of.Hanoi {
    requires javafx.controls;
    requires javafx.fxml;

    opens automatic to javafx.fxml;
    opens manual to javafx.fxml;
    opens application;
    opens model;

    exports automatic;
    exports manual;
}