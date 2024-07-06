package com.example.prm392_fe.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalMarginItemDecoration extends RecyclerView.ItemDecoration{
    private Context context;
    private int horizontalMarginInPx;
    public HorizontalMarginItemDecoration(Context context, int horizontalMarginInPx) {
        this.context = context;
        this.horizontalMarginInPx = horizontalMarginInPx;
    }
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.right = horizontalMarginInPx;
        outRect.left = horizontalMarginInPx;
    }
}
