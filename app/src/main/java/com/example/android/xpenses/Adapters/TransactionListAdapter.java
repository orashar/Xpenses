package com.example.android.xpenses.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.xpenses.DataTypes.TransactionObject;
import com.example.android.xpenses.R;

import java.util.ArrayList;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.TransactionViewHolder> {

    ArrayList<TransactionObject> transactionList;
    private TransactionListAdapter.onItemCLickListener listener;
    String whichActivity;


    public interface onItemCLickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemCLickListener(TransactionListAdapter.onItemCLickListener listener){
        this.listener = listener;
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder{

        public TextView title, amount, tag, date;
        /*public ImageView deleteBtn;
        public ImageView editBtn;*/

        public TransactionViewHolder(@NonNull View itemView, final onItemCLickListener listener) {
            super(itemView);

            amount = itemView.findViewById(R.id.one);
            title = itemView.findViewById(R.id.two);
            tag = itemView.findViewById(R.id.four);
            date = itemView.findViewById(R.id.three);
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

    public TransactionListAdapter(ArrayList<TransactionObject> transactionList, String whichActivity){
        this.transactionList = transactionList;
        this.whichActivity = whichActivity;
    }


    @NonNull
    @Override
    public TransactionListAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view= layoutInflater.inflate(R.layout.transaction_list_item_layout, parent, false);
        TransactionViewHolder viewHolder = new TransactionViewHolder(view, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionListAdapter.TransactionViewHolder holder, int position) {
        holder.title.setText(transactionList.get(position).getTransactionTitle());

        if(transactionList.get(position).getTransactionType().equals("E")) holder.amount.setTextColor(Color.RED);
        else holder.amount.setTextColor(Color.GREEN);

        holder.amount.setText(transactionList.get(position).getTransactionAmount());
        holder.tag.setText(transactionList.get(position).getTransactionTag());
        holder.date.setText(transactionList.get(position).getTransactionDate());
    }

    @Override
    public int getItemCount() {
        int superCount = transactionList.size();
        if(whichActivity.equals("home")){
            return Math.min(superCount, 3);
        } else{
            return superCount;
        }
    }

}
