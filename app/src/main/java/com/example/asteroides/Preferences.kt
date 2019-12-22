package com.example.asteroides

import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment


class Preferences : PreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }
}