package com.manyi.mall.mine;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.ProgressDialog;
import android.view.animation.Animation;
import android.widget.EditText;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.user.FeedbackRequest;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.CommonService;

@EFragment(R.layout.fragment_feedback)
public class FeedBackFragment extends SuperFragment<Integer> {

    @ViewById(R.id.content_feedback)
    EditText mContentFeedback;

    private CommonService mCommonService;

    private ProgressDialog mFeedBackDialog;

    @Click(R.id.feedback_back)
    void feedBackBack() {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        ManyiUtils.closeKeyBoard(getActivity(), mContentFeedback);
        remove();
    }

    @AfterViews
    void init() {
        addAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ManyiUtils.showKeyBoard(getActivity(), mContentFeedback);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Click(R.id.feedback_send)
    void feedbackSend() {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        ManyiUtils.closeKeyBoard(getActivity(), mContentFeedback);
        if (mContentFeedback.getText() != null && mContentFeedback.getText().toString().trim().length() > 0) {
            mFeedBackDialog = ProgressDialog.show(getActivity(), null, "提交中...", true);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        FeedbackRequest feedBackRequest = new FeedbackRequest();
                        int uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
                        feedBackRequest.setUid(uid);
                        feedBackRequest.setContext(mContentFeedback.getText().toString());
                        Response response = mCommonService.feedbackResponse(feedBackRequest);
                        int responseCode = response.getErrorCode();
                        if (responseCode == 0) {
                            // // // 反馈发送成功
                            onCommitSuccess1();
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            onCommitSuccess2();
                        } else {
                            // 反馈发送失败
                            onCommitFailed("提交失败！请重试");
                        }
                    } catch (RestException e) {
                        // 反馈发送失败
                        onCommitFailed(e.getMessage());
                    } catch (Exception e) {
                        // 其他异常
                        onCommitFailed("提交失败！请重试");
                    }
                }
            }).start();
        } else {
            DialogBuilder.showSimpleDialog("亲！请先输入反馈意见哦！", getBackOpActivity());
        }

    }

    @UiThread
    public void onCommitSuccess1() {
        if (mFeedBackDialog != null && mFeedBackDialog.isShowing()) {
            mFeedBackDialog.dismiss();
        }
        mFeedBackDialog = ProgressDialog.show(getActivity(), null, "发送成功！", true);
    }

    @UiThread
    public void onCommitSuccess2() {
        if (mFeedBackDialog != null && mFeedBackDialog.isShowing()) {
            mFeedBackDialog.dismiss();
        }
        remove();
    }

    @UiThread
    public void onCommitFailed(String msg) {
        if (mFeedBackDialog != null && mFeedBackDialog.isShowing()) {
            mFeedBackDialog.dismiss();
        }
        DialogBuilder.showSimpleDialog(msg, getBackOpActivity());
    }
}
