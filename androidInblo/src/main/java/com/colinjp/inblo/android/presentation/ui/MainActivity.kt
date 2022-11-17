package com.colinjp.inblo.android.presentation.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.colinjp.inblo.Greeting
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.databinding.ActivityMainBinding
import com.colinjp.inblo.android.di.dataStore
import com.colinjp.inblo.android.domain.util.PreferenceKeys
import com.colinjp.inblo.android.domain.util.UserPreferences
import com.colinjp.inblo.android.presentation.ui.login.LoginActivity
import com.colinjp.inblo.android.presentation.ui.login.LoginViewModel
import com.colinjp.inblo.domain.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*
import javax.inject.Inject

fun greet(): String {
    return Greeting().greeting()
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var userPreferencesFlow: Flow<UserPreferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launchWhenCreated {
            userPreferencesFlow.collect { up ->
                binding.navView.setNavigationItemSelectedListener {
                    when(it.itemId){
                        R.id.item_horse_info_site -> {
                            openWebPage("https://www.keiba.go.jp/KeibaWeb/DataRoom/DataRoomTop")
                        }
                        R.id.item_horse_archive_list -> {
                            findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_horseArchiveListFragment)
                            binding.drawerLayout.closeDrawers()
                        }
                        R.id.item_logout -> {
                            loginViewModel.logoutUser(up.userId)
                        }
                    }
                    true
                }
                val header = binding.navView.getHeaderView(0)
                header.findViewById<TextView>(R.id.tv_header).text = up.userName
                header.findViewById<TextView>(R.id.tv_caption).text = up.role
            }
        }

        lifecycleScope.launchWhenStarted {
            loginViewModel.logoutFlow.collect { event ->
                when(event){
                    is LoginViewModel.LoginEvent.Error -> {
                        //TODO:

                    }
                    is LoginViewModel.LoginEvent.Success -> {
                        clearUserPreferences()
                        finish()
                        val intent = Intent(this@MainActivity,LoginActivity::class.java)
                        startActivity(intent)
                    }
                    is LoginViewModel.LoginEvent.Loading -> {
                        //TODO:

                    }
                }
            }
        }


//        binding.bottomNavigation.selectedItemId = R.id.page_messages

        binding.bottomNavigation.setOnNavigationItemSelectedListener{ item ->
            when(item.itemId){
                R.id.page_schedule -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_calendarFragment)
                    true
                }
                R.id.page_horse_list -> {
                   findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_horseListFragment)
                   true
                }
                R.id.page_messages -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_messagesFragment)
                    true
                }
                else -> {
                    Toast.makeText(this,"Not yet implemented",Toast.LENGTH_SHORT).show()
                    false
                }
            }
        }

        binding.boxBlue.setOnClickListener {
            if(binding.drawerLayout.isDrawerOpen(GravityCompat.END)){
                binding.drawerLayout.closeDrawers()
            }else{
                binding.drawerLayout.openDrawer(GravityCompat.END)

            }
        }

    }

    override fun onResume() {
        super.onResume()
        binding.tvDate.text =  DateTime().getTodayDateInJapanese()
    }

    private suspend fun clearUserPreferences(){
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}


private fun DateTime.getTodayDateInJapanese(): String {
    val month = this.monthOfYear().get()
    val day = this.dayOfMonth().get()
    val fmt = DateTimeFormat.forPattern("EEEE")
    val strJap = fmt.withLocale(Locale.JAPANESE).print(this).replace("曜日","")
    return "${month}月${day}日(${strJap})"
}

