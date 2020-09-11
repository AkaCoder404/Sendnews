package com.example.news.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.news.R;
import com.example.news.data.HistoryViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.PreferenceChangeEvent;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private String TAG = "SettingsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mtoolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(mtoolbar);
        // getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    public static class NewsPreferenceFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener, DatePickerDialog.OnDateSetListener {

        @Override
        public void onCreate(Bundle savedInstances) {
            super.onCreate(savedInstances);
            addPreferencesFromResource(R.xml.preferences_settings);

            Preference lightMode = findPreference("LIGHT");
            setOnPreferenceClick(lightMode);

            Preference category = findPreference("CATEGORYFILTER");
            setOnPreferenceClick(category);


            Preference fromDate = findPreference("DATE");
            setOnPreferenceClick(fromDate);
            bindPreferenceSummaryToValue(fromDate);


            Preference clearHistory = findPreference(getString(R.string.settings_history_key));
            setOnPreferenceClick(clearHistory);

            Preference clearRecommend = findPreference(getString(R.string.settings_recommend_key));
            setOnPreferenceClick(clearRecommend);

        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);

            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }

        private void setOnPreferenceClick(Preference preference) {
            preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String key = preference.getKey();
                    Log.d("Preference Clicked", key);
                    if (key.equalsIgnoreCase(getString(R.string.settings_date_key))) {
                        showDatePicker();
                    }
                    else if (key.equalsIgnoreCase(getString(R.string.settings_history_key))) {
                        HistoryViewModel historyViewModel =
                                ViewModelProviders.of((FragmentActivity) getActivity()).get(HistoryViewModel.class);
                        historyViewModel.deleteAllNotes();
                        Toast.makeText(getActivity(), "Clear History", Toast.LENGTH_SHORT).show();
                    }
                    else if (key.equalsIgnoreCase("LIGHT")) {
                        Toast.makeText(getActivity(), "Switch Mode", Toast.LENGTH_SHORT).show();
                    }
                    else if (key.equalsIgnoreCase("CATEGORYFILTER")){
                        preference.setTitle("Switch");
                        Log.d("SettingsActivity", key + "has been selected");
                    }
                    return false;
                }
            });
        }

        private void showDatePicker() {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(getActivity(),
                    this, year, month, dayOfMonth).show();
        }
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            month = month + 1;
            String selectedDate = year + "-" + month + "-" + dayOfMonth;
            String formattedDate = formatDate(selectedDate);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("date", formattedDate).apply();

            Preference fromDatePreference = findPreference("DATE");
            bindPreferenceSummaryToValue(fromDatePreference);
        }

        private String formatDate(String selectedDate) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
            Date dateObject = null;
            try {
                dateObject = simpleDateFormat.parse(selectedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd");
            return df.format(dateObject);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            return true;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}


