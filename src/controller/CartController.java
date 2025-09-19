package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Connect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import main.CustomAlert;
import model.CartItem;

public class CartController {

	private MainController mainController;
	private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();

	public CartController(MainController mainController) {
		this.mainController = mainController;
	}

	public void addToCart(String flowerId, int quantity) {
		String userId = mainController.getUser().getUserId();
		saveCartToDatabase(userId, flowerId, quantity);
	}

	private void saveCartToDatabase(String userID, String flowerID, int quantity) {
		String checkQuery = "SELECT Quantity FROM mscart WHERE UserID = ? AND FlowerID = ?";

		try (Connection conn = Connect.getInstance().getConnection();
				PreparedStatement checkStatement = conn.prepareStatement(checkQuery)) {
			checkStatement.setString(1, userID);
			checkStatement.setString(2, flowerID);
			ResultSet rs = checkStatement.executeQuery();

			if (rs.next()) {
				updateCartQty(userID, flowerID, quantity);
			} else {
				insertIntoCart(userID, flowerID, quantity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showError("Failed to save cart to database.");
		}
	}

	private void updateCartQty(String userID, String flowerID, int quantity) {
		String updateQuery = "UPDATE mscart SET Quantity = Quantity + ? WHERE UserID = ? AND FlowerID = ?";

		try (Connection conn = Connect.getInstance().getConnection();
				PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
			updateStatement.setInt(1, quantity);
			updateStatement.setString(2, userID);
			updateStatement.setString(3, flowerID);
			updateStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			showError("Failed to update cart quantity.");
		}
	}

	private void insertIntoCart(String userID, String flowerID, int quantity) {
		String insertQuery = "INSERT INTO mscart (UserID, FlowerID, Quantity) VALUES (?, ?, ?)";

		try (Connection conn = Connect.getInstance().getConnection();
				PreparedStatement insertStatement = conn.prepareStatement(insertQuery)) {
			insertStatement.setString(1, userID);
			insertStatement.setString(2, flowerID);
			insertStatement.setInt(3, quantity);
			insertStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			showError("Failed to insert into cart.");
		}
	}

	private void showError(String message) {
		CustomAlert confirmAlert = new CustomAlert(Alert.AlertType.ERROR, "Add to Cart Failed!", "ERROR", message);
		confirmAlert.showAlert();
	}
}