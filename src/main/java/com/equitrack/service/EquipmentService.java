package com.equitrack.service;


import com.equitrack.model.Equipment;
import java.util.ArrayList;
import java.util.List;

public class EquipmentService {

    /**
     * Filters a list of equipment items by search input and operational status.
     *
     * @param equipmentList the original list of equipment
     * @param searchInput   text to search in name, id, or location (optional)
     * @param statusFilter  "operational" or "out of service" (optional)
     * @return filtered list
     */
	public static ArrayList<Equipment> filterEquipment(List<Equipment> equipmentList, String searchInput, String statusFilter) {
        ArrayList<Equipment> result = new ArrayList<>(equipmentList);

        if (searchInput != null && !searchInput.trim().isEmpty()) {
            String lowerSearch = searchInput.toLowerCase();
            ArrayList<Equipment> filteredList = new ArrayList<>();
            for (Equipment eq : result) {
                if (eq.getName().toLowerCase().contains(lowerSearch) || eq.getId().toLowerCase().contains(lowerSearch)
                        || eq.getLocation().toLowerCase().contains(lowerSearch)) {
                    filteredList.add(eq);
                }
            }
            result = filteredList;
        }

        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            boolean operational = statusFilter.equalsIgnoreCase("operational");
            ArrayList<Equipment> filteredList = new ArrayList<>();
            for (Equipment eq : result) {
                if (eq.isOperational() == operational) {
                    filteredList.add(eq);
                }
            }
            result = filteredList;
        }

        return result;
    }
} 
