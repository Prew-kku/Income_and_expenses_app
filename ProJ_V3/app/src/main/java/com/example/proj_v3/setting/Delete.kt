package com.example.proj_v3.setting


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.proj_v3.data.AppDatabase
import com.example.proj_v3.R
import com.example.proj_v3.data.UserDao
import com.example.proj_v3.login.MainMenu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Delete : AppCompatActivity() {
    private lateinit var btnDelete: Button
    private lateinit var btnBack: ImageButton
    private lateinit var userDao: UserDao
    private var userId: Int = 0  // เก็บ userId ของผู้ใช้ที่ล็อกอิน
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_delete)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ดึง userId จาก Intent
        userId = intent.getIntExtra("userId", 0)
        if (userId == 0) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // เชื่อมต่อฐานข้อมูล
        db = AppDatabase.getDatabase(this)
        userDao = db.userDao()

        // ผูก UI กับตัวแปร
        btnDelete = findViewById(R.id.btnnext)
        btnBack = findViewById(R.id.btnBack)

        // ปุ่มย้อนกลับไปหน้า Setting
        btnBack.setOnClickListener {
            val intent = Intent(this, Setting::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        // ปุ่มลบบัญชี
        btnDelete.setOnClickListener {
            deleteAccount()
        }
    }

    private fun deleteAccount() {
        lifecycleScope.launch(Dispatchers.IO) {
            userDao.deleteUserById(userId)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@Delete, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                // หลังลบเสร็จ ให้กลับไปหน้าแรก (MainMenu)
                val intent = Intent(this@Delete, MainMenu::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
