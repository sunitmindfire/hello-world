package com.nikhil.spycam.database;

import android.provider.BaseColumns;

/**
 * Copyright MDLive.  All rights reserved.
 */

public final class SpyCamDBContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private SpyCamDBContract(){}

    public static class AlarmEntry implements BaseColumns{
        public static final String TABLE_NAME = "spy_alarm";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_DURATION = "duration";
        public static final String COLUMN_NAME_UNIQUE_ID = "unique_id";
    }

}
