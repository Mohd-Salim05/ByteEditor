package application;
	
import controllers.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader =new FXMLLoader(getClass().getResource("/fxmls/Home.fxml"));
			Parent root=loader.load();
			Scene scene = new Scene(root,950,450);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Byte Editor");
			primaryStage.getIcons().add(new Image("/fxmls/user.png"));
			primaryStage.setScene(scene);
			primaryStage.show();
			HomeController h=loader.getController();
			primaryStage.setOnCloseRequest(event->{
				h.onClose();
			});
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.getProperties().setProperty("-D","--module-path \'C:\\Program Files\\javafx\\javafx-sdk-14.0.1\\lib\' --add-modules javafx.controls,javafx.fxml");
		launch(args);
	}
}
