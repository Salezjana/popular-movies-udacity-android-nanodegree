package mrodkiewicz.pl.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by pc-mikolaj on 25.02.2018.
 */

public class TMDBContract {
    private TMDBContract() {}


    public static final class FavouritesEntry implements BaseColumns {
        public final static String TABLE_NAME = "favouties";
        public final static String _ID = BaseColumns._ID;
        public final static String isFAVOURITE = "isFavourite";
        public static final int isFacourite_false = 0;
        public static final int isFacourite_true = 1;
    }
}
