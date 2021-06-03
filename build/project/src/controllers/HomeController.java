package controllers;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import org.controlsfx.dialog.FontSelectorDialog;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomeController implements Initializable {

	BufferedReader reader = null;
	BufferedWriter writer = null;
	FileInputStream fis = null;
	FileOutputStream fos = null;
	FileChooser chooser;
	FileChooser.ExtensionFilter filter;
	File file = null;
	Alert alert;
	boolean changes = false;
	String filename = "Untitled";
	boolean isNamed = false;
	Stage stage;
	Clipboard copy;
	StringSelection transfer;
	FontSelectorDialog fontSelector;
	TextInputDialog dialog;

	@FXML
	private MenuItem save;

	@FXML
	private MenuItem saveAs;

	@FXML
	private MenuItem searchText;

	@FXML
	private JFXTextArea textDisplay;

	@FXML
	void aboutByteEditor(ActionEvent event) {

		loadAboutEditor();
	}

	public HomeController() {
		super();
	}

	@FXML
	void apkExit(ActionEvent event) {
		onClose();
		stage = (Stage) textDisplay.getScene().getWindow();
		stage.close();

	}

	@FXML
	void clearDisplay(ActionEvent event) {

		if (changes) {
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Warning!");
			alert.setContentText("Do you want to save " + filename + " ?");
			stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("/fxmls/user.png"));
			Optional<ButtonType> type = alert.showAndWait();
			if (type.get() == ButtonType.OK && type != null) {
				save();
			}
		}

		textDisplay.setText("");
		stage = (Stage) textDisplay.getScene().getWindow();
		stage.setTitle(filename + "-Byte Editor");
		changes = false;
		save.setDisable(true);
		saveAs.setDisable(true);
	}

	@FXML
	void fileSaveAs(ActionEvent event) {

		chooser = new FileChooser();
		file = chooser.showSaveDialog(textDisplay.getScene().getWindow());
		if (file != null) {
			save();
		}
	}

	@FXML
	void newFile(ActionEvent event) {
		if (changes) {
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Warning!");
			alert.setContentText("Do you want to save " + filename + " ?");
			stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("/fxmls/user.png"));
			Optional<ButtonType> type = alert.showAndWait();
			if (type.get() == ButtonType.OK && type != null) {
				save();
			}
		}
		reader = null;
		writer = null;
		fis = null;
		fos = null;
		file = null;
		changes = false;
		filename = "Untitled";
		isNamed = false;
		save.setDisable(true);
		saveAs.setDisable(true);
		textDisplay.setText("");
		stage = (Stage) textDisplay.getScene().getWindow();
		stage.setTitle(filename + "-Byte Editor");
	}

	@FXML
	void newWindow(ActionEvent event) {
                 loadNewWindow();
	}

	@FXML
	void openFile(ActionEvent event) {

		if (changes) {
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Warning!");
			alert.setContentText("Do you want to save " + filename + " ?");
			stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("/fxmls/user.png"));
			Optional<ButtonType> type = alert.showAndWait();
			if (type != null && type.get() == ButtonType.OK) {
				save();
			}
		}
		chooser = new FileChooser();
		chooser.setTitle("Choose File");
		filter = new FileChooser.ExtensionFilter("TXT files (* .txt)", "*.txt", "JAVA files (* .java)", "*.java",
				"CONF File (.conf)", "*.conf");
		chooser.getExtensionFilters().add(filter);
		file = chooser.showOpenDialog(null);
		changes = false;
		isNamed = true;

		if (file != null) {
			filename = file.getName();
			stage = (Stage) textDisplay.getScene().getWindow();
			stage.setTitle(filename + "-Byte Editor");
			save.setDisable(true);
			saveAs.setDisable(true);
			try {
				fis = new FileInputStream(file);
				reader = new BufferedReader(new InputStreamReader(fis));
				String data;
				textDisplay.setText("");
				while ((data = reader.readLine()) != null) {
					textDisplay.appendText(data);
					textDisplay.appendText("\n");

				}
				if (textDisplay.getText().length() > 10) {
					searchText.setDisable(false);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@FXML
	void saveFile(ActionEvent event) {
		save();
	}

	public void save() {

		if (!isNamed) {
			chooser = new FileChooser();
			file = chooser.showSaveDialog(stage);
			if (file == null)
				return;
			filename = file.getName();
			isNamed = true;
		}
		try {
			String text = textDisplay.getText();
			fos = new FileOutputStream(file);
			writer = new BufferedWriter(new OutputStreamWriter(fos));
			writer.write(text);
			writer.flush();
			stage = (Stage) textDisplay.getScene().getWindow();
			stage.setTitle(filename + "-Byte Editor");
			changes = false;
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setContentText("Great ! your file has been saved..");
			stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("/fxmls/user.png"));
			alert.showAndWait();
			save.setDisable(true);
			saveAs.setDisable(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void searchText(ActionEvent event) {
		String text = textDisplay.getText();
		if (!text.equals("")) {
			dialog = new TextInputDialog();
			dialog.setTitle("Byte Editor");
			dialog.setHeaderText("Search Text \n pattern length should be > 4");
			dialog.setContentText("Enter Pattern: ");
			stage = (Stage) dialog.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("/fxmls/user.png"));
			Optional<String> ans = dialog.showAndWait();
			if (ans.isPresent() && !ans.get().isBlank() && ans.get().length() > 4) {
				List<Integer> pos = KMP.search(text, ans.get());
				if (pos.size() != 0) {
					createMessageAlert("your pattern occurs " + pos.size() + " times");
				} else {
					createMessageAlert("your is pattern not present...");
				}
			}
		} else {
			createMessageAlert("Please Select File..");
		}
	}

	@FXML
	void selectFont(ActionEvent event) {

		fontSelector = new FontSelectorDialog(null);
		fontSelector.setTitle("Byte Editor");
		stage = (Stage) fontSelector.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/fxmls/user.png"));
		Optional<Font> wait = fontSelector.showAndWait();
		if (wait.isPresent()) {
			textDisplay.setFont(wait.get());
			changes = true;
			stage = (Stage) textDisplay.getScene().getWindow();
			stage.setTitle("*" + filename + "-Byte Editor");

		}
	}

	@FXML
	void textCopy(ActionEvent event) {

		transfer = new StringSelection(textDisplay.getText());
		copy = Toolkit.getDefaultToolkit().getSystemClipboard();
		copy.setContents(transfer, transfer);

	}

	@FXML
	void mouseMoveCheck(MouseEvent event) {
		if (textDisplay.getText().equals("")) {
			searchText.setDisable(true);
			save.setDisable(true);
			saveAs.setDisable(true);
			changes = false;
			stage = (Stage) textDisplay.getScene().getWindow();
			stage.setTitle(filename + "-Byte Editor");
			return;
		}
	}

	@FXML
	void enterKey(KeyEvent event) {

		if (!changes) {
			changes = true;
			save.setDisable(false);
			saveAs.setDisable(false);
			stage = (Stage) textDisplay.getScene().getWindow();
			stage.setTitle("*" + filename + "-Byte Editor");

		}
		if (textDisplay.getText().length() > 10) {
			searchText.setDisable(false);
		}
	}

	public void onClose() {
		try {
			if (changes) {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Warning!");
				alert.setContentText("Do you want to save " + filename + " ?");
				stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("/fxmls/user.png"));
				Optional<ButtonType> type = alert.showAndWait();
				if (type.get() == ButtonType.OK && type != null) {
					save();
				}
			}
			if (reader != null && writer != null) {
				reader.close();
				writer.close();
				fis.close();
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	String createMessageAlert(String msg) {
		alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Message!");
		alert.setContentText(msg);
		stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/fxmls/user.png"));
		Optional<ButtonType> type = alert.showAndWait();
		return type.get().getText();
	}

	
	void loadNewWindow() {
		
		Parent root;
		Stage primaryStage=new Stage();
		try {
			FXMLLoader loader =new FXMLLoader(getClass().getResource("/fxmls/Home.fxml"));
		    root=loader.load();
			Scene scene = new Scene(root,950,450);
			primaryStage.setTitle("Byte Editor");
			primaryStage.getIcons().add(new Image("/fxmls/user.png"));
			primaryStage.setScene(scene);
			primaryStage.show();
			HomeController h=loader.getController();
			primaryStage.setOnCloseRequest(event->{
				h.onClose();
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	void loadAboutEditor() {

		Parent root;
		Stage primaryStage=new Stage();
		try {
			FXMLLoader loader =new FXMLLoader(getClass().getResource("/fxmls/About.fxml"));
		    root=loader.load();
			Scene scene = new Scene(root,500,501);
			primaryStage.setTitle("About-Byte Editor");
			primaryStage.getIcons().add(new Image("/fxmls/user.png"));
			primaryStage.setScene(scene);
			primaryStage.initModality(Modality.APPLICATION_MODAL);
			primaryStage.setResizable(false);
			primaryStage.setMaxWidth(500);
			primaryStage.setMaxHeight(501);
			primaryStage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		save.setDisable(true);
		saveAs.setDisable(true);
		searchText.setDisable(true);

	}
}
