package com.example.proj_v3.login

import com.example.proj_v3.data.AppDatabase
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.proj_v3.R
import com.example.proj_v3.setting.Setting
import com.example.proj_v3.data.UserDao
import com.example.proj_v3.main_app.menu1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainMenu : AppCompatActivity() {
    var btnLogin: Button? = null
    var btnReg: Button? = null
    var edtUser: EditText? = null
    var edtPass: EditText? = null
    private lateinit var db: AppDatabase

    private lateinit var userDao: UserDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txt)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        //startActivity(intent)

//*************************************************************************
        db = AppDatabase.getDatabase(this)
        userDao = db.userDao()

        init()

        btnLogin?.setOnClickListener {
            val username = edtUser!!.text.toString().trim()
            val pass = edtPass!!.text.toString().trim()

            if (username.isEmpty()) {
                edtUser?.setError("ช่องต้องไม่เว้นว่าง", null)
            } else if (pass.isEmpty()) {
                edtPass?.setError("ช่องต้องไม่เว้นว่าง", null)
            } else {
                lifecycleScope.launch {
                    val user = withContext(Dispatchers.IO) { userDao.getUser(username, pass) }
                    if (user != null) {
                        // ส่ง userId ไปที่ Setting
                        val intent = Intent(this@MainMenu, menu1::class.java)
                        intent.putExtra("userId", user.id)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@MainMenu, "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }



        btnReg!!.setOnClickListener {
            var intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        /*val tvNextPage = findViewById<TextView>(R.id.forgot)
        tvNextPage.setOnClickListener {
            val intent = Intent(this, Forgot::class.java) // เปลี่ยนเป็นชื่อ Activity ที่ต้องการ
            startActivity(intent)
        }*/
    }

    private fun init(){
        btnLogin = findViewById(R.id.btnnextt)
        btnReg = findViewById(R.id.btnback)
        edtUser = findViewById(R.id.edtUser)
        edtPass = findViewById(R.id.edtPass)

    }




}