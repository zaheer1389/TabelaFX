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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
import org.tabelas.fxapps.enums.AnimalServiceResult;
import org.tabelas.fxapps.enums.DialogType;
import org.tabelas.fxapps.model.Animal;
import org.tabelas.fxapps.model.AnimalService;
import org.tabelas.fxapps.model.Lactation;
import org.tabelas.fxapps.persistence.FacadeFactory;
import org.tabelas.fxapps.util.AppUtil;
import org.tabelas.fxapps.util.DialogFactory;
import org.tabelas.fxapps.view.View;


public class ServiceController implements View{

	private Long id;
	private int PAGE_SIZE = 20;
	private int currentPageIndex;
	private int pageFrom;
	private int pageTo;
	
    @FXML
    private Button btnSave,btnCancel,btnSearch,btnClearSearch;

    @FXML
    private ComboBox<Animal> cbAnimal;

    @FXML
    private TableColumn<AnimalService, String> colAnimalNo,colServiceDate,colMaleAnimalNo,colServiceResult,colRemarks;
    
    @FXML
    private TableColumn<Object, String> colEdit,colDelete;

    @FXML
    private DatePicker txtServiceDate,txtCowingDate,txtDepartureDate;
    
    @FXML
    private Label lblLactationCompleted,lblDepartureDate,lblDepartureVehicleNo; 

    @FXML
    private TextField txtSearchAnimalNo,txtMaleAnimalNo;
    
    @FXML
    private TextArea txtRemarks;
    
    @FXML
    private RadioButton optEmpty,optPregnant;
    
    @FXML
    private TableView<AnimalService> tableView;
    
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
    	
    	txtServiceDate.setConverter(AppUtil.getDatePickerFormatter());
    	
    	btnSave.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.SAVE).size(17));
    	btnCancel.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES).size(17));
    	btnSearch.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.SEARCH).size(17));
    	btnClearSearch.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES).size(17));
    	
    	cbAnimal.setItems(FXCollections.observableArrayList(LactationController.getCurerntAnimals()));
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
    	
    	
    	setKeyboardHandle();
    	
    	List<AnimalService> data = getAnimalServicesByBranch();
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
    
    public void setTable(List<AnimalService> data){

		colAnimalNo.setCellValueFactory(new PropertyValueFactory<AnimalService, String>("animalNo"));
		colServiceDate.setCellValueFactory(new PropertyValueFactory<AnimalService, String>("strServiceDate"));
		colMaleAnimalNo.setCellValueFactory(new PropertyValueFactory<AnimalService, String>("genderNo"));
		colServiceResult.setCellValueFactory(new PropertyValueFactory<AnimalService, String>("result"));
		colRemarks.setCellValueFactory(new PropertyValueFactory<AnimalService, String>("remarks"));
		
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
						AnimalService service = tableView.getItems().get(selectdIndex);
						id = service.getId();
						
						cbAnimal.setValue(service.getLactation().getAnimal());
						txtServiceDate.setValue(AppUtil.toLocalDate(new Date(service.getServiceDate().getTime())));
						
						cbAnimal.setDisable(true);
						txtServiceDate.setDisable(true);
						
						txtMaleAnimalNo.setText(service.getGenderNo());
						if(service.getResult().equals(AnimalServiceResult.PREGNANT.toString())){
							optPregnant.setSelected(true);
						}
						else if(service.getResult().equals(AnimalServiceResult.EMPTY.toString())){
							optEmpty.setSelected(true);
						}
						txtRemarks.setText(service.getRemarks());
						
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
						Response response = DialogFactory.showConfirmationDialog("Do you want to delete this record?", DialogType.YESNOCANCEL, null);
						if(response == Response.YES){
							int selectdIndex = deleteButton.getRowIndex();
							AnimalService service = tableView.getItems().get(selectdIndex);
							FacadeFactory.getFacade().delete(service);
							DialogFactory.showInformationDialog("Animal Service details deleted succssfully", App.appcontroller.stage);
					    	setPagePanel(getAnimalServicesByBranch());
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
    
	public void setPagePanel(List<AnimalService> data){
		if(navigationBox.getChildren().size() > 0){
			navigationBox.getChildren().remove(0);
		}
		int totalPages = (int) Math.ceil((double)data.size() / (double)PAGE_SIZE);
		int pageFrom = 1;
		int pageTo = totalPages > 10 ? 10 : totalPages;   
		navigationBox.getChildren().add(getPagination(pageFrom, pageTo, totalPages));
		
		int showFrom = currentPageIndex*PAGE_SIZE;
		int showTo = (showFrom + PAGE_SIZE) <= data.size() ? (showFrom + PAGE_SIZE) : data.size();
		List<AnimalService> subList = data.subList(showFrom, showTo);
		setTable(FXCollections.observableArrayList(subList));
	}
	
	public HBox getPagination(final int from, final int to, final int totalPages){
		HBox pagecontainer = new HBox();
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
					currentPageIndex = Integer.parseInt(((Hyperlink)(arg0.getSource())).getId()) - 1;
					List<AnimalService> data = getAnimalServicesByBranch();
					int showFrom = currentPageIndex*PAGE_SIZE;
					int showTo = (showFrom + PAGE_SIZE) <= data.size() ? (showFrom + PAGE_SIZE) : data.size();
					System.err.println("[from:"+showFrom+",to:"+showTo+"]");
					List<AnimalService> subList = data.subList(showFrom, showTo);
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
			if(LactationController.getCurrentLactation(cbAnimal.getValue().getAnimalNo()) == null){
    			DialogFactory.showErrorDialog("Animal is not present in tabela,You are not allowed to add service record.", null);
    			return;
    		}
    		Response response = DialogFactory.showConfirmationDialog("Do you want to save details?", DialogType.YESNOCANCEL, null);
    		if(response == Response.YES){
    			AnimalService service;
    			if(id == null){
    				service = new AnimalService();
    				service.setAddedDate(new Timestamp(new Date().getTime()));
    				service.setServiceDate(new Timestamp(AppUtil.toUtilDate(txtServiceDate.getValue()).getTime()));
    				Lactation lactation = LactationController.getCurrentLactation(cbAnimal.getValue().getAnimalNo());
    				service.setLactation(lactation);
    			}
    			else{
    				service = FacadeFactory.getFacade().find(AnimalService.class, id);
    			}
    			service.setGenderNo(txtMaleAnimalNo.getText());
    			service.setRemarks(txtRemarks.getText());
    			if(optPregnant.isSelected()){
    				service.setResult(AnimalServiceResult.PREGNANT.toString());
    			}
    			else if(optEmpty.isSelected()){
    				service.setResult(AnimalServiceResult.EMPTY.toString());
    			}
    			FacadeFactory.getFacade().store(service);
    			DialogFactory.showInformationDialog("Details saved successfully.", null);
    			
    			List<AnimalService> data = getAnimalServicesByBranch();
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
    	cbAnimal.setValue(null);
    	txtServiceDate.setValue(null);
    	txtMaleAnimalNo.setText("");
    	optEmpty.setSelected(false);
    	optPregnant.setSelected(false);
    	txtRemarks.setText("");
    	
    	cbAnimal.setDisable(false);
    	txtServiceDate.setDisable(false);
    	
    	txtSearchAnimalNo.setText("");
    	
    	cbAnimal.requestFocus();
	}

	@Override
	@FXML
	public void search() {
		// TODO Auto-generated method stub
		if(txtSearchAnimalNo.getText().length() == 0){
    		DialogFactory.showErrorDialog("Please enter Animal No to be search", null);
    		return;
    	}
    	List<AnimalService> data = getAnimalServicesByBranch(txtSearchAnimalNo.getText());
    	if(data.size() == 0){
    		DialogFactory.showInformationDialog("No record found,Please try with different no", null);
    	}

		currentPageIndex = 0;
    	setPagePanel(data);
	}
	
	@Override
	@FXML
	public void resetSearch() {
		List<AnimalService> data = getAnimalServicesByBranch();
    	setPagePanel(data);
		reset();
	}

	@Override
	public boolean isValidForm() {
		// TODO Auto-generated method stub
		if(cbAnimal.getValue() == null){
    		DialogFactory.showErrorDialog("Please select animal", null);
    		cbAnimal.requestFocus();
    		return false;
    	}
    	else if(txtServiceDate.getValue() == null){
    		DialogFactory.showErrorDialog("Please select animal service date", null);
    		txtServiceDate.requestFocus();
    		return false;
    	}
		return true;
	}
	
	public static List<AnimalService> getAnimalServicesByBranch(String no){
		String queryStr = "Select s from  AnimalService as s join s.lactation as l join l.animal as a where a.branch = :bid and a.animalNo = :no  order by s.id desc";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("bid", App.appcontroller.getBranch());
		parameters.put("no", no);
		List<AnimalService> list = FacadeFactory.getFacade().list(queryStr, parameters);
		System.out.println(list.size());
		return list;
	}
	
	public static List<AnimalService> getAnimalServicesByBranch(){
		String queryStr = "Select s from  AnimalService as s join s.lactation as l join l.animal as a where a.branch = :bid  order by s.id desc";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("bid", App.appcontroller.getBranch());
		List<AnimalService> list = FacadeFactory.getFacade().list(queryStr, parameters);
		System.out.println(list.size());
		return list;
	}

}
