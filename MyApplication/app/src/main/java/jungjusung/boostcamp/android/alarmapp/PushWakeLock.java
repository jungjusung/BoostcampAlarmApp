package jungjusung.boostcamp.android.alarmapp;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by Jusung on 2017. 1. 26..
 */

public class PushWakeLock {
    private static PowerManager.WakeLock sCpuWakeLock;
    private static KeyguardManager.KeyguardLock mKeyguardLock;
    private static boolean isScreenLock;

    static void acquireCpuWakeLock(Context context) {
        if (sCpuWakeLock != null) {
            return;
        }
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        sCpuWakeLock = pm.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "hello");

        sCpuWakeLock.acquire();
    }

    static void releaseCpuLock() {
        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }
    }
}