package tn.esprit.myapplication.directory;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import tn.esprit.myapplication.entity.User;

@Dao
public interface UserDao {
    @Insert
    long insertUser(User user);
    @Query("SELECT id FROM users WHERE email = :email LIMIT 1")
    int getUserIdByEmail(String email);

    @Query("SELECT * FROM users WHERE id = :userId")
    User getUserById(int userId);
    @Query("SELECT * FROM users WHERE email = :email")
    User getUserByEmail(String email);
    @Query("SELECT COUNT(*) FROM users WHERE email = :email AND password = :password")
    int checkLoginCredentials(String email, String password);
}
