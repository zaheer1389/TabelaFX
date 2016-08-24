package org.tabelas.fxapps.controller;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.tabelas.fxapps.App;
import org.tabelas.fxapps.model.AnimalPojo;
import org.tabelas.fxapps.persistence.JPAFacade;
import org.tabelas.fxapps.util.DialogFactory;
import org.tabelas.fxapps.util.ReportManager;

public class DashboardController {

    @FXML
    private TableView<AnimalPojo> tableServiceDueList;

    @FXML
    private Button btnPrintSalvageList;

    @FXML
    private SplitPane splitPane;

    @FXML
    private TableView<AnimalPojo> tableSalvageAnimalList;

    @FXML
    private Button btnPrintServiceDueList;
    
    @FXML
    private TableColumn<AnimalPojo, String> colAnimalNo,colAnimalNo2;
    
    @FXML
    private TableColumn<AnimalPojo, String>  colDepartureDate,colExpectedArrivalDate;
    
    @FXML
    private TableColumn<AnimalPojo, String>  colArrivalDate,colServiceDate;

    @FXML
    void print(ActionEvent event) {
    	
    	/*if(App.appcontroller.getBranch() == null){
			DialogFactory.showErrorDialog("No branch has been selected.Please select branch to print report", null);
			return;
		}*/
    	
    	if(event.getSource() == btnPrintSalvageList){
    		Map<String, Object> map = new HashMap<>();
			//map.put("branchName",  App.appcontroller.getBranch().getBranchName());
			InputStream is3 = getClass().getResourceAsStream("/reports/SalvageAnimalReport2.jasper");
			ReportManager.showReport(is3, map, "Salvage Due Animal List");
    	}
    	else if(event.getSource() == btnPrintServiceDueList){
    		Map<String, Object> map = new HashMap<>();
			//map.put("branchName",  App.appcontroller.getBranch().getBranchName());
			InputStream is3 = getClass().getResourceAsStream("/reports/ServiceDueReport2.jasper");
			ReportManager.showReport(is3, map, "Service Due Animal List");
    	}
    }
    
    @FXML
    void initialize() {
    	btnPrintSalvageList.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.PRINT));
    	btnPrintServiceDueList.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.PRINT));
    	
    	setServiceDueTable();
    	setSalvageDueTable();
    }
    
    public void setServiceDueTable(){
    	colAnimalNo.setCellValueFactory(new PropertyValueFactory<AnimalPojo, String>("animalNo"));
		colArrivalDate.setCellValueFactory(new PropertyValueFactory<AnimalPojo, String>("arrivalDate"));
		colServiceDate.setCellValueFactory(new PropertyValueFactory<AnimalPojo, String>("serviceDate"));
		
		tableServiceDueList.setItems(FXCollections.observableArrayList(getServiceDueAnimals()));
    }
    
    public void setSalvageDueTable(){
    	colAnimalNo2.setCellValueFactory(new PropertyValueFactory<AnimalPojo, String>("animalNo"));
    	colDepartureDate.setCellValueFactory(new PropertyValueFactory<AnimalPojo, String>("departureDate"));
		colExpectedArrivalDate.setCellValueFactory(new PropertyValueFactory<AnimalPojo, String>("expectedArrivalDate"));
		
		tableSalvageAnimalList.setItems(FXCollections.observableArrayList(getSalvageAnimals()));
    }
    
    public List<AnimalPojo> getServiceDueAnimals(){
		List<AnimalPojo> list = new ArrayList<AnimalPojo>();
		String queryStr = "Select AnimalNo,ArrivalDate From Animal a,Lactation l "
				+ "where a.id = l.AnimalId "
				+ "and DepartureDate IS NULL "
				+ "and date('now') > date(l.ArrivalDate, '+60 day') "
				+ "and l.CurrentLactation = 1 "
				+ "and (select count(id) from AnimalService where lactationId = l.id) = 0";
		EntityManager em = JPAFacade.getEntityManager();
		Query query = em.createNativeQuery(queryStr);
		System.out.println(queryStr);
		List<Object[]> data = query.getResultList();
		for (Object[] objects : data) {
			String animalNo = objects[0].toString();
			Date dt = new Date((Long)objects[1]);
			Date dt2 = new Date(dt.getTime()+(60L*24*60*60*1000));
			System.out.println(dt);
			System.out.println(dt2);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMMMM-yyyy");
			list.add(new AnimalPojo(animalNo, sdf.format(dt), "", sdf.format(dt2), ""));
		}
		
		return list;
	}
    
    public List<AnimalPojo> getSalvageAnimals(){
		List<AnimalPojo> list = new ArrayList<AnimalPojo>();
		String queryStr = "Select AnimalNo,DepartureDate,ServiceDate From Animal a, Lactation l, AnimalService s "
				+ "where a.id = l.AnimalId "
				+ "and l.id in (select max(id) from Lactation l "
				+ "where AnimalId NOT IN (select AnimalId from Lactation "
				+ "where CurrentLactation = 1) "
				+ "and l.id = s.LactationId "
				+ "and s.Result = 'PREGNANT' "
				+ "Group By AnimalId "
				+ "Order By DepartureDate Desc) ORDER BY l.id desc LIMIT 1";	
		System.out.println(queryStr);
		EntityManager em = JPAFacade.getEntityManager();
		Query query = em.createNativeQuery(queryStr);
		
		List<Object[]> data = query.getResultList();
		for (Object[] objects : data) {
			String animalNo = objects[0].toString();
			Date dt = new Date((Long)objects[1]);
			Date dtt = new Date((Long)objects[2]);
			Date dt2 = new Date(dtt.getTime()+(320L*24*60*60*1000));
			System.out.println(dt);
			System.out.println(dt2);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMMMM-yyyy");
			list.add(new AnimalPojo(animalNo, "", sdf.format(dt), "", sdf.format(dt2)));
		}
		
		return list;
	}
}
