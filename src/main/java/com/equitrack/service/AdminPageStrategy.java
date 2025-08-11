package com.equitrack.service;



/**
 * Page-role strategy for administrator users.
 *
 * Compared to ManagerPageStrategy, admins have full user-management
 * capabilities (create and delete) and the role label is "Admin".
 *
 */
public class AdminPageStrategy extends ManagerPageStrategy {

    /**
     * Indicates whether this role can create users.
     * For admin, this returns true
     *
     * @return true
     */
    @Override
    public boolean canCreateUsers() { return true; }

    /**
     * Indicates whether this role can delete users.
     * For admins, this returns true
     *
     * @return true
     */
    @Override
    public boolean canDeleteUsers() { return true; }

    /**
     * Returns the display label for this role
     *
     * @return "Admin"
     */
    @Override
    public String getRoleLabel() { return "Admin"; }
}
