package com.equitrack.service;


/**
 * Role-based page strategy for regular users
 *
 * Provides limited access and actions appropriate to non-privileged users.
 * 
 *
 */
public class RegularUserPageStrategy implements PageRoleStrategy {

    /**
     * Whether regular users can access user management features
     *
     * @return false
     */
    @Override
    public boolean canManageUsers() { return false; }

    /**
     * Whether regular users can edit existing equipment records
     *
     * @return false
     */
    @Override
    public boolean canEditEquipment() { return false; }

    /**
     * Whether regular users can add new equipment records
     *
     * @return false
     */
    @Override
    public boolean canAddEquipment() { return false; }

    /**
     * Whether regular users can perform direct equipment checkout actions (Creating approved checkout requests)
     *
     * @return false
     */
    @Override
    public boolean canCheckout() { return false; }

    /**
     * Whether regular users can delete equipment records
     *
     * @return false
     */
    @Override
    public boolean canDeleteEquipment() { return false; }

    /**
     * Whether regular users can submit a pending checkout request for approval
     *
     * @return true
     */
    @Override
    public boolean canRequestCheckout() { return true; }

    /**
     * Whether regular users can approve or decline checkout requests
     *
     * @return false
     */
    @Override
    public boolean canApproveOrDeclineRequests() { return false; }

    /**
     * Whether regular users can view all checkout requests across users
     *
     * @return false
     */
    @Override
    public boolean canViewAllRequests() { return false; }

    /**
     * Whether regular users can create new user accounts
     *
     * @return false
     */
    @Override
    public boolean canCreateUsers() { return false; }

    /**
     * Whether regular users can delete user accounts
     *
     * @return false
     */
    @Override
    public boolean canDeleteUsers() { return false; }

    /**
     * Display label for this role
     *
     * @return "Regular User"
     */
    @Override
    public String getRoleLabel() { return "Regular User"; }
}
