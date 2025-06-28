package io.github.fouratchaaben.customerapp;

import io.github.fouratchaaben.customerapp.controller.CustomerController;
import io.github.fouratchaaben.customerapp.view.CustomerScene;
import io.github.fouratchaaben.customerapp.view.HomeScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientApplication extends Application {
    private final CustomerController customerController = new CustomerController();
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        primaryStage.setScene(new HomeScene(this));
        primaryStage.show();
    }

    public void showHomeScene() {
        stage.setScene(new HomeScene(this));
    }

    public void showCustomerScene() {
        stage.setScene(new CustomerScene(customerController, this));
    }

    public Stage getStage() {
        return stage;
    }
}
