package com.example.calendardemo23124

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.calendardemo23124.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var date:String
    private  var text: String="null"
    private lateinit var service: Calendar
    private lateinit var event: Event
    private var flag=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            flag=1
            var Day = dayOfMonth.toString()
            var mm = (month + 1).toString()
            if (dayOfMonth < 10) {
                Day = "0$dayOfMonth"
            }
            if (month < 9) {
                mm = "0$mm"
            }
            date = ("$year-$mm-$Day")
            binding.textView.text = date
        }

        googlecalendar()
        binding.button.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Create Event")
                .setMessage("Are you sure You want to create Event?")
                .setPositiveButton("Yes") { di: DialogInterface?, i: Int ->
                    savefunction()
                    Toast.makeText(this,"Thank You!!",Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No"){ DialogInterface,i->
                    Toast.makeText(this,"OK Fine",Toast.LENGTH_SHORT).show()
                }
                .create().show()

        }
    }

    private fun savefunction() {
        text=binding.editTextText.text.toString()
        event=eventcreate()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                service.events().insert("primary", event).execute()
                withContext(Dispatchers.Main) {
                    // Update UI on success
                    Toast.makeText(this@MainActivity, "Event Created!!", Toast.LENGTH_SHORT).show()
                }
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Handle exceptions on UI thread
                    Toast.makeText(this@MainActivity, "Error creating event", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    private fun googlecalendar() {
        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(CalendarScopes.CALENDAR))
            .build()

        val googleSignIn=GoogleSignIn.getClient(this,gso)
        val signInIntent=googleSignIn.signInIntent
        startActivityForResult(signInIntent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==100){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignIn(task)
        }
    }

    private fun handleSignIn(task: Task<GoogleSignInAccount>) {
        try {
            val account=task.getResult(ApiException::class.java)
            val credential = GoogleAccountCredential.usingOAuth2(this, listOf(CalendarScopes.CALENDAR))
            credential.selectedAccount = account?.account
            service = Calendar.Builder(AndroidHttp.newCompatibleTransport(), GsonFactory(), credential)
                .setApplicationName(getString(R.string.app_name))
                .build()
            val cl=java.util.Calendar.getInstance()
           val day= cl.get(java.util.Calendar.DATE).toString()
            val mm=cl.get(java.util.Calendar.MONTH)
            var mmm=(mm+1).toString()
            if (mm<9)
            {
                 mmm="0$mmm"
            }
            val yr=cl.get(java.util.Calendar.YEAR).toString()
            if (flag==0) {
                date = ("$yr-$mmm-$day")
            }

            Log.d("Tag","$date")
        }
        catch (e:Exception){
            Log.w("SignInActivity", "signInResult:failed code=" + e.message)
        }
    }

    private fun eventcreate():Event {
            val event = Event().setSummary(text)
                .setLocation("Ahmedabad")
                .setDescription(" Description")

            val startDateTime = DateTime(date+"T01:00:00")
            val start = EventDateTime().setDateTime(startDateTime)
            event.setStart(start)

            val endDateTime = DateTime(date+"T10:00:00")
            val end = EventDateTime().setDateTime(endDateTime)
            event.setEnd(end)

            return event
    }

}