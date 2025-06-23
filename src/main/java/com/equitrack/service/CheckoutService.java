package com.equitrack.service;

import java.sql.Date;
import java.time.LocalDate;

import com.equitrack.dao.EquipmentDao;
import com.equitrack.model.*;

public class CheckoutService {
	
	
	public String checkoutForm(User user, String itemId) {
		Equipment equipment = EquipmentDao.getEquipment(itemId);
		String html = String.format("<html>\r\n"
				+ "<head title=\"Checkout\">\r\n"
				+ "<body>\r\n"
				+ "<h1>Checkout %s</h1>\r\n"
				+ "<form method=\"post\">\r\n"
				+ "<label for=\"itemName\">Item Name: </label>\r\n"
				+ "<input type=\"text\" readonly name=\"itemName\" id=\"itemName\" value=\"%s\"><br>\r\n"
				+ "<label for=\"itemId\">Item ID: </label>\r\n"
				+ "<input type=\"text\" readonly name=\"itemId\" id=\"itemId\" value=\"%s\"><br>\r\n"
				+ "<label for=\"userFName\">Employee First Name: </label>\r\n"
				+ "<input type=\"text\" readonly name=\"userFName\" id=\"userFName\" value=\"%s\"><br>\r\n"
				+ "<label for=\"userLName\">Employee Last Name: </label>\r\n"
				+ "<input type=\"text\" readonly name=\"userLName\" id=\"userLName\" value=\"%s\"><br>\r\n"
				+ "<label for=\"userId\">Employee ID: </label>\r\n"
				+ "<input type=\"text\" readonly name=\"userId\" id=\"userId\" value=\"%s\"><br>\r\n"
				+ "<label for=\"checkoutDate\">Checkout Date: </label>\r\n"
				+ "<input type=\"date\" readonly name=\"checkoutDate\" id=\"checkoutDate\" value=\"%s\"><br>\r\n"
				+ "<label for=\"returnDate\">Return Date: </label>\r\n"
				+ "<input type=\"date\" required name=\"returnDate\" id=\"returnDate\" value=\"\"><br>\r\n"
				+ "<input type=submit value=\"Submit\">"
				+ "</form>\r\n"
				+ "</body>\r\n"
				+ "</head>\r\n"
				+ "</html>", equipment.getName(), equipment.getName(), equipment.getId().toString(), 
				user.getfName(), user.getlName(), Integer.toString(user.getId()), Date.valueOf(LocalDate.now()).toString());
		
		return html;
	}
	
	public void checkoutItem(String itemId, int userId, Date checkoutDate, Date returnDate) {
		Equipment equipment = EquipmentDao.getEquipment(itemId);
		
		if (equipment.isAvailable()) {
			equipment.setAvailbale(false);
		}
		
		EquipmentDao.logCheckout(itemId, userId, checkoutDate, returnDate);
		
		EquipmentDao.updateEquipment(null);
	}
}

