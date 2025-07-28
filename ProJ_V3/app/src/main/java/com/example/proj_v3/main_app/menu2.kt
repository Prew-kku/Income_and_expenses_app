package com.example.proj_v3.main_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Transaction
import com.example.proj_v3.Add_and_Subtract.add_money
import com.example.proj_v3.Add_and_Subtract.reduce_money
import com.example.proj_v3.R
import com.example.proj_v3.data.AppDatabase
import com.example.proj_v3.data.TransactionAdapter
import com.example.proj_v3.data.TransactionDao
import com.example.proj_v3.data.Transaction2
import com.example.proj_v3.data.Transaction2Dao
import com.example.proj_v3.setting.Setting
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class menu2 : AppCompatActivity() {
    private lateinit var btnAdd: FloatingActionButton
    private var btnHome: Button? = null
    private var btnSettings: Button? = null
    private var btn_in: Button? = null
    private var btn_out: Button? = null

    private var txtBalance: TextView? = null
    private lateinit var transaction2Dao: Transaction2Dao

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter

    private lateinit var db: AppDatabase
    private lateinit var transactionDao: TransactionDao
    private lateinit var pieChart: PieChart


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu2)
        init()
        loadBalance()


        btn_in?.setOnClickListener {
            //val intent = Intent(this,menu1::class.java)
            //startActivity(intent)

            val userId = intent.getIntExtra("userId", 0)  // รับ userId จาก Intent ก่อนหน้านี้
            if (userId != 0) {
                val intent = Intent(this@menu2, menu1::class.java)
                intent.putExtra("userId", userId)  // ส่ง userId ไปหน้า menu1
                startActivity(intent)
            } else {
                Toast.makeText(this@menu2, "User ID not found", Toast.LENGTH_SHORT).show()
            }
        }
        btn_out?.setOnClickListener {
            loadTransactions()
            loadPieChartData()
            loadBalance()
        }


        btnAdd.setOnClickListener { view ->
            Snackbar.make(view, "Add new item", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.btnAdd).show()
            //val intent = Intent(this, reduce_money::class.java)
            //startActivity(intent)

            val userId = intent.getIntExtra("userId", 0)  // รับ userId จาก Intent ก่อนหน้านี้
            if (userId != 0) {
                val intent = Intent(this@menu2, reduce_money::class.java)
                intent.putExtra("userId", userId)  // ส่ง userId ไปหน้า reduce_money
                startActivity(intent)
            } else {
                Toast.makeText(this@menu2, "User ID not found", Toast.LENGTH_SHORT).show()
            }
        }

        btnHome?.setOnClickListener { view ->
            Snackbar.make(view, "Your in Home Now", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.btnAdd).show()
        }

        btnSettings?.setOnClickListener { view ->
            Snackbar.make(view, "Go Settings", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.btnAdd).show()
            val userId = intent.getIntExtra("userId", 0)  // รับ userId จาก Intent ก่อนหน้านี้
            if (userId != 0) {
                val intent = Intent(this@menu2, Setting::class.java)
                intent.putExtra("userId", userId)  // ส่ง userId ไปหน้า Setting
                startActivity(intent)
            } else {
                Toast.makeText(this@menu2, "User ID not found", Toast.LENGTH_SHORT).show()
            }
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        db = AppDatabase.getDatabase(this)
        transactionDao = db.transactionDao()

        loadTransactions()
        loadPieChartData()

    }

    private fun loadPieChartData() {
        lifecycleScope.launch {
            val transactions = transactionDao.getAllTransactions()

            val categoryMap = transactions.groupBy { it.category }
                .mapValues { entry -> entry.value.sumOf { it.amount.toDouble() } }

            val entries = ArrayList<PieEntry>()
            val colors = ArrayList<Int>()

            val categoryColors = mapOf(
                "อาหาร" to ContextCompat.getColor(this@menu2, R.color.pie_out_color1),
                "เดินทาง" to ContextCompat.getColor(this@menu2, R.color.pie_out_color2),
                "บันเทิง" to ContextCompat.getColor(this@menu2, R.color.pie_out_color3),
                "อื่นๆ" to ContextCompat.getColor(this@menu2, R.color.pie_out_color4),
            )

            categoryMap.forEach { (category, amount) ->
                entries.add(PieEntry(amount.toFloat(), category))
                colors.add(categoryColors[category] ?: ContextCompat.getColor(this@menu2, R.color.grey))
            }

            val dataSet = PieDataSet(entries, "รายรับ-รายจ่าย")
            dataSet.colors = colors
            dataSet.valueTextSize = 14f

            val pieData = PieData(dataSet)
            pieChart.data = pieData
            pieChart.description.isEnabled = false
            pieChart.invalidate()
        }
    }

    private fun loadTransactions() {
        lifecycleScope.launch {
            val transactions = transactionDao.getAllTransactions()
            adapter = TransactionAdapter(transactions, this@menu2::editTransaction, this@menu2::deleteTransaction)
            recyclerView.adapter = adapter
        }
    }

    private fun editTransaction(transaction: com.example.proj_v3.data.Transaction) {
        // Handle editing here. You may want to start an Activity that allows editing the details.
    }

    private fun deleteTransaction(transaction: com.example.proj_v3.data.Transaction) {
        lifecycleScope.launch {
            // Assume you have a method to delete transaction
            transactionDao.deleteTransaction(transaction)
            loadTransactions()  // Refresh the list
            loadPieChartData()
        }
    }
    private fun loadBalance() {
        lifecycleScope.launch {
            val totalExpense = transactionDao.getTotalIncome() ?: 0f
            val totalIncome = transaction2Dao.getTotalExpense() ?: 0f
            val balance = totalIncome - totalExpense

            runOnUiThread {
                txtBalance?.text = "ยอดเงินคงเหลือ: %.2f บาท".format(balance)
            }
        }
    }



    private fun init() {
        db = AppDatabase.getDatabase(this)
        transactionDao = db.transactionDao()
        transaction2Dao = db.transaction2Dao()
        btnAdd = findViewById(R.id.btnAdd)
        btnHome = findViewById(R.id.btnHome)
        btnSettings = findViewById(R.id.btnSettings)
        btn_in = findViewById(R.id.btn_in)
        btn_out = findViewById(R.id.btn_out)
        pieChart = findViewById(R.id.pieChart)
        txtBalance = findViewById(R.id.txtBalance)
    }




}