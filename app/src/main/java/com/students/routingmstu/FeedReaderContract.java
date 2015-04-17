package com.students.routingmstu;

import android.provider.BaseColumns;

/**
 * Created by mosin on 17.04.2015.
 */
public class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class PointEntry implements BaseColumns {
        public static final String TABLE_NAME = "Points";
        public static final String COLUMN_NAME_ENTRY_ID = "Id";
        public static final String COLUMN_NAME_SHORTNAME = "ShortName";
        public static final String COLUMN_NAME_FULLNAME = "FullName";

    }


    /* Inner class that defines the table contents */
    public static abstract class LengthEntry implements BaseColumns {
        public static final String TABLE_NAME = "Lengthes";
        public static final String COLUMN_NAME_ENTRY_ID = "Id";
        public static final String COLUMN_NAME_STARTPOINTID = "StartPointId";
        public static final String COLUMN_NAME_ENDPOINTID = "EndPointId";
        public static final String COLUMN_NAME_LENGTH = "Length";
    }
}



