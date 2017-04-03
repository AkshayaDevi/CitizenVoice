package com.feedback.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feedback.Model.FeedbackResponse;
import com.feedback.R;
import com.feedback.Service.FeedbackService;
import com.feedback.Session.SessionLogin;
import com.feedback.Utils.Utils;

import java.util.List;

import static com.feedback.Utils.Constants_app.dateTimeFormat;



public class AdapterPost extends RecyclerView.Adapter<AdapterPost.PostViewHolder> {

    List<FeedbackResponse> feedbackResponseList;
    FeedBackPostListener feedBackPostListener;
    private Context mContext;


    public interface FeedBackPostListener {
        void actionClick(FeedbackResponse feedbackResponse);

        void rejectedClick(FeedbackResponse feedbackResponse);

        void onItemClick(FeedbackResponse feedbackResponse);
    }

    public AdapterPost(List<FeedbackResponse> feedbackResponseList, FeedBackPostListener feedBackPostListener) {
        this.feedBackPostListener = feedBackPostListener;
        this.feedbackResponseList = feedbackResponseList;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, final int position) {
        final FeedbackResponse feedbackResponse = feedbackResponseList.get(position);
        String desc = feedbackResponse.getDescription();
        String image = FeedbackService.BASE_URL_IMAGE + feedbackResponse.getImage();
        holder.tvDesc.setText(desc);
        Glide.with(mContext).load(image).placeholder(R.drawable.ic_place_holder).into(holder.ivPost);

        holder.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedBackPostListener.actionClick(feedbackResponseList.get(position));
            }
        });

        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedBackPostListener.rejectedClick(feedbackResponseList.get(position));
            }
        });


        if (SessionLogin.getUsertype().equals("1")) {
            holder.btnReject.setVisibility(View.GONE);
            holder.btnAction.setVisibility(View.GONE);
        } else {
            if (feedbackResponse.getAction_by() == null) {
                holder.btnReject.setVisibility(View.VISIBLE);
                holder.btnAction.setVisibility(View.VISIBLE);
            } else {
                holder.btnReject.setVisibility(View.GONE);
                holder.btnAction.setVisibility(View.GONE);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedBackPostListener.onItemClick(feedbackResponseList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return feedbackResponseList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPost;
        TextView tvDesc;
        Button btnAction, btnReject;

        public PostViewHolder(View itemView) {
            super(itemView);
            ivPost = (ImageView) itemView.findViewById(R.id.iv_post);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
            btnAction = (Button) itemView.findViewById(R.id.btn_take_action);
            btnReject = (Button) itemView.findViewById(R.id.btn_reject);

        }
    }
}
