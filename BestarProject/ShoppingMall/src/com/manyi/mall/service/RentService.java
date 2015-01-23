package com.manyi.mall.service;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.api.rest.MediaType;

import com.huoqiu.framework.rest.AppAuthInterceptor;
import com.huoqiu.framework.rest.Loading;
import com.huoqiu.framework.rest.RequestMapping;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.rest.RestService;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.CheckedRecordsRequest;
import com.manyi.mall.cachebean.mine.CheckedRecordsResponse;
import com.manyi.mall.cachebean.mine.CheckingRecordsRequest;
import com.manyi.mall.cachebean.mine.CheckingRecordsResponse;
import com.manyi.mall.cachebean.release.ReleasedRentFirstRequest;
import com.manyi.mall.cachebean.release.ReleasedRentFirstResponse;
import com.manyi.mall.cachebean.release.ReleasedRentInfoListRequest;
import com.manyi.mall.cachebean.release.ReleasedRentRequest;
import com.manyi.mall.cachebean.release.RentAndSellReleaseRecordInfoRequest;
import com.manyi.mall.cachebean.release.RentAndSellReleaseRecordInfoResponse;
import com.manyi.mall.cachebean.release.RentInfoRequest;
import com.manyi.mall.cachebean.release.RentInfoResponse;
import com.manyi.mall.cachebean.release.RentPublishRecordRequest;
import com.manyi.mall.cachebean.release.RentPublishRecordResponse;
import com.manyi.mall.cachebean.user.HouseRequest;
import com.manyi.mall.cachebean.user.HouseResponse;

@RequestMapping(value = "/rent", interceptors = AppAuthInterceptor.class)
public interface RentService extends RestService{

	@Loading(value = R.id.loading, layout = R.layout.loading)
	@RequestMapping("/chenkHoustIsSell.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public ReleasedRentFirstResponse rentFirst(ReleasedRentFirstRequest req);

	@Loading(value = R.id.loading, layout = R.layout.loading,container = R.id.release_rent_submit)
	@RequestMapping("/publishRentInfo.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public Response releasedRent(ReleasedRentRequest req);

	@Loading(value=R.id.loading,layout=R.layout.loading)
	@RequestMapping("/indexList.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public HouseResponse getrent(ReleasedRentInfoListRequest req);

	@RequestMapping("/indexList.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public HouseResponse refreshRent(ReleasedRentInfoListRequest req);
	
	@Loading(value = R.id.loading, layout = R.layout.loading)
	@RequestMapping("/findRentByPage.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public HouseResponse findRentByPage(HouseRequest req);

	
	//出租详情
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.release_sell)
	@RequestMapping("/rentDetails.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public RentInfoResponse rentInfo(RentInfoRequest req);
	
	
	/**
	 * 发布出租记录
	 */
	
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.mine_record_loadding)
	@RequestMapping("/rentPublishRecord.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public RentPublishRecordResponse rentRecord(RentPublishRecordRequest request);
	
	@RequestMapping("/rentPublishRecord.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public RentPublishRecordResponse rentRecord2(RentPublishRecordRequest request);
	
	
	/**
	 * 发布出租记录详情
	 */
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.release_record_info)
	@RequestMapping("/rentPublishRecordDetail.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public RentAndSellReleaseRecordInfoResponse rentRecordInfo(RentAndSellReleaseRecordInfoRequest req);

	
	
	/**
	 * 出租审核记录  已审核
	 */
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.checkedLayout)
	@RequestMapping("/getRentExamineRecodList.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public CheckedRecordsResponse getCheckedRentRecords(CheckedRecordsRequest request);
	
	@RequestMapping("/getRentExamineRecodList.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public CheckedRecordsResponse getCheckedRentRecordsNoLoading(CheckedRecordsRequest request);
	
	
	/**
	 * 出租审核记录 审核中
	 */
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.checkingLayout)
	@RequestMapping("/getRentExamineRecodIng.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public CheckingRecordsResponse getCheckingRentRecords(CheckingRecordsRequest request);
}
