package com.manyi.mall.provider;

import android.net.Uri;

import com.tjeannin.provigen.ProviGenBaseContract;

/**
 * @author wangying
 *
 */
public interface ManyiBaseContract extends ProviGenBaseContract{

    /**
     * Special value for indicating that an entry
     * has never been updated, or doesn't exist yet.
     */
    public static final long UPDATED_NEVER = -2;
    
    
    public static final String CONTENT_AUTHORITY = "com.manyi.fyb";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    
}