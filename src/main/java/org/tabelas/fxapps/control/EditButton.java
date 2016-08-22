package org.tabelas.fxapps.control;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class EditButton  extends TableCell<Object, String> {
    
	Hyperlink button = new Hyperlink();
    
    public EditButton(){
    	button.setPrefWidth(100);
    	button.setPrefHeight(20);
    	//button.getStyleClass().add("btnEdit");
    	Glyph image = new Glyph("FontAwesome", FontAwesome.Glyph.PENCIL).size(20);
    	button.setGraphic(image);
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
