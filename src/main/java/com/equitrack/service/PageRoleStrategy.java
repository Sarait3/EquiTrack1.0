package com.equitrack.service;

import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;

public interface PageRoleStrategy {
	public String buildSidebar();
	String buildEquipmentActions(User user, Equipment equipment);
	String buildRequestActions(User user, Request request);
}
