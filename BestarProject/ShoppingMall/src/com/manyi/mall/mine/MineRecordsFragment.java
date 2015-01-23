package com.manyi.mall.mine;

import java.text.SimpleDateFormat;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.widget.pullrefresh.PullToRefreshBase;
import com.huoqiu.widget.pullrefresh.PullToRefreshBase.Mode;
import com.huoqiu.widget.pullrefresh.PullToRefreshBase.OnRefreshListener2;
import com.huoqiu.widget.pullrefresh.PullToRefreshListView;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.MineRecordsRequest;
import com.manyi.mall.cachebean.release.RentPublishRecordRequest;
import com.manyi.mall.cachebean.release.RentPublishRecordResponse;
import com.manyi.mall.cachebean.release.RentReleaseResponse;
import com.manyi.mall.cachebean.release.SellPublishRecordRequest;
import com.manyi.mall.common.Constants;
import com.manyi.mall.release.ReleasedRecordInfoFragment;
import com.manyi.mall.service.RentService;
import com.manyi.mall.service.SellService;
import com.manyi.mall.service.SourceLogService;

@SuppressLint("WorldWriteableFiles")
@EFragment(R.layout.fragment_mine_records)
public class MineRecordsFragment extends SuperFragment<Object> {

    private  Adapter mAdapter;
    private int mRecordType = 1;// 1为发布记录，2为新增小区

    @ViewById(R.id.pull_refresh_list)
    PullToRefreshListView mPullRefreshListView;

    @ViewById(R.id.record_nodata)
    LinearLayout mRecordNodata;

    @ViewById(R.id.publishing_community)
    RadioButton mPublishingCommunity;

    @ViewById(R.id.publishing_other)
    RadioButton mPublishingOther;

    private  boolean isFirst = true;

    private SourceLogService mSourceLogService;

    private  RentService mRentService;
    private SellService mSellService;
    private   RentPublishRecordRequest mRentPublishRecordRequest = new RentPublishRecordRequest();
    private  SellPublishRecordRequest mSellPublishRecordRequest = new SellPublishRecordRequest();

    private  RentPublishRecordResponse mPublishRecordResponse; //出售出租response相同 用同一个

    @FragmentArg
    String releaseRecordType;


    // 发布记录
    @Click(R.id.publishing_community)
    void publishingCommunity() {
        mRecordType = 1;
        getData(true);
    }

    // 新增小区
    @Click(R.id.publishing_other)
    void addEstateFragment() {
        mRecordType = 2;
//		getAddEstate(true);
    }

    @AfterViews
    void refreshView() {
        mPublishingCommunity.setChecked(true);
        mPullRefreshListView.setVisibility(View.VISIBLE);
        mRecordNodata.setVisibility(View.GONE);
        mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                if (CheckDoubleClick.isFastDoubleClick())
                    return;
                intentToDetail(position);
            }

        });

        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                getData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadMore();
            }
        });

        mPullRefreshListView.setMode(Mode.BOTH);
        if (mAdapter != null) {
            mPullRefreshListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        showBottomOnHasMoreData();
        if (isFirst) {
            mRecordNodata.setVisibility(View.GONE);
            mPullRefreshListView.setVisibility(View.VISIBLE);
            loadData();
        }
    }

    /**
     * 首次加载数据
     */
    private void loadData() {
        getData(true);

        isFirst = false;

    }


    @Background
    void getData(boolean b) {
        showBottomOnHasMoreData();
        //	mReq = new MineRecordsRequest();

        int userId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
        if (releaseRecordType == Constants.RELEASE_RECORD_RENT) {  //出租
            mRentPublishRecordRequest.setUid(userId);
            mRentPublishRecordRequest.setStart(0);
            mRentPublishRecordRequest.setEnd(25);
        } else if (releaseRecordType == Constants.RELEASE_RECORD_SELL) { //出售
            mSellPublishRecordRequest.setUid(userId);
            mSellPublishRecordRequest.setStart(0);
            mSellPublishRecordRequest.setEnd(25);
        }
        try {
            setUnable(mPublishingOther);
            if (b) {
                if (releaseRecordType == Constants.RELEASE_RECORD_RENT) {
                    mPublishRecordResponse = mRentService.rentRecord(mRentPublishRecordRequest);
                } else if (releaseRecordType == Constants.RELEASE_RECORD_SELL) {
                    mPublishRecordResponse = mSellService.sellRecord(mSellPublishRecordRequest);
                }
            } else {
                if (releaseRecordType == Constants.RELEASE_RECORD_RENT) {
                    mPublishRecordResponse = mRentService.rentRecord2(mRentPublishRecordRequest);
                } else if (releaseRecordType == Constants.RELEASE_RECORD_SELL) {
                    mPublishRecordResponse = mSellService.sellRecord2(mSellPublishRecordRequest);
                }
            }
            if (mPublishRecordResponse == null || mPublishRecordResponse.getResult() == null || mPublishRecordResponse.getResult().toArray().length == 0) {
                noData();
            } else {
                hasData();
            }
            upData();
            setEnable(mPublishingOther);
        } catch (RestException e) {
            finishLoading();
            throw e;
        } catch (Exception e) {
            finishLoading();
            throw e;
        }
    }

    @UiThread
    public void setUnable(RadioButton radioButton) {
        radioButton.setEnabled(false);
    }

    @UiThread
    public void setEnable(RadioButton radioButton) {
        radioButton.setEnabled(true);
    }

    View preView = null;

    @Click(R.id.recordclear)
    void clear() {
        Dialog alertDialog = new AlertDialog.Builder(getActivity()).setMessage("清空后，您只能通过联系客服才能重新显示审核记录，确认清空吗?")
                .setPositiveButton("清空", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearRecords();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        alertDialog.show();
    }

    private boolean isDataEnd = false;


    // 发布记录加载更多
    @Background
    public void loadMore() {
        if (!isDataEnd) {
            if (mPublishRecordResponse == null) {
                showBottomOnDataEnd();
                finishLoading();
                return;
            }


            try {


                List<RentReleaseResponse> list = null;
                int lengh = mPublishRecordResponse.getResult().size();
                int userId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);

                if (releaseRecordType == Constants.RELEASE_RECORD_RENT) {
                    mRentPublishRecordRequest.setStart(lengh);
                    mRentPublishRecordRequest.setEnd(12);
                    mRentPublishRecordRequest.setUid(userId);
                    list = mRentService.rentRecord2(mRentPublishRecordRequest).getResult();
                } else if (releaseRecordType == Constants.RELEASE_RECORD_SELL) {
                    mSellPublishRecordRequest.setUid(userId);
                    mSellPublishRecordRequest.setStart(lengh);
                    mSellPublishRecordRequest.setEnd(12);
                    list = mSellService.sellRecord2(mSellPublishRecordRequest).getResult();
                }


                mPublishRecordResponse.getResult().addAll(list);
                if (list.size() == 0 || list.size() < 12) {
                    showBottomOnDataEnd();
                }

                if (mPublishRecordResponse == null || mPublishRecordResponse.getResult().toArray().length == 0) {
                    noData();
                }
                upData();

            } catch (Exception e) {
                finishLoading();
                throw e;
            }

        }
    }


    @Background
    void clearRecords() {
        MineRecordsRequest req = new MineRecordsRequest();
        int userId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
        req.setUserId(userId);
        mSourceLogService.mineRecordclear(req);
        remove();
    }

    @Click(R.id.mrecordback)
    void mRecordBack() {
        remove();
    }

    @UiThread
    public void upData() {
        if (getActivity() == null || getActivity().isFinishing() || mPublishRecordResponse == null) {
            return;
        }
        mPullRefreshListView.setVisibility(View.VISIBLE);
        notifySelected();
        if (mAdapter == null) {
            mAdapter = new Adapter((LayoutInflater) getBackOpActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), mPublishRecordResponse);
            mPullRefreshListView.setAdapter(mAdapter);
        } else {
            mAdapter.res = mPublishRecordResponse;
            mAdapter.notifyDataSetChanged();
            mPullRefreshListView.onRefreshComplete();
        }
    }

    @UiThread
    public void finishLoading() {
        mPullRefreshListView.onRefreshComplete();
    }

    @SuppressLint("SimpleDateFormat")
    public class Adapter extends BaseAdapter {
        RentPublishRecordResponse res = null;
        private LayoutInflater mInflater;

        public Adapter(LayoutInflater mInflater, RentPublishRecordResponse res) {
            this.res = res;
            this.mInflater = mInflater;
        }

        private int mResource = R.layout.item_mine_realse_record;

        @Override
        public int getCount() {
            return res.getResult().size();
        }

        @Override
        public Object getItem(int position) {
            return res.getResult().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint({"ResourceAsColor", "InlinedApi"})
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view;

            if (convertView == null) {
                view = mInflater.inflate(mResource, parent, false);

            } else
                view = convertView;

            TextView estateName = (TextView) view.findViewById(R.id.estateName);
            TextView sourceState = (TextView) view.findViewById(R.id.sourceState);
            TextView typeName = (TextView) view.findViewById(R.id.typeName);
            TextView publishDate = (TextView) view.findViewById(R.id.publishDate);
            String estateNametext;
            RentReleaseResponse o = (RentReleaseResponse) getItem(position);

            if (o.getEstateName().length() > 8) {
                String string = o.getEstateName();
                estateNametext = string.substring(0, 8) + "...";
            } else {

                estateNametext = o.getEstateName();
            }

            String Building = o.getBuildingNameStr();

            if (mRecordType == 1) {
                if (o.getSubEstateName() == null) {
                    if (("").equals(o.getBuildingNameStr())) {
                        estateName.setText(estateNametext);
                    } else {
                        estateName.setText(estateNametext + " " + "・" + Building);
                    }

                } else {
                    if (("").equals(o.getBuildingNameStr())) {
                        estateName.setText(estateNametext + o.getSubEstateName());
                    } else {
                        estateName.setText(estateNametext + o.getSubEstateName() + " " + "・" + Building);
                    }

                }
                typeName.setText(o.getTypeName());

                if (o.getPublishDate() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String date = sdf.format(o.getPublishDate());
                    publishDate.setText(date + " ");
                }

                if (o.getSourceState() == 1) {
                    sourceState.setTextColor(Color.parseColor("#12c1c4"));
                    sourceState.setText(o.getSourceStateStr());

                } else if (o.getSourceState() == 2) {
                    sourceState.setTextColor(Color.parseColor("#7f7f7f"));
                    sourceState.setText(o.getSourceStateStr());
                } else if (o.getSourceState() == 3) {
                    sourceState.invalidate();
                    sourceState.setTextColor(Color.parseColor("#FF4404"));
                    sourceState.setText(o.getSourceStateStr());
                }

            }
            int le = 0;

            le = typeName.getText().toString().length();

            if (o.getHot() == 1) {
                SpannableStringBuilder builder = new SpannableStringBuilder(typeName.getText().toString() + "    [热点小区]");
                ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor("#ff5104"));
                builder.setSpan(redSpan, le, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                typeName.setText(builder);//
            }
            return view;
        }
    }

    @UiThread
    public void showBottomOnHasMoreData() {
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多！");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.pull_to_refresh_refreshing_label));
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松手加载！");
    }

    @UiThread
    public void showBottomOnDataEnd() {
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("没有更多数据！");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("没有更多数据！");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("没有更多数据！");
    }

    /**
     * 跳转详情
     *
     * @param position
     */
    private void intentToDetail(int position) {
        int state = mPublishRecordResponse.getResult().get(position - 1).getSourceState();
        if (state == 1 || state == 2 || state == 3) {
            ReleasedRecordInfoFragment releasedRentRecordInfoFragment = GeneratedClassUtils.getInstance(ReleasedRecordInfoFragment.class);
            Bundle args = new Bundle();
            if (releaseRecordType == Constants.RELEASE_RECORD_SELL) { //出售
                args.putString("releaseTypeId", Constants.RECORD_INFO_SELL);
            } else if (releaseRecordType == Constants.RELEASE_RECORD_RENT) { //出租
                args.putString("releaseTypeId", Constants.RECORD_INFO_RENT);
            }
            args.putInt("estateId", mPublishRecordResponse.getResult().get(position - 1).getEstateId());
            args.putInt("historyId", mPublishRecordResponse.getResult().get(position - 1).getHistoryId());
            args.putInt("houseId", mPublishRecordResponse.getResult().get(position - 1).getHouseId());
            args.putInt("typeId", mPublishRecordResponse.getResult().get(position - 1).getTypeId());
            args.putString("typeName", mPublishRecordResponse.getResult().get(position - 1).getTypeName());
            args.putInt("SourceState", mPublishRecordResponse.getResult().get(position - 1).getSourceState());
            releasedRentRecordInfoFragment.setArguments(args);
            releasedRentRecordInfoFragment.setContainerId(R.id.main_container);
            releasedRentRecordInfoFragment.tag = ReleasedRecordInfoFragment.class.getName();
            releasedRentRecordInfoFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
                    R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);
            releasedRentRecordInfoFragment.setContainerId(R.id.main_container);
            releasedRentRecordInfoFragment.setManager(getFragmentManager());
            releasedRentRecordInfoFragment.show(SHOW_ADD_HIDE);
        }
    }

    public void notifySelected() {
        if (getSelectListener() != null)
            getSelectListener().onSelected(null);
    }

    @UiThread
    void showState(int state) {
        if (state == 2) {
            DialogBuilder.showSimpleDialog("该房源还在审核中", getBackOpActivity());

        } else if (state == 3) {
            DialogBuilder.showSimpleDialog("审核未通过不能查看", getBackOpActivity());
        }
    }

    @UiThread
    void noData() {
        mPullRefreshListView.setVisibility(View.GONE);
        mRecordNodata.setVisibility(View.VISIBLE);
    }

    @UiThread
    void hasData() {
        mPullRefreshListView.setVisibility(View.VISIBLE);
        mRecordNodata.setVisibility(View.GONE);
    }

    @UiThread
    void showError(String e) {
        DialogBuilder.showSimpleDialog(e, getBackOpActivity());
    }

    @UiThread
    public void restErrorProcess(String detailMessage, int errorCode) {
        if (errorCode == RestException.INVALID_TOKEN) {
            DialogBuilder.showSimpleDialog(detailMessage, getActivity());
        } else {
            DialogBuilder.showSimpleDialog(detailMessage, getActivity());
        }
    }

    @UiThread
    public void unexpectedErrorProcess(String detailMessage) {
        DialogBuilder.showSimpleDialog(detailMessage, getActivity());
    }

    @Click(R.id.goto_review_content)
    public void gotoReviewContent() {
        CommonProblemFragment commonProblemFragment = GeneratedClassUtils.getInstance(CommonProblemFragment.class);
        Bundle argsBundle = new Bundle();
        argsBundle.putBoolean("isFromReviewRule", true);
        commonProblemFragment.setArguments(argsBundle);
        commonProblemFragment.tag = CommonProblemFragment.class.getName();
        commonProblemFragment.setManager(getFragmentManager());
        commonProblemFragment.setContainerId(R.id.main_container);
        commonProblemFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);

        commonProblemFragment.show(SHOW_ADD_HIDE);
    }
}
