package com.feedback.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.feedback.Activities.ActivityPostDetails;
import com.feedback.Adapters.AdapterPost;
import com.feedback.Model.FeedbackResponse;
import com.feedback.Presenter.FeedbackPresenter;
import com.feedback.R;
import com.feedback.Session.SessionLogin;
import com.feedback.Utils.Constants_app;
import com.feedback.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.feedback.Utils.Constants_app.dateTimeFormat;


public class FragmentPosts extends BaseFragment implements FeedbackPresenter.FeedbackGetListener, AdapterPost.FeedBackPostListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private String from;

    public FragmentPosts() {
    }

    public static FragmentPosts newInstance(String from) {
        FragmentPosts fragment = new FragmentPosts();
        Bundle args = new Bundle();
        args.putString("from", from);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            from = getArguments().getString("from");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        getList();
    }

    private void initView() {
        try {
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(manager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onGetPosts(List<FeedbackResponse> list) {
        utils.hideProgress();
        List<FeedbackResponse> pendingList = new ArrayList<>();
        List<FeedbackResponse> actionList = new ArrayList<>();
        List<FeedbackResponse> rejectedList = new ArrayList<>();
        String today = Utils.getCurrentDateTime(dateTimeFormat);
        String userType = SessionLogin.getUsertype();
        String dep = SessionLogin.getUser().getDep();
        Log.e("userType", "-->" + userType);
        Log.e("dep", "-->" + dep);

        for (int i = 0; i < list.size(); i++) {
            FeedbackResponse response = list.get(i);
            if (response.getAction_by() == null) {
                if (userType.equals("3")) {
                    String postedDate = response.getPosted_date();
                    int daysCount = Utils.get_count_of_days(today, postedDate, dateTimeFormat);
                    if (daysCount >= 2)
                        pendingList.add(response);
                } else if (userType.equals("2")) {
                    String category = response.getCategory();
                    Log.e("category", "-->" + category);
                    if (dep.equals(category)) {
                        pendingList.add(response);
                    }
                } else
                    pendingList.add(response);
            } else if (response.getAction_by().equals("0")) {
                rejectedList.add(response);
            } else {
                actionList.add(response);
            }
        }

        AdapterPost adapterPost = new AdapterPost(actionList, this);
        if (from.equals("pending"))
            adapterPost = new AdapterPost(pendingList, this);
        else if (from.equals("rejected"))
            adapterPost = new AdapterPost(rejectedList, this);

        recyclerView.setAdapter(adapterPost);
    }

    @Override
    public void onGetFailure(String msg) {
        utils.hideProgress();
        Utils.alertBox(mActivity, msg);
    }

    @Override
    public void onActionTakenSuccess() {
        utils.hideProgress();
        showSuccess();
    }

    private void showSuccess() {
        new MaterialDialog.Builder(mActivity)
                .content("Success!")
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.black)
                .positiveColorRes(R.color.colorPrimaryDark)
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        FragmentHome fragmentHome = (FragmentHome) getParentFragment().getActivity().getSupportFragmentManager().findFragmentByTag("FragmentHome");
                        fragmentHome.refresh();
                    }
                })
                .show();
    }

    private void getList() {
        utils.showProgress();
        FeedbackPresenter feedbackPresenter = new FeedbackPresenter(null, this, mActivity);
        FeedbackResponse feedbackResponse = new FeedbackResponse();
        if (SessionLogin.getUsertype().equals("1"))
            feedbackResponse.setPosted_by(SessionLogin.getUserId());
        feedbackPresenter.get(feedbackResponse);
    }

    @Override
    public void actionClick(FeedbackResponse res) {
        utils.showProgress();
        FeedbackResponse feedbackResponse = new FeedbackResponse();
        feedbackResponse.setAction_by(SessionLogin.getUserId());
        feedbackResponse.setAction_date(Utils.getCurrentDateTime(Constants_app.dateTimeFormat));
        feedbackResponse.setRejected_by("0");
        feedbackResponse.setPost_id(res.getPost_id());
        FeedbackPresenter feedbackPresenter = new FeedbackPresenter(null, this, mActivity);
        feedbackPresenter.action(feedbackResponse);
    }

    @Override
    public void rejectedClick(FeedbackResponse res) {
        utils.showProgress();
        FeedbackResponse feedbackResponse = new FeedbackResponse();
        feedbackResponse.setAction_by("0");
        feedbackResponse.setAction_date(Utils.getCurrentDateTime(Constants_app.dateTimeFormat));
        feedbackResponse.setRejected_by(SessionLogin.getUserId());
        feedbackResponse.setPost_id(res.getPost_id());
        FeedbackPresenter feedbackPresenter = new FeedbackPresenter(null, this, mActivity);
        feedbackPresenter.action(feedbackResponse);
    }

    @Override
    public void onItemClick(FeedbackResponse feedbackResponse) {
        Intent intent = new Intent(mActivity, ActivityPostDetails.class);
        intent.putExtra("FeedbackResponse", feedbackResponse);
        startActivityForResult(intent, 100);
    }

}
