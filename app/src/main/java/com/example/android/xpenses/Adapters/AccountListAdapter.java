package com.example.android.xpenses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.xpenses.R;

import java.util.ArrayList;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.AccountViewHolder> {

    ArrayList<String> list;
    private onItemCLickListener listener;

    public interface onItemCLickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemCLickListener(onItemCLickListener listener){
        this.listener = listener;
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public ImageView deleteBtn;
        public ImageView editBtn;

        public AccountViewHolder(@NonNull View itemView, final onItemCLickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.account_tv);
            deleteBtn = itemView.findViewById(R.id.delete_btn);
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
            });
        }
    }

    public AccountListAdapter(ArrayList<String> listItems){
        this.list = listItems;
    }

    @NonNull
    @Override
    public AccountListAdapter.AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view= layoutInflater.inflate(R.layout.account_list_item, parent, false);
        AccountViewHolder viewHolder = new AccountViewHolder(view, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        holder.title.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}