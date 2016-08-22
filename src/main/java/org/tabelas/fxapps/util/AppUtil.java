package org.tabelas.fxapps.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.util.StringConverter;

public class AppUtil {

	public static double getScreenWidth(){
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		return primScreenBounds.getWidth();
	}
	
	public static double getScreenHeight(){
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		return primScreenBounds.getHeight();
	}
	
	public static boolean isNumeric(String str){	
		try{
			Double.parseDouble(str);
		}
		catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	
	public static StringConverter<LocalDate> getDatePickerFormatter(){
		final String pattern = "d-M-yyyy";
		StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = 
                DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        }; 
        return converter;
	}
	
	public static Date toUtilDate(LocalDate localDate){
		Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();				
		return Date.from(instant);
	}
	
	public static LocalDate toLocalDate(Date date){
		Instant instant = date.toInstant();
		LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
		return localDate;
	}
	
}
