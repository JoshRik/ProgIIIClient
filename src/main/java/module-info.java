module it.unito.progiii.progiiiclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.unito.progiii.progiiiclient to javafx.fxml;
    exports it.unito.progiii.progiiiclient;
    opens it.unito.progiii.progiiiclient.controllers to javafx.fxml;

}