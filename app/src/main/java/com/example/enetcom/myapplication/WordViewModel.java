package com.example.enetcom.myapplication;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.enetcom.myapplication.data.WordRepository;
import com.example.enetcom.myapplication.model.word;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private final WordRepository mRepository;

    private final LiveData<List<word>> mAllWords;

    public WordViewModel (Application application) {
        super(application);
        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();
    }

    LiveData<List<word>> getAllWords() { return mAllWords; }

    public void insert(word word) { mRepository.insert(word); }
}