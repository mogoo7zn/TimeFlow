package cn.edu.ustc.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflow.R;

import java.util.List;

import cn.edu.ustc.timeflow.bean.Action;
import cn.edu.ustc.timeflow.restriction.TimeRestriction;

public class FragmentGoalOverviewRecyclerItemAdapter extends RecyclerView.Adapter<FragmentGoalOverviewRecyclerItemAdapter.ViewHolder> {
    public List<Action> actionList;
    private Context context;

    public FragmentGoalOverviewRecyclerItemAdapter(Context context, List<Action> actionList) {
        this.context = context;
        this.actionList = actionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_goal_overview_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Action action = actionList.get(position);
        holder.actionName.setText(action.getName());
        holder.actionDescription.setText(action.getNote());
        if(action.getRestriction("TimeRestriction") != null) {
            TimeRestriction timeRestriction = (TimeRestriction) action.getRestriction("TimeRestriction");
            holder.actionStartTime.setText(timeRestriction.getStart().toString());
            holder.actionEndTime.setText(timeRestriction.getEnd().toString());
        }
        else {
            holder.actionStartTime.setText("");
            holder.actionEndTime.setText("");
        }

        holder.actionPlace.setText(action.getLocation());
    }

    @Override
    public int getItemCount() {
        return actionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView actionName, actionDescription, actionStartTime, actionEndTime, actionPlace;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            actionName = itemView.findViewById(R.id.action_name);
            actionDescription = itemView.findViewById(R.id.action_description);
            actionStartTime = itemView.findViewById(R.id.action_start_time);
            actionEndTime = itemView.findViewById(R.id.action_end_time);
            actionPlace = itemView.findViewById(R.id.action_place);
        }
    }
}