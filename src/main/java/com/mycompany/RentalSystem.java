package com.mycompany;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class RentalSystem {
    private List<Vehicle> inventory;
    private List<Vehicle> pendingRequests;
    private final String FILE_NAME = "Vehicles.dat";

    public RentalSystem() {
        inventory = new ArrayList<>();
        pendingRequests = new ArrayList<>();
        loadData();
    }

    public void addVehicle(Vehicle v) {
        inventory.add(v);
        saveData();
    }

    public void removeVehicle(Vehicle v) {
        if (inventory.remove(v)) {
            pendingRequests.remove(v);
            saveData();
        }
    }

    public List<Vehicle> getInventory() { return inventory; }
    public List<Vehicle> getPendingRequests() { return pendingRequests; }
    public void sortInventory() { Collections.sort(inventory); }

    public Vehicle findVehicleById(String id) {
        for (Vehicle v : inventory) {
            if (v.getVehicleId().equalsIgnoreCase(id)) return v;
        }
        return null;
    }

    public void requestVehicle(Vehicle v) {
        if (v != null && v.isAvailable()) {
            v.setRequested(true);
            pendingRequests.add(v);
        }
    }

    public void approveRequest(Vehicle v) {
        if (pendingRequests.contains(v)) {
            v.setRequested(false);
            v.rent();
            pendingRequests.remove(v);
            saveData();
        }
    }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public void loadData() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                inventory = (List<Vehicle>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data: " + e.getMessage());
            }
        }
    }
}