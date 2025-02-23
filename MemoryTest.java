package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

	/*
 	A simple project to compare the dates of martyrs, the project includes:
	1- Add a new martyr (name and date of martyrdom) 
	2- Comparing the dates of the martyrs with each other by entering the names, and the result appears according to the entry 
	3- You can change the background in red, green, blue and yellow. 
	*/

public class MemoryTest extends Application {

	File file = new File("MartyrsList.dat");

	private ArrayList<Martyr> nameList = new ArrayList<>();
	private Label[] nameLabel = new Label[32];
	private TextField first = new TextField();
	private TextField second = new TextField();
	private Button submit = new Button("Submit");
	private Button clear = new Button("Clear");
	private Label response = new Label("");

	TextField textFieldCreateMartyr = new TextField();
	// It is printed on the screen if the addition is completed or an error occurs +
	// How to enter
	Label labelCreateMartyr2 = new Label("Enter like : martyr 7/10/2023");

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {

		/*
		 * Read the binary file and save it in the array List, as an object and assign
		 * it to the label array
		 */
		int count = 0; // Counter number of items in the array + index to array
		try {
			DataInputStream fileIn = new DataInputStream(new FileInputStream(file));
			while (fileIn.available() > 0) {
				String dataRed = fileIn.readUTF();
				String[] dataArray = dataRed.split(" ");
				Label label = new Label(dataArray[0]);
				nameList.add(new Martyr(dataArray[0], dataArray[1]));
				nameLabel[count] = label;
				count++;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Forming a interface displaying the names of martyrs
		VBox vboxMemoryTestS = new VBox();
		HBox hboxMemoryTestS1 = new HBox();
		HBox hboxMemoryTestS2 = new HBox();
		HBox hboxMemoryTestS3 = new HBox();
		HBox hboxMemoryTestS4 = new HBox();
		for (int i = 0, y = 0; i < nameLabel.length; i++, y++) {
			/*
			 * The names of the martyrs are printed on the front
			 */
			if (nameLabel[i] == null)
				break;
			if (y < 8) { // To determine which hbox to add
				Label label = new Label("\t" + nameLabel[i].getText());
				label.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 15));
				hboxMemoryTestS1.getChildren().add(label);
				hboxMemoryTestS1.setAlignment(Pos.CENTER);
			} else if (y >= 8 && y < 16) {
				Label label = new Label("\t" + nameLabel[i].getText());
				label.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 15));
				hboxMemoryTestS2.getChildren().add(label);
				hboxMemoryTestS2.setAlignment(Pos.CENTER);
			} else if (y >= 16 && y < 24) {
				Label label = new Label("\t" + nameLabel[i].getText());
				label.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 15));
				hboxMemoryTestS3.getChildren().add(label);
				hboxMemoryTestS3.setAlignment(Pos.CENTER);
			} else {
				Label label = new Label("\t" + nameLabel[i].getText());
				label.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 15));
				hboxMemoryTestS4.getChildren().add(label);
				hboxMemoryTestS4.setAlignment(Pos.CENTER);
			}
		}
		vboxMemoryTestS.getChildren().addAll(hboxMemoryTestS1, hboxMemoryTestS2, hboxMemoryTestS3, hboxMemoryTestS4);
		/*
		 * for guarantee that it will always be in the center. I added the label at the
		 * beginning in hbox and at the end I added them in vbox and always specified
		 * its location as the center.
		 */
		vboxMemoryTestS.setAlignment(Pos.CENTER);
		BorderPane borderPaneMemoryTest = new BorderPane();

		HBox hboxMemoryTest1 = new HBox(10);
		ComboBox<String> combB = new ComboBox<>();
		combB.getItems().addAll("Red", "Green", "Blue", "Yellow");
		combB.setOnAction(e -> { // control the color of the background
			if (combB.getValue().equals("Red")) {
				borderPaneMemoryTest.setStyle("-fx-background-color: Red;");
			} else if (combB.getValue().equals("Green")) {
				borderPaneMemoryTest.setStyle("-fx-background-color: Green;");
			} else if (combB.getValue().equals("Blue")) {
				borderPaneMemoryTest.setStyle("-fx-background-color: Blue;");
			} else if (combB.getValue().equals("Yellow")) {
				borderPaneMemoryTest.setStyle("-fx-background-color: Yellow;");
			}
		});

		hboxMemoryTest1.getChildren().addAll(submit, clear, combB);
		hboxMemoryTest1.setAlignment(Pos.CENTER);

		submit.setOnAction(new EventHandler<ActionEvent>() {
			// Control the submit button and show whether the result is correct or if there
			// is a problem
			public void handle(ActionEvent e) {
				
				//I can only delete the text using the clear button
				first.setEditable(false); 
				second.setEditable(false);
				
				if (first.getText().equals("") || second.getText().equals("")) {
					response.setText("Enter names in both boxes. Then press Submit.");
					//I let him write normally because he is basically empty
					if (first.getText().equals("")) first.setEditable(true);
					if (second.getText().equals("")) second.setEditable(true);
				} else if (!inList(first.getText()) && !inList(second.getText())) {
					response.setText("Neither entry is in the name list.");
				} else if (!inList(first.getText())) {
					response.setText(" First entry not in name list – check spelling.");
				} else if (!inList(second.getText())) {
					response.setText("Second entry not in name list – check spelling.");
				} else if (first.getText().equals(second.getText())) {
					response.setText("You entered the same names. Try again.");
				} else if (checkDate()) {
					response.setText("You are correct!");
				} else if (!checkDate()) {
					response.setText("Wrong. Try again.");
				}

			}
		});

		clear.setOnAction(e -> { // Control the clear button
			// Delete the result and the entered names of martyrs
			first.clear();
			second.clear();
			response.setText("");
			first.setEditable(true);
			second.setEditable(true);
		});

		HBox hboxMemoryTest2 = new HBox(20);
		first.setPrefColumnCount(10);
		second.setPrefColumnCount(10);
		Label labelMemoryTest = new Label("martyed before:");

		hboxMemoryTest2.getChildren().addAll(first, labelMemoryTest, second);
		hboxMemoryTest2.setAlignment(Pos.CENTER);

		VBox vboxMemoryTest1 = new VBox(15);
		vboxMemoryTest1.getChildren().addAll(hboxMemoryTest2, hboxMemoryTest1, response);
		vboxMemoryTest1.setAlignment(Pos.CENTER);

		VBox vboxMemoryTest2 = new VBox(15);
		Text textMemoryTest1 = new Text("Test your memory");
		textMemoryTest1.setFont(new Font(35));
		Text textMemoryTest2 = new Text(
				"Hey, my friend! Test your memory to see if you " + "remeber who wae martyred before.");
		textMemoryTest2.setFont(new Font(12));

		VBox vboxMemoryTest3 = new VBox(5);
		Text textMemoryTest3 = new Text("pick two Matyr from the following list, enter them in the"
				+ "boxes in the correct order (date of death), and then press");
		textMemoryTest3.setFont(new Font(12));
		Text textMemoryTest4 = new Text("the Submit button.");
		textMemoryTest4.setFont(new Font(12));
		vboxMemoryTest3.getChildren().addAll(textMemoryTest3, textMemoryTest4);
		vboxMemoryTest3.setAlignment(Pos.CENTER);

		vboxMemoryTest2.getChildren().addAll(textMemoryTest1, textMemoryTest2, vboxMemoryTest3);
		vboxMemoryTest2.setAlignment(Pos.CENTER);

		borderPaneMemoryTest.setBottom(vboxMemoryTest1);
		borderPaneMemoryTest.setTop(vboxMemoryTest2);
		borderPaneMemoryTest.setCenter(vboxMemoryTestS);
		// The interface is ready
		Scene sceneMemoryTest = new Scene(borderPaneMemoryTest, 800, 450);

		// Create an interface to add martyrs
		HBox hboxCreateMartyr1 = new HBox(10);
		Label labelCreateMartyr = new Label("Add Martyr:(Name data of martyrdom)");

		Button buttonCreateMartyr = new Button("Add to File");
		hboxCreateMartyr1.getChildren().addAll(labelCreateMartyr, textFieldCreateMartyr, buttonCreateMartyr);
		hboxCreateMartyr1.setAlignment(Pos.CENTER);

		VBox vboxCreateMartyr = new VBox(10);

		vboxCreateMartyr.getChildren().addAll(hboxCreateMartyr1, labelCreateMartyr2);
		vboxCreateMartyr.setAlignment(Pos.CENTER);

		buttonCreateMartyrHandler bcmh = new buttonCreateMartyrHandler();
		buttonCreateMartyr.setOnAction(bcmh);

		// The second interface is ready
		Scene sceneCreateMartyr = new Scene(vboxCreateMartyr, 500, 100);

		// Designing the basic interface of the program
		Button buttonInitialWindow1 = new Button("create Martyr list Window");
		Button buttonInitialWindow2 = new Button("Memory Test Window");
		VBox vboxInitialWindow = new VBox(10);
		vboxInitialWindow.getChildren().addAll(buttonInitialWindow1, buttonInitialWindow2);
		vboxInitialWindow.setAlignment(Pos.CENTER);
		buttonInitialWindow1.setOnAction(e -> { // Moved to sane Create martyrs
			arg0.setScene(sceneCreateMartyr);
			arg0.setTitle("Add a Martyr to the File");
		});
		buttonInitialWindow2.setOnAction(e -> { // Moved to sane martyrs display
			arg0.setScene(sceneMemoryTest);
			arg0.setTitle("Memory Test");
		});
		Scene sceneInitialWindow = new Scene(vboxInitialWindow, 300, 300);
		arg0.setScene(sceneInitialWindow);
		arg0.setTitle("Add a Martyr to the File");
		arg0.setTitle("The War on Gaza");

		arg0.show();

	}

	private boolean inList(String name) { // Determine whether the name exists or not
		for (int i = 0; i < nameList.size(); i++) {
			if (nameList.get(i).getMartyrName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	boolean checkDate() { // You examine history and determine who was the first martyr
		try {
			int yearOfFirst = 0, manthOfFirst = 0, dayOfFirst = 0;
			int yearOfsecond = 0, manthOfsecond = 0, dayOfsecond = 0;
			for (int i = 0; i < nameList.size(); i++) { // for first text filed (first martyr)
				if (nameList.get(i).getMartyrName().equals(this.first.getText())) {
					String[] datefirst = nameList.get(i).getDateOfMartyrdom().split("/");
					yearOfFirst = Integer.parseInt(datefirst[2].trim());
					manthOfFirst = Integer.parseInt(datefirst[1].trim());
					dayOfFirst = Integer.parseInt(datefirst[0].trim());
				} else if (nameList.get(i).getMartyrName().equals(this.second.getText())) { // for second text filed
																							// (second martyr)
					String[] datesecond = nameList.get(i).getDateOfMartyrdom().split("/");
					yearOfsecond = Integer.parseInt(datesecond[2].trim());
					manthOfsecond = Integer.parseInt(datesecond[1].trim());
					dayOfsecond = Integer.parseInt(datesecond[0].trim());
				}
			}
			// Compare citation history
			if (yearOfFirst == yearOfsecond) {
				if (manthOfFirst == manthOfsecond) {
					if (dayOfFirst < dayOfsecond) {
						return true;
					} else {
						return false;
					}
				} else if (manthOfFirst < manthOfsecond) {
					return true;
				} else if (manthOfFirst > manthOfsecond) {
					return false;
				}
			} else if (yearOfFirst < yearOfsecond) {
				return true;
			} else if (yearOfFirst > yearOfsecond) {
				return false;
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return false;
	}

	class buttonCreateMartyrHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			try {
				DataOutputStream fileOut = new DataOutputStream(new FileOutputStream(file, true));
				String[] checkArray = textFieldCreateMartyr.getText().split(" ");

				//Check the date before attaching it
				String[] checkArrayDate = checkArray[1].split("/");
				int yearOfFirst = Integer.parseInt(checkArrayDate[2].trim());
				int manthOfFirst = Integer.parseInt(checkArrayDate[1].trim());
				int dayOfFirst = Integer.parseInt(checkArrayDate[0].trim());
				
				boolean list = inList(checkArray[0]); 
				//Check the value if it is valid
				if (dayOfFirst < 1 || dayOfFirst > 31) {
					list = true;
				} else if (manthOfFirst < 1 || manthOfFirst > 12) {
					list = true;
				} 
				
				if (!list || nameList.isEmpty()) { // If the name has not been entered before, it will
																	// be added
					nameList.add(new Martyr(checkArray[0], checkArray[1]));
					fileOut.writeUTF(textFieldCreateMartyr.getText() + "\n");
					labelCreateMartyr2.setText("Added successfully!");
				} else {
					labelCreateMartyr2.setText("An error occurred, Check the name or date");
				}
				fileOut.close();
			} catch (IOException e1) { 
				labelCreateMartyr2.setText("An error occurred, try again.");
			} catch (ArrayIndexOutOfBoundsException e2) { //If you enter more or less than the name and date
				labelCreateMartyr2.setText("Please enter like : martyr 7/10/2023");
			} catch (NumberFormatException e3) { // If you enter symbols and letters in date
				labelCreateMartyr2.setText("Please enter the date correctly like : 7/10/2023");
			}
			textFieldCreateMartyr.clear(); // clear the text field

		}

	}

}

class Martyr {
	private String martyrName;
	private String dateOfMartyrdom;

	public Martyr() {
		super();
	}

	public Martyr(String martyrName, String dateOfMartyrdom) {
		super();
		this.martyrName = martyrName;
		this.dateOfMartyrdom = dateOfMartyrdom;
	}

	public String getMartyrName() {
		return martyrName;
	}

	public void setMartyrName(String martyrName) {
		this.martyrName = martyrName;
	}

	public String getDateOfMartyrdom() {
		return dateOfMartyrdom;
	}

	public void setDateOfMartyrdom(String dateOfMartyrdom) {
		this.dateOfMartyrdom = dateOfMartyrdom;
	}

}