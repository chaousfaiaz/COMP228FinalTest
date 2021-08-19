/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp228finaltest2021;

/**
 *
 * @author faiaz
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;


public class StudentProfile extends Application {
	private TextField studentID, firstName, lastName, address, city, province, postalCode,  phoneNumber;
	private ObservableList<Student> students = FXCollections.observableArrayList();
	
	private ObservableList<String> selectedStudent = FXCollections.observableArrayList();
	
	private Button btnSubmit = new Button("Submit");
	private Button btnAddStudent = new Button("Add Information to User");
	private Button btnUpdate = new Button("Update Information");
	private ComboBox<String> listOfStudents = new ComboBox<>();
	
	private PreparedStatement pst;
	private Connection conn;
	
	private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String DATABASE_URL = "jdbc:sqlserver://localhost:1433;";
	private static final String DATABASE_USER = "COMP228_M21_57";
	private static final String DATABASE_PASSWORD = "password";
	
	@Override
	public void start(Stage primaryStage) throws Exception {
	
		//initialize declared TextFields
		studentID = new TextField();
		firstName = new TextField();
		lastName = new TextField();
		address = new TextField();
                city = new TextField();
                province = new TextField();
		postalCode = new TextField();
		phoneNumber = new TextField();
		
		
		
		GridPane formPane = new GridPane();
		GridPane.setConstraints(formPane, 10, 10, 10 , 10);
		btnSubmit.setAlignment(Pos.BASELINE_CENTER);
			formPane.setPadding(new Insets(10,10,10,10));
			formPane.setHgap(10);
			formPane.setVgap(10);
			//Add labels to declared GridPane formPane
			formPane.add(new Label("Student ID: "), 0, 0);
			formPane.add(new Label("First Name: "), 0, 1);
			formPane.add(new Label("Last Name: "),0, 2);
			formPane.add(new Label("Address: "), 0, 3);
                        formPane.add(new Label("City: "), 0, 7);
			formPane.add(new Label("Postal Code: "), 0, 4);
			formPane.add(new Label("Province: "), 0, 5);
			formPane.add(new Label("Phone Number: "), 0, 6);
			
		
		
			//Add input TextBoxes to formPane
			formPane.add(studentID, 1, 0);	
			formPane.add(firstName, 1, 1);		
			formPane.add(lastName, 1, 2);
			formPane.add(address, 1, 3);
                        formPane.add(city, 1, 7);
                        formPane.add(province, 1, 5);
			formPane.add(postalCode, 1, 4);
			formPane.add(phoneNumber, 1, 6);
			
		//Table of Students
		TableView<Student> StudentTable = new TableView<>();
		TableColumn<Student, String> colStudentID = new TableColumn<>("Student ID");
		colStudentID.setMinWidth(100);
		colStudentID.setCellValueFactory(new PropertyValueFactory<>(" Student ID"));
		
		TableColumn<Student, String> colFirstName = new TableColumn<>("First Name");
		colFirstName.setMinWidth(100);
		colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		
		TableColumn<Student, String> colLastName = new TableColumn<>("Last Name");
		colLastName.setMinWidth(100);
		colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		
		TableColumn<Student, String> colAddress = new TableColumn<>("Address");
		colAddress.setMinWidth(100);
		colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
                
                TableColumn<Student, String> colCity = new TableColumn<>("City");
		colCity.setMinWidth(100);
		colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
		
		TableColumn<Student, String> colProvince = new TableColumn<>("Province");
		colProvince.setMinWidth(100);
		colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
		
		TableColumn<Student, String> colPostalCode = new TableColumn<>("Postal Code");
		colPostalCode.setMinWidth(100);
		colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
		
		
		TableColumn<Student, String> colPhoneNumber = new TableColumn<>("Phone Number");
		colPhoneNumber.setMinWidth(150);
		colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
		
		StudentTable.setItems(students);
		StudentTable.getColumns().addAll(colStudentID, colFirstName, colLastName, colAddress, colCity, colProvince, colPostalCode, colPhoneNumber);
		
		//Display Student properties
		GridPane buttonPane = new GridPane();
		buttonPane.setPadding(new Insets(10,10,10,10));
		buttonPane.setHgap(10);
		buttonPane.add(btnSubmit,0,0);
		buttonPane.add(btnUpdate,1,0);
		buttonPane.add(btnAddStudent,2,0);

		//Students info pane
		GridPane studentInfoPane = new GridPane();
			studentInfoPane.setPadding(new Insets(10, 10, 0, 10));
			studentInfoPane.setMinWidth(150);
			studentInfoPane.setMaxWidth(250);
		
		//List of selected Students 
		ListView<String> Students = new ListView<>();
			studentInfoPane.add(new Label("List of Students: "), 0, 0);
			studentInfoPane.add(Students, 0, 1);
			
	
		//Main Application layout
		BorderPane mainLayout = new BorderPane();
			mainLayout.setPadding(new Insets(10, 10, 10, 10));
			mainLayout.setLeft(formPane);
			mainLayout.setCenter(StudentTable);
			mainLayout.setRight(studentInfoPane);
			mainLayout.setBottom(buttonPane);
			
		//Add event handlers to submit button
		btnSubmit.setOnAction(e -> {
			try {
				//Database driver
				Class.forName(DRIVER);
				//Set database connection options
				conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
				
				//Create the prepared statement for inserting a Student
				pst = conn.prepareStatement("INSERT INTO Student (student_id, first_name, last_name, address, city, province,postal_code, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				
				//Parameters for the statement
				pst.setString(1, studentID.getText());
				pst.setString(2, firstName.getText());
				pst.setString(3, lastName.getText());
				pst.setString(4, address.getText());
                                pst.setString(5, city.getText());
                                pst.setString(6, province.getText());
				pst.setString(7, postalCode.getText());
				pst.setString(8, phoneNumber.getText());
				
				//Execute the statement
				pst.executeUpdate();
				
				//Alert message on student information insert success
				Alert alert = new Alert(AlertType.INFORMATION, "Student information has been successfully added!");
				alert.setHeaderText("Information");
				alert.setTitle("Student Added");
				alert.show();
			}
			catch (Exception ex) {
				//Alert message on error
				Alert alert = new Alert(AlertType.ERROR, "Username already exists.");
				alert.setHeaderText("Error");
				alert.setTitle("Error");
				alert.show();
			}
			finally {
				//Close connection and statement; must be surrounded with try/catch
				try {
					pst.close();
					conn.close();
				}
				catch (Exception ex) {}
			}
			
			//Repopulate the table
			populateTable();
		});
		
		//Add event handlers to update button
		btnUpdate.setOnAction(e -> {
			if (!studentID.getText().equals("")) {
				try {
					//Database driver
					Class.forName(DRIVER);
					//Set database connection options
					conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
					
					//Create the prepared statement for inserting a Student
					pst = conn.prepareStatement("UPDATE Student SET student_ id= ?, first_name=?, last_name=?, address=?, city=?, province=?, postal_code=?, phone_number=? WHERE student_id=?");
					
					//Parameters for the statement
                                        pst.setString(1, studentID.getText());
					pst.setString(2, firstName.getText());
					pst.setString(3, lastName.getText());
					pst.setString(4, address.getText());
                                        pst.setString(5, city.getText());
                                        pst.setString(6, province.getText());
					pst.setString(7, postalCode.getText());
					pst.setString(8, phoneNumber.getText());
					
					
					//Execute the statement and check if update is successful
					if (pst.executeUpdate() == 0) {
						throw new Exception();
					}
					
					//Alert message on Student insert success
					Alert alert = new Alert(AlertType.INFORMATION, "Student information has been successfully updated!");
					alert.setHeaderText("Information");
					alert.setTitle("Student Information Updated");
					alert.show();
				}
				catch (Exception ex) {
					//Alert message on error
					Alert alert = new Alert(AlertType.ERROR, "Username not found / cannot change username.");
					alert.setHeaderText("Error");
					alert.setTitle("Error");
					alert.show();
				}
				finally {
					//Close connection and statement; must be surrounded with try/catch
					try { pst.close(); conn.close(); }
					catch (Exception ex) {}
				}
			}
			else {
				//Alert message on error
				Alert alert = new Alert(AlertType.ERROR, "Username cannot be empty.");
				alert.setHeaderText("Error");
				alert.setTitle("Error");
				alert.show();
			}
			
			//Repopulate the table
			populateTable();
				
		});
		
		//Add event handlers to add Student button
		btnAddStudent.setOnAction(e -> {
			try {
				//Database driver
				Class.forName(DRIVER);
				//Set database connection options
				conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
				
				//Create the prepared statement for adding values to students
				pst = conn.prepareStatement("INSERT INTO students (student_id, first_name, last_name, address, city, province, postal_code, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				
				//Parameters for the statement
				pst.setInt(1, listOfStudents.getSelectionModel().getSelectedIndex() + 1);
				pst.setString(2, studentID.getText());
				

				//Execute the statement
				pst.executeUpdate();
				
				//Alert message on Student insert success
				Alert alert = new Alert(AlertType.INFORMATION, "Student information has been successfully added!");
				alert.setHeaderText("Information");
				alert.setTitle("Information Added");
				alert.show();
			}
			catch (Exception ex) {
				//Alert message on error
				Alert alert = new Alert(AlertType.ERROR, ex.getMessage());
				alert.setHeaderText("Error");
				alert.setTitle("Error");
				alert.show();
			}
			finally {
				//Close connection and statement; must be surrounded with try/catch
				try {
					pst.close();
					conn.close();
				}
				catch (Exception ex) {}
				
				//Repopulate the table
				populateTable();
				
				
			}});
		
		//When the user makes a selection on the table
		StudentTable.getSelectionModel().selectedItemProperty().addListener(e -> {
			if (StudentTable.getSelectionModel().getSelectedItem() != null) {
				//Clears the list of the selected Students's 
				selectedStudent.clear();
						
				//Gets the selected students and assigns it to a temporary variable
				Student selected = StudentTable.getSelectionModel().getSelectedItem();
						
				
						
				//Sets TextBox values for updating
				studentID.setText(selected.getUserName());
				firstName.setText(selected.getFirstName());
				lastName.setText(selected.getLastName());
				address.setText(selected.getAddress());
                                city.setText(selected.getCity());
                                province.setText(selected.getProvince());
				postalCode.setText(selected.getPostalCode());
				phoneNumber.setText(selected.getPhoneNumber());
			}
		});
		
		
		
		//Populate the Student table
		populateTable();
		
		
		
		Scene scene = new Scene(mainLayout);
			primaryStage.setMinHeight(600);
			primaryStage.setHeight(500);
			primaryStage.setMinWidth(1024);
			primaryStage.setTitle("Student Profile");
			primaryStage.setScene(scene);
			primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	//Populates the Student table
	protected void populateTable() {
		students.clear();
		try {
			//Database driver
			Class.forName(DRIVER);
			//Set database connection options
			conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
			
			//Create the prepared statement for inserting a Student
			pst = conn.prepareStatement("SELECT * FROM Students");
			
			//Set ResultSet to query
			ResultSet StudentRS = pst.executeQuery();
			
			while (StudentRS.next()) {
				//Temporary list of games for insert into new Student object
				List<Student> listOfStudents = new ArrayList<Student>();
				
				//SQL query for Student
				pst = conn.prepareStatement(
						"SELECT Student.student_id, Student.first_name, Student.last_name, Student.address, Student.city, Student.province, Student.postal_code, Student.phone_number "
						+ "FROM Student "
						+ "INSERT INTO Student (student_id, Student.first_name, Student.last_name, Student.address, Student.city, Student.province, Student.postal_code, Student.phone_number)  "
						+ "VALUES ('300111222','Sam', 'Malone', '10 SomewhereRoad', 'Toronto','ON','M1Y2H2')"
                                );
				
				
				//Adds a new Student object to the list of students
				students.add(new Student(StudentRS.getString(1), StudentRS.getString(2), StudentRS.getString(3), 
						StudentRS.getString(4), StudentRS.getString(5), StudentRS.getString(6), 
						StudentRS.getString(7), StudentRS.getString(8)));
				}
			}
		catch (Exception ex) {}
		finally {
			//Close connection and statement; must be surrounded with try/catch
			try { pst.close(); conn.close(); }
			catch (Exception ex) {}	
		}
		
	}
	
	
		
	
}
