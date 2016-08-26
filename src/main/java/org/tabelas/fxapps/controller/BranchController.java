package org.tabelas.fxapps.controller;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.tabelas.fxapps.App;
import org.tabelas.fxapps.control.DeleteButton;
import org.tabelas.fxapps.control.EditButton;
import org.tabelas.fxapps.control.FXOptionPane.Response;
import org.tabelas.fxapps.enums.DialogType;
import org.tabelas.fxapps.model.Animal;
import org.tabelas.fxapps.model.Branch;
import org.tabelas.fxapps.persistence.FacadeFactory;
import org.tabelas.fxapps.util.AppUtil;
import org.tabelas.fxapps.util.DialogFactory;
import org.tabelas.fxapps.view.View;

public class BranchController implements View{

	private Long id;
	
    @FXML
    private Button btnSearch;

    @FXML
    private TextField txtBranchNo;

    @FXML
    private TableColumn<Branch, String> colBranchNo,colBranchName;
    
    @FXML
    private TableColumn<Object, String> colEdit,colDelete;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private TextField txtBranchName;
    
    @FXML
    private TableView<Branch> tableView;
    
    @FXML
    private AnchorPane leftPane,rightPane;
    
    @FXML
    private SplitPane splitPane;
    
    @FXML
    private GridPane form;
    
    @FXML
    void initialize() {
		// TODO Auto-generated constructor stub
    	txtBranchNo.requestFocus();
    	
    	btnSave.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.SAVE).size(17));
    	btnCancel.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES).size(17));
    	//btnSearch.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.SEARCH).size(17));
    	//btnClearSearch.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES).size(17));
    	
    	setKeyboardHandle();
    	setTable(FacadeFactory.getFacade().list(Branch.class));
	}
    
    public void setKeyboardHandle(){
    	//Keyboard handling
    	EventHandler<KeyEvent> keyboard = new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getCode() == KeyCode.ENTER){
					save();
				}
				else if(arg0.getCode() == KeyCode.ESCAPE){
					reset();
				}
			}
		};
		form.setOnKeyPressed(keyboard);
		form.requestFocus();
    }
    
    public void setTable(List<Branch> data){
    	
    	colBranchNo.setCellValueFactory(new PropertyValueFactory<Branch, String>("id"));
    	colBranchName.setCellValueFactory(new PropertyValueFactory<Branch, String>("branchName"));
		
		colEdit.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Object, String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Object, String> arg0) {
				// TODO Auto-generated method stub
				return new SimpleStringProperty(arg0.getValue().toString());
			}
		});
	 
		colEdit.setCellFactory(new Callback<TableColumn<Object, String>, TableCell<Object, String>>() {
			
			@Override
			public TableCell<Object, String> call(TableColumn<Object, String> arg0) {
				// TODO Auto-generated method stub
				final EditButton editButton = new EditButton();
				editButton.getButton().setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent arg0) {
						// TODO Auto-generated method stub
						int selectdIndex = editButton.getRowIndex();
						Branch branch = tableView.getItems().get(selectdIndex);
						id = branch.getId();
						txtBranchNo.setText(branch.getId()+"");
						txtBranchNo.setDisable(true);
						txtBranchName.setText(branch.getBranchName());
					}
				});
				return editButton;
			}
		});
		
		colDelete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Object, String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Object, String> arg0) {
				// TODO Auto-generated method stub
				return new SimpleStringProperty(arg0.getValue().toString());
			}
		});
		 
		colDelete.setCellFactory(new Callback<TableColumn<Object, String>, TableCell<Object, String>>() {
			
			@Override
			public TableCell<Object, String> call(TableColumn<Object, String> arg0) {
				// TODO Auto-generated method stub
				final DeleteButton deleteButton = new DeleteButton();
				deleteButton.getButton().setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent arg0) {
						// TODO Auto-generated method stub
						Response response = DialogFactory.showConfirmationDialog("Do you want to delete record?", DialogType.YESNOCANCEL);
						if(response == Response.YES){
							int selectdIndex = deleteButton.getRowIndex();
							Branch branch = tableView.getItems().get(selectdIndex);
							FacadeFactory.getFacade().delete(branch);
							DialogFactory.showInformationDialog("Branch details deleted succssfully");
					    	setTable(FacadeFactory.getFacade().list(Branch.class));
					    	App.appcontroller.updateBranchList();
					    	reset();
						}
						
					}
				});
				
				return deleteButton;
			}
		});
		
		tableView.setItems(FXCollections.observableArrayList(data));
    }
    
	@Override
	@FXML
	public void save() {
		// TODO Auto-generated method stub
		if(txtBranchNo.getText().length() == 0){
    		DialogFactory.showErrorDialog("Please enter branch number");
    		return;
    	}
    	else if(!AppUtil.isNumeric(txtBranchNo.getText())){
    		DialogFactory.showErrorDialog("Branch number should not contains characters");
    		return;
    	}
    	else if(txtBranchName.getText().length() == 0){
    		DialogFactory.showErrorDialog("Please enter branch name");
    		return;
    	}
    	
    	Response response = DialogFactory.showConfirmationDialog("Do you want to save Branch?",DialogType.YESNOCANCEL);
    	
    	if(response == Response.YES){
	    	Long id = Long.parseLong(txtBranchNo.getText());
	    	Branch branch = FacadeFactory.getFacade().find(Branch.class, id);
	    	if(branch == null){
	    		branch = new Branch();
	    		branch.setId(id);
	        	//branch.setAddedDate(new Timestamp(new Date().getTime()));
	    	}
	    	branch.setBranchName(txtBranchName.getText().toUpperCase());
	    	FacadeFactory.getFacade().store(branch);
	    	DialogFactory.showInformationDialog("Branch details saved succssfully");
	    	setTable(FacadeFactory.getFacade().list(Branch.class));
	    	App.appcontroller.updateBranchList();
	    	reset();
    	}
	}

	@Override
	@FXML
	public void reset() {
		// TODO Auto-generated method stub
		id = null;		
		txtBranchNo.setText("");
		txtBranchName.setText("");
		txtBranchNo.setDisable(false);
		txtBranchNo.requestFocus();
	}

	@Override
	public void search() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	@FXML
	public void resetSearch() {
		reset();
		
	}

	@Override
	public boolean isValidForm() {
		// TODO Auto-generated method stub
		return true;
	}

}
