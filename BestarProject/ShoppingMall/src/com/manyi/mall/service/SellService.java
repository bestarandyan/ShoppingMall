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
import com.manyi.mall.cachebean.release.ReleasedSellFirstRequest;
import com.manyi.mall.cachebean.release.ReleasedSellFirstResponse;
import com.manyi.mall.cachebean.release.ReleasedSellRequest;
import com.manyi.mall.cachebean.release.RentAndSellReleaseRecordInfoRequest;
import com.manyi.mall.cachebean.release.RentAndSellReleaseRecordInfoResponse;
import com.manyi.mall.cachebean.release.RentPublishRecordResponse;
import com.manyi.mall.cachebean.release.SellInfoListRequest;
import com.manyi.mall.cachebean.release.SellInfoRequest;
import com.manyi.mall.cachebean.release.SellInfoResponse;
import com.manyi.mall.cachebean.release.SellPublishRecordRequest;
import com.manyi.mall.cachebean.release.UpdatePublishRequest;
import com.manyi.mall.cachebean.user.HouseRequest;
import com.manyi.mall.cachebean.user.HouseResponse;
@RequestMapping(value = "/sell", interceptors = AppAuthInterceptor.class)
public interface SellService extends RestService {
	// SELL_CENTER_URL
	// indexList.rest
	// @Loading(value=R.id.loading,layout=R.layout.loading,container=R.id.pull_refresh_list_container)
	@Loading(value = R.id.loading, layout = R.layout.loading)
	@RequestMapping("/indexList.rest")
	public HouseResponse gethouse(SellInfoListRequest req);
	
	@RequestMapping("/indexList.rest")
	public HouseResponse refreshHouse(SellInfoListRequest req);

	@Loading(value = R.id.loading, layout = R.layout.loading,container = R.id.release_sell_submit)
	@RequestMapping("/releaseSellInfo.rest")
	public Response releasedSell(ReleasedSellRequest req);

	@Loading(value = R.id.loading, layout = R.layout.loading)
	@RequestMapping("/updatePublishCount.rest")
	public Response publishCount(UpdatePublishRequest req);

	@Loading(value = R.id.loading, layout = R.layout.loading)
	@RequestMapping("/chenkHoustIsSell.rest")
	public ReleasedSellFirstResponse checkSell(ReleasedSellFirstRequest req);

	//出售详情
	@Loading(value = R.id.loading, layout = R.layout.loading,container = R.id.release_sell)
	@RequestMapping("/sellDetails.rest")
	public SellInfoResponse sellInfos(SellInfoRequest req);

	@Loading(value = R.id.loading, layout = R.layout.loading)
	@RequestMapping("/findSellByPage.rest")
	public HouseResponse findSellByPage(HouseRequest req);
	//
	// @RequestMapping("/updatePublishSellInfo.rest")
	// @Accept(MediaType.APPLICATION_JSON)
	// public HouseResponse findSellByPage(HouseRequest req);
	
	
	/**
	 * 发布出售记录
	 */
	
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.mine_record_loadding)
	@RequestMapping("/sellPublishRecord.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public RentPublishRecordResponse sellRecord(SellPublishRecordRequest request);
	
	@RequestMapping("/sellPublishRecord.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public RentPublishRecordResponse sellRecord2(SellPublishRecordRequest request);

	
	/**
	 * 出售审核记录  已审核
	 */
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.checkedLayout)
	@RequestMapping("/getSellExamineRecodList.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public CheckedRecordsResponse getCheckedSellRecords(CheckedRecordsRequest request);
	
	@RequestMapping("/getSellExamineRecodList.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public CheckedRecordsResponse getCheckedSellRecordsNoLoading(CheckedRecordsRequest request);
	
	
	/**
	 * 出售审核记录 审核中
	 */
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.checkingLayout)
	@RequestMapping("/getSellExamineRecodIng.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public CheckingRecordsResponse getCheckingSellRecords(CheckingRecordsRequest request);
	
	
	/**
	 * 发布出租记录详情
	 */
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.release_record_info)
	@RequestMapping("/sellPublishRecordDetail.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public RentAndSellReleaseRecordInfoResponse sellRecordInfo(RentAndSellReleaseRecordInfoRequest req);
	
}
