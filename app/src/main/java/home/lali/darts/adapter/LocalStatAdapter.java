package home.lali.darts.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import home.lali.darts.R;
import home.lali.darts.model.OnlineMatches;

public class LocalStatAdapter extends RecyclerView.Adapter<LocalStatAdapter.ViewHolder> {

    private List<OnlineMatches> localMatchList;

    public LocalStatAdapter(List<OnlineMatches> localMatchList) {
        this.localMatchList = localMatchList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.localstatview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        holder.player1.setText(localMatchList.get(pos).getName1());
        holder.p1LegW.setText(String.valueOf(localMatchList.get(pos).getLegW_1()));
        holder.p1Avg.setText(String.valueOf(localMatchList.get(pos).getAvg_1()));
        holder.player2.setText(localMatchList.get(pos).getName2());
        holder.p2LegW.setText(String.valueOf(localMatchList.get(pos).getLegW_2()));
        holder.p2Avg.setText(String.valueOf(localMatchList.get(pos).getAvg_2()));
    }

    @Override
    public int getItemCount() {
        return localMatchList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View view;

        TextView player1;
        TextView p1LegW;
        TextView p1Avg;
        TextView player2;
        TextView p2LegW;
        TextView p2Avg;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            player1 = view.findViewById(R.id.statP1Name_tv);
            p1LegW = view.findViewById(R.id.statP1Leg_tv);
            p1Avg = view.findViewById(R.id.statP1Avg_tv);
            player2 = view.findViewById(R.id.statP2Name_tv);
            p2LegW = view.findViewById(R.id.statP2Leg_tv);
            p2Avg = view.findViewById(R.id.statP2Avg_tv);
        }
    }
}
