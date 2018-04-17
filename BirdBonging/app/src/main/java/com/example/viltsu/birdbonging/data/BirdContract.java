package com.example.viltsu.birdbonging.data;

import android.provider.BaseColumns;

/**
 * Created by ville-pekkapalmgren on 25/01/18.
 */

public final class BirdContract {

    private BirdContract(){}

    public static class BirdEntry implements BaseColumns {

        public static final String TABLE_NAME="birds";

        public static final String _ID=BaseColumns._ID;
        public static final String COLUMN_BIRD_BREED="laji";
        public static final String COLUMN_BIRD_POINTS="pisteet";
        public static final String COLUMN_BIRD_PIC="kuva";
        public static final String COLUMN_BIRD_MESSAGE="viesti";
    }
}
