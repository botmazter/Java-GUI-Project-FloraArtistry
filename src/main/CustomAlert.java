package main;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CustomAlert {

	private AlertType alertType;
	private String title, header, context;
	
	public CustomAlert(AlertType alertType, String title, String header, String context) {
		super();
		this.alertType = alertType;
		this.title = title;
		this.header = header;
		this.context = context;
	}
	
	
	public void showAlert() {
		Alert alert = new Alert(this.alertType);
		alert.setTitle(this.title);
		alert.setHeaderText(this.header);
		alert.setContentText(this.context);
		alert.showAndWait();
	}

}