package phanloi.horizontalsnaprecyclerviewsample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Copyright (c) 2017, VNG Corp. All rights reserved.
 *
 * @author Lio <loipn@vng.com.vn>
 * @version 1.0
 * @since July 30, 2017
 */

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.BookViewHolder> {

    private String mStringSample;
    private ItemWidth mItemWidth;
    private int mCellWidthMedium;
    private int mCellWidthSmall;

    public SimpleAdapter(String stringSample, ItemWidth itemWidth, int cellWidthMedium, int cellWidthSmall) {
        mStringSample = stringSample;
        mItemWidth = itemWidth;
        mCellWidthMedium = cellWidthMedium;
        mCellWidthSmall = cellWidthSmall;
    }

    public void setItemWidth(ItemWidth itemWidth) {
        mItemWidth = itemWidth;
        notifyDataSetChanged();
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        holder.mTextView.setText(position + "\n" + mStringSample);
        if (mItemWidth == ItemWidth.MATCH_PARENT) {
            ViewGroup.LayoutParams layoutParams = holder.mRootView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            holder.mRootView.setLayoutParams(layoutParams);
        } else if (mItemWidth == ItemWidth.MEDIUM) {
            ViewGroup.LayoutParams layoutParams = holder.mRootView.getLayoutParams();
            layoutParams.width = mCellWidthMedium;
            holder.mRootView.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = holder.mRootView.getLayoutParams();
            layoutParams.width = mCellWidthSmall;
            holder.mRootView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rootView)
        LinearLayout mRootView;
        @BindView(R.id.textView)
        TextView mTextView;

        BookViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
