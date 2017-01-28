package jungjusung.boostcamp.android.alarmapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Jusung on 2017. 1. 28..
 */

public class AlarmProvider extends ContentProvider {

    static final Uri CONTENT_URI = Uri.parse("content://jungjusung.boostcamp.android.alarmapp/data");
    Realm realm;
    static final String[] sColumns = new String[]{"alarm_id", "alarm_hour", "alarm_minute","alarm_memo","alarm_sound_name"};
    RealmQuery<Alarm> query;
    RealmResults<Alarm> results;
    MatrixCursor cursor;


    static UriMatcher Matcher;

    static {
        Matcher = new UriMatcher(UriMatcher.NO_MATCH);
    }
    @Override
    public boolean onCreate() {
        return true;
    }
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        realm = Realm.getDefaultInstance();
        query = realm.where(Alarm.class);
        results = query.findAll();
        cursor = new MatrixCursor(sColumns);
        for (Alarm alarm : results) {
            Object[] rowData = new Object[]{alarm.getAlarm_id(),alarm.getAlarm_hour(),alarm.getAlarm_minute(),alarm.getAlarm_memo(),alarm.getAlarm_sound_name()};
            cursor.addRow(rowData);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
