package com.pitapat.a721;

import android.net.Uri;
import android.provider.BaseColumns;

public final class a721 {

	    public static final String AUTHORITY = "com.pitapat.a721.provider.a721";

	    // This class cannot be instantiated
	    private a721() {}
	    
	    /**
	     * Notes table
	     */
	    public static final class TaskListColumns implements BaseColumns {
	        // This class cannot be instantiated
	        private TaskListColumns() {}

	        /**
	         * The content:// style URL for this table
	         */
	        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/tasks");

	        /**
	         * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
	         */
	        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.task";

	        /**
	         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single note.
	         */
	        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.task";

	        /**
	         * The default sort order for this table
	         */
	        public static final String DEFAULT_SORT_ORDER = "created DESC";

	        /**
	         * The title of the note
	         * <P>Type: TEXT</P>
	         */
	        public static final String TASK = "task";

	        /**
	         * The note itself
	         * <P>Type: INTEGER</P>
	         */
	        public static final String TIME = "time";

	        /**
	         * The timestamp for when the note was created
	         * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
	         */
	        public static final String CREATED_DATE = "created";

	        /**
	         * The timestamp for when the note was last modified
	         * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
	         */
	        public static final String MODIFIED_DATE = "modified";
	        
	        /**
	         * The note itself
	         * <P>Type: INTEGER</P>
	         */
	        public static final String COMPLETE_FLAG = "complete_flag";
	        
	        /**
	         * The note itself
	         * <P>Type: INTEGER</P>
	         */
	        public static final String REPORT_TYPE = "report_type";
	        
	        /**
	         * The note itself
	         * <P>Type: INTEGER</P>
	         */
	        public static final String REPORT_FLAG = "report_flag";
	        
	        /**
	         * The note itself
	         * <P>Type: INTEGER</P>
	         */
	        public static final String REPORT_INTERVAL = "report_interval";
	        
	        /**
	         * The note itself
	         * <P>Type: INTEGER</P>
	         */
	        public static final String E_REPORT = "e_report";
	        
	        /**
	         * The note itself
	         * <P>Type: INTEGER</P>
	         */
	        public static final String E_REPORT_INTERVAL = "e_report_interval";
	    }

	    public static final class SettingsColumns implements BaseColumns {
	        // This class cannot be instantiated
	        private SettingsColumns() {}

	        /**
	         * The content:// style URL for this table
	         */
	        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/notes");

	        /**
	         * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
	         */
	        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.note";

	        /**
	         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single note.
	         */
	        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.note";

	        /**
	         * The title of the note
	         * <P>Type: TEXT</P>
	         */
	        public static final String SHARE_FLAG = "share_flag";

	        /**
	         * The note itself
	         * <P>Type: TEXT</P>
	         */
	        public static final String SNS_ID = "sns_id";

	        /**
	         * The note itself
	         * <P>Type: TEXT</P>
	         */
	        public static final String SNS_PW = "sns_pw";

	        /**
	         * The note itself
	         * <P>Type: INTEGER</P>
	         */
	        public static final String TREE_FLAG = "tree_flag";
	    }

}
