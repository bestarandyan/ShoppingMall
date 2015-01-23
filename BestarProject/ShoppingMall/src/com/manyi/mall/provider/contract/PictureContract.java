package com.manyi.mall.provider.contract;

import android.net.Uri;

import com.manyi.mall.provider.ManyiBaseContract;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.Column.Type;
import com.tjeannin.provigen.annotation.ContentUri;

public interface PictureContract extends ManyiBaseContract {
	public static final String TABLE_NAME = "picture_table";

	@ContentUri
	public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

	@Column(Type.TEXT)
	public static final String PICTURE_TASKID = "picture_taskid";

	@Column(Type.INTEGER)
	public static final String PICTURE_SUBMIT_STATUS = "picture_submit_status";

	@Column(Type.INTEGER)
	public static final String PICTURE_EXSIT_STATUS = "picture_exsit_status";

	@Column(Type.TEXT)
	public static final String PICTURE_TIME = "picture_time";

	@Column(Type.TEXT)
	public static final String PICTURE_LATITUDE = "picture_latitude";

	@Column(Type.TEXT)
	public static final String PICTURE_LONGITUDE = "picture_longitude";

	@Column(Type.TEXT)
	public static final String PICTURE_PATH = "picture_path";

	@Column(Type.INTEGER)
	public static final String PICTURE_SECTION_TYPE = "picture_section_type";

	@Column(Type.INTEGER)
	public static final String PICTURE_POSITION_TYPE = "picture_position_type";

	@Column(Type.TEXT)
	public static final String PICTURE_PARENT_FOLDER = "picture_parent_folder";

	@Column(Type.INTEGER)
	public static final String PICTURE_SECTION_TYPE_SUM = "picture_section_type_sum";

	public static final String DEFAULT_SORT = PictureContract.PICTURE_SECTION_TYPE + " ASC, " + PictureContract.PICTURE_POSITION_TYPE + " ASC";

}
