package com.equitrack.service;

import java.util.Map;
import java.util.UUID;

import com.equitrack.dao.UserDao;
import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;

/**
 * Defines what content to show for Manager users. Handles the sidebar,
 * equipment actions, request actions, account management, and user list
 * features
 */
public class ManagerPageStrategy implements PageRoleStrategy {
    public boolean canManageUsers() { return true; }
    public boolean canEditEquipment() { return true; }
    public boolean canAddEquipment() { return true; }
    public boolean canCheckout() { return true; }
    public boolean canDeleteEquipment() { return true; }
    public boolean canRequestCheckout() { return false; }
    public boolean canApproveOrDeclineRequests() { return true; }
    public boolean canViewAllRequests() { return true; }
    public boolean canCreateUsers() { return false; }
    public boolean canDeleteUsers() { return false; }
    public String getRoleLabel() { return "Manager"; }
}