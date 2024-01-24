package com.example.calendardemo23124

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->

            var day = dayOfMonth.toString()
            var mm = (month + 1).toString()
            if (dayOfMonth < 10) {
                day = "0$dayOfMonth"
            }
            if (month < 9) {
                mm = "0$mm"
            }
            date = ("$year-$mm-$day")
            binding.textView.text = date
        }
        googlecalendar()
        binding.button.setOnClickListener {
            text=binding.editTextText.text.toString()
            event=eventcreate()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val createdEvent = service.events().insert("primary", event).execute()
                    withContext(Dispatchers.Main) {
                        // Update UI on success
                        Toast.makeText(this@MainActivity, "Event Created: ${createdEvent.htmlLink}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        // Handle exceptions on UI thread
                        Toast.makeText(this@MainActivity, "Error creating event", Toast.LENGTH_SHORT).show()
                    }
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
            Toast.makeText(this,"58 ",Toast.LENGTH_SHORT).show()
            handleSignIn(task)
        }
    }

    private fun handleSignIn(task: Task<GoogleSignInAccount>) {

        try {
            Toast.makeText(this,"65 ",Toast.LENGTH_SHORT).show()
            val account=task.getResult(ApiException::class.java)

            val credential = GoogleAccountCredential.usingOAuth2(this, listOf(CalendarScopes.CALENDAR))
            credential.selectedAccount = account?.account

            service = Calendar.Builder(AndroidHttp.newCompatibleTransport(), GsonFactory(), credential)
                .setApplicationName(getString(R.string.app_name))
                .build()
            Toast.makeText(this,"79 ",Toast.LENGTH_SHORT).show()

            Log.d("Tag","Service")




      //      service.events().insert("primary", event).execute()
            Log.d("Tag","Service")
            Toast.makeText(this,"577 ",Toast.LENGTH_SHORT).show()
       //    insertEvent(service,event)


            //  val now = DateTime(System.currentTimeMillis())
//            service.events().list("primary")
//                .setMaxResults(10)
//                .setTimeMin(now)
//                .setOrderBy("startTime")
//                .setSingleEvents(true)
//                .execute()
//            Toast.makeText(this,"87 ",Toast.LENGTH_SHORT).show()

        }
        catch (e:Exception){
            Log.w("SignInActivity", "signInResult:failed code=" + e.message)

        }
    }

    private fun insertEvent(service: Calendar, event: Event){
        try {
            val events = service.events().insert("primary", event).execute()
            Toast.makeText(this,"inserted ",Toast.LENGTH_SHORT).show()

            System.out.printf("Event created: %s\n", events.htmlLink)
        } catch (e: java.lang.Exception) {
            Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
            Log.d("Tag",e.message!!)
            e.printStackTrace()
        }

    }

    private fun eventcreate():Event {
        val event = Event().setSummary(text)
            .setLocation("Event Location")
            .setDescription("Event Description")

        val startDateTime = DateTime(date+"T01:00:00")
        val start = EventDateTime().setDateTime(startDateTime)
        event.setStart(start)

        val endDateTime = DateTime(date+"T10:00:00")
        val end = EventDateTime().setDateTime(endDateTime)
        event.setEnd(end)
        Toast.makeText(this,"event ",Toast.LENGTH_SHORT).show()

        return event
    }


    // Example: List the next 10 events from the primary calendar.



}