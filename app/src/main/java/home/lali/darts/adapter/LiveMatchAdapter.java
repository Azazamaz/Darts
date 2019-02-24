package home.lali.darts.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import home.lali.darts.R;
import home.lali.darts.model.OnlineMatches;

public class LiveMatchAdapter extends RecyclerView.Adapter<LiveMatchAdapter.ViewHolder> {

    private List<OnlineMatches> liveScoreList;

    public LiveMatchAdapter(List<OnlineMatches> liveScoreList) {
        this.liveScoreList = liveScoreList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.livescore_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        holder.name1.setText(liveScoreList.get(pos).getName1());
        holder.leg1.setText(String.valueOf(liveScoreList.get(pos).getLegW_1()));
        holder.score1.setText(String.valueOf(liveScoreList.get(pos).getScore1()));
        holder.name2.setText(liveScoreList.get(pos).getName2());
        holder.leg2.setText(String.valueOf(liveScoreList.get(pos).getLegW_2()));
        holder.score2.setText(String.valueOf(liveScoreList.get(pos).getScore2()));
    }

    @Override
    public int getItemCount() {
        return liveScoreList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        TextView name1;
        TextView leg1;
        TextView score1;
        TextView name2;
        TextView leg2;
        TextView score2;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            name1 = view.findViewById(R.id.liveName1_tv);
            leg1 = view.findViewById(R.id.liveLeg1_tv);
            score1 = view.findViewById(R.id.liveScore1_tv);
            name2 = view.findViewById(R.id.liveName2_tv);
            leg2 = view.findViewById(R.id.liveLeg2_tv);
            score2 = view.findViewById(R.id.liveScore2_tv);
        }
    }
}
