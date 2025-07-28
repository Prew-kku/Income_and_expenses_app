package com.example.proj_v3.setting


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proj_v3.data.AppDatabase
import com.example.proj_v3.R
import com.example.proj_v3.data.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditAccount : AppCompatActivity() {
    private lateinit var btnSave: Button
    private lateinit var etUsername: EditText
    private lateinit var rEdtPass: EditText
    private lateinit var etBirthdate: EditText
    private lateinit var etPhone: EditText

    private lateinit var userDao: UserDao
    private var userId: Int = 0 // เก็บ userId ของผู้ใช้ที่ล็อกอิน
    private lateinit var db: AppDatabase

    private var btnnext: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account)
        init()
        // เชื่อมฐานข้อมูล
        db = AppDatabase.getDatabase(this)
        userDao = db.userDao()

        // เชื่อม UI

        rEdtPass = findViewById(R.id.rEdtPass)
        btnSave = findViewById(R.id.rBtnReg)
        etUsername = findViewById(R.id.rEdtUser)
        etBirthdate = findViewById(R.id.birth)
        etPhone = findViewById(R.id.phone)

        btnnext?.setOnClickListener {
            val intent = Intent(this, Setting::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        // รับ userId ที่ส่งมาจาก Intent
        userId = intent.getIntExtra("userId", 0)
        if (userId == 0) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // โหลดข้อมูลผู้ใช้จากฐานข้อมูลตาม userId
        fetchUserInfo()

        // ปุ่มบันทึกการแก้ไข
        btnSave.setOnClickListener {
            updateUser()
        }
    }

    private fun fetchUserInfo() {
        lifecycleScope.launch {
            val user = withContext(Dispatchers.IO) { userDao.getUserById(userId) }
            if (user != null) {
                etUsername.setText(user.username)
                rEdtPass.setText(user.password)
                etBirthdate.setText(user.birthdate)
                etPhone.setText(user.phone)
            } else {
                Toast.makeText(this@EditAccount, "ไม่พบข้อมูลผู้ใช้", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUser() {
        val newUsername = etUsername.text.toString().trim()
        val newpassword = rEdtPass.text.toString().trim()
        val newBirthdate = etBirthdate.text.toString().trim()
        val newPhone = etPhone.text.toString().trim()

        if (newUsername.isEmpty() || newpassword.isEmpty()|| newBirthdate.isEmpty() || newPhone.isEmpty()) {
            Toast.makeText(this, "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                userDao.updateUser(userId, newUsername,newpassword, newBirthdate, newPhone)
            }
            Toast.makeText(this@EditAccount, "อัปเดตข้อมูลสำเร็จ!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun init() {
        btnnext = findViewById(R.id.btnBack)
    }
}
