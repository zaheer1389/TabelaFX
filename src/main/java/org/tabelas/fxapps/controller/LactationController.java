package org.tabelas.fxapps.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
import javafx.util.StringConverter;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.tabelas.fxapps.App;
import org.tabelas.fxapps.control.AutoCompleteComboBoxListener;
import org.tabelas.fxapps.control.DeleteButton;
import org.tabelas.fxapps.control.EditButton;
import org.tabelas.fxapps.control.FXOptionPane.Response;
import org.tabelas.fxapps.enums.DialogType;
import org.tabelas.fxapps.model.Animal;
import org.tabelas.fxapps.model.Lactation;
import org.tabelas.fxapps.persistence.FacadeFactory;
import org.tabelas.fxapps.util.AppUtil;
import org.tabelas.fxapps.util.DialogFactory;
import org.tabelas.fxapps.view.View;


public class LactationController implements View{

	private Long id;
	private int PAGE_SIZE = 20;
	private int currentPageIndex;
	private int pageFrom;
	private int pageTo;
	private boolean completeLactation;    
    private Lactation lactation;
	
    @FXML
    private Button btnSave,btnCancel,btnSearch,btnClearSearch;

    @FXML
    private ComboBox<Animal> cbAnimal;

    @FXML
    private TableColumn<Animal, String> colAnimalNo,colArrivalDate,colCowingDate,colVehicleNo,colDepartureDate,colDepartureVehicleNo;
    
    @FXML
    private TableColumn<Object, String> colEdit,colCompleteLactation,colDelete;

    @FXML
    private TextField txtVehicleNo,txtDepartureVehicleNo;
    
    @FXML
    private DatePicker txtArrivalDate,txtCowingDate,txtDepartureDate;
    
    @FXML
    private Label lblLactationCompleted,lblDepartureDate,lblDepartureVehicleNo; 
    
    @FXML
    private CheckBox chCompleteLactation;

    @FXML
    private TextField txtSearchAnimalNo;
    
    @FXML
    private TableView<Lactation> tableView;
    
    @FXML
    private AnchorPane leftPane,rightPane;
    
    @FXML
    private SplitPane splitPane;
    
    @FXML
    private HBox navigationBox,controls;
    
    @FXML
    private GridPane form;
    
    @FXML
    void initialize() {
		// TODO Auto-generated constructor stub
    	
    	txtArrivalDate.setConverter(AppUtil.getDatePickerFormatter());
    	txtCowingDate.setConverter(AppUtil.getDatePickerFormatter());
    	txtDepartureDate.setConverter(AppUtil.getDatePickerFormatter());
    	
    	btnSave.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.SAVE).size(17));
    	btnCancel.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES).size(17));
    	btnSearch.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.SEARCH).size(17));
    	btnClearSearch.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES).size(17));
    	
    	cbAnimal.setItems(FXCollections.observableArrayList(AnimalController.getUnsoldAnimalsByBranch()));
    	new AutoCompleteComboBoxListener<>(cbAnimal);
    	cbAnimal.setCellFactory(new Callback<ListView<Animal>, ListCell<Animal>>() {
			
			@Override
			public ListCell<Animal> call(ListView<Animal> arg0) {
				// TODO Auto-generated method stub
				final ListCell<Animal> cell = new ListCell<Animal>(){

                    @Override
                    protected void updateItem(Animal animal, boolean bln) {
                        super.updateItem(animal, bln);
                        
                        if(animal != null){
                            setText(animal.getAnimalNo());
                        }else{
                            setText(null);
                        }
                    }
 
                };
                
                return cell;
			}
		});
    	cbAnimal.setConverter(new StringConverter<Animal>() {
			Animal animal;
			@Override
			public String toString(Animal animal) {
				// TODO Auto-generated method stub
				if(animal == null){
					return null;
				}
				else{
					this.animal = animal;
					return animal.getAnimalNo()+"";
				}
			}
			
			@Override
			public Animal fromString(String arg0) {
				// TODO Auto-generated method stub
				return animal;
			}
		});
    	
    	chCompleteLactation.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				txtDepartureDate.setDisable(!newValue);
				txtDepartureVehicleNo.setDisable(!newValue);
			}
    		
		});
    	
    	setKeyboardHandle();
    	hideCompleteLactationForm();
    	
    	List<Lactation> data = getLactationsByBranch();
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
    
    public void setTable(List<Lactation> data){

		colAnimalNo.setCellValueFactory(new PropertyValueFactory<Animal, String>("animalNo"));
		colArrivalDate.setCellValueFactory(new PropertyValueFactory<Animal, String>("strArrivalDate"));
		colCowingDate.setCellValueFactory(new PropertyValueFactory<Animal, String>("strCowingDate"));
		colVehicleNo.setCellValueFactory(new PropertyValueFactory<Animal, String>("vehicleNo"));
		colDepartureDate.setCellValueFactory(new PropertyValueFactory<Animal, String>("strDepartureDate"));
		colDepartureVehicleNo.setCellValueFactory(new PropertyValueFactory<Animal, String>("departureVehicleNo"));
		
		
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
						lactation = tableView.getItems().get(selectdIndex);
						if(lactation.getAnimal().isSold()){
							DialogFactory.showErrorDialog("Animal has been sold.You are not allowed to edit/delete details once animal sold.");
							return;
						}
						cbAnimal.setValue(lactation.getAnimal());
						txtArrivalDate.setValue(AppUtil.toLocalDate(new Date(lactation.getArrivalDate().getTime())));
						txtCowingDate.setValue(AppUtil.toLocalDate(new Date(lactation.getCowingDate().getTime())));
						txtVehicleNo.setText(lactation.getVehicleNo());
						cbAnimal.setDisable(true);
						
						showCompleteLactationForm();
						
						if(lactation.getDepartureDate() != null){
							chCompleteLactation.setSelected(true);
							txtDepartureDate.setValue(AppUtil.toLocalDate(new Date(lactation.getDepartureDate().getTime())));
							txtDepartureVehicleNo.setText(lactation.getDepartureVehicleNo());
							
							txtDepartureDate.setDisable(false);
							txtDepartureVehicleNo.setDisable(false);
						}
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
						Lactation animal = tableView.getItems().get(selectdIndex);
						if(animal.getAnimal().isSold()){
							DialogFactory.showErrorDialog("Animal has been sold.You are not allowed to edit/delete details once animal sold.");
							return;
						}
						Response response = DialogFactory.showConfirmationDialog("Do you want to delete this record?", DialogType.YESNOCANCEL);
						if(response == Response.YES){
							FacadeFactory.getFacade().delete(animal);
							DialogFactory.showInformationDialog("Lactation details deleted succssfully");
					    	setPagePanel(getLactationsByBranch());
					    	reset();
						}
						
					}
				});
				
				return deleteButton;
			}
		});
		
		for(TableColumn col : tableView.getColumns()){
			makeHeaderWrappable(col);
		}
		
		tableView.setItems(FXCollections.observableArrayList(data));
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
    
    public void showCompleteLactationForm(){
    	form.add(lblLactationCompleted, 0, 4);
    	form.add(chCompleteLactation, 1, 4);
    	form.add(lblDepartureDate, 0, 5);
    	form.add(txtDepartureDate, 1, 5);
    	form.add(lblDepartureVehicleNo, 0, 6);
    	form.add(txtDepartureVehicleNo, 1, 6);
		GridPane.setRowIndex(controls, 7);
		//form.setPrefHeight(400);
    }
    
    public void hideCompleteLactationForm(){
    	if(form.getChildren().size() >= 5){
    		form.getChildren().remove(lblLactationCompleted);
    		form.getChildren().remove(chCompleteLactation);
    		form.getChildren().remove(lblDepartureDate);
    		form.getChildren().remove(txtDepartureDate);
    		form.getChildren().remove(lblDepartureVehicleNo);
    		form.getChildren().remove(txtDepartureVehicleNo);
    		GridPane.setRowIndex(controls, 4);
    		//form.setPrefHeight(270);
		}
    }
    
	public void setPagePanel(List<Lactation> data){
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
		List<Lactation> subList = data.subList(showFrom, showTo);
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
					List<Lactation> data = getLactationsByBranch();
					int showFrom = currentPageIndex*PAGE_SIZE;
					int showTo = (showFrom + PAGE_SIZE) <= data.size() ? (showFrom + PAGE_SIZE) : data.size();
					System.err.println("[from:"+showFrom+",to:"+showTo+"]");
					List<Lactation> subList = data.subList(showFrom, showTo);
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
			Response response = DialogFactory.showConfirmationDialog("Do you really want to save details?", DialogType.YESNOCANCEL);
    		if(response == Response.YES){
    			completeLactation = chCompleteLactation.isSelected();
    			if(lactation == null){
    				//Trying to add new lactation , while there is an open lactation
    				if(getCurrentLactation(cbAnimal.getValue().getAnimalNo()) != null){
    					DialogFactory.showErrorDialog("Animal is present in tabela,you are not allowed to start new lactation.Please mark complete , old lactation first.");
    					return;
    				}
    				lactation = new Lactation();
    				lactation.setAnimal(cbAnimal.getValue());
    			}
    			
    			if(!completeLactation){
    				lactation.setArrivalDate(new Timestamp(AppUtil.toUtilDate(txtArrivalDate.getValue()).getTime()));
    				lactation.setCowingDate(new Timestamp(AppUtil.toUtilDate(txtCowingDate.getValue()).getTime()));
    				lactation.setVehicleNo(txtVehicleNo.getText().toUpperCase());
    				lactation.setDepartureDate(null);
    				lactation.setDepartureVehicleNo("");
    				lactation.setCurrentLactation(true);
    				FacadeFactory.getFacade().store(lactation);
    				DialogFactory.showInformationDialog("Lactation details save successfully");
    			}
    			else if(!SoldAnimalController.isAnimalSold(cbAnimal.getValue().getAnimalNo())){
    				//Mark existing lactation as complete(update lactation) only if animal not sold
    				lactation.setDepartureDate(new Timestamp(AppUtil.toUtilDate(txtDepartureDate.getValue()).getTime()));
    				lactation.setDepartureVehicleNo(txtDepartureVehicleNo.getText().toUpperCase());
    				lactation.setCurrentLactation(false);
    				FacadeFactory.getFacade().store(lactation);
    				DialogFactory.showInformationDialog("Lactation mark completed successfully");
    			}
    			
    			List<Lactation> data = getLactationsByBranch();
		    	setPagePanel(data);

				reset();
    			
    		}
		}
	}

	@Override
	@FXML
	public void reset() {
		// TODO Auto-generated method stub
		id = null;
    	lactation = null;
    	
    	txtDepartureDate.setValue(null);
    	txtDepartureVehicleNo.setText("");
    	hideCompleteLactationForm();
    	
    	cbAnimal.setValue(null);
    	txtArrivalDate.setValue(null);
    	txtCowingDate.setValue(null);
    	txtVehicleNo.setText("");
    	
    	cbAnimal.setDisable(false);
		txtArrivalDate.setDisable(false);
		txtCowingDate.setDisable(false);
		txtVehicleNo.setDisable(false);
		
		txtSearchAnimalNo.setText("");
    	
    	id = null;
    	lactation = null;
    	completeLactation = false;
    	
    	cbAnimal.requestFocus();
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
		List<Lactation> data = getLactationsByBranchAnimal(txtSearchAnimalNo.getText());
		currentPageIndex = 0;
    	setPagePanel(data);
	}
	
	@Override
	@FXML
	public void resetSearch() {
		List<Lactation> data = getLactationsByBranch();
    	setPagePanel(data);

		reset();
	}

	@Override
	public boolean isValidForm() {
		// TODO Auto-generated method stub
		if(cbAnimal.getValue() == null){
    		DialogFactory.showErrorDialog("Please Select Animal");
    		cbAnimal.requestFocus();
    		return false;
    	}
    	if(txtArrivalDate.getValue() == null){
    		DialogFactory.showErrorDialog("Please Select Animal Arrival Date");
    		txtArrivalDate.requestFocus();
    		return false;
    	}
    	if(txtCowingDate.getValue() == null){
    		DialogFactory.showErrorDialog("Please Select Animal Cowing Date");
    		txtCowingDate.requestFocus();
    		return false;
    	}
    	if(completeLactation){
    		if(txtDepartureDate.getValue() == null){
    			DialogFactory.showErrorDialog("Please Select Animal Departure Date");
    			txtDepartureDate.requestFocus();
        		return false;
    		}
    	}
		return true;
	}
	
    public static Lactation getCurrentLactation(String no){
		String queryStr = "Select l from Lactation as l join l.animal as a where a.branch = :bid and a.animalNo = :no and l.currentLactation=true";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("bid", App.appcontroller.getBranch());
		parameters.put("no", no);
		return FacadeFactory.getFacade().find(queryStr, parameters);
	}
	
	public static long countLactation(String no){
    	String queryStr = "Select count(l) from Lactation as l join l.animal as a where a.branch = :bid and a.animalNo = :no  order by l.id desc";
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("bid", App.appcontroller.getBranch());
    	parameters.put("no", no);
    	return FacadeFactory.getFacade().count(queryStr, parameters);
    }

	public static List<Lactation> getLactationsByBranch(){
		String queryStr = "Select l from Lactation as l join l.animal as a where a.branch = :bid  order by l.id desc";
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("bid", App.appcontroller.getBranch());
    	return FacadeFactory.getFacade().list(queryStr, parameters);
	}
	
	public static List<Lactation> getLactationsByBranchAnimal(String no){
		String queryStr = "Select l from Lactation as l join l.animal as a where a.branch = :bid and a.animalNo = :no  order by l.id desc";
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("bid", App.appcontroller.getBranch());
    	parameters.put("no", no);
    	return FacadeFactory.getFacade().list(queryStr, parameters);
	}
	
	public static List<Animal> getCurerntAnimals(){
		String queryStr = "Select a from Lactation as l join l.animal as a where a.branch = :bid AND a.sold = false AND l.departureDate IS NULL order by l.id desc";
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("bid", App.appcontroller.getBranch());
    	return FacadeFactory.getFacade().list(queryStr, parameters);
	}
	
}
