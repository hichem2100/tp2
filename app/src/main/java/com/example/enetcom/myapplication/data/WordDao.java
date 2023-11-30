package com.example.enetcom.myapplication.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.enetcom.myapplication.model.word;

import java.util.List;

@Dao
public interface WordDao {

    // Permettre l'insertion du même mot plusieurs fois en passant
    // une stratégie de résolution de conflits.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(word word);

    @Query("DELETE FROM word_table")
    void deleteAll();

    @Query("SELECT * FROM word_table ORDER BY word ASC")
    LiveData<List<word>> getAlphabetizedWords();
    @Query("SELECT * from word_table LIMIT 1")
    Word[] getAnyWord();
}