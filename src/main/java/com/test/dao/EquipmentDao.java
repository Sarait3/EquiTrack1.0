package com.test.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import com.test.model.*;

public class EquipmentDao implements EquipmentService {

	@Override
	public Map<UUID, Equipment> getAllEquipment() {
		String sql = "SELECT id, name, isAvailable, location, imagePath, notes FROM Equipment ";
		Map<UUID, Equipment> result = new LinkedHashMap<>();
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				UUID id = UUID.fromString(rs.getString("id"));
				Equipment equipment = new Equipment(rs.getString("id"), rs.getString("name"),
						"Available".equalsIgnoreCase(rs.getString("isAvailable")), rs.getString("location"),
						rs.getString("imagePath"), rs.getString("notes"));
				result.put(id, equipment);
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new RuntimeException("Failed to read equipment", e);
		}
		return result;
	}

	@Override
	public Equipment getEquipment(String id) {
		String sql = "SELECT id, name, isAvailable, location, imagePath, notes FROM Equipment WHERE id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return new Equipment(rs.getString("id"), rs.getString("name"),
							"Available".equalsIgnoreCase(rs.getString("isAvailable")), rs.getString("location"),
							rs.getString("imagePath"), rs.getString("notes"));
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new RuntimeException("Failed to find equipment: " + id, e);
		}
		return null;
	}

	@Override
	public void createEquipment(Equipment equipment) {
		String sql = "INSERT INTO Equipment (id, name, isAvailable, location, imagePath, notes) VALUES (?, ?, ?, ?, ?, ?)";
		UUID newId = UUID.randomUUID();
		equipment.setId(newId.toString());
		String available = equipment.isAvailable() ? "Available" : "Unavailable";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, equipment.getId());
			stmt.setString(2, equipment.getName());
			stmt.setString(3, available);
			stmt.setString(4, equipment.getLocation());
			stmt.setString(5, equipment.getImagePath());
			stmt.setString(6, equipment.getNotes());
			stmt.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			throw new RuntimeException("Failed to create equipment", e);
		}
	}

	@Override
	public void updateEquipment(Equipment equipment) {
		String sql = "UPDATE Equipment SET name = ?, isAvailable = ?, location= ?, imagePath= ?, notes= ? WHERE id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, equipment.getName());
			stmt.setString(2, equipment.isAvailable() ? "Available" : "Unavailable");
			stmt.setString(3, equipment.getLocation());
			stmt.setString(4, equipment.getImagePath());
			stmt.setString(5, equipment.getNotes());
			stmt.setString(6, equipment.getId());
			stmt.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			throw new RuntimeException("Failed to update equipment: " + equipment.getId(), e);
		}
	}

	@Override
	public void deleteEquipment(String id) {
		String sql = "DELETE FROM Equipment WHERE id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, id);
			stmt.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			throw new RuntimeException("Failed to delete equipment: " + id, e);
		}
	}

	@Override
	public void createOrUpdateEquipment(Equipment equipment) {
		if (equipment.getId() == null || equipment.getId().isEmpty()) {
			createEquipment(equipment);
		} else {
			updateEquipment(equipment);
		}
	}
}
