package com.example.android.xpenses.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.xpenses.DataTypes.BudgetObject;
import com.example.android.xpenses.R;

import java.util.ArrayList;

public class BudgetHistoryAdapter extends RecyclerView.Adapter<BudgetHistoryAdapter.BudgetViewHolder> {
    ArrayList<BudgetObject> list;
    private onItemCLickListener listener;

    public interface onItemCLickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemCLickListener(onItemCLickListener listener){
        this.listener = listener;
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder{

        public TextView amount;
        public TextView fund;
        public TextView startDate;
        public TextView endDate;

        public ImageView editBudget, deleteBudget;

        public BudgetViewHolder(@NonNull View itemView, final onItemCLickListener listener) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            fund = itemView.findViewById(R.id.fund);
            startDate = itemView.findViewById(R.id.start_date);
            endDate = itemView.findViewById(R.id.end_date);
            editBudget = itemView.findViewById(R.id.edit_budget);
            deleteBudget = itemView.findViewById(R.id.delete_budget);

            editBudget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onEditClick(position);
                        }
                    }
                }
            });

            deleteBudget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public BudgetHistoryAdapter(ArrayList<BudgetObject> listItems){
        this.list = listItems;
    }

    @NonNull
    @Override
    public BudgetHistoryAdapter.BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view= layoutInflater.inflate(R.layout.budget_list_item, parent, false);
        BudgetViewHolder viewHolder = new BudgetViewHolder(view, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        holder.amount.setText(list.get(position).getAmount());
        holder.fund.setText(list.get(position).getFund());
        holder.startDate.setText(list.get(position).getStartDate());
        holder.endDate.setText(list.get(position).getEndDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
