package home.lali.darts.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import home.lali.darts.R;

public class RecyclerItemDecorator extends RecyclerView.ItemDecoration {

    private final int decorationHeight;
    private Context ctx;

    public RecyclerItemDecorator(Context context) {
        this.ctx = context;
        decorationHeight = ctx.getResources().getDimensionPixelOffset(R.dimen.height);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent != null && view != null) {
            int itemPos = parent.getChildAdapterPosition(view);
            int count = parent.getAdapter().getItemCount();

            if (itemPos >= 0 && itemPos < count - 1) {
                outRect.bottom = decorationHeight;
            }
        }
    }
}
