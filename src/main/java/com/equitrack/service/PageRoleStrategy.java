package com.equitrack.service;

import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;

/**
 * Defines role-specific page elements for the EquiTrack web application.
 * Implemented by strategy classes for Manager and Regular users
 */
public interface PageRoleStrategy {
    boolean canManageUsers();
    boolean canAddEquipment();
    boolean canEditEquipment();
    boolean canCheckout();
    boolean canDeleteEquipment();
    boolean canRequestCheckout();
    boolean canApproveOrDeclineRequests();
    boolean canViewAllRequests();
    boolean canCreateUsers();
    boolean canDeleteUsers();
    String getRoleLabel();
}