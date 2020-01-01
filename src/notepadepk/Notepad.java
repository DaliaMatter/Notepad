package notepadepk;

import java.io.File;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Notepad extends Application{

	public Scene scene;
	String path = "";
	FileChooser fileChooser = new FileChooser();
	
	public void init()
	{
		MenuBar bar = new MenuBar();   // Super Menu
		TextArea note = new TextArea();
		SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
		SeparatorMenuItem separatorMenuItem1 = new SeparatorMenuItem();
		SeparatorMenuItem separatorMenuItem2 = new SeparatorMenuItem();
		
		Menu file = new Menu("File"); // Super Menu Item
		//--------------SubMenu for 1st Item--------------//
		                   // SubMenu Items
		MenuItem New = new MenuItem("New");
		New.setAccelerator(KeyCombination.keyCombination("Ctrl+d")); //Shortcut
		NewAction(New, note, bar);
		
		
		MenuItem open = new MenuItem("Open");
		open.setAccelerator(KeyCombination.keyCombination("Ctrl+i")); //Shortcut
		Open(open, note, bar);
		
		
		MenuItem save = new MenuItem("Save");
		SaveAction(save, note, bar);
		
		
		MenuItem saveAs = new MenuItem("Save As");
		saveAs.setAccelerator(KeyCombination.keyCombination("Ctrl+w")); //Shortcut
		SaveAs(saveAs, note, bar);
		
		
		MenuItem exit = new MenuItem("Exit");
		exit.setAccelerator(KeyCombination.keyCombination("Ctrl+e")); //Shortcut
		Exit(exit, note, bar);
		
		file.getItems().addAll(New , open, save, saveAs, separatorMenuItem, exit); //Fill SubMenu
		
		Menu edit = new Menu("Edit"); // Super Menu Item
		//--------------SubMenu for 2nd Item--------------//
        					// SubMenu Items
		MenuItem undo = new MenuItem("Undo");
		undo.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event)
			{
				 note.undo();
			}
			
		});
		
		MenuItem cut = new MenuItem("Cut");
		cut.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event)
			{
				 note.cut();
			}
			
		});
		
		MenuItem copy = new MenuItem("Copy");
		copy.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event)
			{
				 note.copy();
			}
			
		});
		
		MenuItem past = new MenuItem("Paste");
		past.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event)
			{
				 note.paste();
			}
			
		});
		
		MenuItem delete = new MenuItem("Delete");
		delete.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event)
			{
				 note.replaceSelection("");
			}
			
		});
		MenuItem select = new MenuItem("Select All");
		select.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event)
			{
				 note.selectAll();
			}
			
		});
		edit.getItems().addAll(undo, separatorMenuItem1, cut, copy, past, delete, separatorMenuItem2, select); //Fill SubMenu
		//--------------------------------------------------------------//
		
		Menu help = new Menu("Help"); // Super Menu Item
		//--------------SubMenu for 2nd Item--------------//
		                  // SubMenu Items
		MenuItem about = new MenuItem("About");
		about.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event)
			{
				Alert a = new Alert(AlertType.NONE);
                a.setAlertType(AlertType.INFORMATION);
                a.setHeaderText("");
                a.setContentText("Created By Dalia");
                a.show(); 
			}
			
		});
		help.getItems().addAll(about); //Fill SubMenu
		//--------------------------------------------------------------//
		
		//---------------------Fill Super Menu--------------------------//
		bar.getMenus().addAll(file, edit, help); 
		//--------------------------------------------------------------//
		
		//--------------------Build Scene------------------------------// 
		BorderPane pane = new BorderPane();
		pane.setTop(bar);
		pane.setCenter(note);
		scene = new Scene(pane , 300,400);
		//--------------------------------------------------------------//
	}
	
	
	public void Open(MenuItem open ,TextArea note, MenuBar bar)
	{
		open.setOnAction(new EventHandler<ActionEvent>() {
					
					public void handle(ActionEvent event) {
						fileChooser.setTitle("Open Resource File");
						File openedFile =fileChooser.showOpenDialog(bar.getScene().getWindow());
						if(openedFile != null )
						{
							path = openedFile.getAbsolutePath();
							note.setText("");
							Scanner in = null;
							try {
									in = new Scanner(openedFile);
									while(in.hasNext()) 
									{
										String line = in.nextLine();
										note.appendText(line+"\n");
									}
								} 
							catch (Exception ex) 
							{
								ex.printStackTrace();
							} 
							finally 
							{
								in.close();
							}
						}
					}
				});
	}
	
	public void NewAction(MenuItem New ,TextArea note, MenuBar bar)
	{
		New.setOnAction(new EventHandler<ActionEvent>() {    // Handling
					
					@Override
					public void handle(ActionEvent event) {
						Alert alert = new Alert(AlertType.CONFIRMATION);
		                alert.setHeaderText("Do you want save?");
		                Optional<ButtonType> btn =  alert.showAndWait();
		                if(btn.get() == ButtonType.OK)
		                {
		                	SavingUsingPath(path, note, bar);
		                	note.clear();
		                }
		                else
		                {
		                	note.clear();
		                }
						
					}
				});
	}
	
	public void SaveAction(MenuItem save ,TextArea note, MenuBar bar)
	{
		save.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				SavingUsingPath(path, note, bar);
			}
		});
	}
	
	public void SaveAs(MenuItem saveAs, TextArea note, MenuBar bar)
	{
		saveAs.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				 	SaveToFile(note , bar);
			}
		});
	}
	
	public void Exit(MenuItem exit ,TextArea note, MenuBar bar)
	{
		exit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event)
			{
				Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setHeaderText("Do you want save?");
                Optional<ButtonType> btn =  alert.showAndWait();
                if(btn.get() == ButtonType.OK)
                {
                	if(path == "")
    				{
    					fileChooser.setTitle("Open Resource File");
    					FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("txt (*.txt)", "*.txt");
    					fileChooser.getExtensionFilters().add(ext);
    					File savedFile =fileChooser.showSaveDialog(bar.getScene().getWindow());
    					PrintWriter out = null;
    					try {
    						out = new PrintWriter(savedFile);
    						String output = note.getText();
    						out.println(output);
    						out.close();
    					} 
    					catch (Exception ex) {
    						ex.printStackTrace();
    					} 	
    				}
    				else 
    				{
    					File savedFile = new File(path);
    					PrintWriter out = null;
    					try 
    					{
    						out = new PrintWriter(savedFile);
    						String output = note.getText();
    						out.println(output);
    						out.close();
    					} 
    					catch (Exception ex) {
    						ex.printStackTrace();
    					} 
    				}
                }
                else 
                {
                	Platform.exit();
                }
				 
			}
		});
	}
	
	//---------------- Helper Functions --------------
	public void SaveToFile(TextArea note, MenuBar bar) 
	{
		fileChooser.setTitle("Open Resource File");
		FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("txt (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(ext);
		File savedFile = fileChooser.showSaveDialog(bar.getScene().getWindow());
		if(savedFile != null)
		{
			PrintWriter out = null;
			try {
				out = new PrintWriter(savedFile);
				String output = note.getText();
				out.println(output);
				out.close();
			} 
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void SavingUsingPath(String path, TextArea note, MenuBar bar)
	{
		if(path == "")
		{
			SaveToFile(note, bar); 	
		}
		else 
		{
			File savedFile = new File(path);
			PrintWriter out = null;
			try 
			{
				out = new PrintWriter(savedFile);
				String output = note.getText();
				out.println(output);
				out.close();
			} 
			catch (Exception ex) {
				ex.printStackTrace();
			} 
		}
	}
	//-------------------------------------------------
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setTitle("DuD's Notepad");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void main(String[] args) {
		Application.launch(args);

	}

}
