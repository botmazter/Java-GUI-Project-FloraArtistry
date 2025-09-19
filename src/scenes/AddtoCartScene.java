package scenes;

import controller.MainController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;
import main.CustomAlert;
import model.Product;

public class AddtoCartScene {

    private MainController mainController;
    private Product selectedProduct;

    public AddtoCartScene(MainController mainController, Product selectedProduct) {
        this.mainController = mainController;
        this.selectedProduct = selectedProduct;
    }

    public void showAddtoCartScene() {
        Stage addToCartStage = new Stage();
        addToCartStage.initModality(Modality.APPLICATION_MODAL);
        addToCartStage.setTitle("Add to Cart");
        Window win = new Window("Add to Cart");
        BorderPane bp = new BorderPane();

        Label flowerNameLbl = new Label("Flower Name: " + selectedProduct.getFlowerName());
        Label flowerTypeLbl = new Label("Flower Type: " + selectedProduct.getType());
        Label flowerPriceLbl = new Label("Flower Price: $" + selectedProduct.getPrice());

        Label qtyLbl = new Label("Quantity:");
        Spinner<Integer> qtySpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        qtySpinner.setValueFactory(valueFactory);

        Button addToCartBtn = new Button("Add to Cart");
        Button cancelBtn = new Button("Cancel");

        addToCartBtn.setOnAction(e -> {
            int quantity = qtySpinner.getValue();
            mainController.getCartController().addToCart(selectedProduct.getFlowerID(), quantity);
            showConfirmation("Successfully added " + selectedProduct.getFlowerName() + " (Quantity: " + quantity + ") to your cart.");
        });

        cancelBtn.setOnAction(e -> addToCartStage.close());
        HBox buttonBox = new HBox(10, addToCartBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(flowerNameLbl, flowerTypeLbl, flowerPriceLbl, qtyLbl, qtySpinner, buttonBox);      

        win.getContentPane().getChildren().add(layout);
        bp.setCenter(win);

        Scene scene = new Scene(bp, 800, 600);

        addToCartStage.setScene(scene);
        addToCartStage.showAndWait();
    }

    private void showConfirmation(String message) {
        CustomAlert confirmAlert = new CustomAlert(Alert.AlertType.INFORMATION, "Add to Cart Successful!", "SUCCESSFUL", message);
        confirmAlert.showAlert();
    }
}