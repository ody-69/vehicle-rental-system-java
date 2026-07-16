package com.mycompany;

import java.io.Serializable;

interface Rentable {
    double calculateRentalCost(int days);
    boolean isAvailable();
}

abstract class Vehicle implements Rentable, Comparable<Vehicle>, Serializable {
    private static final long serialVersionUID = 1L;
    protected String vehicleId;
    protected String model;
    protected double dailyRate;
    protected boolean isRented;
    protected boolean isRequested;

    public Vehicle(String vehicleId, String model, double dailyRate) {
        this.vehicleId = vehicleId;
        this.model = model;
        this.dailyRate = dailyRate;
        this.isRented = false;
        this.isRequested = false;
    }

    public abstract String getDetails();

    public void rent() { this.isRented = true; }
    public void returnVehicle() { this.isRented = false; this.isRequested = false; }

    @Override
    public boolean isAvailable() { return !isRented && !isRequested; }

    @Override
    public int compareTo(Vehicle o) {
        return Double.compare(this.dailyRate, o.dailyRate);
    }

    public String getVehicleId() { return vehicleId; }
    public String getModel() { return model; }
    public double getDailyRate() { return dailyRate; }
    public boolean isRequested() { return isRequested; }
    public void setRequested(boolean requested) { this.isRequested = requested; }
}