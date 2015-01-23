package com.manyi.mall.service;

import com.huoqiu.framework.rest.AppAuthInterceptor;
import com.huoqiu.framework.rest.Loading;
import com.huoqiu.framework.rest.RequestMapping;
import com.huoqiu.framework.rest.RestService;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.release.CommonReleasedFirstRequest;
import com.manyi.mall.cachebean.release.CommonReleasedFirstResponse;
import com.manyi.mall.cachebean.release.IntentDetailRequest;
import com.manyi.mall.cachebean.release.IntentDetailResponse;
import com.manyi.mall.cachebean.release.RentRecordInfoRequest;
import com.manyi.mall.cachebean.release.RentRecordInfoResponse;
import com.manyi.mall.cachebean.search.AreaRequest;
import com.manyi.mall.cachebean.search.AreasResponse;
import com.manyi.mall.cachebean.search.GetBuildingListRequest;
import com.manyi.mall.cachebean.search.GetBuildingListResponse;
import com.manyi.mall.cachebean.search.IncrementalCityRequest;
import com.manyi.mall.cachebean.search.IncrementalCityResponce;
import com.manyi.mall.cachebean.search.SearchBuildingListResponse;
import com.manyi.mall.cachebean.search.SearchBuildingListResquest;
import com.manyi.mall.cachebean.search.SearchRequest;
import com.manyi.mall.cachebean.search.SearchRespose;
import com.manyi.mall.cachebean.search.ServiceTimeResponse;
import com.manyi.mall.cachebean.search.SubRequest;
import com.manyi.mall.cachebean.search.SubResponse;
import com.manyi.mall.cachebean.user.AutoUpdateResponse;
import com.manyi.mall.cachebean.user.FeedbackRequest;
import com.manyi.mall.cachebean.user.FeedbackResponse;
import com.manyi.mall.cachebean.user.VersionRequest;

@RequestMapping(value = "/common", interceptors = AppAuthInterceptor.class)
public interface CommonService extends RestService {

	@RequestMapping("/findEstateByName.rest")
	public SearchRespose search(SearchRequest request);

	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.area_select_container)
	@RequestMapping("/findAreaByAreaId.rest")
	public AreasResponse getDistrict(AreaRequest req);
	
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.register_area_relative_layout)
	@RequestMapping("/findAreaByAreaId.rest")
	public AreasResponse getArea(AreaRequest req);
	
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.register_town_relative_layout)
	@RequestMapping("/findAreaByAreaId.rest")
	public AreasResponse getTown(AreaRequest req);

	@RequestMapping("/findAreaByAreaId.rest")
	public AreasResponse getSubDistrict(AreaRequest req);

	// /rest/common/getHouseCount.rest
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.city_base_layout)
	@RequestMapping("/incrementalCity.rest")
	public IncrementalCityResponce incrementalCity(IncrementalCityRequest req);

	@Loading(value = R.id.loading, layout = R.layout.loading)
	@RequestMapping("/getHouseCount.rest")
	public IntentDetailResponse houseCount(IntentDetailRequest req);

	// 发布记录详情
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.release_record_info)
	@RequestMapping("/recordDetails.rest")
	public RentRecordInfoResponse rentRecordInfo(RentRecordInfoRequest req);
	
	@Loading(value=R.id.loading,layout=R.layout.loading, container = R.id.release_sell)
	@RequestMapping("/checkPublishSell.rest")
	public CommonReleasedFirstResponse commonReleased(CommonReleasedFirstRequest req);
	
	@Loading(value=R.id.loading,layout=R.layout.loading, container = R.id.release_rent)
	@RequestMapping("/release.rest")
	public CommonReleasedFirstResponse commonReleased2(CommonReleasedFirstRequest req);
	
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.feedback_loadding)
	@RequestMapping("/feedback.rest")
	public FeedbackResponse feedbackResponse(FeedbackRequest req);

	@Loading(value = R.id.loading, layout = R.layout.loading,container = R.id.areaChildDialogLayout)
	@RequestMapping("/findSubEstateListByEstateId.rest")
	public SubResponse subResponse(SubRequest req);

	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.start_loadding)
	@RequestMapping("/getServerTime.rest")
	public ServiceTimeResponse getServerTime();
	
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.search_living_area_layout)
	@RequestMapping("/getBuildingListBySubestateId.rest")
	public GetBuildingListResponse getBuildingList(GetBuildingListRequest request);
	
	@RequestMapping("/getBuildingListByBuildingName.rest")
	public SearchBuildingListResponse searchBuildingList(SearchBuildingListResquest request);
	
	// @Loading(value = R.id.loading, layout = R.layout.loading)
	@RequestMapping("/automaticUpdates.rest")
	public AutoUpdateResponse getUpdateResponse(VersionRequest req);

	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.mine_loadding)
	@RequestMapping("/automaticUpdates.rest")
	public AutoUpdateResponse getUpdateResponse2(VersionRequest req);

}
