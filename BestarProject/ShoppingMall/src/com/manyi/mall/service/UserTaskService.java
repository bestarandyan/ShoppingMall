package com.manyi.mall.service;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.api.rest.MediaType;

import com.huoqiu.framework.rest.AppAuthInterceptor;
import com.huoqiu.framework.rest.Loading;
import com.huoqiu.framework.rest.RequestMapping;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.rest.RestService;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.AddUserTaskRequest;
import com.manyi.mall.cachebean.search.AddUserTaskResponse;
import com.manyi.mall.cachebean.search.GetTaskRequest;
import com.manyi.mall.cachebean.search.GetTaskResponse;
import com.manyi.mall.cachebean.search.UserTaskCountRequest;
import com.manyi.mall.cachebean.search.UserTaskCountResponse;
import com.manyi.mall.cachebean.search.UserTaskDetailRequest;
import com.manyi.mall.cachebean.search.UserTaskDetailResponse;
import com.manyi.mall.cachebean.user.FinishedTaskRequest;
import com.manyi.mall.cachebean.user.TaskCommitRequest;
import com.manyi.mall.cachebean.user.TaskListResponse;
import com.manyi.mall.cachebean.user.UpdateHouseRequest;
import com.manyi.mall.cachebean.user.UploadFileRequest;
import com.manyi.mall.cachebean.user.UserTaskRequest;

@RequestMapping(value = "/userTask", interceptors = AppAuthInterceptor.class)
public interface UserTaskService extends RestService {
	/**
	 * 
	 * @param actiontastlist
	 * @return
	 */

	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.action_required_loaddinng)
	@RequestMapping("/userTaskIndex.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public TaskListResponse userTaskIndex(UserTaskRequest request);

	@RequestMapping("/userTaskIndex.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public TaskListResponse userTaskIndex2(UserTaskRequest request);

	/**
	 * userTaskCount
	 */
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.release_fragment)
	@RequestMapping("/userTaskCount.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public UserTaskCountResponse userTaskCount(UserTaskCountRequest request);

	/**
	 * Task Info
	 */
	@Loading(value = R.id.loading, layout = R.layout.loading,container= R.id.check_task_info)
	@RequestMapping("/userTaskDetail.rest")
	public UserTaskDetailResponse userTaskDetail(UserTaskDetailRequest request);

	/**
	 * get task
	 */
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.release_sell)
	@RequestMapping("/findHouseResource4UserTaskPhoto.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public GetTaskResponse getTaskResponse(GetTaskRequest request);

	/**
	 * get task
	 */
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.release_sell)
	@RequestMapping("/addUserTask.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public AddUserTaskResponse addUserTaskResponse(AddUserTaskRequest request);

	@RequestMapping("/userTaskIndexById.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public TaskListResponse userTaskIndexById2(FinishedTaskRequest request);
	
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.action_required_loaddinng)
	@RequestMapping("/updateHouseType.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public Response updateHouseType(UpdateHouseRequest request);

	/**
	 * taskUploadSingleFile.rest
	 */
	@RequestMapping("/taskUploadSingleFile.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public Response taskUploadSingleFile(UploadFileRequest request);

	/**
	 * taskSubmit.rest
	 */
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.action_required_loaddinng)
	@RequestMapping("/taskSubmit.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public Response taskSubmit(TaskCommitRequest request);

}
