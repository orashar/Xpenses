package com.example.android.xpenses.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.xpenses.DataTypes.FundObject;
import com.example.android.xpenses.R;

import java.util.ArrayList;

public class FundListAdapter extends RecyclerView.Adapter<FundListAdapter.FundViewHolder> {

    ArrayList<FundObject> fundList;
    private FundListAdapter.onItemCLickListener listener;


    public interface onItemCLickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemCLickListener(FundListAdapter.onItemCLickListener listener){
        this.listener = listener;
    }

    public static class FundViewHolder extends RecyclerView.ViewHolder{

        public TextView title, account, green, red;
        /*public ImageView deleteBtn;
        public ImageView editBtn;*/

        public FundViewHolder(@NonNull View itemView, final onItemCLickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.fund_title_tv);
            account = itemView.findViewById(R.id.fund_account_tv);
            green = itemView.findViewById(R.id.fund_green_tv);
            red = itemView.findViewById(R.id.fund_red_tv);
            /*deleteBtn = itemView.findViewById(R.id.delete_btn);
            editBtn = itemView.findViewById(R.id.edit_btn);

            editBtn.setOnClickListener(new View.OnClickListener() {
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

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });*/
        }
    }

    public FundListAdapter(ArrayList<FundObject> fundList){
        this.fundList = fundList;
    }


    @NonNull
    @Override
    public FundListAdapter.FundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view= layoutInflater.inflate(R.layout.fund_list_item, parent, false);
        FundViewHolder viewHolder = new FundViewHolder(view, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FundListAdapter.FundViewHolder holder, int position) {
        holder.title.setText(fundList.get(position).getFund());
        holder.account.setText(fundList.get(position).getAccount());
        holder.green.setText(fundList.get(position).getBudget());
        holder.red.setText(fundList.get(position).getExpense());
    }

    @Override
    public int getItemCount() {
        return fundList.size();
    }

}
