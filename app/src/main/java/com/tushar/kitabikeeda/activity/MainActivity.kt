package com.tushar.kitabikeeda.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.tushar.kitabikeeda.*
import com.tushar.kitabikeeda.fragment.AboutappFragment
import com.tushar.kitabikeeda.fragment.DashboardFragment
import com.tushar.kitabikeeda.fragment.FavouritesFragment
import com.tushar.kitabikeeda.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var navigationView:NavigationView
    lateinit var frameLayout: FrameLayout
    var previousmenuitem:MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout=findViewById(R.id.drawer)
        frameLayout=findViewById(R.id.frame)
        toolbar=findViewById(R.id.toolbar)
        navigationView=findViewById(R.id.navigation_view)
        coordinatorLayout=findViewById(R.id.coordinator_Layout)
        setUpToolbar()

       openDashboard()

        val actionBarDrawerToggle=ActionBarDrawerToggle(this@MainActivity,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            if(previousmenuitem!=null){
                previousmenuitem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousmenuitem=it


            when (it.itemId){
                R.id.dashboard ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        DashboardFragment()
                    )
                        //.addToBackStack("Dashboard")
                          .commit()
                    supportActionBar?.title="Dashboard"
                    drawerLayout.closeDrawers()
                }
                R.id.favourites ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        FavouritesFragment()
                    )
                        //.addToBackStack("Favourites")
                        .commit()
                    supportActionBar?.title="Favourites"
                    drawerLayout.closeDrawers()
                }
                R.id.profile ->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame, ProfileFragment())
                        //.addToBackStack("Profile")
                        .commit()
                    supportActionBar?.title="Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.aboutapp ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        AboutappFragment()
                    )
                        //.addToBackStack("Aboutapp")
                        .commit()
                    supportActionBar?.title="Aboutapp"
                    drawerLayout.closeDrawers()
                    Toast.makeText(this@MainActivity,"About CLicked",Toast.LENGTH_SHORT).show()
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    fun openDashboard(){
        val fragment= DashboardFragment()
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame,fragment)
        transaction.commit()
        supportActionBar?.title="Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)
    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frame)
        when (frag){
            !is DashboardFragment ->openDashboard()
            else->super.onBackPressed()
        }
    }
}
