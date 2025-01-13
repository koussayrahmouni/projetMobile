package tn.esprit.myapplication.directory;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

import tn.esprit.myapplication.entity.Vehicle;

@Dao
public interface VehicleDao {
    @Query("SELECT * FROM vehicle WHERE id = :vehicleId LIMIT 1")
    Vehicle getVehicleById(int vehicleId);
    @Delete
    void delete(Vehicle vehicle);
    // Update an existing vehicle
    @Update
    void update(Vehicle vehicle);
    @Insert
    void insert(Vehicle vehicle);
    @Query("SELECT * FROM vehicle WHERE userId = :userId")
    LiveData<List<Vehicle>> getAllVehicles(int userId);

}
