package com.manyi.mall.service;

import com.huoqiu.framework.rest.AppAuthInterceptor;
import com.huoqiu.framework.rest.Loading;
import com.huoqiu.framework.rest.RequestMapping;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.rest.RestService;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.AwardDetailRequest;
import com.manyi.mall.cachebean.mine.AwardDetailResponse;
import com.manyi.mall.cachebean.mine.AwardPageRequest;
import com.manyi.mall.cachebean.mine.BindPaypalRequest;
import com.manyi.mall.cachebean.mine.ChangePaypalRequest;
import com.manyi.mall.cachebean.mine.ChangePhoneNumberNextRequest;
import com.manyi.mall.cachebean.mine.MineAwardRequest;
import com.manyi.mall.cachebean.mine.MineAwardResponse;
import com.manyi.mall.cachebean.mine.MineBonusCollectRequest;
import com.manyi.mall.cachebean.mine.MineBonusCollectResponse;
import com.manyi.mall.cachebean.mine.MineHomeRequest;
import com.manyi.mall.cachebean.mine.MineHomeResponse;
import com.manyi.mall.cachebean.mine.RecommendRequest;
import com.manyi.mall.cachebean.mine.UpdateMobileRequest;
import com.manyi.mall.cachebean.user.AddEstateRequest;
import com.manyi.mall.cachebean.user.BandBankCardRequest;
import com.manyi.mall.cachebean.user.CaptchaCodeRequest;
import com.manyi.mall.cachebean.user.CaptchaCodeResponse;
import com.manyi.mall.cachebean.user.CheckMsgCodeRequest;
import com.manyi.mall.cachebean.user.ForgetGetCordRequest;
import com.manyi.mall.cachebean.user.ForgetGetCordResponse;
import com.manyi.mall.cachebean.user.ForgetNextRequest;
import com.manyi.mall.cachebean.user.GetFailedDetailRequest;
import com.manyi.mall.cachebean.user.GetFailedDetailResponse;
import com.manyi.mall.cachebean.user.InformRequest;
import com.manyi.mall.cachebean.user.InformResponse;
import com.manyi.mall.cachebean.user.LoginRequest;
import com.manyi.mall.cachebean.user.LoginResponse;
import com.manyi.mall.cachebean.user.ModifyLoginPwdRequest;
import com.manyi.mall.cachebean.user.RegistNextRequest;
import com.manyi.mall.cachebean.user.RegistRequest;
import com.manyi.mall.cachebean.user.RegisterAgainRequest;
import com.manyi.mall.cachebean.user.ResetPasswordRequest;
import com.manyi.mall.cachebean.user.UpdateBankCardRequest;
import com.manyi.mall.cachebean.user.UpdateUserPublicNumRequest;

@RequestMapping(value = "/uc", interceptors = AppAuthInterceptor.class)
public interface UcService extends RestService {

    //
    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.login_relative_layout)
    @RequestMapping("/userLogin.rest")
    public LoginResponse login(LoginRequest request);

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.start_loadding)
    @RequestMapping("/userLogin.rest")
    public LoginResponse startLogin(LoginRequest request);

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.band_bank_card_relative_layout)
    @RequestMapping("/bindBankCard.rest")
    public Response bandBankCard(BandBankCardRequest request);

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.band_bank_card_relative_layout)
    @RequestMapping("/updateBankCard.rest")
    public Response updateBankCard(UpdateBankCardRequest request);

    @RequestMapping("/userLogin.rest")
    public LoginResponse splashLogin(LoginRequest request);

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.register_relative_layout)
    @RequestMapping("/checkMobile.rest")
    public Response check(RegistRequest request);

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.register_relative_layout)
    @RequestMapping("/findMsgCode.rest")
    public CaptchaCodeResponse code(CaptchaCodeRequest request);

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.register_next_relative_layout)
    @RequestMapping("/regist.rest")
    public Response register(RegistNextRequest request);

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.forget_password_relatitive_layout)
    @RequestMapping("/findLoginPwd.rest")
    public ForgetGetCordResponse Forgetgetcode(ForgetGetCordRequest request);

    // ForgetNextRequest
    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.forget_password_relatitive_layout)
    @RequestMapping("/checkVerifyCode.rest")
    public Response ForgetNextRequest(ForgetNextRequest request);

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.reset_password_relative_layout)
    @RequestMapping("/updateLoginPwd.rest")
    public Response Restpassword(ResetPasswordRequest req);

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.update_password_relative_layout)
    @RequestMapping("/modifyLoginPwd.rest")
    public Response modifyLoginPwd(ModifyLoginPwdRequest req);

    // uc/myAccount.rest
    @Loading(value = R.id.loading, layout = R.layout.loading)
    @RequestMapping("/addEstate.rest")
    public Response addEstate(AddEstateRequest req);

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.mine_loadding)
    @RequestMapping("/myAccount.rest")
    public MineHomeResponse Minehome(MineHomeRequest request);

    @Loading(value = R.id.loading, layout = R.layout.loading)
    @RequestMapping("/checkMsgCode.rest")
    public Response checkMsgCode(CheckMsgCodeRequest request);

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.release_sell)
    @RequestMapping("/reportDis.rest")
    public InformResponse reportDis(InformRequest request);

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.review_failed_relative_layout)
    @RequestMapping("/getFailedDetail.rest")
    public GetFailedDetailResponse getFailedDetail(GetFailedDetailRequest request);

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.register_next_relative_layout)
    @RequestMapping("/againRegist.rest")
    public Response againRegist(RegisterAgainRequest request);

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.first_binding_loadding)
    @RequestMapping("/bindPaypal.rest")
    public Response bindPaypal(BindPaypalRequest request);

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.modification_loadding)
    @RequestMapping("/changePaypal.rest")
    public Response changePaypal(ChangePaypalRequest request);

    // @Loading(value = R.id.loading, layout = R.layout.loading)
    @RequestMapping("/award.rest")
    public MineAwardResponse mineAward2(MineAwardRequest req);

    @Loading(value = R.id.loading, layout = R.layout.loading)
    @RequestMapping("/award.rest")
    public MineAwardResponse mineAward(MineAwardRequest req);

    // @Loading(value = R.id.loading, layout = R.layout.loading)
    @RequestMapping("/findAwardPage.rest")
    public MineAwardResponse findAwardPage(AwardPageRequest req);

    // @Loading(value = R.id.loading, layout = R.layout.loading)
    @RequestMapping("/recommend.rest")
    public Response saveRecommendNum(RecommendRequest req);

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.mine_bonus_loadding)
    @RequestMapping("/award4MeSum.rest")
    public MineBonusCollectResponse award4MeSum(MineBonusCollectRequest req);

    @RequestMapping("/award4MeSum.rest")
    public MineBonusCollectResponse award4MeSum2(MineBonusCollectRequest req);

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.mine_bonus_detaill_loadding)
    @RequestMapping("/awardDetail.rest")
    public AwardDetailResponse awardDetail(AwardDetailRequest req);

    @RequestMapping("/awardDetail.rest")
    public AwardDetailResponse awardDetail2(AwardDetailRequest req);


    /**
     * 发布标识清除
     */
    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.release_fragment)
    @RequestMapping("/updateUserPublicNum.rest")
    public Response updatePublicNum(UpdateUserPublicNumRequest req);

    /**
     * 修改电话号码，获取验证码
     */
    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.change_phone_number_layout)
    @RequestMapping("/sendUpdateMobileSMS.rest")
    public Response getVerifyCode(CaptchaCodeRequest request);


    /**
     * 修改电话号码，获取验证码
     */
    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.new_phone_number)
    @RequestMapping("/sendUpdateMobileSMS.rest")
    public Response getNewPhoneCode(CaptchaCodeRequest request);
    /**
     * 修改电话号码下一步
     */
    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.change_phone_number_layout)
    @RequestMapping("/checkUpdateMobile.rest")
    public Response changePhoneNumberNext(ChangePhoneNumberNextRequest req);

    /**
     * ChangeNewPhoneNumber
     */

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.new_phone_number)
    @RequestMapping("/updateMobile.rest")
    public Response updatePhone(UpdateMobileRequest req);



    // @Loading(value = R.id.loading, layout = R.layout.loading)
    // @RequestMapping("/hisTaskDetail.rest")
    // @Accept(MediaType.APPLICATION_JSON)
    // public HisTaskDetailResponse getFinishedTaskInfo(
    // HisTaskDetailRequest request);
    //
    // @Loading(value = R.id.loading, layout = R.layout.loading)
    // @RequestMapping("/taskDetails.rest")
    // @Accept(MediaType.APPLICATION_JSON)
    // public ToDaysTaskDetailsResponse getToDayTaskInfo(
    // ToDaysTaskDetailsRequest request);
    //
    // @RequestMapping("/taskLookFail.rest")
    // @Accept(MediaType.APPLICATION_JSON)
    // public Response taskNotAs(TaskLookFailRequest request);
}