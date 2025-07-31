package com.equitrack.service;

import java.util.Map;
import java.util.UUID;

import com.equitrack.dao.UserDao;
import com.equitrack.model.User;

public class AdminPageStrategy extends ManagerPageStrategy {
    @Override public boolean canCreateUsers() { return true; }
    @Override public boolean canDeleteUsers() { return true; }
    @Override public String getRoleLabel() { return "Admin"; }
}