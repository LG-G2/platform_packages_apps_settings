/*
 * Copyright (C) 2013 BeerGang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package com.android.settings.beergang;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.net.ConnectivityManager;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;


public class PowerMenuConfig extends SettingsPreferenceFragment implements
OnPreferenceChangeListener {
    
    private static final String POWER_MENU_CATEGORY = "category_power_menu";
    private static final String POWER_MENU_MOBILE_DATA = "power_menu_mobile_data";
    private static final String POWER_MENU_SCREENSHOT = "power_menu_screenshot";
    private static final String POWER_MENU_SCREENRECORD = "power_menu_screenrecord";
    private static final String POWER_MENU_AIRPLANE_MODE = "power_menu_airplane_mode";
    private static final String POWER_MENU_SOUND_TOGGLES = "power_menu_sound_toggles";
    
    private CheckBoxPreference mMobileDataPowerMenu;
    private CheckBoxPreference mScreenshotPowerMenu;
    private CheckBoxPreference mScreenrecordPowerMenu;
    private CheckBoxPreference mAirplaneModePowerMenu;
    private CheckBoxPreference mSoundTogglesPowerMenu;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.power_menu_config);
        PreferenceScreen prefSet = getPreferenceScreen();
        
        mMobileDataPowerMenu = (CheckBoxPreference) prefSet.findPreference(POWER_MENU_MOBILE_DATA);
        Context context = getActivity();
        ConnectivityManager cm = (ConnectivityManager)
        context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.isNetworkSupported(ConnectivityManager.TYPE_MOBILE)) {
            mMobileDataPowerMenu.setChecked(Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                        Settings.System.MOBILE_DATA_IN_POWER_MENU, 0) == 1);
            mMobileDataPowerMenu.setOnPreferenceChangeListener(this);
        } else {
            prefSet.removePreference(mMobileDataPowerMenu);
        }

        mScreenshotPowerMenu = (CheckBoxPreference) prefSet.findPreference(POWER_MENU_SCREENSHOT);
        mScreenshotPowerMenu.setChecked(Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                Settings.System.SCREENSHOT_IN_POWER_MENU, 0) == 1);
        mScreenshotPowerMenu.setOnPreferenceChangeListener(this);
        
        mScreenrecordPowerMenu = (CheckBoxPreference) prefSet.findPreference(POWER_MENU_SCREENRECORD);
        if (!getResources().getBoolean(com.android.internal.R.bool.config_enableScreenrecordChord)) {
            PreferenceGroup powerMenuCategory = (PreferenceGroup)
            findPreference(POWER_MENU_CATEGORY);
            powerMenuCategory.removePreference(mScreenrecordPowerMenu);
        } else {
            prefSet.removePreference(mScreenrecordPowerMenu);
        }
        
        
        mAirplaneModePowerMenu = (CheckBoxPreference) prefSet.findPreference(POWER_MENU_AIRPLANE_MODE);
        mAirplaneModePowerMenu.setChecked(Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.AIRPLANE_MODE_IN_POWER_MENU, 1) == 1);
        mAirplaneModePowerMenu.setOnPreferenceChangeListener(this);
        
        mSoundTogglesPowerMenu = (CheckBoxPreference) prefSet.findPreference(POWER_MENU_SOUND_TOGGLES);
        mSoundTogglesPowerMenu.setChecked(Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.SOUND_TOGGLES_IN_POWER_MENU, 1) == 1);
        mSoundTogglesPowerMenu.setOnPreferenceChangeListener(this);
    }
    
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mScreenshotPowerMenu) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(), Settings.System.SCREENSHOT_IN_POWER_MENU, value ? 1 : 0);
            return true;
        } else if (preference == mScreenrecordPowerMenu) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(), Settings.System.SCREENRECORD_IN_POWER_MENU, value ? 1 : 0);
            return true;
        } else if (preference == mMobileDataPowerMenu) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(), Settings.System.MOBILE_DATA_IN_POWER_MENU, value ? 1 : 0);
            return true;
        } else if (preference == mAirplaneModePowerMenu) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(), Settings.System.AIRPLANE_MODE_IN_POWER_MENU, value ? 1 : 0);
            return true;
        } else if (preference == mSoundTogglesPowerMenu) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(), Settings.System.SOUND_TOGGLES_IN_POWER_MENU, value ? 1 : 0);
            return true;
        }
        return false;
    }
    
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return false;
    }
}