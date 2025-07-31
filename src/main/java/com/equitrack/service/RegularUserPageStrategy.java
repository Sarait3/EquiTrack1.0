package com.equitrack.service;

import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;

/**
 * Role-based page strategy for regular users Provides limited access and
 * actions appropriate to regular users
 */
public class RegularUserPageStrategy implements PageRoleStrategy {
    public boolean canManageUsers() { return false; }
    public boolean canEditEquipment() { return false; }
    public boolean canAddEquipment() { return false; }
    public boolean canCheckout() { return false; }
    public boolean canDeleteEquipment() { return false; }
    public boolean canRequestCheckout() { return true; }
    public boolean canApproveOrDeclineRequests() { return false; }
    public boolean canViewAllRequests() { return false; }
    public boolean canCreateUsers() { return false; }
    public boolean canDeleteUsers() { return false; }
    public String getRoleLabel() { return "Regular User"; }
}