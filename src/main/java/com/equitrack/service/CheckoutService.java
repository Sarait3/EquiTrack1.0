package com.equitrack.service;

import java.sql.Date;
import java.time.LocalDate;

import com.equitrack.dao.DBAccessor;
import com.equitrack.model.*;

public class CheckoutService {
	
	public String checkoutForm(User user, String itemId) {
		Equipment equipment = DBAccessor.getEquipment(itemId);
		String html = String.format("<html>\r\n"
				+ "<head title=\"Checkout\">\r\n"
				+ "<body>\r\n"
				+ "<h1>Checkout %s</h1>\r\n"
				+ "<form method=\"post\">\r\n"
				+ "<label for=\"itemName\">Item Name: </label>\r\n"
				+ "<input type=\"text\" disabled name=\"itemName\" id=\"itemName\" value=\"%s\">\r\n"
				+ "<label for=\"itemId\">Item ID: </label>\r\n"
				+ "<input type=\"text\" disabled name=\"itemId\" id=\"itemId\" value=\"%s\">\r\n"
				+ "<label for=\"userFName\">Employee First Name: </label>\r\n"
				+ "<input type=\"text\" disabled name=\"userFName\" id=\"userFName\" value=\"%s\">\r\n"
				+ "<label for=\"userLName\">Employee Last Name: </label>\r\n"
				+ "<input type=\"text\" disabled name=\"userLName\" id=\"userLName\" value=\"%s\">\r\n"
				+ "<label for=\"userId\">Employee ID: </label>\r\n"
				+ "<input type=\"text\" disabled name=\"userId\" id=\"userId\" value=\"%s\">\r\n"
				+ "<label for=\"checkoutDate\">Checkout Date: </label>\r\n"
				+ "<input type=\"date\" disabled name=\"checkoutDate\" id=\"checkoutDate\" value=\"%s\">\r\n"
				+ "<input type=submit value=\"Submit\">"
				+ "</form>\r\n"
				+ "</body>\r\n"
				+ "</head>\r\n"
				+ "</html>", equipment.getName(), equipment.getName(), equipment.getId(), 
				user.getfName(), user.getlName(), user.getId(), Date.valueOf(LocalDate.now()));
		
		return html;
	}
	
	public void checkoutItem(String itemId, int userId, Date checkoutDate) {
		Equipment equipment = DBAccessor.getEquipment(itemId);
		
		if (equipment.isAvailable()) {
			equipment.setAvailbale(false);
		}
		
		DBAccessor.logCheckout(itemId, userId, checkoutDate);
		
		DBAccessor.updateEquipment(null);
	}
}

