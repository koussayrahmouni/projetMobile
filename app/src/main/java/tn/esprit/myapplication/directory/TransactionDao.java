package tn.esprit.myapplication.directory;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import tn.esprit.myapplication.entity.Transaction;

@Dao
public interface TransactionDao {
    @Insert
    long insertTransaction(Transaction transaction);

    @Query("SELECT * FROM transactions WHERE userId = :userId")
    List<Transaction> getTransactionsByUserId(int userId);
}
