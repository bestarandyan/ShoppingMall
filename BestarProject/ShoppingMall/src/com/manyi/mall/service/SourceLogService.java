package com.manyi.mall.service;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.api.rest.MediaType;

import com.huoqiu.framework.rest.AppAuthInterceptor;
import com.huoqiu.framework.rest.Loading;
import com.huoqiu.framework.rest.RequestMapping;
import com.huoqiu.framework.rest.RestService;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.CheckedRecordsRequest;
import com.manyi.mall.cachebean.mine.CheckedRecordsResponse;
import com.manyi.mall.cachebean.mine.CheckingRecordsRequest;
import com.manyi.mall.cachebean.mine.CheckingRecordsResponse;
import com.manyi.mall.cachebean.mine.ClearResponse;
import com.manyi.mall.cachebean.mine.MineRecordsRequest;
import com.manyi.mall.cachebean.mine.MineRecordsResponse;
import com.manyi.mall.cachebean.mine.RecordsMoreRequest;
import com.manyi.mall.cachebean.search.AddAreaLogrequest;
import com.manyi.mall.cachebean.search.ChangeHouseRequest;
import com.manyi.mall.cachebean.search.ChangeHouseResponse;
import com.manyi.mall.cachebean.search.HouseDetailRequest;
import com.manyi.mall.cachebean.search.HouseDetailResponse;

@RequestMapping(value = "/sourceLog", interceptors = AppAuthInterceptor.class)
public interface SourceLogService extends RestService {

	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.mine_record_loadding)
	@RequestMapping("/indexList.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public MineRecordsResponse mineRecord(MineRecordsRequest request);
	
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.checkedLayout)
	@RequestMapping("/getExamineRecodList.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public CheckedRecordsResponse getCheckedRecords(CheckedRecordsRequest request);
	
	@RequestMapping("/getExamineRecodList.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public CheckedRecordsResponse getCheckedRecordsNoLoading(CheckedRecordsRequest request);
	
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.checkingLayout)
	@RequestMapping("/getExamineRecodIng.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public CheckingRecordsResponse getCheckingRecords(CheckingRecordsRequest request);

	// @Loading(value=R.id.loading,layout=R.layout.loading)
	@RequestMapping("/indexList.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public MineRecordsResponse mineRecord2(MineRecordsRequest request);

	@Loading(value = R.id.loading, layout = R.layout.loading)
	@RequestMapping("/clearPublishLog.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public ClearResponse mineRecordclear(MineRecordsRequest request);

	// 改盘第一步
	@Loading(value = R.id.loading, layout = R.layout.loading,container= R.id.release_sell)
	@RequestMapping("/checkCanUpdateDisc.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public ChangeHouseResponse checkCanUpdateDisc(ChangeHouseRequest request);

//	// 改盘第二步
//	@Loading(value = R.id.loading, layout = R.layout.loading,container = R.id.change_submit)
//	@RequestMapping("/updateDisc.rest")
//	@Accept(MediaType.APPLICATION_JSON)
//	public ChangeHouseNextResponse updateDisc(ChangeHouseNextRequest request);

	// 我的发布记录加载更多
	// @Loading(value=R.id.loading,layout=R.layout.loading)
	@RequestMapping("/findPageLog.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public MineRecordsResponse mineRecordsmore(RecordsMoreRequest request);

	//
	// 我的发布记录新增小区
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.mine_record_loadding)
	@RequestMapping("/indexListAddAreaLog.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public MineRecordsResponse addAreaLog(AddAreaLogrequest request);

	@RequestMapping("/indexListAddAreaLog.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public MineRecordsResponse addAreaLog2(AddAreaLogrequest request);

	// indexListAddAreaLog
	//
	// 我的发布记录新增小区
	// @Loading(value=R.id.loading,layout=R.layout.loading)
	@RequestMapping("/findPageAddAreaLog.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public MineRecordsResponse findPageAddAreaLog(RecordsMoreRequest request);
	
	@RequestMapping("/examineRecordDetail.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public HouseDetailResponse showInfo(HouseDetailRequest request);
	
	

}
