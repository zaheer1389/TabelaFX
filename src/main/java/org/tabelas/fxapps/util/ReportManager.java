package org.tabelas.fxapps.util;

import java.io.InputStream;
import java.util.Map;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.tabelas.fxapps.App;

public class ReportManager {
	
	public static void showReport(InputStream is, Map<String, Object> map,String title) {
		try {
			System.out.println(map);
			JasperReport jr = (JasperReport) JRLoader.loadObject(is);
			JasperPrint jp = JasperFillManager.fillReport(jr, map,App.getConnection());
	
			JasperViewer viewer = new JasperViewer(jp, false);
			viewer.setTitle(title);
			viewer.setVisible(true);
			viewer.toFront();
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			DialogFactory.showExceptionDialog(e, null);
		}
	}

}
