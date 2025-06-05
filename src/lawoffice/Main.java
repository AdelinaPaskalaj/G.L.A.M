package lawoffice;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
	    try {
	    	File fxmlFile = new File("src/lawoffice/view/WelcomePage.fxml");
	    	FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
	    	Parent root = loader.load();


	        Scene scene = new Scene(root, 1200, 600);

	        primaryStage.setTitle("Law Office Management System");
	        primaryStage.setScene(scene);
	        primaryStage.show();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}