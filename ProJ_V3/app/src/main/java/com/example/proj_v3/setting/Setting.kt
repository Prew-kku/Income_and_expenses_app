package com.example.proj_v3.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.proj_v3.Add_and_Subtract.reduce_money

import com.example.proj_v3.data.AppDatabase
import com.example.proj_v3.R
import com.example.proj_v3.data.User
import com.example.proj_v3.data.UserDao
import com.example.proj_v3.main_app.menu1
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Setting : AppCompatActivity() {
    private var btnnext: ImageButton? = null
    private var btnnext2: ImageButton? = null
    private var btnnext3: ImageButton? = null
    private var btnnext4: ImageButton? = null
    private var btnnext5: ImageButton? = null
    private lateinit var btnAdd: FloatingActionButton


    private var btnHome: Button? = null
    private var btnSettings: Button? = null



    private lateinit var tvUsername: TextView
    private lateinit var tvBirthdate: TextView
    private lateinit var tvPhone: TextView
    private var txtlogout: TextView? = null
    private var txtdelete: TextView? = null

    private lateinit var userDao: UserDao
    private var userId: Int = 0
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_seting)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the database and DAOs
        db = AppDatabase.getDatabase(this)
        userDao = db.userDao()

        // Get userId passed from previous activity
        userId = intent.getIntExtra("userId", 0)
        Log.d("Debug", "User ID: $userId")
        if (userId == 0) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        init()
        fetchUserInfo()

        // เมื่อกดปุ่มสำหรับไปหน้า EditAccount ให้ส่ง userId ไปด้วย
        btnnext?.setOnClickListener {
            val intent = Intent(this, EditAccount::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        btnnext2?.setOnClickListener {
            val intent = Intent(this, EditAccount::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        btnnext3?.setOnClickListener {
            val intent = Intent(this, EditAccount::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        btnnext4?.setOnClickListener {
            val intent = Intent(this, Logout::class.java)
            intent.putExtra("userId", userId) // ส่ง userId ไปหน้า Logout
            startActivity(intent)
        }

        btnnext5?.setOnClickListener {
            val intent = Intent(this, Delete::class.java)
            intent.putExtra("userId", userId) // ส่ง userId ไปหน้า Delete
            startActivity(intent)
        }

        btnHome?.setOnClickListener { view ->
            Toast.makeText(
                this@Setting,
                "Your Go Home Now",
                Toast.LENGTH_SHORT
            ).show()
            //val intent = Intent(this,menu1::class.java)
            //startActivity(intent)

            val userId = intent.getIntExtra("userId", 0)  // รับ userId จาก Intent ก่อนหน้านี้
            if (userId != 0) {
                val intent = Intent(this@Setting, menu1::class.java)
                intent.putExtra("userId", userId)  // ส่ง userId ไปหน้า menu1
                startActivity(intent)
            } else {
                Toast.makeText(this@Setting, "User ID not found", Toast.LENGTH_SHORT).show()
            }
        }

        btnSettings?.setOnClickListener { view ->
            Snackbar.make(view, "Your in Settings Now", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.btnAdd).show()

        }

        btnAdd.setOnClickListener { view ->
            Snackbar.make(view, "Add new item", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.btnAdd).show()
            //val intent = Intent(this, reduce_money::class.java)
            //startActivity(intent)

            val userId = intent.getIntExtra("userId", 0)  // รับ userId จาก Intent ก่อนหน้านี้
            if (userId != 0) {
                val intent = Intent(this@Setting, reduce_money::class.java)
                intent.putExtra("userId", userId)  // ส่ง userId ไปหน้า reduce_money
                startActivity(intent)
            } else {
                Toast.makeText(this@Setting, "User ID not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun init() {
        btnnext = findViewById(R.id.btnNext)
        btnnext2 = findViewById(R.id.btnNext2)
        btnnext3 = findViewById(R.id.btnNext3)
        btnnext4 = findViewById(R.id.btnNext4)
        btnnext5 = findViewById(R.id.btnNext5)
        btnHome = findViewById(R.id.btnHome)
        btnSettings = findViewById(R.id.btnSettings)
        btnAdd = findViewById(R.id.btnAdd)


        tvUsername = findViewById(R.id.txtname)
        tvBirthdate = findViewById(R.id.txtdate)
        tvPhone = findViewById(R.id.txtphone)

        txtdelete = findViewById(R.id.txtdelete)
        txtlogout = findViewById(R.id.txtlogout)
    }

    private fun fetchUserInfo() {
        lifecycleScope.launch {
            val userFromDB: User? = withContext(Dispatchers.IO) { userDao.getUserById(userId) }
            if (userFromDB != null) {
                updateUI(userFromDB)
            } else {
                Toast.makeText(this@Setting, "User not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(user: User) {
        tvUsername.text = user.username
        tvBirthdate.text = user.birthdate
        tvPhone.text = user.phone

    }
}
