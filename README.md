# Rentify — Vehicle Rental System

A JavaFX desktop application for managing a vehicle rental business: browsing inventory, requesting rentals, and approving/managing requests through a GUI, with object serialization for data persistence.

## Features

- **GUI Interface (JavaFX)** — Login screen, customer browsing view, and owner/admin management view under a single app ("RENTIFY")
- **Polymorphic Vehicle Hierarchy** — Abstract `Vehicle` base class implemented by `Car`, `Bike`, and `Van`, each with its own rental cost calculation and detail formatting
- **Rental Workflow** — Customers request a vehicle, requests go into a pending queue, and an owner/admin approves them to finalize the rental
- **Availability Tracking** — Vehicles track both "rented" and "requested" states so a vehicle already pending approval can't be double-booked
- **Sortable Inventory** — Vehicles implement `Comparable` for sorting by daily rate
- **Data Persistence** — Inventory is serialized to disk (`Vehicles.dat`) via Java object serialization, so data survives between runs

## Project Structure

- `App.java` — JavaFX application entry point and all UI screens (login, browse, admin)
- `Vehicle.java` — Abstract base class + `Rentable` interface defining the rental contract
- `Car.java` / `Bike.java` / `Van.java` — Concrete vehicle types with type-specific attributes and pricing
- `RentalSystem.java` — Core business logic: inventory management, request/approval workflow, save/load persistence

## Tools & Requirements

- Java (JDK 11+ recommended)
- JavaFX SDK
- Maven (project is structured as a Maven project — see `pom.xml`)

## How to Run

1. Make sure JavaFX is set up for your JDK (bundled separately from JDK 11+)
2. Build with Maven:
   ```bash
   mvn clean install
   ```
3. Run `App.java` (via your IDE, or `mvn javafx:run` if configured in `pom.xml`)

## Author

Eyad Ashraf Zaki — Mechatronics & Robotics Engineering, Ain Shams University