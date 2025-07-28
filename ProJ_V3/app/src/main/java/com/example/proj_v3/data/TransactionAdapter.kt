package com.example.proj_v3.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proj_v3.R

class TransactionAdapter(
    private val transactions: List<Transaction>,
    private val onEditClick: (Transaction) -> Unit,
    private val onDeleteClick: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction, onEditClick, onDeleteClick)
    }

    override fun getItemCount(): Int = transactions.size

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtCategory: TextView = itemView.findViewById(R.id.txtCategory)
        private val txtAmount: TextView = itemView.findViewById(R.id.txtAmount)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(transaction: Transaction, onEditClick: (Transaction) -> Unit, onDeleteClick: (Transaction) -> Unit) {
            txtCategory.text = transaction.category
            txtAmount.text = transaction.amount.toString()
            btnDelete.setOnClickListener { onDeleteClick(transaction) }
        }
    }
}