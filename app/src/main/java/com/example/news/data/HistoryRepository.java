package com.example.news.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Update;

import java.util.List;

public class HistoryRepository {
    private HistoryDao historyDao;
    private LiveData<List<History>> allHistory;

    public HistoryRepository(Application application)  {
        HistoryDatabase database = HistoryDatabase.getInstance(application);
        historyDao = database.historyDao();
        allHistory = historyDao.getAllHistory();
    }

    public void insert (History history) {
        new InsertHistoryAysncTest(historyDao).execute(history);

    }
    public void update (History history) {
        new UpdateHistoryAysncTest(historyDao).execute(history);

    }
    public void delete (History history) {
        new DeleteHistoryAysncTest(historyDao).execute(history);
    }
    public void deleteAllNotes() {
        new DeleteAllHistoryAysncTest(historyDao).execute();
    }

    public LiveData<List<History>> getAllHistory() {
        return allHistory;
    }

    private static class InsertHistoryAysncTest extends AsyncTask<History, Void, Void> {
        private HistoryDao historyDao;
        private InsertHistoryAysncTest(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }
        @Override
        protected Void doInBackground(History... histories) {
            historyDao.insert((histories[0]));
            return null;
        }
    }

    private static class UpdateHistoryAysncTest extends AsyncTask<History, Void, Void> {
        private HistoryDao historyDao;
        private UpdateHistoryAysncTest(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }
        @Override
        protected Void doInBackground(History... histories) {
            historyDao.update(histories[0]);
            return null;
        }
    }
    private static class DeleteHistoryAysncTest extends AsyncTask<History, Void, Void> {
        private HistoryDao historyDao;
        private DeleteHistoryAysncTest(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }
        @Override
        protected Void doInBackground(History... histories) {
            historyDao.delete(histories[0]);
            return null;
        }
    }
    private static class DeleteAllHistoryAysncTest extends AsyncTask<Void, Void, Void> {
        private HistoryDao historyDao;
        private DeleteAllHistoryAysncTest(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            historyDao.deleteAllNotes();
            return null;
        }
    }





}
