package com.manyi.mall.common;

import android.os.Environment;

import java.io.File;

public class Constants {
	public static final int RESULT_SUCCESS = 0x3333;
	public static final int RESULT_ERROR = 0x0000;
	public static final int NO_ERROR = 0;
	public static final int SYSTEM_ERROR = 1;
	public static final int INVALID_TOKEN = 2;
	public static final int INVALID_APPID = 3;
	public static final int INVALID_APPSIG = 4;
	public static final String LOGIN_TIMES = "LOGIN_times";
	
	//--------------------- city id  start------------------------------
	public static final int ID_CITY_BEIJING = 12438;
	public static final int ID_CITY_SHANGHAI = 2;
	public static final int ID_CITY_GUANGZHOU = 40000;
	//---------------------------city id end-------------------------------
	//更新信用的标识
	public static final String NEW_VERSION_TAG = "NEW_VERSION_TAG";
	//更新的标题
	public static final String APP_UPDATE_TITLE = "String_UPDATE_TITLE";
	//更新版本的主体消息
	public static final String APP_UPDATE_MESSAGE = "UPDATE_MESSAGE";
	//版本号
	public static final String APP_UPDATE_VERSION = "UPDATE_VERSION";
	//更新的URL
	public static final String APP_UPDATE_URL = "UPDATE_URL";
	//是否强制更新
	public static final String APP_UPDATE_FORCE = "UPDATE_FORCE";
	
	public static final String HOUSE_CONFIG_SHARED_VALUE = "house_config";//房源配套信息存储数据标记
	public final static String TO_DAY_TASK_INFO = "house_info";
	
	public final static String GUIDE_SHAREDPREFERENCES_FLAG = "guide_flag";
	public final static String GUIDE_IS_GUIDE = "isGuide";
	
	public final static String TASK_INFO = "house_info";
	public final static String HOUSE_IMAGE_IS_FIRST_TAKE_PHOTO = "is_first_take_photo_in_application";
	public final static String IS_FIRST_TAKE_PHOTO = "is_first_take_photo";
	
	public static final String IMAGE_ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"fyb"+File.separator;
	public static final String IMAGE_REGISTER_ROOT_PATH = IMAGE_ROOT_PATH+"register"+File.separator;
	public static final String CODE_FILE_PATH = IMAGE_REGISTER_ROOT_PATH + "code"+ File.separator;
	public static final String CODE_RESIZE_FILE_PATH = IMAGE_REGISTER_ROOT_PATH + "code_resize"+ File.separator;
	public static final String CARD_FILE_PATH = IMAGE_REGISTER_ROOT_PATH +"card"+ File.separator;
	public static final String CARD_RESIZE_FILE_PATH = IMAGE_REGISTER_ROOT_PATH +"card_resize"+ File.separator;
	public static final String OBJECT_FILE_PATH = IMAGE_ROOT_PATH +"object_file_operator"+ File.separator;
	
	//---------------------选择配置时用到的标记 start---------------------
	public static final String TAG_HASTV = "hasTv";
	public static final String TAG_HASREFRIGERATOR = "hasRefrigerator";
	public static final String TAG_HASWASHINGMACHINE = "hasWashingMachine";
	public static final String TAG_HASAIRCONDITIONER = "hasAirConditioner";
	public static final String TAG_HASWATERHEATER = "hasWaterHeater";
	public static final String TAG_HASBED = "hasBed";
	public static final String TAG_HASSOFA = "hasSofa";
	public static final String TAG_HASBATHTUB = "hasBathtub";
	public static final String TAG_HASCENTRALHEATING = "hasCentralHeating";
	public static final String TAG_HASCENTRALAIRCONDITIONING = "hasCentralAirConditioning";
	public static final String TAG_HASCLOAKROOM = "hasCloakroom";
	public static final String TAG_HASRESERVEDPARKING = "hasReservedParking";
	public static final String TAG_HASCOURTYARD = "hasCourtyard";
	public static final String TAG_HASGAZEBO = "hasGazebo";
	public static final String TAG_HASPENTHOUSE = "hasPenthouse";
	public static final String TAG_DECORATETYPE = "decorateType";
	public static final String TAG_SAVESUCCESS = "tag_save_success";//是否保存成功
	//	---------------------选择配置时用到的标记 end---------------------

	/**
	 ****************************** 租售分离 Start***************************
	 */
	//发布记录
	public static final String RELEASE_RECORD_SELL = "release_record_sell";
	public static final String RELEASE_RECORD_RENT = "release_record_rent";
	
	//审核记录
	public static final String CHECK_RECORD_SELL = "check_record_sell";
	public static final String CHECK_RECORD_RENT = "check_record_rent";
	
	//记录详情
	public static final String RECORD_INFO_SELL = "record_info_sell";
	public static final String RECORD_INFO_RENT = "record_info_rent";
	
	/**
	 ****************************** 租售分离 End***************************
	 */
	
	// 常见问题
	public static final int GOTO_RENT_CONTENT = 1;
	public static final int GOTO_SELL_CONTENT = 2;
	public static final int GOTO_AWARD_CONTENT = 3;
	public static final int GOTO_REVIEW_CONTENT = 4;
	public static final int GOTO_MONTH_ARARD_CONTENT_RENT = 5;
    public static final int GOTO_MONTH_ARARD_CONTENT_SELL = 6;
	
	// 拍照
	public static final String HOUSE_ROOT_PATH = IMAGE_ROOT_PATH + "images" + File.separator;
	public static final String PHOTO_COMPLETE = "photo_complete";
	public static final String BED_ROOM = "bedRoom";
	public static final String LIVING_ROOM = "livingRoom";
	public static final String WC_ROOM = "wc";
	public final static String DOOR_PLATE = "doorPlate";
	public final static String MAIL_BOX_ELEVATOR_ROOM = "mailBox_or_elevatorRoom";
	public final static String ELEVATOR_ROOM = "elevatorRoom";
	public final static String KITCHEN = "kitchen";
	public final static String BALCONY = "balcony";
	public final static String EXTERIOR = "exterior";
	public final static String OTHERS = "others";
	
	public static final String TAKE_PHOTO_TAG = "TakePhotoFragment";
	public final static String TAKE_PHOTO_IMAGES = "images";
	public final static String TAKE_PHOTO_IMAGE_POSITION = "image_position";
	public static final long UPTATE_INTERVAL_TIME = 400;
	
	// 房间类型
	public final static int FACILITY_TYPE_NONE = 0;
	public final static int FACILITY_TYPE_ELEVATOR_OR_MAINLBOX = 1;
	public final static int FACILITY_TYPE_DOOR_PLATE = 2;
	public final static int FACILITY_TYPE_LIVING_ROOM = 3;
	public final static int FACILITY_TYPE_BEDROOM = 4;
	public final static int FACILITY_TYPE_WCROOM = 5;
	public final static int FACILITY_TYPE_KITCHEN = 6;
	public final static int FACILITY_TYPE_OTHERS = 7;
	
	// 绑定银行卡
	public final String BANK_NAME_STRING = "bank_name_string";
	
	// 登录，审核状态
	public static final int ACCOUNT_REVIEW_SUCCESS = 0x001; // 审核成功
	public static final int ACCOUNT_REVIEW_ING = 0x002; // 审核中
	public static final int ACCOUNT_REVIEW_FAILED = 0x003; // 审核失败

    //查看审核记录
    public static final int UP_REFRESH = 0x0001;
    public static final int DOWN_REFRESH = 0x0002;
    //查看联系人

    /* 自定义ACTION常数，作为广播的Intent Filter识别常数 */
    public static String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";
    public static String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";
    
    // main fragment

	public final static String TAB_SEATCH_HOUSE = "tab_search_house";
	public final static String TAB_POST = "tab_post";
	public final static String TAB_MINE = "tab_mine";
	
	public final static String UNSLECTED_TEXT_COLOR = "#8affffff";
	public final static String SLECTED_TEXT_COLOR = "#ffffff";
	
	//AreaChildDialogFragment
	public final static String CURRENT_TIME = "DIALOG_SELECT";
	public final static String SELECTED_ESTATE_ID = "selected_estate_id";
	public final static String SELECTED_ESTATE_DATA = "selected_estate_data";
	public final static String SELECTED_ESTATE_NAME = "selected_estate_name";
	
	//SearchFragment
	public final static String SEARCH_TARGET_CLASS = "search_target_class";
	public final static int MAX_LOCAL_HISTORY = 50;
	
	//SearchHouseFragment
	public final static String PERF_SEARCH = "perf_searsh";
	public final static String KEY_SEARCH_SELL = "key_search_sell";
	public final static long NORMAL_ANIMATION_INTERVERL = 200;
	public final static long SHORT_ANIMATION_INTERVERL = 1;
	
	//SearchLivingAreaFragment
	public final static int MAX_LOCAL_HISTORY_SEARCH_LIVING = 50;
	public final static int ESATE_NO_SUBAREA = 0;
	public final static int ESATE_HAS_SUBAREA = 1;
    //
    public static final int DEFAULT_TIME_OUT = 2 * 60 * 1000;
}
