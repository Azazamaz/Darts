package home.lali.darts.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import home.lali.darts.R;
import home.lali.darts.model.OnlineMatches;

public class OnlineMatchAdapter extends RecyclerView.Adapter<OnlineMatchAdapter.ViewHolder> {

    private List<OnlineMatches> matchList;

    public OnlineMatchAdapter(List<OnlineMatches> matchList) {
        this.matchList = matchList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.globalstat_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        holder.name1.setText(matchList.get(pos).getName1());
        holder.leg1.setText(String.valueOf(matchList.get(pos).getLegW_1()));
        holder.avg1.setText(String.valueOf(matchList.get(pos).getAvg_1()));
        holder.name2.setText(matchList.get(pos).getName2());
        holder.leg2.setText(String.valueOf(matchList.get(pos).getLegW_2()));
        holder.avg2.setText(String.valueOf(matchList.get(pos).getAvg_2()));
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;

        public TextView name1;
        public TextView leg1;
        public TextView avg1;
        public TextView name2;
        public TextView leg2;
        public TextView avg2;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            name1 = view.findViewById(R.id.globalName1_tv);
            leg1 = view.findViewById(R.id.globalLeg1_tv);
            avg1 = view.findViewById(R.id.globalAvg1_tv);
            name2 = view.findViewById(R.id.globalName2_tv);
            leg2 = view.findViewById(R.id.globalLeg2_tv);
            avg2 = view.findViewById(R.id.globalAvg2_tv);
        }

    }
}
