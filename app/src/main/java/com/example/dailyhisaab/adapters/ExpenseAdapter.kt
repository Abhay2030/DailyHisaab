package com.example.dailyhisaab.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyhisaab.R
import com.example.dailyhisaab.models.Expense
import com.example.dailyhisaab.utils.DateUtils
import com.example.dailyhisaab.utils.FormatUtils

class ExpenseAdapter(
    private val context: Context,
    var expenses: MutableList<Expense>?,
    private val showActions: Boolean,
    private val listener: OnExpenseClickListener?
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    interface OnExpenseClickListener {
        fun onEditClick(expense: Expense)
        fun onDeleteClick(expense: Expense, position: Int)
    }

    fun setExpensesList(newExpenses: List<Expense>) {
        this.expenses = newExpenses.toMutableList()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        val list = expenses
        if (list != null && position in list.indices) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses!![position]
        holder.tvItemTitle.text = expense.title
        holder.tvItemAmount.text = FormatUtils.formatRupee(expense.amount)

        val payMethod = expense.paymentMethod
        if (payMethod.isNotBlank()) {
            holder.tvItemCategory.text = "${expense.category} • $payMethod"
        } else {
            holder.tvItemCategory.text = expense.category
        }

        holder.tvItemDate.text = DateUtils.formatForDisplay(expense.date)

        if (showActions) {
            holder.layoutActions.visibility = View.VISIBLE
            holder.btnEdit.setOnClickListener { listener?.onEditClick(expense) }
            holder.btnDelete.setOnClickListener { listener?.onDeleteClick(expense, holder.adapterPosition) }
        } else {
            holder.layoutActions.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return expenses?.size ?: 0
    }

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItemTitle: TextView = itemView.findViewById(R.id.tvItemTitle)
        val tvItemAmount: TextView = itemView.findViewById(R.id.tvItemAmount)
        val tvItemCategory: TextView = itemView.findViewById(R.id.tvItemCategory)
        val tvItemDate: TextView = itemView.findViewById(R.id.tvItemDate)
        val layoutActions: LinearLayout = itemView.findViewById(R.id.layoutActions)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }
}
