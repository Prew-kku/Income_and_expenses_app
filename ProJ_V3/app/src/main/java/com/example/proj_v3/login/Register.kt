package com.example.proj_v3.login


import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.proj_v3.data.AppDatabase
import com.example.proj_v3.R
import com.example.proj_v3.setting.Setting
import com.example.proj_v3.data.User
import com.example.proj_v3.data.UserDao
import com.example.proj_v3.main_app.menu1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Register : AppCompatActivity() {
    private lateinit var rBtnReg: Button
    private lateinit var rEdtUser: EditText
    private lateinit var rEdtPass: EditText
    private lateinit var rEdtConf: EditText
    private lateinit var rEdtBirth: EditText
    private lateinit var rEdtPhone: EditText
    private lateinit var radioBtn: RadioButton
    private lateinit var tvNextPage: TextView
    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txt)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = AppDatabase.getDatabase(this)
        userDao = db.userDao()

        init()

        // ตรวจจับการกดปุ่ม radio เพื่อเปิดใช้งานปุ่มสมัครใช้งาน
        radioBtn.setOnCheckedChangeListener { _, isChecked ->
            rBtnReg.isEnabled = isChecked
        }

        // ปุ่มสมัครใช้งาน เมื่อกดแล้ว insert ข้อมูลลงฐานข้อมูลและส่ง userId ไปหน้า Setting
        rBtnReg.setOnClickListener {
            if (validateInput()) {
                val user = User(
                    username = rEdtUser.text.toString().trim(),
                    password = rEdtPass.text.toString().trim(),
                    birthdate = rEdtBirth.text.toString().trim(),
                    phone = rEdtPhone.text.toString().trim()
                )

                lifecycleScope.launch(Dispatchers.IO) {
                    val rowId = userDao.insertUser(user)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@Register, "Register Success", Toast.LENGTH_LONG).show()
                        // ส่ง userId ที่ได้จากการ insert ไปที่ Setting
                        val intent = Intent(this@Register, menu1::class.java)
                        intent.putExtra("userId", rowId.toInt())
                        startActivity(intent)
                    }
                }
            }
        }


        // ปุ่ม "เข้าสู่ระบบ" ไปยังหน้า MainMenu
        tvNextPage.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)

        }
    }

    private fun validateInput(): Boolean {
        val username = rEdtUser.text.toString().trim()
        val pass = rEdtPass.text.toString().trim()
        val confirm = rEdtConf.text.toString().trim()
        val birthdate = rEdtBirth.text.toString().trim()
        val phone = rEdtPhone.text.toString().trim()

        val usernamePattern = "^[a-zA-Z0-9._]{6,12}$".toRegex()
        val passwordPattern = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{6,}$".toRegex()
        val birthdatePattern = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$".toRegex()
        val phonePattern = "^[0-9]{10}$".toRegex()

        when {
            username.isEmpty() -> {
                rEdtUser.error = "กรุณากรอกชื่อผู้ใช้"
                return false
            }
            !username.matches(usernamePattern) -> {
                rEdtUser.error = "ชื่อผู้ใช้ต้องมี 6-12 ตัวอักษร"
                return false
            }
            pass.isEmpty() -> {
                rEdtPass.error = "กรุณากรอกรหัสผ่าน"
                return false
            }
            !pass.matches(passwordPattern) -> {
                rEdtPass.error = "รหัสผ่านต้องมีตัวอักษรและตัวเลขอย่างน้อย 6 ตัว"
                return false
            }
            confirm.isEmpty() -> {
                rEdtConf.error = "กรุณายืนยันรหัสผ่าน"
                return false
            }
            confirm != pass -> {
                rEdtConf.error = "รหัสผ่านไม่ตรงกัน"
                return false
            }
            birthdate.isEmpty() -> {
                rEdtBirth.error = "กรุณากรอกวันเกิด"
                return false
            }
            !birthdate.matches(birthdatePattern) -> {
                rEdtBirth.error = "รูปแบบวันเกิดต้องเป็น dd/mm/yyyy"
                return false
            }
            phone.isEmpty() -> {
                rEdtPhone.error = "กรุณากรอกเบอร์โทร"
                return false
            }
            !phone.matches(phonePattern) -> {
                rEdtPhone.error = "เบอร์โทรศัพท์ต้องมี 10 หลัก"
                return false
            }
            !radioBtn.isChecked -> {
                Toast.makeText(this, "กรุณายอมรับข้อตกลง", Toast.LENGTH_LONG).show()
                return false
            }
        }
        return true
    }

    private fun init() {
        rBtnReg = findViewById(R.id.rBtnReg)
        rEdtUser = findViewById(R.id.rEdtUser)
        rEdtPass = findViewById(R.id.rEdtPass)
        rEdtConf = findViewById(R.id.rEdtConf)
        rEdtBirth = findViewById(R.id.birth)
        rEdtPhone = findViewById(R.id.phone)
        radioBtn = findViewById(R.id.radio)
        tvNextPage = findViewById(R.id.textLog2)

        // ปิดปุ่มสมัครใช้งานก่อน กด radio ถึงจะเปิด
        rBtnReg.isEnabled = false
    }
}
