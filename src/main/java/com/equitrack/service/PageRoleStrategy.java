package com.equitrack.service;

/**
 * Defines role-specific page capabilities for the EquiTrack web application
 *
 * Implementations answer whether a given UI action or feature should be
 * enabled for the current user's role. 
 * 
 */
public interface PageRoleStrategy {

    /**
     * Whether the role can access user management features
     * such as viewing or editing user details
     *
     * @return true if user management is allowed
     */
    boolean canManageUsers();

    /**
     * Whether the role can add new equipment records
     *
     * @return true if adding equipment is allowed
     */
    boolean canAddEquipment();

    /**
     * Whether the role can edit existing equipment records
     *
     * @return true if editing equipment is allowed
     */
    boolean canEditEquipment();

    /**
     * Whether the role can perform direct equipment checkout (creating an approved checkout request)
     *
     * @return true if checkout is allowed
     */
    boolean canCheckout();

    /**
     * Whether the role can delete equipment records
     *
     * @return true if deleting equipment is allowed
     */
    boolean canDeleteEquipment();

    /**
     * Whether the role can submit a pending checkout request for approval
     *
     * @return true if requesting checkout is allowed
     */
    boolean canRequestCheckout();

    /**
     * Whether the role can approve or decline checkout requests
     * submitted by other users
     *
     * @return true if request approval is allowed
     */
    boolean canApproveOrDeclineRequests();

    /**
     * Whether the role can view all checkout requests across users
     *
     * @return true if viewing all requests is allowed
     */
    boolean canViewAllRequests();

    /**
     * Whether the role can create new user accounts
     *
     * @return true if creating users is allowed
     */
    boolean canCreateUsers();

    /**
     * Whether the role can delete user accounts
     *
     * @return true if deleting users is allowed
     */
    boolean canDeleteUsers();

    /**
     * Display label for the role
     *
     * @return the role name to show 
     */
    String getRoleLabel();
}
