package scenes;

import controller.MainController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RegisterScene {

	private MainController mainController;

	public RegisterScene(MainController mainController) {
		super();
		this.mainController = mainController;
	}

	
	public Pane getScene() {
		MenuBar menuBar = new MenuBar();
        Menu navigationMenu = new Menu("Page");

        MenuItem loginItem = new MenuItem("Login Page");
        MenuItem registerItem = new MenuItem("Register Page");
        
        menuBar.getMenus().add(navigationMenu);
        navigationMenu.getItems().addAll(loginItem, registerItem);
        
        loginItem.setOnAction(e -> {
            mainController.getPageController().showLoginPage();
        });
        
        registerItem.setOnAction(e -> {
            
            mainController.getPageController().showRegisterPage();
        });
        
        Text title = new Text("Register");
        title.setFont(Font.font("Arial", 30));
        
        Label emailLbl = new Label("Email");
        TextField emailTF = new TextField();
        emailTF.setMaxWidth(250);
        
        Label usernameLbl = new Label("Username");
        TextField usernameTF = new TextField();
        usernameTF.setMaxWidth(250);
        
        Label passwordLbl = new Label("Password");
        PasswordField passwordPF = new PasswordField();
        passwordPF.setMaxWidth(250);
        
        Label confirmPasswordLbl = new Label("Confirm Password");
        PasswordField confirmPasswordPF = new PasswordField();
        confirmPasswordPF.setMaxWidth(250);
        
        Label addressLbl = new Label("Address");
        TextArea addressTA = new TextArea();
        addressTA.setMaxWidth(250);
        	
        Label phoneLbl = new Label("Phone Number");
        TextField phoneTF = new TextField();
        phoneTF.setMaxWidth(250);
        
        CheckBox agree = new CheckBox();
        Label checkAgree = new Label("I agree to create an account");
        
        Button submitBtn = new Button("Register");
        submitBtn.setOnAction(e -> {
        	String email = emailTF.getText();
        	String password = passwordPF.getText();
        	String username = usernameTF.getText();
        	String confirmPassword = confirmPasswordPF.getText();
        	String phone = phoneTF.getText();
        	String address = addressTA.getText();
        	mainController.getRegisterController().register(email, username, password, confirmPassword, phone,
        			address);
        });
        
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(20));
        
        gp.add(emailLbl, 0, 0);
        gp.add(emailTF, 1, 0);
        gp.add(usernameLbl, 0, 1);
        gp.add(usernameTF, 1, 1);
        gp.add(passwordLbl, 0, 2);
        gp.add(passwordPF, 1, 2);
        gp.add(confirmPasswordLbl, 0, 3);
        gp.add(confirmPasswordPF, 1, 3);
        gp.add(addressLbl, 0, 4);
        gp.add(addressTA, 1, 4);
        gp.add(phoneLbl, 0, 5);
        gp.add(phoneTF, 1, 5);
        gp.add(agree, 0, 6, 2, 1);
        gp.add(checkAgree, 1, 6);
        gp.add(submitBtn, 1, 7);
        
        VBox mainLayout = new VBox(10); // Adjust spacing between Title and Form
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(10));
        mainLayout.getChildren().addAll(title, gp);
      
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(mainLayout);
       
        
		return root;
 
	}

}
