package com.manyi.mall.provider.contract;

import android.net.Uri;

import com.manyi.mall.provider.ManyiBaseContract;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.Column.Type;
import com.tjeannin.provigen.annotation.ContentUri;

public interface CityContract extends ManyiBaseContract {
	public static final String TABLE_NAME = "city_table";

	@ContentUri
	public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

	@Column(Type.TEXT)
	public static final String CITY_LIST_CITY_ID = "city_id";

	@Column(Type.TEXT)
	public static final String CITY_LIST_CITY_NAME = "city_name";

	@Column(Type.TEXT)
	public static final String CITY_LIST_CODE = "code";

	@Column(Type.TEXT)
	public static final String CITY_LIST_CREATE_TIME = "create_time";

	@Column(Type.TEXT)
	public static final String CITY_LIST_PATH = "path";

	@Column(Type.TEXT)
	public static final String CITY_LIST_REMARK = "remark";

	@Column(Type.TEXT)
	public static final String CITY_LIST_PARENT_ID = "parent_id";

	@Column(Type.TEXT)
	public static final String CITY_LIST_STATUS = "status";

	@Column(Type.TEXT)
	public static final String CITY_LIST_SERIAL_CODE = "serialCode";

	@Column(Type.TEXT)
	public static final String CITY_LIST_USER_ID = "userId";
}
