package jungjusung.boostcamp.android.alarmapp;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jusung on 2017. 1. 23..
 */

public class Alarm extends RealmObject {

    @PrimaryKey
    private int alarm_id;
    private boolean isDoing;
    private String alarm_time;
    private RealmList<RealmString> iterList;
    private String alarm_memo;
    private String alarm_sound;
    private String alarm_optional;
    private boolean alarm_replay;


    public int getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(int alarm_id) {
        this.alarm_id = alarm_id;
    }

    public boolean isDoing() {
        return isDoing;
    }

    public void setDoing(boolean doing) {
        isDoing = doing;
    }

    public String getAlarm_time() {
        return alarm_time;
    }

    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }

    public RealmList<RealmString> getIterList() {
        return iterList;
    }

    public void setIterList(RealmList<RealmString> iterList) {
        this.iterList = iterList;
    }

    public String getAlarm_memo() {
        return alarm_memo;
    }

    public void setAlarm_memo(String alarm_memo) {
        this.alarm_memo = alarm_memo;
    }

    public String getAlarm_sound() {
        return alarm_sound;
    }

    public void setAlarm_sound(String alarm_sound) {
        this.alarm_sound = alarm_sound;
    }

    public String getAlarm_optional() {
        return alarm_optional;
    }

    public void setAlarm_optional(String alarm_optional) {
        this.alarm_optional = alarm_optional;
    }

    public boolean isAlarm_replay() {
        return alarm_replay;
    }

    public void setAlarm_replay(boolean alarm_replay) {
        this.alarm_replay = alarm_replay;
    }
}

