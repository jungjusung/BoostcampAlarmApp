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
    private boolean alarm_isDoing;
    private String alarm_hour;
    private String alarm_minute;
    private RealmList<RealmString> iterList;
    private String alarm_memo;
    private String alarm_sound_name;
    private String alarm_sound_uri;
    private String alarm_optional;
    private boolean alarm_replay;

    public int getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(int alarm_id) {
        this.alarm_id = alarm_id;
    }

    public boolean isAlarm_isDoing() {
        return alarm_isDoing;
    }

    public void setAlarm_isDoing(boolean alarm_isDoing) {
        this.alarm_isDoing = alarm_isDoing;
    }

    public String getAlarm_hour() {
        return alarm_hour;
    }

    public void setAlarm_hour(String alarm_hour) {
        this.alarm_hour = alarm_hour;
    }

    public String getAlarm_minute() {
        return alarm_minute;
    }

    public void setAlarm_minute(String alarm_minute) {
        this.alarm_minute = alarm_minute;
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

    public String getAlarm_sound_name() {
        return alarm_sound_name;
    }

    public void setAlarm_sound_name(String alarm_sound_name) {
        this.alarm_sound_name = alarm_sound_name;
    }

    public String getAlarm_sound_uri() {
        return alarm_sound_uri;
    }

    public void setAlarm_sound_uri(String alarm_sound_uri) {
        this.alarm_sound_uri = alarm_sound_uri;
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

