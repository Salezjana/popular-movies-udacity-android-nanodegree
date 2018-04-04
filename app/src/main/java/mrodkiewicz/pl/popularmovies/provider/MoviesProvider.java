package mrodkiewicz.pl.popularmovies.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import mrodkiewicz.pl.popularmovies.db.FavouritesMoviesDatebaseHelper;
import timber.log.Timber;

import static mrodkiewicz.pl.popularmovies.helpers.Config.CONTENT_AUTHORITY;
import static mrodkiewicz.pl.popularmovies.helpers.Config.CONTENT_DIR_TYPE;
import static mrodkiewicz.pl.popularmovies.helpers.Config.CONTENT_ITEM_TYPE;
import static mrodkiewicz.pl.popularmovies.helpers.Config.KEY_MOVIE_ID;
import static mrodkiewicz.pl.popularmovies.helpers.Config.TABLE_MOVIE;
import static mrodkiewicz.pl.popularmovies.helpers.Config.buildFlavorsUri;

public class MoviesProvider extends ContentProvider{
    private FavouritesMoviesDatebaseHelper favouritesMoviesDatebaseHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int FLAVOR = 100;
    private static final int FLAVOR_WITH_ID = 200;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        matcher.addURI(authority, TABLE_MOVIE, FLAVOR);
        matcher.addURI(authority, TABLE_MOVIE + "/#", FLAVOR_WITH_ID);

        return matcher;
    }



    @Override
    public boolean onCreate() {
        favouritesMoviesDatebaseHelper = new FavouritesMoviesDatebaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        switch(sUriMatcher.match(uri)){
            // All Flavors selected
            case FLAVOR:{
                retCursor = favouritesMoviesDatebaseHelper.getReadableDatabase().query(
                        TABLE_MOVIE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            // Individual flavor based on Id selected
            case FLAVOR_WITH_ID:{
                retCursor = favouritesMoviesDatebaseHelper.getReadableDatabase().query(
                        TABLE_MOVIE,
                        projection,
                        KEY_MOVIE_ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            default:{
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case FLAVOR:{
                return CONTENT_DIR_TYPE;
            }
            case FLAVOR_WITH_ID:{
                return CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = favouritesMoviesDatebaseHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case FLAVOR: {
                long _id = db.insert(TABLE_MOVIE, null, values);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = buildFlavorsUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = favouritesMoviesDatebaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;
        switch(match){
            case FLAVOR:
                numDeleted = db.delete(
                        TABLE_MOVIE, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        TABLE_MOVIE + "'");
                break;
            case FLAVOR_WITH_ID:
                numDeleted = db.delete(TABLE_MOVIE,
                        KEY_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        TABLE_MOVIE + "'");

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numDeleted;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values){
        final SQLiteDatabase db = favouritesMoviesDatebaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch(match){
            case FLAVOR:
                // allows for multiple transactions
                db.beginTransaction();

                // keep track of successful inserts
                int numInserted = 0;
                try{
                    for(ContentValues value : values){
                        if (value == null){
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;
                        try{
                            _id = db.insertOrThrow(TABLE_MOVIE,
                                    null, value);
                        }catch(SQLiteConstraintException e) {
                            Timber.d("Attempting to insert  but value is already in database.");
                    }
                        if (_id != -1){
                            numInserted++;
                        }
                    }
                    if(numInserted > 0){
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }
                } finally {
                    // all transactions occur at once
                    db.endTransaction();
                }
                if (numInserted > 0){
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
        final SQLiteDatabase db = favouritesMoviesDatebaseHelper.getWritableDatabase();
        int numUpdated = 0;

        if (contentValues == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch(sUriMatcher.match(uri)){
            case FLAVOR:{
                numUpdated = db.update(TABLE_MOVIE,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case FLAVOR_WITH_ID: {
                numUpdated = db.update(TABLE_MOVIE,
                        contentValues,
                        KEY_MOVIE_ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }

}
