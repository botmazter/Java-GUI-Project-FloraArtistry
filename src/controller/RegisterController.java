package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Connect;
import javafx.scene.control.Alert;
import main.CustomAlert;

public class RegisterController {
	private MainController mainController;

	public RegisterController(MainController mainController) {
		super();
		this.mainController = mainController;
	}

	public void register(String email, String username, String password, String confirmPassword, String phone,
			String address) {

		if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()
				|| address.isEmpty()) {
			showError("All fields must be filled!");
			return;
		}

		if (username.length() < 4 || username.length() > 20) {
			showError("Username must be between 4 and 20 characters.");
			return;
		}

		if (!isEmailUnique(email)) {
			showError("Email must be unique.");
			return;
		}
		
		if(!email.endsWith("@gmail.com")) {
			showError("Email must end with @gmail.com");
			return;
		}

		if (password.length() < 8) {
			showError("Password must be 8 or more characters.");
			return;
		}

		if (!password.equals(confirmPassword)) {
			showError("Confirm Password must match Password.");
			return;
		}

		if (!isNumeric(phone)) {
			showError("Phone Number must be numeric.");
			return;
		}

		if (phone.length() < 8 || phone.length() > 20) {
			showError("Phone Number must be between 8 and 20 digits.");
			return;
		}

		 String userID = generateUserID();
		 String userRole = null;
	      
	        if (saveUserToDatabase(email, username, password, phone, address, userID, userRole)) {
	            showSuccess("Successfully Registered!");
	            mainController.getPageController().showLoginPage();
	        } else {
	            showError("Registration failed! Please try again.");
	        }
	
    }
	
	private String generateUserID() {
	    String lastUserID = getLastUserID(); 
	    if (lastUserID == null || lastUserID.isEmpty()) {
	        return "US001"; 
	    }

	    String numericPart = lastUserID.substring(2); 
	    int newID = Integer.parseInt(numericPart) + 1; 
	    
	    return String.format("US%03d", newID);
	}

	private String getLastUserID() {
	    String query = "SELECT UserID FROM MsUser ORDER BY UserID DESC LIMIT 1";
	    Connect con = Connect.getInstance();
	    try (ResultSet rs = con.executeQuery(query)) {
	        if (rs.next()) {
	            return rs.getString("UserID");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	private boolean isNumeric(String str) {
		for(int i = 0; i < str.length(); i++) {
			if(!Character.isDigit(str.charAt(i))) {
				System.out.println("Harus angka");
				return false;
			}
		}
		return true;
	}
	
	private boolean isEmailUnique(String email) {
	    String query = "SELECT COUNT(*) AS count FROM MsUser WHERE UserEmail = '" + email + "'";
	    Connect con = Connect.getInstance();
	    ResultSet rs = con.executeQuery(query);
	    try {
	        if (rs.next()) {
	            return rs.getInt("count") == 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	 private boolean saveUserToDatabase(String email, String username, String password, String phone, String address, String userID, String role) {
	        String insertQuery = "INSERT INTO MsUser (UserID, UserEmail, UserName, UserPassword, UserPhonenumber, UserAddress, UserRole) VALUES (?, ?, ?, ?, ?, ?, ?)";
	        try (PreparedStatement statement = Connect.getInstance().getConnection().prepareStatement(insertQuery)) {
	        	statement.setString(1, userID);
	            statement.setString(2, email);
	            statement.setString(3, username);
	            statement.setString(4, password);
	            statement.setString(5, phone);
	            statement.setString(6, address);
	            statement.setString(7, role);	       
	            
	            statement.executeUpdate(); 
	            return true; 
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false; 
	        }
	    }

	private void showError(String message) {
		CustomAlert errorAlert = new CustomAlert(Alert.AlertType.ERROR, "Registration Error", "ERROR", message);
		errorAlert.showAlert();
	}

	private void showSuccess(String message) {
		CustomAlert successAlert = new CustomAlert(Alert.AlertType.INFORMATION, "Registration Successful", "SUCCESS",
				message);
		successAlert.showAlert();
	}

}
