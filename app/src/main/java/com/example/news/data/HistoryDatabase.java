package com.example.news.data;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.loopj.android.http.LogInterface;

@Database(entities = {History.class}, version = 1, exportSchema = false)
public abstract class HistoryDatabase extends RoomDatabase {
    private static HistoryDatabase instance;
    public abstract HistoryDao historyDao();

    //Singleton
    public static synchronized HistoryDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    HistoryDatabase.class, "history_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private HistoryDao historyDao;
        private PopulateDbAsyncTask(HistoryDatabase db) {
            historyDao = db.historyDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            historyDao.insert(new History("1","Category","Test Database Entry Ignore","2020/09/06","1234567890","1.0","En","Seg_Text","Source", "Time","Title","Type","Status"));
            historyDao.insert(new History("2","Category","Test Database Entry Ignore","2020/09/07","1234567890","1.0","En","Seg_Text","Source", "Time","Title","Type","Status"));
            historyDao.insert(new History("3","Category","Test Database Entry Ignore","2020/09/08","1234567890","1.0","En","Seg_Text","Source", "Time","Title","Type","Status"));
            return null;
        }
    }
}
