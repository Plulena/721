/*
 * Copyright (C) 2007 The Android Open Source Project
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

package com.pitapat.a721;

import com.pitapat.a721.a721.TaskListColumns;
import com.pitapat.a721.a721.SettingsColumns;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.LiveFolders;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * Provides access to a database of notes. Each note has a title, the note
 * itself, a creation date and a modified data.
 */
public class a721Provider extends ContentProvider {

    private static final String TAG = "a721Provider";

    private static final String DATABASE_NAME = "a721.db";
    private static final int DATABASE_VERSION = 2;
    private static final String A721_TABLE_NAME = "tasklist";
    private static final String A721_SETTINGS_TABLE_NAME = "settings";

    private static HashMap<String, String> sa721ProjectionMap;
    private static HashMap<String, String> sLiveFolderProjectionMap;

    private static final int TASKS = 1;
    private static final int TASK_ID = 2;
    private static final int LIVE_FOLDER_TASKS = 3;

    private static final UriMatcher sUriMatcher;

    /**
     * This class helps open, create, and upgrade the database file.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + A721_TABLE_NAME + " ("
                    + TaskListColumns._ID + " INTEGER PRIMARY KEY,"
                    + TaskListColumns.TASK + " TEXT,"
                    + TaskListColumns.TIME + " INTEGER,"
                    + TaskListColumns.CREATED_DATE + " INTEGER,"
                    + TaskListColumns.MODIFIED_DATE + " INTEGER"
                    + TaskListColumns.COMPLETE_FLAG + " INTEGER"
                    + TaskListColumns.REPORT_TYPE + " INTEGER"
                    + TaskListColumns.REPORT_FLAG + " INTEGER"
                    + TaskListColumns.REPORT_INTERVAL + " INTEGER"
                    + TaskListColumns.E_REPORT + " INTEGER"
                    + TaskListColumns.E_REPORT_INTERVAL + " INTEGER"
                    + ");");
            
            db.execSQL("CREATE TABLE " + A721_SETTINGS_TABLE_NAME + " ("
                    + SettingsColumns._ID + " INTEGER PRIMARY KEY,"
                    + SettingsColumns.SHARE_FLAG + " TEXT,"
                    + SettingsColumns.SNS_ID + " TEXT,"
                    + SettingsColumns.SNS_PW + " TEXT,"
                    + SettingsColumns.TREE_FLAG + " INTEGER"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    private DatabaseHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(A721_TABLE_NAME);
        
        switch (sUriMatcher.match(uri)) {
        case TASKS:
            qb.setProjectionMap(sa721ProjectionMap);
            break;

        case TASK_ID:
            qb.setProjectionMap(sa721ProjectionMap);
            qb.appendWhere(TaskListColumns._ID + "=" + uri.getPathSegments().get(1));
            break;

        case LIVE_FOLDER_TASKS:
            qb.setProjectionMap(sLiveFolderProjectionMap);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // If no sort order is specified use the default
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = TaskListColumns.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        // Get the database and run the query
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
        
        // Tell the cursor what uri to watch, so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
        
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
        case TASKS:
        case LIVE_FOLDER_TASKS:
            return TaskListColumns.CONTENT_TYPE;

        case TASK_ID:
            return TaskListColumns.CONTENT_ITEM_TYPE;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        // Validate the requested uri
        if (sUriMatcher.match(uri) != TASKS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        Long now = Long.valueOf(System.currentTimeMillis());

        // Make sure that the fields are all set
        if (values.containsKey(TaskListColumns.CREATED_DATE) == false) {
            values.put(TaskListColumns.CREATED_DATE, now);
        }

        if (values.containsKey(TaskListColumns.MODIFIED_DATE) == false) {
            values.put(TaskListColumns.MODIFIED_DATE, now);
        }

        if (values.containsKey(TaskListColumns.TASK) == false) {
            Resources r = Resources.getSystem();
            values.put(TaskListColumns.TASK, r.getString(android.R.string.untitled));
        }

        values.put(TaskListColumns.TIME, "259200");
        values.put(TaskListColumns.REPORT_TYPE, "0");
        values.put(TaskListColumns.REPORT_FLAG, "1");
        values.put(TaskListColumns.REPORT_INTERVAL, "1");
        values.put(TaskListColumns.E_REPORT, "0");
        values.put(TaskListColumns.E_REPORT_INTERVAL, "0");

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(A721_TABLE_NAME, TaskListColumns.TASK, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(TaskListColumns.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case TASKS:
            count = db.delete(A721_TABLE_NAME, where, whereArgs);
            break;

        case TASK_ID:
            String noteId = uri.getPathSegments().get(1);
            count = db.delete(A721_TABLE_NAME, TaskListColumns._ID + "=" + noteId
                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case TASKS:
            count = db.update(A721_TABLE_NAME, values, where, whereArgs);
            break;

        case TASK_ID:
            String noteId = uri.getPathSegments().get(1);
            count = db.update(A721_TABLE_NAME, values, TaskListColumns._ID + "=" + noteId
                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(a721.AUTHORITY, "tasks", TASKS);
        sUriMatcher.addURI(a721.AUTHORITY, "tasks/#", TASK_ID);
        sUriMatcher.addURI(a721.AUTHORITY, "live_folders/tasks", LIVE_FOLDER_TASKS);

        sa721ProjectionMap = new HashMap<String, String>();
        sa721ProjectionMap.put(TaskListColumns._ID, TaskListColumns._ID);
        sa721ProjectionMap.put(TaskListColumns.TASK, TaskListColumns.TASK);
        sa721ProjectionMap.put(TaskListColumns.TIME, TaskListColumns.TIME);
        sa721ProjectionMap.put(TaskListColumns.CREATED_DATE, TaskListColumns.CREATED_DATE);
        sa721ProjectionMap.put(TaskListColumns.MODIFIED_DATE, TaskListColumns.MODIFIED_DATE);
        sa721ProjectionMap.put(TaskListColumns.COMPLETE_FLAG, TaskListColumns.COMPLETE_FLAG);
        sa721ProjectionMap.put(TaskListColumns.REPORT_TYPE, TaskListColumns.REPORT_TYPE);
        sa721ProjectionMap.put(TaskListColumns.REPORT_FLAG, TaskListColumns.REPORT_FLAG);
        sa721ProjectionMap.put(TaskListColumns.REPORT_INTERVAL, TaskListColumns.REPORT_INTERVAL);
        sa721ProjectionMap.put(TaskListColumns.E_REPORT, TaskListColumns.E_REPORT);
        sa721ProjectionMap.put(TaskListColumns.E_REPORT_INTERVAL, TaskListColumns.E_REPORT_INTERVAL);

        // Support for Live Folders.
        sLiveFolderProjectionMap = new HashMap<String, String>();
        sLiveFolderProjectionMap.put(LiveFolders._ID, TaskListColumns._ID + " AS " +
                LiveFolders._ID);
        sLiveFolderProjectionMap.put(LiveFolders.NAME, TaskListColumns.TASK + " AS " +
                LiveFolders.NAME);
        // Add more columns here for more robust Live Folders.
    }
}
