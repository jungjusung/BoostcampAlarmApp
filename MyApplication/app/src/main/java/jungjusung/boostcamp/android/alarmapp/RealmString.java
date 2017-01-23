package jungjusung.boostcamp.android.alarmapp;

import io.realm.RealmObject;

/**
 * Created by Jusung on 2017. 1. 23..
 */

public class RealmString extends RealmObject {
    private String val;

    public String getValue() {
        return val;
    }
    public void setValue(String value) {
        this.val = value;
    }
}
