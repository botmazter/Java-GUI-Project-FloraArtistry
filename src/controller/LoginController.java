package controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.Connect;
import javafx.scene.control.Alert.AlertType;
import main.CustomAlert;
import model.User;
import scenes.CustomerNavigationScene;

public class LoginController {
	private MainController mainController;

	public LoginController(MainController mainController) {
		super();
		this.mainController = mainController;
	}

	public void login(String email, String password) {
		
		if(email == null || password == null || email.isEmpty() || password.isEmpty()) {
			CustomAlert errorAlert = new CustomAlert(AlertType.ERROR, "Error", "ERROR", "Please enter your email and password!");
			errorAlert.showAlert();
			return;
		}
		
		String query = String.format("SELECT * FROM MsUser WHERE UserEmail = '%s' AND UserPassword = '%s'", email, password);
		ResultSet rs = Connect.getInstance().executeQuery(query);
		
		try {
			if(rs.next()) {
				CustomAlert successAlert = new CustomAlert(AlertType.INFORMATION, "Successful", "SUCCESFULL", "Your email and password successfully logged in!");
				successAlert.showAlert();
				
				String userId = rs.getString("userid");
				String username = rs.getString("username");
				email = rs.getString("userEmail");
				password = rs.getString("userPassword");
				String phone = rs.getString("userPhonenumber");
				String address = rs.getString("userAddress");
				String role = rs.getString("userRole");
				
				User user = new User(userId, email, username, password, phone, address, role);
				mainController.setUser(user);
				
				if(user.getRole().equals("Admin")) {
					mainController.getPageController().showManageProductScene();
				}else {
					CustomerNavigationScene customerNavScene = new CustomerNavigationScene(mainController);
					mainController.getPageController().setPage(customerNavScene.getScene());
				}
			
			}else {
				CustomAlert errorAlert = new CustomAlert(AlertType.ERROR, "Error", "ERROR", "Invalid email or password!");
				errorAlert.showAlert();

			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
				
	}
	
}
	