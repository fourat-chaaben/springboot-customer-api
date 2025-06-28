package io.github.fouratchaaben.customerapp.view;

import java.time.LocalDate;
import java.util.List;

import io.github.fouratchaaben.customerapp.ClientApplication;
import io.github.fouratchaaben.customerapp.controller.CustomerController;
import io.github.fouratchaaben.customerapp.model.Customer;
import io.github.fouratchaaben.customerapp.util.CustomerSortingOptions;
import io.github.fouratchaaben.customerapp.util.CustomerSortingOptions.SortField;
import io.github.fouratchaaben.customerapp.util.CustomerSortingOptions.SortingOrder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

public class CustomerScene extends Scene {
    private final CustomerController customerController;
    private final ClientApplication application;
    private final CustomerSortingOptions sortingOptions;
    private final ObservableList<Customer> customerList;
    private final TableView<Customer> table;

    public CustomerScene(CustomerController customerController, ClientApplication application) {
        super(new VBox(), 640, 500);
        this.customerController = customerController;
        this.application = application;
        this.sortingOptions = new CustomerSortingOptions();
        this.customerList = FXCollections.observableArrayList();

        table = new TableView<>(customerList);
        table.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                showPopup(table.getSelectionModel().getSelectedItem());
            }
        });

        var idColumn = new TableColumn<Customer, String>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setSortable(false);
        idColumn.setPrefWidth(620 / 4D);
        var firstNameColumn = new TableColumn<Customer, String>("First Name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameColumn.setSortable(false);
        firstNameColumn.setPrefWidth(620 / 4D);
        var lastNameColumn = new TableColumn<Customer, String>("Last Name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameColumn.setSortable(false);
        lastNameColumn.setPrefWidth(620 / 4D);
        var birthdayColumn = new TableColumn<Customer, String>("Birthday");
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        birthdayColumn.setSortable(false);
        birthdayColumn.setPrefWidth(620 / 4D);
        //noinspection unchecked
        table.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, birthdayColumn);

        var vBox = new VBox(10, createSortOptionBox(), table, createButtonBox());
        vBox.setAlignment(Pos.CENTER);
        setRoot(vBox);

        customerController.getAllCustomers(sortingOptions, this::setCustomers);
    }

    private HBox createSortOptionBox() {
        var sortFieldChoiceBox = new ChoiceBox<SortField>();
        sortFieldChoiceBox.getItems().addAll(SortField.values());
        sortFieldChoiceBox.setValue(sortingOptions.getSortField());
        sortFieldChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            sortingOptions.setSortField(newValue);
            customerController.getAllCustomers(sortingOptions, this::setCustomers);
        });

        var sortingOrderChoiceBox = new ChoiceBox<SortingOrder>();
        sortingOrderChoiceBox.getItems().addAll(SortingOrder.values());
        sortingOrderChoiceBox.setValue(sortingOptions.getSortingOrder());
        sortingOrderChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            sortingOptions.setSortingOrder(newValue);
            customerController.getAllCustomers(sortingOptions, this::setCustomers);
        });

        var hBox = new HBox(10, sortFieldChoiceBox, sortingOrderChoiceBox);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private HBox createButtonBox() {
        var backButton = new Button("Back");
        backButton.setOnAction(event -> application.showHomeScene());

        var addButton = new Button("Add Customer");
        addButton.setOnAction(event -> showPopup(null));

        var refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> customerController.getAllCustomers(sortingOptions, this::setCustomers));

        var buttonBox = new HBox(10, backButton, addButton, refreshButton);
        buttonBox.setAlignment(Pos.CENTER);
        return buttonBox;
    }

    private void showPopup(Customer customer) {
        var popup = new Popup();
        var firstNameTextField = new TextField();
        firstNameTextField.setPromptText("First Name");
        firstNameTextField.setText(customer == null ? "" : customer.getFirstName());

        var lastNameTextField = new TextField();
        lastNameTextField.setPromptText("Last Name");
        lastNameTextField.setText(customer == null ? "" : customer.getLastName());

        var birthdayPicker = new DatePicker();
        birthdayPicker.setValue(customer == null ? LocalDate.now() : customer.getBirthday());

        var addButton = new Button("Save");
        addButton.setOnAction(event -> {
            var newCustomer = customer != null ? customer : new Customer();
            newCustomer.setFirstName(firstNameTextField.getText());
            newCustomer.setLastName(lastNameTextField.getText());
            newCustomer.setBirthday(birthdayPicker.getValue());
            if (customer == null) {
                customerController.addCustomer(newCustomer, this::setCustomers);
            } else {
                customerController.updateCustomer(newCustomer, this::setCustomers);
            }
            popup.hide();
        });

        var cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> popup.hide());

        var deleteButton = new Button("Delete");
        deleteButton.setTextFill(Color.RED);
        deleteButton.setOnAction(event -> {
            customerController.deleteCustomer(customer, this::setCustomers);
            popup.hide();
        });

        var hBox = new HBox(10, addButton, cancelButton);
        hBox.setAlignment(Pos.CENTER);
        if (customer != null) {
            hBox.getChildren().add(deleteButton);
        }

        var vBox = new VBox(10, firstNameTextField, lastNameTextField, birthdayPicker, hBox);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        vBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        vBox.setPrefWidth(200);
        vBox.setPrefHeight(150);
        vBox.setPadding(new Insets(5));
        popup.getContent().add(vBox);
        popup.show(application.getStage());
        popup.centerOnScreen();
    }

    private void setCustomers(List<Customer> customers) {
        Platform.runLater(() -> customerList.setAll(customers));
    }
}
