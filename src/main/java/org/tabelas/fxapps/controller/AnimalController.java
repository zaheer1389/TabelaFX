package org.tabelas.fxapps.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.tabelas.fxapps.App;
import org.tabelas.fxapps.control.DeleteButton;
import org.tabelas.fxapps.control.EditButton;
import org.tabelas.fxapps.control.FXOptionPane.Response;
import org.tabelas.fxapps.enums.DialogType;
import org.tabelas.fxapps.enums.View;
import org.tabelas.fxapps.model.Animal;
import org.tabelas.fxapps.persistence.FacadeFactory;
import org.tabelas.fxapps.util.AppUtil;
import org.tabelas.fxapps.util.DialogFactory;


public class AnimalController implements View{

	private Long id;
	
    @FXML
    private Button btnSearch;

    @FXML
    private TextField txtAnimalNo;

    @FXML
    private TableColumn<Animal, String> colAnimalNo,colOwnerName,colPrice,colDate;
    
    @FXML
    private TableColumn<Object, String> colEdit,colDelete;

    @FXML
    private TextField txtPurchasePrice;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private TextField txtOwnerName;

    @FXML
    private DatePicker txtPurchasedDate;

    @FXML
    private TextField txtSearchAnimalNo;
    
    @FXML
    private TableView<Animal> tableView;
    
    @FXML
    private AnchorPane leftPane,rightPane;
    
    @FXML
    private SplitPane splitPane;
    
    @FXML
    void initialize() {
		// TODO Auto-generated constructor stub
    	
    	btnSave.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.SAVE));
    	btnCancel.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES));
    	btnSearch.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.SEARCH));
    	
    	setTable(FacadeFactory.getFacade().list(Animal.class));
	}
    
    public void setTable(List<Animal> data){
    	colAnimalNo.setCellValueFactory(new PropertyValueFactory<Animal, String>("animalNo"));
    	colOwnerName.setCellValueFactory(new PropertyValueFactory<Animal, String>("ownerName"));
		colDate.setCellValueFactory(new PropertyValueFactory<Animal, String>("strPurchaseDate"));
		colPrice.setCellValueFactory(new PropertyValueFactory<Animal, String>("purchasePrice"));
		 
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
					return new EditButton();
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
						return new DeleteButton();
					}
	      });
		
		tableView.setItems(FXCollections.observableArrayList(data));
    }
    
	@Override
	@FXML
	public void save() {
		// TODO Auto-generated method stub
		if(isValidForm()){
			if(id == null && getAnimalByNumber(txtAnimalNo.getText()) != null){
				DialogFactory.showErrorDialog("Animal with same number already present,Please choose some other number.", null);
				return;
			}
			Response response = DialogFactory.showConfirmationDialog("Do you want to save animal details?", DialogType.YESNOCANCEL, null);
			if(response == Response.YES){
				Animal animal = null;							
				if(id == null){
					animal = new Animal();
					animal.setAnimalNo(txtAnimalNo.getText().toUpperCase());					
					animal.setBranch(App.appcontroller.getBranch());
					animal.setAddedDate(new Timestamp(new Date().getTime()));					
				}
				else{
					animal = FacadeFactory.getFacade().find(Animal.class, id);					
				}
				animal.setOwnerName(txtOwnerName.getText().toUpperCase());
				animal.setPurchaseDate(new Timestamp(AppUtil.toUtilDate(txtPurchasedDate.getValue()).getTime()));
				animal.setPurchasePrice(Double.parseDouble(txtPurchasePrice.getText()));
				FacadeFactory.getFacade().store(animal);
				
				setTable(getAnimalsByBranch());
				DialogFactory.showInformationDialog("Animal details saved successfully", null);
				reset();
			}
		}
	}

	@Override
	@FXML
	public void reset() {
		// TODO Auto-generated method stub
		id = null;
    	txtAnimalNo.setEditable(true);
    	txtAnimalNo.setText("");
    	txtPurchasedDate.setValue(null);
    	txtPurchasePrice.setText("");
    	txtOwnerName.setText("");
    	txtAnimalNo.requestFocus();
	}

	@Override
	@FXML
	public void search() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValidForm() {
		// TODO Auto-generated method stub
		if(txtAnimalNo.getText().length() == 0){
    		DialogFactory.showErrorDialog("Please enter Animal No", null);
    		txtAnimalNo.requestFocus();
    		return false;
    	}
		if(txtOwnerName.getText().length() == 0){
    		DialogFactory.showErrorDialog("Please enter Merchant name", null);
    		txtOwnerName.requestFocus();
    		return false;
    	}
    	if(txtPurchasePrice.getText().length() == 0){
    		DialogFactory.showErrorDialog("Please enter Animal Purchase Price", null);
    		txtPurchasePrice.requestFocus();
    		return false;
    	}
    	if(!AppUtil.isNumeric(txtPurchasePrice.getText())){
    		DialogFactory.showErrorDialog("Please enter only number in Purchase Price", null);
    		txtPurchasePrice.requestFocus();
    		return false;
    	}
    	if(txtPurchasedDate.getValue() == null){
    		DialogFactory.showErrorDialog("Please select Animal Purchase Date", null);
    		txtPurchasedDate.requestFocus();
    		return false;
    	}
		return true;
	}
	
	public static List<Animal> getAnimalsByBranch() {
		String queryStr = "Select a from Animal as a where a.branch = :bid "
				+ "and a.sold = false  order by a.id desc";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("bid", App.appcontroller.getBranch());
		return FacadeFactory.getFacade().list(queryStr, parameters);
	}

	public static Animal getAnimalByNumber(String no) {
		String queryStr = "Select a from Animal as a where a.branch = :bid "
				+ "and a.animalNo = :no order by a.id desc";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("bid", App.appcontroller.getBranch());
		parameters.put("no", no);
		return FacadeFactory.getFacade().find(queryStr, parameters);
	}

}
