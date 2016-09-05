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
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import javafx.scene.Node;

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


public class AnimalController implements View{

	private Long id;
	private int PAGE_SIZE = 20;
	private int currentPageIndex;
	private int pageFrom;
	private int pageTo;
	
    @FXML
    private Button btnSearch,btnClearSearch;

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
    private GridPane form;
    
    @FXML
    void initialize() {
		// TODO Auto-generated constructor stub
    	
    	
    	txtPurchasedDate.setConverter(AppUtil.getDatePickerFormatter());

    	btnSave.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.SAVE).size(17));
    	btnCancel.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES).size(17));
    	btnSearch.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.SEARCH).size(17));
    	btnClearSearch.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES).size(17));
    	
    	setKeyboardHandle();
    	
    	List<Animal> data = getAnimalsByBranch();
    	setPagePanel(data);
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
				final EditButton editButton = new EditButton();
				editButton.getButton().setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent arg0) {
						// TODO Auto-generated method stub
						int selectdIndex = editButton.getRowIndex();
						Animal animal = tableView.getItems().get(selectdIndex);
						id = animal.getId();
						
						txtAnimalNo.setText(animal.getAnimalNo());
						txtOwnerName.setText(animal.getOwnerName());
						txtPurchasePrice.setText(animal.getPurchasePrice()+"");
						txtPurchasedDate.setValue(AppUtil.toLocalDate(new Date(animal.getPurchaseDate().getTime())));
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
						int selectdIndex = deleteButton.getRowIndex();
						Animal animal = tableView.getItems().get(selectdIndex);
						if(LactationController.getLactationsByBranchAnimal(animal.getAnimalNo()).size() > 0){
							DialogFactory.showErrorDialog("Animal can not be delete from tabelas as it has previous records.");
							return;
						}
						Response response = DialogFactory.showConfirmationDialog("Do you want to delete this record?", DialogType.YESNOCANCEL);
						if(response == Response.YES){							
							FacadeFactory.getFacade().delete(animal);
							DialogFactory.showInformationDialog("Animal details deleted succssfully");
					    	setPagePanel(getAnimalsByBranch());
					    	reset();
						}
						
					}
				});
				
				return deleteButton;
			}
		});
		
		tableView.setItems(FXCollections.observableArrayList(data));
		
		for(TableColumn col : tableView.getColumns()){
			makeHeaderWrappable(col);
		}
    }
    

    private void makeHeaderWrappable(TableColumn col) {
        Label label = new Label(col.getText());
        label.setStyle("-fx-padding: 2px;");
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
     
        StackPane stack = new StackPane();
        stack.getChildren().add(label);
        stack.prefWidthProperty().bind(col.widthProperty().subtract(5));
        label.prefWidthProperty().bind(stack.prefWidthProperty());
        col.setGraphic(stack);
    }
    
	public void setPagePanel(List<Animal> data){
		if(navigationBox.getChildren().size() > 0){
			navigationBox.getChildren().remove(0);
		}
		int totalPages = (int) Math.ceil((double)data.size() / (double)PAGE_SIZE);
		int pageFrom = 1;
		int pageTo = totalPages > 10 ? 10 : totalPages;   

		HBox pagination = getPagination(pageFrom, pageTo, totalPages);
		pagination.getChildren().get(1).getStyleClass().add("navigation-current-page");
		
		navigationBox.getChildren().add(pagination);
		
		int showFrom = currentPageIndex*PAGE_SIZE;
		int showTo = (showFrom + PAGE_SIZE) <= data.size() ? (showFrom + PAGE_SIZE) : data.size();
		List<Animal> subList = data.subList(showFrom, showTo);
		setTable(FXCollections.observableArrayList(subList));
	}
	
	public HBox getPagination(final int from, final int to, final int totalPages){
		HBox pagecontainer = new HBox();
		pagecontainer.getStyleClass().add("page-nav");
		pagecontainer.setAlignment(Pos.CENTER_RIGHT);
		
		Hyperlink prev = new Hyperlink("<<");
		pagecontainer.getChildren().add(prev);
		System.out.println("[CurrentPageIndex = "+currentPageIndex+",from = "+from+" , To = "+to+"]");
		for(int i=from; i<=to; i++){
			Hyperlink link = new Hyperlink(i+"");
			link.setId(i+"");
			pagecontainer.getChildren().add(link);
			link.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					// TODO Auto-generated method stub
					for(Node node : pagecontainer.getChildren()){
						node.getStyleClass().remove("navigation-current-page");
					}
					link.getStyleClass().add("navigation-current-page");
					currentPageIndex = Integer.parseInt(((Hyperlink)(arg0.getSource())).getId()) - 1;
					List<Animal> data = getAnimalsByBranch();
					int showFrom = currentPageIndex*PAGE_SIZE;
					int showTo = (showFrom + PAGE_SIZE) <= data.size() ? (showFrom + PAGE_SIZE) : data.size();
					System.err.println("[from:"+showFrom+",to:"+showTo+"]");
					List<Animal> subList = data.subList(showFrom, showTo);
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
				int pageFrom = from-10 <= 1 ? 1 : from-10;
				int pageTo = pageFrom+9 > totalPages ? totalPages : pageFrom+9;
				
				HBox pagination = getPagination(pageFrom, pageTo, totalPages);
				
				navigationBox.getChildren().add(pagination);
				
			}
		});
		
		next.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				navigationBox.getChildren().remove(0);
				int pageFrom = to+1;
				int pageTo = pageFrom+9 > totalPages ? totalPages  : pageFrom+9;
				
				HBox pagination = getPagination(pageFrom, pageTo, totalPages);
				
				navigationBox.getChildren().add(pagination);
				
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
				DialogFactory.showErrorDialog("Animal with same number already present,Please choose some other number.");
				return;
			}
			Response response = DialogFactory.showConfirmationDialog("Do you want to save animal details?", DialogType.YESNOCANCEL);
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
		    	
				DialogFactory.showInformationDialog("Animal details saved successfully");
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
    	txtSearchAnimalNo.setText("");
    	txtAnimalNo.requestFocus();
	}

	@Override
	@FXML
	public void search() {
		// TODO Auto-generated method stub
		if(txtSearchAnimalNo.getText().length() == 0){
    		DialogFactory.showErrorDialog("Please enter animal no to search records");
    		txtSearchAnimalNo.requestFocus();
    		return;
    	}
		List<Animal> data = getAnimalsByNumber(txtSearchAnimalNo.getText());
		currentPageIndex = 0;
    	setPagePanel(data);
	}
	
	@Override
	@FXML
	public void resetSearch() {
		reset();
		
		List<Animal> data = getAnimalsByBranch();
    	setPagePanel(data);
	}

	@Override
	public boolean isValidForm() {
		// TODO Auto-generated method stub
		if(txtAnimalNo.getText().length() == 0){
    		DialogFactory.showErrorDialog("Please enter Animal No");
    		txtAnimalNo.requestFocus();
    		return false;
    	}
		if(txtOwnerName.getText().length() == 0){
    		DialogFactory.showErrorDialog("Please enter Merchant name");
    		txtOwnerName.requestFocus();
    		return false;
    	}
    	if(txtPurchasePrice.getText().length() == 0){
    		DialogFactory.showErrorDialog("Please enter Animal Purchase Price");
    		txtPurchasePrice.requestFocus();
    		return false;
    	}
    	if(!AppUtil.isNumeric(txtPurchasePrice.getText())){
    		DialogFactory.showErrorDialog("Please enter only number in Purchase Price");
    		txtPurchasePrice.requestFocus();
    		return false;
    	}
    	if(txtPurchasedDate.getValue() == null){
    		DialogFactory.showErrorDialog("Please select Animal Purchase Date");
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

	public static List<Animal> getAnimalsByNumber(String no) {
		String queryStr = "Select a from Animal as a where a.branch = :bid "
				+ "and a.sold = false   and a.animalNo = :no order by a.id desc";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("bid", App.appcontroller.getBranch());
		parameters.put("no", no);
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
