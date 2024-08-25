package cn.edu.ustc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflow.R;

import java.util.List;

import cn.edu.ustc.timeflow.bean.Action;

public class FragmentGoalOverviewRecyclerItemAdapter extends RecyclerView.Adapter<FragmentGoalOverviewRecyclerItemAdapter.ViewHolder> {
    List<Action> actions;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_goal_overview_recycler_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.actionName.setText("Goal Name");   //TODO: connect database and get goal name
        holder.CardBackground.setImageResource(R.drawable.blue_label);  //TODO: 根据goal的标签设置背景颜色（用户自选）
//        holder.taskList.setAdapter(null);    //TODO: connect database and get task list
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView actionName;
        private ExpandableListView taskList;
        private ImageView CardBackground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            actionName = itemView.findViewById(R.id.action_name);
            taskList = itemView.findViewById(R.id.goal_overview_task_list);
            CardBackground = itemView.findViewById(R.id.goal_overview_recycler_view_item_card_background);
        }
    }
}
