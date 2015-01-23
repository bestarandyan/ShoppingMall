package com.manyi.mall.provider.contract;

import android.net.Uri;

import com.manyi.mall.provider.ManyiBaseContract;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.Column.Type;
import com.tjeannin.provigen.annotation.ContentUri;

public interface LocalHistoryContract extends ManyiBaseContract {
	public static final String TABLE_NAME = "localhistory";
	public static final int ESTATE_TYPE = 0;
	public static final int AREA_TYPE = 1;

	public static final String DEFAULT_SORT = LocalHistoryContract.DATE + " DESC";

	@ContentUri
	public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

	@Column(Type.TEXT)
	public static final String NAME = "name";
	
	@Column(Type.TEXT)
	public static final String ESTATENAMESTR = "estateNameStr";//地址
	
	@Column(Type.TEXT)
	public static final String ALIASNAME = "aliasName";
	
	@Column(Type.TEXT)
	public static final String CITYNAME = "cityname";//区域 名称

	@Column(Type.TEXT)
	public static final String TOWNNAME = "townname";//板块 名称

	@Column(Type.TEXT)
	public static final String ESTATE_ID = "estate_id";

	@Column(Type.TEXT)
	public static final String DATE = "date";

	@Column(Type.TEXT)
	public static final String USER_ID = "user_id";

	@Column(Type.INTEGER)
	public static final String IS_AREA = "is_area";
}
