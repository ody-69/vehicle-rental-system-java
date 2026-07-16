package com.mycompany;

class Car extends Vehicle {
    private final int numberOfSeats;
    public Car(String vehicleId, String model, double dailyRate, int numberOfSeats) {
        super(vehicleId, model, dailyRate);
        this.numberOfSeats = numberOfSeats;
    }
    @Override
    public double calculateRentalCost(int days) { return dailyRate * days; }
    @Override
    public String getDetails() { return "CAR  | ID: " + vehicleId + " | " + model + " (" + numberOfSeats + " seats) - $" + dailyRate + "/day"; }
}