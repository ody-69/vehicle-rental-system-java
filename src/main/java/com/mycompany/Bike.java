package com.mycompany;

class Bike extends Vehicle {
    private final boolean hasHelmet;
    public Bike(String vehicleId, String model, double dailyRate, boolean hasHelmet) {
        super(vehicleId, model, dailyRate);
        this.hasHelmet = hasHelmet;
    }
    @Override
    public double calculateRentalCost(int days) { return dailyRate * days; }
    @Override
    public String getDetails() { 
        String helmetStatus = hasHelmet ? "With Helmet" : "No Helmet";
        return "BIKE | ID: " + vehicleId + " | " + model + " [" + helmetStatus + "] - $" + dailyRate + "/day"; 
    }
}