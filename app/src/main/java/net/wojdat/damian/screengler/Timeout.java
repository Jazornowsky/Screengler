package net.wojdat.damian.screengler;

import androidx.annotation.DrawableRes;

public enum Timeout {

    UNKNOWN(-2, R.drawable.ic_stat_time_none, false),
    NEVER(-1, R.drawable.ic_stat_time_inf, false),
    FIFTEEN_SEC(15_000, R.drawable.ic_stat_time_15s, true),
    THIRTY_SEC(30_000, R.drawable.ic_stat_time_30s, true),
    ONE_MIN(60_000, R.drawable.ic_stat_time_1, true),
    TWO_MIN(120_000, R.drawable.ic_stat_time_2, true),
    FIVE_MIN(300_000, R.drawable.ic_stat_time_5, true),
    TEN_MIN(600_000, R.drawable.ic_stat_time_10, true),
    FIFTEEN_MIN(900_000, R.drawable.ic_stat_time_15, true),
    THIRTEEN_MIN(1_800_000, R.drawable.ic_stat_time_30, true);

    Timeout(int timeMs,
            @DrawableRes int resId,
            boolean showInSettings) {
        this.timeMs = timeMs;
        this.resId = resId;
        this.showInSettings = showInSettings;
    }

    private int timeMs;

    @DrawableRes
    private int resId;

    private boolean showInSettings;

    public int getTimeMs() {
        return timeMs;
    }

    @DrawableRes
    public int getResId() {
        return resId;
    }

    public boolean isShowInSettings() {
        return showInSettings;
    }

    public String getTitle() {
        if (timeMs / 60_000 > 0) {
            return timeMs / 60_000 + " min.";
        } else {
            return timeMs / 1_000 + " sec.";
        }
    }
}
