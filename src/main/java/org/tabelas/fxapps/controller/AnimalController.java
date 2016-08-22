package org.tabelas.fxapps.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
	private int PAGE_SIZE = 20;
	
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
    private HBox navigationBox;
    
    @FXML
    void initialize() {
		// TODO Auto-generated constructor stub
    	/*for(int i = 0; i<= 1000; i++){
    		Animal animal = new Animal();
    		if(getAnimalByNumber(i+"") == null){
    			animal.setAnimalNo(i+"");
    			animal.setOwnerName("Owenr "+i);
    			animal.setPurchasePrice(new Random().nextDouble());
    			animal.setPurchaseDate(new Timestamp(new Date().getTime()));
    			FacadeFactory.getFacade().store(animal);
    		}
    	}*/
    	btnSave.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.SAVE));
    	btnCancel.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES));
    	btnSearch.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.SEARCH));
    	
    	List<Animal> data = FacadeFactory.getFacade().list(Animal.class);
    	setPagePanel(data);
    	int currentPageIndex = 0;
    	List<Animal> subList = data.subList(currentPageIndex*PAGE_SIZE, ((currentPageIndex * PAGE_SIZE + PAGE_SIZE <= data.size()) 
				? currentPageIndex * PAGE_SIZE + PAGE_SIZE : data.size()));
		setTable(FXCollections.observableArrayList(subList));
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
    
	public void setPagePanel(List<Animal> books){
		if(navigationBox.getChildren().size() > 0){
			navigationBox.getChildren().remove(0);
		}
		int totalPages = (int) Math.ceil((double)books.size() / (double)PAGE_SIZE);
		int showFrom = 1;
		int showTo = totalPages > 10 ? 10 : totalPages;   
		navigationBox.getChildren().add(getPagination(showFrom, showTo, totalPages));
		
	}
	
	public HBox getPagination(final int from, final int to, final int totalPages){
		HBox pagecontainer = new HBox();
		pagecontainer.setAlignment(Pos.CENTER_RIGHT);
		
		Hyperlink prev = new Hyperlink("<<");
		pagecontainer.getChildren().add(prev);
		System.out.println("[from = "+from+" , To = "+to+"]");
		for(int i=from; i<=to; i++){
			Hyperlink link = new Hyperlink(i+"");
			link.setId(i+"");
			pagecontainer.getChildren().add(link);
			link.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					// TODO Auto-generated method stub
					int currentPageIndex = Integer.parseInt(((Hyperlink)(arg0.getSource())).getId()) - 1;
					List<Animal> animals = getAnimalsByBranch();
					List<Animal> subList = animals.subList(currentPageIndex*PAGE_SIZE, ((currentPageIndex * PAGE_SIZE + PAGE_SIZE <= animals.size()) 
							? currentPageIndex * PAGE_SIZE + PAGE_SIZE : animals.size()));
					setTable(FXCollections.observableArrayList(subList));
				}
			});
		}
		
		Hyperlink next = new Hyperlink(">>");
		pagecontainer.getChildren().add(next);
		
		prev.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				navigationBox.getChildren().remove(0);
				int showFrom = from-10 <= 1 ? 1 : from-10;
				int showTo = showFrom+9 > totalPages ? totalPages : showFrom+9;
				navigationBox.getChildren().add(getPagination(showFrom, showTo, totalPages));
			}
		});
		
		next.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				navigationBox.getChildren().remove(0);
				int showFrom = to+1;
				int showTo = showFrom+9 > totalPages ? totalPages  : showFrom+9;
				navigationBox.getChildren().add(getPagination(showFrom, showTo, totalPages));
			}
		});
		
		if(from <= 1)
			prev.setDisable(true);
		if(to >= totalPages)
			next.setDisable(true);
		
		return pagecontainer;
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
				
				List<Animal> data = getAnimalsByBranch();
		    	setPagePanel(data);
		    	int currentPageIndex = 0;
		    	List<Animal> subList = data.subList(currentPageIndex*PAGE_SIZE, ((currentPageIndex * PAGE_SIZE + PAGE_SIZE <= data.size()) 
						? currentPageIndex * PAGE_SIZE + PAGE_SIZE : data.size()));
				setTable(FXCollections.observableArrayList(subList));
				
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
		List<Animal> data = getAnimalsByBranch();
    	setTable(data);
    	setPagePanel(data);
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
