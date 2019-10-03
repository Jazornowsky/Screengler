package net.wojdat.damian.screengler;

import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.provider.Settings;
import android.service.quicksettings.TileService;
import android.util.AndroidRuntimeException;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.preference.PreferenceManager;

public class ScreenglerTileService extends TileService {

    private static final int MIN_INTERVAL = 60_000;
    private static final int MAX_TIMEOUT = Timeout.THIRTEEN_MIN.getTimeMs() + 1;

    @Override
    public void onStartListening() {
        super.onStartListening();
        try {
            refreshIcon(getCurrentTimeout());
        } catch (Settings.SettingNotFoundException e) {
            throw new AndroidRuntimeException(e);
        }
    }

    @Override
    public void onClick() {
        super.onClick();
        try {
            Timeout newTimeout = getNextTimeout();
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT,
                    newTimeout.getTimeMs());
            refreshIcon(newTimeout);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "New screen timeout: " + newTimeout.getTimeMs() / 60_000 + " min.",
                    Toast.LENGTH_LONG);
            toast.show();
        } catch (Settings.SettingNotFoundException e) {
            throw new AndroidRuntimeException(e);
        }
    }

    private List<Timeout> getAvailableTimeouts() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        List<Timeout> availableTimeout = new ArrayList<>();
        for (Timeout timeout : Timeout.values()) {
            boolean timeoutActive = sharedPreferences.getBoolean(
                    SettingsActivity.TIMEOUT_PREFERENCE_PREFIX + timeout.name().toLowerCase(),
                    Boolean.FALSE);
            if (timeoutActive) {
                availableTimeout.add(timeout);
            }
        }
        return availableTimeout;
    }

    private List<Timeout> getAllTimeouts() {
        return new ArrayList<>(Arrays.asList(Timeout.values()));
    }

    private Timeout getNextTimeout() throws Settings.SettingNotFoundException {
        Timeout currentTimeout = getCurrentTimeout();
        int currentAllTimeoutIdx = getAllTimeouts().indexOf(currentTimeout);
        for (int i = currentAllTimeoutIdx; i < getAllTimeouts().size(); i++) {
            if (i+1 >= getAllTimeouts().size()) {
                return getAvailableTimeouts().get(0);
            }
            int nextAllTimeoutIdx = i+1;
            if (getAvailableTimeouts().contains(getAllTimeouts().get(nextAllTimeoutIdx))) {
                return getAllTimeouts().get(nextAllTimeoutIdx);
            }
        }
        return getAvailableTimeouts().get(0);
    }

    private Timeout getCurrentTimeout() throws Settings.SettingNotFoundException {
        int currentTimeoutValue =
                Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
        Timeout currentTimeout = null;
        if (currentTimeoutValue >= MAX_TIMEOUT) {
            currentTimeoutValue = MIN_INTERVAL;
        }
        for (Timeout timeout : getAllTimeouts()) {
            if (currentTimeoutValue == timeout.getTimeMs()) {
                currentTimeout = timeout;
                break;
            }
        }
        if (currentTimeout == null) {
            currentTimeout = Timeout.UNKNOWN;
        }
        return currentTimeout;
    }

    private void refreshIcon(Timeout timeout) {
        getQsTile().setIcon(Icon.createWithResource(this, timeout.getResId()));
        getQsTile().updateTile();
    }
}
