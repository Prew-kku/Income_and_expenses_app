package com.example.proj_v3.Add_and_Subtract

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.proj_v3.R
import com.example.proj_v3.data.AppDatabase
import com.example.proj_v3.data.Transaction
import com.example.proj_v3.data.TransactionDao
import com.example.proj_v3.main_app.menu1
import com.example.proj_v3.main_app.menu2
import com.example.proj_v3.setting.Setting
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class reduce_money : AppCompatActivity() {
    private var btn_add_money: Button? = null
    private var btn_reduce_money: Button? = null
    private var btnShowDialog: Button? = null
    private var btnHome: Button? = null
    private var btnSettings: Button? = null
    private var backhome: ImageButton? = null
    private var btncheck: ImageButton? = null
    private var editTextNumberDecimal: EditText? = null
    private var editTextText: EditText? = null
    private lateinit var btnAdd: FloatingActionButton
    private var selectedCategory: String? = null


    private lateinit var db: AppDatabase
    private lateinit var transactionDao: TransactionDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(com.example.proj_v3.R.layout.activity_reduce_money)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.example.proj_v3.R.id.activity_reduce_money)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()

        btn_add_money?.setOnClickListener { view ->
            Toast.makeText(
                this@reduce_money,
                "Your Go Back To add money Now",
                Toast.LENGTH_SHORT
            ).show()
            //val intent = Intent(this,add_money::class.java)
            //startActivity(intent)

            val userId = intent.getIntExtra("userId", 0)  // รับ userId จาก Intent ก่อนหน้านี้
            if (userId != 0) {
                val intent = Intent(this@reduce_money, add_money::class.java)
                intent.putExtra("userId", userId)  // ส่ง userId ไปหน้า menu1
                startActivity(intent)
            } else {
                Toast.makeText(this@reduce_money, "User ID not found", Toast.LENGTH_SHORT).show()
            }
        }
        btn_reduce_money?.setOnClickListener { view ->
            Toast.makeText(
                this@reduce_money,
                "Your in reduce money Now",
                Toast.LENGTH_SHORT
            ).show()
        }


        btnAdd.setOnClickListener { view ->
            Snackbar.make(view, "Add new item", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.btnAdd).show()
        }

        btnShowDialog?.setOnClickListener(View.OnClickListener {
            showBottomDialog() })

        backhome?.setOnClickListener { view ->
            Toast.makeText(
                this@reduce_money,
                "Your Go Back Home Now",
                Toast.LENGTH_SHORT
            ).show()
            //val intent = Intent(this,menu2::class.java)
            //startActivity(intent)

            val userId = intent.getIntExtra("userId", 0)  // รับ userId จาก Intent ก่อนหน้านี้
            if (userId != 0) {
                val intent = Intent(this@reduce_money, menu2::class.java)
                intent.putExtra("userId", userId)  // ส่ง userId ไปหน้า menu1
                startActivity(intent)
            } else {
                Toast.makeText(this@reduce_money, "User ID not found", Toast.LENGTH_SHORT).show()
            }
        }

        btncheck?.setOnClickListener {
            if (selectedCategory == null) {
                Toast.makeText(this, "กรุณาเลือกหมวดหมู่", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amountText = editTextNumberDecimal?.text.toString()
            val amount = amountText.toFloatOrNull()

            if (amount == null) {
                editTextNumberDecimal?.error = "กรุณากรอกจำนวนเงินที่ถูกต้อง"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                transactionDao.insertTransaction(Transaction(category = selectedCategory!!, amount = amount))
                runOnUiThread {
                    Toast.makeText(this@reduce_money, "เพิ่มข้อมูลเรียบร้อย", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            resetFields()
        }

        btnHome?.setOnClickListener { view ->
            Toast.makeText(
                this@reduce_money,
                "Your Go Home Now",
                Toast.LENGTH_SHORT
            ).show()
            //val intent = Intent(this,menu1::class.java)
            //startActivity(intent)

            val userId = intent.getIntExtra("userId", 0)  // รับ userId จาก Intent ก่อนหน้านี้
            if (userId != 0) {
                val intent = Intent(this@reduce_money, menu1::class.java)
                intent.putExtra("userId", userId)  // ส่ง userId ไปหน้า menu1
                startActivity(intent)
            } else {
                Toast.makeText(this@reduce_money, "User ID not found", Toast.LENGTH_SHORT).show()
            }
        }

        btnSettings?.setOnClickListener { view ->
            Toast.makeText(
                this@reduce_money,
                "Go Settings",
                Toast.LENGTH_SHORT
            ).show()
            val userId = intent.getIntExtra("userId", 0)  // รับ userId จาก Intent ก่อนหน้านี้
            if (userId != 0) {
                val intent = Intent(this@reduce_money, Setting::class.java)
                intent.putExtra("userId", userId)  // ส่ง userId ไปหน้า Setting
                startActivity(intent)
            } else {
                Toast.makeText(this@reduce_money, "User ID not found", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun showBottomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheetlayout_reduce_money)

        val layoutfood = dialog.findViewById<LinearLayout>(R.id.layoutfood)
        val layoutTrv = dialog.findViewById<LinearLayout>(R.id.layoutTrv)
        val layoutEntertain = dialog.findViewById<LinearLayout>(R.id.layoutEntertain)
        val cancelButton = dialog.findViewById<ImageView>(R.id.cancelButton)
        val layoutother = dialog.findViewById<LinearLayout>(R.id.layoutother)

        layoutfood.setOnClickListener {
            selectedCategory = "อาหาร"
            dialog.dismiss()
            btnShowDialog?.text = "เลือก หมวดหมู่ อาหาร"
            btnShowDialog?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_add_24, 0, 0, 0)
        }

        layoutTrv.setOnClickListener {
            selectedCategory = "เดินทาง"
            dialog.dismiss()
            btnShowDialog?.text = "เลือก หมวดหมู่ เดินทาง"
            btnShowDialog?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_add_24, 0, 0, 0)
        }

        layoutEntertain.setOnClickListener {
            selectedCategory = "บันเทิง"
            dialog.dismiss()
            btnShowDialog?.text = "เลือก หมวดหมู่ บันเทิง"
            btnShowDialog?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_add_24, 0, 0, 0)
        }
        layoutother.setOnClickListener {
            selectedCategory = "อื่นๆ"
            dialog.dismiss()
            btnShowDialog?.text = "เลือก หมวดหมู่ อื่นๆ"
            btnShowDialog?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_add_24, 0, 0, 0)
        }

        cancelButton.setOnClickListener { dialog.dismiss() }

        dialog.show()
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }

    private fun init() {
        db = AppDatabase.getDatabase(this) // หรือสร้าง instance ของ database
        transactionDao = db.transactionDao()

        btnShowDialog = findViewById(R.id.btn_show_dialog)
        btnHome = findViewById(R.id.btnHome)
        btnSettings = findViewById(R.id.btnSettings)
        backhome = findViewById(R.id.backhome)
        btncheck = findViewById(R.id.btncheck)
        editTextNumberDecimal = findViewById(R.id.editTextNumberDecimal)
        //editTextText = findViewById(R.id.editTextText)
        btnAdd = findViewById(R.id.btnAdd)
        btn_add_money = findViewById(R.id.btn_add_money)
        btn_reduce_money = findViewById(R.id.btn_reduce_money)
    }

    private fun resetFields() {

        // รีเซ็ตข้อความใน EditTexts
        editTextNumberDecimal?.text?.clear()
        editTextText?.text?.clear()

        // รีเซ็ตปุ่มกลับไปที่ข้อความเริ่มต้น
        btnShowDialog?.text = "เลือกหมวดหมู่"
    }


}