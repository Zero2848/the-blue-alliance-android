package com.thebluealliance.androidclient.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.thebluealliance.androidclient.models.BasicModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for typed storage of models in the database
 * @param <T> Type of the corresponding model, must extend {@link BasicModel}
 */
public abstract class ModelTable<T extends BasicModel> {

    private SQLiteDatabase mDb;

    public ModelTable(SQLiteDatabase db){
        mDb = db;
    }

    /**
     * Adds a model to the database, if it doesn't already exist
     * If the model is already entered, update the existing row via {@link #update(BasicModel)}
     * If the insert was successful, call {@link #insertCallback(BasicModel)}
     * @param in Model to be added
     * @return The value from {@link SQLiteDatabase#insert(String, String, ContentValues)} if a new
     * row is inserted (row ID or -1 on error), or the value from
     * {@link SQLiteDatabase#update(String, ContentValues, String, String[])} if an existing row
     * was updated (number of affected rows)
     */
    public final long add(T in) {
        mDb.beginTransaction();
        long ret = -1;
        try {
            if (!exists(in.getKey())) {
                ret = mDb.insert(getTableName(), null, in.getParams());
                if (ret != -1) {
                    insertCallback(in);
                }
            } else {
                ret = update(in);
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
        return ret;
    }

    /**
     * Adds a List of items to the database via {@link #add(BasicModel)}
     * @param inList List of models to be added
     */
    public final void add(List<T> inList){
        mDb.beginTransaction();
        for (T in: inList) {
            add(in);
        }
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }

    /**
     * Updates an existing row in the database.
     * Uses {@link #getKeyColumn()} = {@link BasicModel#getKey()} as the WHERE clause
     * If the update was successful, call {@link #updateCallback(BasicModel)}
     * @param in Model to be updated
     * @return Value from {@link SQLiteDatabase#update(String, ContentValues, String, String[])},
     * or number of rows affected by the query
     */
    public final int update(T in){
        int affectedRows;
        mDb.beginTransaction();
        try {
            affectedRows = mDb.update(
                    getTableName(),
                    in.getParams(),
                    getKeyColumn() + "=?",
                    new String[]{in.getKey()});
            if (affectedRows > 0) {
                updateCallback(in);
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
        return affectedRows;
    }

    /**
     * Fetches a model with all fields from the database by key
     * Wrapper for {@link #get(String, String[])}, with null as the second parameter
     * @param key Key to match {@link #getKeyColumn()} on
     * @return An inflated model found with the provided key
     */
    public final T get(String key) {
        return get(key, null);
    }

    /**
     * Fetch a model with the given fields from the database by key
     * @param key Key to match {@link #getKeyColumn()} on
     * @param fields String array of database column names to use as a projection
     * @return Inflated model from the first row returned in the query
     */
    public final T get(String key, String[] fields){
        Cursor cursor = mDb.query(
                getTableName(),
                fields,
                getKeyColumn() + " = ?",
                new String[]{key},
                null,
                null,
                null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            T model = inflate(cursor);
            cursor.close();
            return model;
        } else {
            return null;
        }
    }

    /**
     * Query the table. Wrapper for
     * {@link SQLiteDatabase#query(String, String[], String, String[], String, String, String, String)}
     * @param columns Column projection to fetch
     * @param selection WHERE clause (use ? for variables)
     * @param selectionArgs Replacement args for the WHERE clause
     * @param groupBy GROUP BY clause
     * @param having HAVING clause
     * @param orderBy ORDER BY clause
     * @param limit LIMIT clause
     * @return Cursor with the rows returned by the query
     */
    public final Cursor query (
            String[] columns,
            String selection,
            String[] selectionArgs,
            String groupBy,
            String having,
            String orderBy,
            String limit){
        return mDb.query(getTableName(), columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * Fetches all rows from the table
     * Equivalent to SELECT * FROM {@link #getTableName()}
     * @return List of all inflated models in the table
     */
    public final List<T> getAll(){
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + getTableName(), new String[]{});
        List<T> ret = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                T model = inflate(cursor);
                ret.add(model);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ret;
    }

    /**
     * Check if a model with the given key exists
     * @param key Key to match {@link #getKeyColumn()} on
     * @return true if a row with the given key exists
     */
    public final boolean exists(String key){
        Cursor cursor = mDb.query(
                getTableName(),
                new String[]{getKeyColumn()},
                getKeyColumn() + "=?",
                new String[]{key},
                null,
                null,
                null,
                null);
        boolean result;
        if (cursor != null) {
            result = cursor.moveToFirst();
            cursor.close();
        } else {
            result = false;
        }
        return result;
    }

    /**
     * Delete a given model from the database
     * Same as running DELETE FROM {@link #getTableName()} WHERE {@link #getKeyColumn()} = key
     * If the deletion was successful, call {@link #deleteCallback(BasicModel)}
     * @param in Model to delete. Uses {@link BasicModel#getKey()} for the key
     * @return Return value from {@link SQLiteDatabase#delete(String, String, String[])}
     * (number of rows affected)
     */
    public final int delete(T in) {
        int ret;
        mDb.beginTransaction();
        try {
            ret = mDb.delete(getTableName(), getKeyColumn() + " = ?", new String[]{in.getKey()});
            if (ret > 0) {
                deleteCallback(in);
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
        return ret;
    }

    /**
     * Delete from the table with a provided WHERE clause
     * DOES NOT CALL {@link #deleteCallback(BasicModel)} - can't make the types work :(
     * @param whereClause SQL WHERE clause to use in deletion
     * @param whereArgs Substitution rguments for the clause
     * @return Value from {@link SQLiteDatabase#delete(String, String, String[])}
     * (number of rows affected)
     */
    public final int delete(String whereClause, String[] whereArgs) {
        return mDb.delete(getTableName(), whereClause, whereArgs);
    }

    /**
     * Called after a successful row insert
     * Override to let concrete implementations do something with an inserted row
     * e.g. add Search Indexes
     * @param model Model that was inserted
     */
    protected void insertCallback(T model){
        // default to no op
    }

    /**
     * Called after a successful row update
     * Override to let concrete implementations do something after a row update
     * @param model Model that was updated
     */
    protected void updateCallback(T model){
        // default to no op
    }

    /**
     * Called after a successful row delete
     * Override to let concrete implementations do something after a row delete
     * @param model Model that wasa deleted
     */
    protected void deleteCallback(T model){
        // default to no op
    }

    /**
     * @return a String containing the same of the backing SQL table
     */
    protected abstract String getTableName();

    /**
     * @return a String containing the column name of the primary key in the backing table
     */
    protected abstract String getKeyColumn();

    /**
     * Inflates a cursor row from the db to a model class
     * Probably uses a method in {@link ModelInflater}
     * @param cursor Cursor moved to a row in the database
     * @return An inflated model from the input cursor
     */
    public abstract T inflate(Cursor cursor);
}