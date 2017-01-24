package jungjusung.boostcamp.android.alarmapp;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Jusung on 2017. 1. 23..
 */

public class RealmInit extends Application{
    private int REALM_INDEX=0;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

    public int getREALM_INDEX() {
        return REALM_INDEX;
    }

    public void setREALM_INDEX(int REALM_INDEX) {
        this.REALM_INDEX = REALM_INDEX;
    }
}
