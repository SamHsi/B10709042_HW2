package com.example.b10709042_hw2;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private ListPreference listPreference;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        listPreference = (ListPreference) findPreference(getString(R.string.display));
        pref.registerOnSharedPreferenceChangeListener(this);
        listPreference.setSummary(listPreference.getEntry());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        listPreference.setSummary(listPreference.getEntry());
    }
}
