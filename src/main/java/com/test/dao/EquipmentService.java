package com.test.dao;

import java.util.Map;
import java.util.UUID;
import com.test.model.*;

/**
 * Defines CRUD operations for Equipment.
 */
public interface EquipmentService {

	Map<UUID, Equipment> getAllEquipment();

	Equipment getEquipment(String id);

	void createEquipment(Equipment equipment);

	void updateEquipment(Equipment equipment);

	void deleteEquipment(String id);

	void createOrUpdateEquipment(Equipment equipment);
}
