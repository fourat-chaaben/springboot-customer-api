package io.github.fouratchaaben.customerapp.view;

import io.github.fouratchaaben.customerapp.ClientApplication;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class HomeScene extends Scene {
    public HomeScene(ClientApplication application) {
        super(new VBox(), 640, 500);

        var customerButton = new Button("Customers");
        customerButton.setOnAction(event -> application.showCustomerScene());

        var vBox = new VBox(10, customerButton);
        vBox.setAlignment(Pos.CENTER);
        setRoot(vBox);
    }
}
