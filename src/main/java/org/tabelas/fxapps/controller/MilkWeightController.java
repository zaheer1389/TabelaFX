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
import org.tabelas.fxapps.model.AnimalMilkWeight;
import org.tabelas.fxapps.model.AnimalService;
import org.tabelas.fxapps.model.Lactation;
import org.tabelas.fxapps.persistence.FacadeFactory;
import org.tabelas.fxapps.util.AppUtil;
import org.tabelas.fxapps.util.DialogFactory;
import org.tabelas.fxapps.view.View;


public class MilkWeightController implements View{

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
    private TableColumn<AnimalMilkWeight, String> colAnimalNo,colObservedDate,colWeight;
    
    @FXML
    private TableColumn<Object, String> colEdit,colDelete;

    @FXML
    private DatePicker txtObservedDate,txtCowingDate,txtDepartureDate;
    
    @FXML
    private TextField txtSearchAnimalNo,txtResult;
    
    @FXML
    private TableView<AnimalMilkWeight> tableView;
    
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
    	
    	txtObservedDate.setConverter(AppUtil.getDatePickerFormatter());
    	
    	btnSave.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.SAVE));
    	btnCancel.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES));
    	btnSearch.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.SEARCH));
    	btnClearSearch.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES));
    	
    	cbAnimal.setItems(FXCollections.observableArrayList(AnimalController.getAnimalsByBranch()));
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
    	
    	List<AnimalMilkWeight> data = getAnimalMilkWeightByBranch();
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
    
    public void setTable(List<AnimalMilkWeight> data){

    	colAnimalNo.setCellValueFactory(new PropertyValueFactory<AnimalMilkWeight, String>("animalNo"));
		colObservedDate.setCellValueFactory(new PropertyValueFactory<AnimalMilkWeight, String>("strObservedDate"));
		colWeight.setCellValueFactory(new PropertyValueFactory<AnimalMilkWeight, String>("weight"));		
		
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
						AnimalMilkWeight milkWeight = tableView.getItems().get(selectdIndex);
						id = milkWeight.getId();
						
						cbAnimal.setValue(milkWeight.getLactation().getAnimal());
						txtObservedDate.setValue(AppUtil.toLocalDate(new Date(milkWeight.getWeightDate().getTime())));
						txtResult.setText(milkWeight.getWeight()+"");
						
						cbAnimal.setDisable(true);
						txtObservedDate.setDisable(true);
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
							AnimalMilkWeight weight = tableView.getItems().get(selectdIndex);
							FacadeFactory.getFacade().delete(weight);
							DialogFactory.showInformationDialog("Animal Milk Weight details deleted succssfully", App.appcontroller.stage);
					    	setPagePanel(getAnimalMilkWeightByBranch());
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
    
	public void setPagePanel(List<AnimalMilkWeight> data){
		if(navigationBox.getChildren().size() > 0){
			navigationBox.getChildren().remove(0);
		}
		int totalPages = (int) Math.ceil((double)data.size() / (double)PAGE_SIZE);
		int pageFrom = 1;
		int pageTo = totalPages > 10 ? 10 : totalPages;   
		navigationBox.getChildren().add(getPagination(pageFrom, pageTo, totalPages));
		
		int showFrom = currentPageIndex*PAGE_SIZE;
		int showTo = (showFrom + PAGE_SIZE) <= data.size() ? (showFrom + PAGE_SIZE) : data.size();
		List<AnimalMilkWeight> subList = data.subList(showFrom, showTo);
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
					List<AnimalMilkWeight> data = getAnimalMilkWeightByBranch();
					int showFrom = currentPageIndex*PAGE_SIZE;
					int showTo = (showFrom + PAGE_SIZE) <= data.size() ? (showFrom + PAGE_SIZE) : data.size();
					System.err.println("[from:"+showFrom+",to:"+showTo+"]");
					List<AnimalMilkWeight> subList = data.subList(showFrom, showTo);
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
    			DialogFactory.showErrorDialog("Animal is not present in tabela,You are not allowed to record milk weight.", null);
    			return;
    		}
    		Response response = DialogFactory.showConfirmationDialog("Do you want to save details?", DialogType.YESNOCANCEL, App.appcontroller.getStage());
    		if(response == Response.YES){
    			AnimalMilkWeight milkWeight;
    			if(id == null){
    				milkWeight = new AnimalMilkWeight();
    				milkWeight.setAddedDate(new Timestamp(new Date().getTime()));
    				milkWeight.setWeightDate(new Timestamp(AppUtil.toUtilDate(txtObservedDate.getValue()).getTime()));
    				Lactation lactation = LactationController.getCurrentLactation(cbAnimal.getValue().getAnimalNo());
    				milkWeight.setLactation(lactation);
    			}
    			else{
    				milkWeight = FacadeFactory.getFacade().find(AnimalMilkWeight.class, id);
    			}
    			milkWeight.setWeight(Double.parseDouble(txtResult.getText()));
    			FacadeFactory.getFacade().store(milkWeight);
    			DialogFactory.showInformationDialog("Milk weight record saved successfully", null);
    			
    			List<AnimalMilkWeight> data = getAnimalMilkWeightByBranch();
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
    	txtObservedDate.setValue(null);
    	txtResult.setText("");
    	
    	cbAnimal.setDisable(false);
    	txtObservedDate.setDisable(false);
    	
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
    	List<AnimalMilkWeight> data = getAnimalMilkWeightByBranch(txtSearchAnimalNo.getText());
    	if(data.size() == 0){
    		DialogFactory.showInformationDialog("No record found,Please try with different no", null);
    	}

		currentPageIndex = 0;
    	setPagePanel(data);
	}
	
	@Override
	@FXML
	public void resetSearch() {
		List<AnimalMilkWeight> data = getAnimalMilkWeightByBranch();
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
    	else if(txtObservedDate.getValue() == null){
    		DialogFactory.showErrorDialog("Please select animal observation date", null);
    		txtObservedDate.requestFocus();
    		return false;
    	}   
    	else if(txtResult.getText().length() == 0){
    		DialogFactory.showErrorDialog("Please enter animal milk weight", null);
    		txtResult.requestFocus();
    		return false;
    	}
    	else if(!AppUtil.isNumeric(txtResult.getText())){
    		DialogFactory.showErrorDialog("Weight must have digit only", null);
    		txtResult.setText("");
    		txtResult.requestFocus();
    		return false;
    	}
		return true;
	}
	
	public static List<AnimalMilkWeight> getAnimalMilkWeightByBranch(String no){
		String queryStr = "Select w from  AnimalMilkWeight as w join w.lactation as l join l.animal as a where a.branch = :bid and a.animalNo = :no  order by w.id desc";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("bid", App.appcontroller.getBranch());
		parameters.put("no", no);
		List<AnimalMilkWeight> list = FacadeFactory.getFacade().list(queryStr, parameters);
		System.out.println(list.size());
		return list;
	}
	
	public static List<AnimalMilkWeight> getAnimalMilkWeightByBranch(){
		String queryStr = "Select w from  AnimalMilkWeight as w join w.lactation as l join l.animal as a where a.branch = :bid  order by w.id desc";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("bid", App.appcontroller.getBranch());
		List<AnimalMilkWeight> list = FacadeFactory.getFacade().list(queryStr, parameters);
		System.out.println(list.size());
		return list;
	}

}
