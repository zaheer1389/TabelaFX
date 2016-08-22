package org.tabelas.fxapps.persistence;

public class FacadeFactory {
	
	public static IFacade getFacade(){
		return new JPAFacade();
	}

}
