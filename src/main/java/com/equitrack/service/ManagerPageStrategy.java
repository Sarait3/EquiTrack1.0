package com.equitrack.service;


/**
 * Page-role strategy for Manager users.
 *
 * Determines what UI elements and actions are available to managers,
 * including sidebar items, equipment actions, request actions, account
 * management options, and access to the user list.
 *
 */
public class ManagerPageStrategy implements PageRoleStrategy {

    /**
     * Indicates whether managers can manage users in general.
     * This covers tasks like viewing the user list or editing basic details.
     * Creation and deletion are controlled separately.
     *
     * @return true
     */
    public boolean canManageUsers() { return true; }

    /**
     * Indicates whether managers can edit existing equipment records
     *
     * @return true
     */
    public boolean canEditEquipment() { return true; }

    /**
     * Indicates whether managers can add new equipment records
     *
     * @return true
     */
    public boolean canAddEquipment() { return true; }

    /**
     * Indicates whether managers can create an automatically approved checkout request
     *
     * @return true
     */
    public boolean canCheckout() { return true; }

    /**
     * Indicates whether managers can delete equipment records
     *
     * @return true
     */
    public boolean canDeleteEquipment() { return true; }

    /**
     * Indicates whether managers submit pending checkout requests.
     *
     * @return false
     */
    public boolean canRequestCheckout() { return false; }

    /**
     * Indicates whether managers can approve or decline checkout requests
     *
     * @return true
     */
    public boolean canApproveOrDeclineRequests() { return true; }

    /**
     * Indicates whether managers can view all requests across users
     *
     * @return true
     */
    public boolean canViewAllRequests() { return true; }

    /**
     * Indicates whether managers can create new user accounts.
     * Reserved for admin
     *
     * @return false
     */
    public boolean canCreateUsers() { return false; }

    /**
     * Indicates whether managers can delete user accounts.
     * Reserved for admin
     *
     * @return false
     */
    public boolean canDeleteUsers() { return false; }

    /**
     * Returns the display label for this role
     *
     * @return "Manager"
     */
    public String getRoleLabel() { return "Manager"; }
}
