package tn.esprit.myapplication.directory;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import tn.esprit.myapplication.entity.Wallet;

@Dao
public interface WalletDao {
    @Insert
    long insertWallet(Wallet wallet);

    @Query("SELECT * FROM wallet WHERE userId = :userId")
    List<Wallet> getWalletsByUserId(int userId);
    @Query("UPDATE wallet SET balanceToAdd = :balance WHERE userId = :userId")
    void updateWalletBalance(int userId, double balance);

    @Query("SELECT balanceToAdd FROM wallet WHERE userId = :userId LIMIT 1")
    double getWalletBalance(int userId);
}
