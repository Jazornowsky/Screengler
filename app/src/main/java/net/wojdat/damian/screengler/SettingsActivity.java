package net.wojdat.damian.screengler;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsActivity extends AppCompatActivity {
    public static final String TIMEOUT_PREFERENCE_PREFIX = "timeout_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            Context context = getPreferenceManager().getContext();
            PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

            PreferenceCategory timeoutCategory = new PreferenceCategory(context);
            timeoutCategory.setKey("timeout_category");
            timeoutCategory.setTitle(R.string.settings_timeout_category_title);
            screen.addPreference(timeoutCategory);

            for (Timeout timeout : Timeout.values()) {
                if (!timeout.isShowInSettings()) {
                    continue;
                }
                SwitchPreferenceCompat timeoutSwitch = new SwitchPreferenceCompat(context);
                timeoutSwitch.setKey(TIMEOUT_PREFERENCE_PREFIX + timeout.name().toLowerCase());
                timeoutSwitch.setTitle(timeout.getTitle());
                timeoutSwitch.setDefaultValue(Boolean.TRUE);
                timeoutCategory.addPreference(timeoutSwitch);
            }

            setPreferenceScreen(screen);
        }
    }
}