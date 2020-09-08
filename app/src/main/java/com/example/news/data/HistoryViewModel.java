package com.example.news.data;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private HistoryRepository repository;
    private LiveData<List<History>> allHistory;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        repository = new HistoryRepository(application);
        allHistory = repository.getAllHistory();
    }

    public void insert(History history) {
        repository.insert(history);
    }
    public void update(History history) {
        repository.update(history);
    }
    public void delete(History history){
        repository.delete(history);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<History>> getAllHistory() {
        return allHistory;
    }

}
