package com.equitrack.service;

import java.sql.Date;
import java.time.LocalDate;

import com.equitrack.dao.EquipmentDao;
import com.equitrack.model.*;

public class CheckoutService {
	private EquipmentDao dao = new EquipmentDao();
	
	public String checkoutForm(User user, String itemId) {
		Equipment equipment = dao.getEquipment(itemId);
		String html = String.format("<!DOCTYPE html>\r\n"
				+ "<html lang='en'>\r\n"
				+ "<html>\r\n"
				+ "\r\n"
				+ "<head>\r\n"
				+ "    <meta charset='UTF-8'>\r\n"
				+ "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>\r\n"
				+ "    <title>Checkout</title>\r\n"
				+ "    <link rel='stylesheet' href='css/style.css'>\r\n"
				+ "</head>\r\n"
				+ "\r\n"
				+ "<body>\r\n"
				+ "    <div class='header'>\r\n"
				+ "        <h1>Ceckout %s</h1>\r\n"
				+ "        <div class='header-content'>\r\n"
				+ "        </div>\r\n"
				+ "    </div>\r\n"
				+ "    <form class='container-detail edit-form' action='Checkout' method='POST' enctype=\"multipart/form-data\">\r\n"
				+ "        <label for='itemName'>Item Name: </label>\r\n"
				+ "        <input type='text' readonly name='itemName' id='itemName' value='%s'>\r\n"
				+ "        <label for='itemId'>Item ID: </label>\r\n"
				+ "        <input type='text' readonly name='itemId' id='itemId' value='%s'>\r\n"
				+ "        <label for='userFName'>Employee First Name: </label>\r\n"
				+ "        <input type='text' readonly name='userFName' id='userFName' value='%s'>\r\n"
				+ "        <label for='userLName'>Employee Last Name: </label>\r\n"
				+ "        <input type='text' readonly name='userLName' id='userLName' value='%s'>\r\n"
				+ "        <label for='userId'>Employee ID: </label>\r\n"
				+ "        <input type='text' readonly name='userId' id='userId' value='%s'>\r\n"
				+ "        <label for='checkoutDate'>Checkout Date: </label>\r\n"
				+ "        <input type='date' readonly name='checkoutDate' id='checkoutDate' value='%s'>\r\n"
				+ "        <label for='returnDate'>Return Date: </label>\r\n"
				+ "        <input type='date' required name='returnDate' id='returnDate' value=''>\r\n"
				+ "        <button type='submit'>Submit</button>\r\n"
				+ "    </form>\r\n"
				+ "</body>\r\n"
				+ "\r\n"
				+ "</html>", equipment.getName(), equipment.getName(), equipment.getId().toString(), 
				user.getFName(), user.getLName(), Integer.toString(user.getId()), Date.valueOf(LocalDate.now()).toString());
		
		return html;
	}
	
	public void checkoutItem(String itemId, int userId, Date checkoutDate, Date returnDate) {
		Equipment equipment = dao.getEquipment(itemId);
		
		if (equipment.isAvailable()) {
			equipment.setAvailbale(false);
		}
		
		dao.logCheckout(itemId, userId, checkoutDate, returnDate);
		
		dao.updateEquipment(null);
	}
}

