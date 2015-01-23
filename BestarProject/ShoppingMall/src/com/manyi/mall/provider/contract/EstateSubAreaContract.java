package com.manyi.mall.provider.contract;

import android.net.Uri;

import com.manyi.mall.provider.ManyiBaseContract;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.Column.Type;
import com.tjeannin.provigen.annotation.ContentUri;

public interface EstateSubAreaContract extends ManyiBaseContract {
	public static final String TABLE_NAME = "subestate";

	@ContentUri
	public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

	@Column(Type.TEXT)
	public static final String ESTATE_ID = "estate_id";

	@Column(Type.TEXT)
	public static final String SUBESTATE_ID = "subestate_id";

	@Column(Type.TEXT)
	public static final String SUBESTATE_NAME = "subestate_name";
}
