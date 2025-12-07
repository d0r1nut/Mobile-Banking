package com.d0r1nut.mobilebankingproject.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.d0r1nut.mobilebankingproject.R;
import com.d0r1nut.mobilebankingproject.Models.Transaction;
import com.d0r1nut.mobilebankingproject.Models.TransactionManager;
import com.d0r1nut.mobilebankingproject.Services.FirebaseService;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactionList;
    private OnTransactionClickListener onTransactionClickListener;
    private FirebaseService fbDatabase;


    public interface OnTransactionClickListener {
        void onTransactionClick(Transaction Transaction);
    }
    public TransactionAdapter(List<Transaction> transactionList, OnTransactionClickListener listener){
        this.transactionList = transactionList;
        this.onTransactionClickListener = listener;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.tvTransactionType.setText(transaction.getTransactionType().toString());
        holder.tvTransactionAmount.setText(String.format(Locale.US, "%.2f", transaction.getAmount()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.tvTransactionDate.setText(dateFormat.format(transaction.getDate()));

        holder.btnDelete.setOnClickListener(v -> {
            fbDatabase = new FirebaseService();
            fbDatabase.deleteTransaction(transaction);

            TransactionManager.getInstance().deleteTransaction(transaction);

            transactionList.remove(transaction);

            notifyItemRemoved(position);
        });

        holder.itemView.setOnClickListener(v -> {
            onTransactionClickListener.onTransactionClick(transaction);
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public void updateTransaction(List<Transaction> newTransactionList){
        this.transactionList.clear();
        this.transactionList.addAll(newTransactionList);
        notifyDataSetChanged();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder{
        TextView tvTransactionType, tvTransactionAmount, tvTransactionDate;
        Button btnDelete;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTransactionType = itemView.findViewById(R.id.tv_transaction_type);
            tvTransactionAmount = itemView.findViewById(R.id.tv_transaction_amount);
            tvTransactionDate = itemView.findViewById(R.id.tv_transaction_date);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
