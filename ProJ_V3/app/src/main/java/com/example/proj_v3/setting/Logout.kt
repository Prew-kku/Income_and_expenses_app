package com.example.proj_v3.setting

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proj_v3.R
import com.example.proj_v3.login.MainMenu

class Logout : AppCompatActivity() {
    var btnnextt: Button? = null
    var btnb: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_logout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userId = intent.getIntExtra("userId", 0)
        if (userId == 0) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        init()

        btnnextt!!.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            intent.putExtra("userId", userId) // ส่ง userId ไปยังเมนูหลัก
            startActivity(intent)
        }

        btnb!!.setOnClickListener {
            val intent = Intent(this, Setting::class.java)
            intent.putExtra("userId", userId) // ส่ง userId กลับไปหน้า Setting
            startActivity(intent)
        }
    }

    private fun init() {
        btnnextt = findViewById(R.id.btnnextt)
        btnb= findViewById(R.id.btnback)
    }
}