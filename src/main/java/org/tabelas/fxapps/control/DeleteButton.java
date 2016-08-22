package org.tabelas.fxapps.control;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.tabelas.fxapps.util.DialogFactory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

public class DeleteButton  extends TableCell<Object, String> {
    
	Hyperlink button = new Hyperlink();
    
    public DeleteButton(){
    	button.setPrefWidth(100);
    	button.setPrefHeight(22);
    	//button.getStyleClass().add("btnDelete");
    	button.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TRASH_ALT).size(20).color(Color.BLUE));
    }

    //Display button if the row is not empty
    @Override
    protected void updateItem(String t, boolean empty) {
        super.updateItem(t, empty);
        if(!empty){
            setGraphic(button);
        }
    }
    
    public Hyperlink getButton(){
    	return button;
    }
    
    public int getRowIndex(){
    	return getTableRow().getIndex();
    }
}
