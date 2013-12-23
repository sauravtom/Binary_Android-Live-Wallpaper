package com.sauravtom.app;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by saurav on 22/12/13.
 */
public class BGPrefs extends PreferenceActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.xml.preference);
        addPreferencesFromResource(R.xml.preference);

    }
}
