package br.unb.bugstenio.agendaplusplus

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main_navigation_drawer.*
import kotlinx.android.synthetic.main.app_bar_main_navigation_drawer.*
import kotlinx.android.synthetic.main.content_main_navigation_drawer.*

import br.unb.bugstenio.agendaplusplus.model.DAO.*
import br.unb.bugstenio.agendaplusplus.model.Object.*
import org.joda.time.*
import kotlinx.android.synthetic.main.nav_header_main_navigation_drawer.*
import org.json.JSONArray
import org.json.JSONObject

class MainNavigationDrawer : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_navigation_drawer)
        setSupportActionBar(toolbar)

        val toggle = object : ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerOpened(drawerView: View) {
                nav_header_email.text = Session.user?.email ?: "Erro"
                nav_header_username.text = Session.user?.username ?: "Erro"
                super.onDrawerOpened(drawerView)
            }
        }
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        fragmentManager
                .beginTransaction()
                .replace(
                        fragment_content.id,
                        CalendarFragment())
                .commit()

        if(Session.user == null)
            startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_navigation_drawer, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_manage -> {
                Session.project = null
//                replacePlaceholderFragment("Manage")
                fragmentManager
                        .beginTransaction()
                        .replace(
                                fragment_content.id,
                                UserEditFragment()
                        ).commit()
            }
            R.id.nav_calendar -> {
                Session.project = null
                fragmentManager
                        .beginTransaction()
                        .replace(
                                fragment_content.id,
                                CalendarFragment()
                        ).commit()
            }
            R.id.nav_tasks -> {
                Session.project = null
                fragmentManager
                        .beginTransaction()
                        .replace(
                                fragment_content.id,
                                TaskFragment()
                        ).commit()
            }
            R.id.nav_events -> {
                Session.project = null
                fragmentManager
                        .beginTransaction()
                        .replace(
                                fragment_content.id,
                                EventFragment()
                        ).commit()
            }
            R.id.nav_projects -> {
                fragmentManager
                        .beginTransaction()
                        .replace(
                                fragment_content.id,
                                ProjectFragment()
                        ).commit()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replacePlaceholderFragment(arg: String) {
        fragmentManager.beginTransaction()
                .replace(
                        fragment_content.id,
                        PlaceholderFragment().apply {
                            arguments = Bundle().apply {
                                putString("word", arg)
                            }
                        })
                .commit()
    }

    val ARG1 = "user_id"
    val ARG2 = "username"
    val ARG3 = "email"

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Session.user = User(
                savedInstanceState?.getLong(ARG1) ?: 0,
                savedInstanceState?.getString(ARG2) ?: "",
                savedInstanceState?.getString(ARG3) ?: "",
                "",
                DateTime.now()
        )

        if(Session.user!!.id == 0.toLong()){
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            UserDAO().getUser(Session.user!!.id) {
                Session.user = it?.parseUser()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putLong(ARG1, Session.user?.id ?: 0 )
        outState?.putString(ARG2, Session.user?.username ?: "" )
        outState?.putString(ARG3, Session.user?.email ?: "" )
    }

}
