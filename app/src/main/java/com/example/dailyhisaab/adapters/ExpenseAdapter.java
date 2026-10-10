package com.example.dailyhisaab.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dailyhisaab.R;
import com.example.dailyhisaab.models.Expense;
import com.example.dailyhisaab.utils.DateUtils;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private final Context context;
    private List<Expense> expenseList;
    private final boolean showActions;
    private final OnExpenseClickListener listener;

    public interface OnExpenseClickListener {
        void onEditClick(Expense expense);
        void onDeleteClick(Expense expense, int position);
    }

    public ExpenseAdapter(Context context, List<Expense> expenseList, boolean showActions, OnExpenseClickListener listener) {
        this.context = context;
        this.expenseList = expenseList;
        this.showActions = showActions;
        this.listener = listener;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenseList = expenses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.tvItemTitle.setText(expense.getTitle());
        holder.tvItemAmount.setText(String.format(Locale.getDefault(), "₹%.2f", expense.getAmount()));
        holder.tvItemCategory.setText(expense.getCategory());
        holder.tvItemDate.setText(DateUtils.formatForDisplay(expense.getDate()));

        if (showActions) {
            holder.layoutActions.setVisibility(View.VISIBLE);
            holder.btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(expense);
                }
            });
            holder.btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(expense, holder.getAdapterPosition());
                }
            });
        } else {
            holder.layoutActions.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return expenseList != null ? expenseList.size() : 0;
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemTitle, tvItemAmount, tvItemCategory, tvItemDate;
        LinearLayout layoutActions;
        ImageButton btnEdit, btnDelete;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.tvItemTitle);
            tvItemAmount = itemView.findViewById(R.id.tvItemAmount);
            tvItemCategory = itemView.findViewById(R.id.tvItemCategory);
            tvItemDate = itemView.findViewById(R.id.tvItemDate);
            layoutActions = itemView.findViewById(R.id.layoutActions);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}