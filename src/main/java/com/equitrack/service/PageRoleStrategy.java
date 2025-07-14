package com.equitrack.service;

import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;

public interface PageRoleStrategy {
	public String buildSidebar();
	public String buildEquipmentActions(User user, Equipment equipment);
	public String buildRequestActions(User user, Request request);
	public String buildManageAccount();
	public String buildUserList();
	public String buildCreateUser();
}
