package com.aca.lovalaundry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.aca.lovalaundry.databinding.ActivityMainBinding
import com.aca.lovalaundry.helper.SharePref
import com.aca.lovalaundry.nav.HomeFragment
import com.aca.lovalaundry.nav.ProfileFragment
import com.aca.lovalaundry.nav.RiwayatFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    val fragmentHome : Fragment = HomeFragment()
    val fragmentProfile : Fragment = ProfileFragment()
    val fragmentRiwayat : Fragment = RiwayatFragment()
    val fm : FragmentManager = supportFragmentManager
    var active : Fragment = fragmentHome

    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var bottomNavigationView: BottomNavigationView

    private  lateinit var  sph : SharePref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sph = SharePref(this)
        setUpBottomNav()
    }

    private fun setUpBottomNav() {
        fm.beginTransaction().add(R.id.container, fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container, fragmentProfile).show(fragmentProfile).commit()
        fm.beginTransaction().add(R.id.container, fragmentRiwayat).show(fragmentRiwayat).commit()

        bottomNavigationView = binding.btmView
        menu = bottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isCheckable = true

        bottomNavigationView.setOnNavigationItemReselectedListener { item ->

            when (item.itemId){
                R.id.nav_home -> {
                    callFragment( 0, fragmentHome)
                }

                R.id.nav_riwayat -> {
                    callFragment(1, fragmentRiwayat)
                }

                R.id.nav_profile -> {
                   if (sph.getStatusLogin()){
                       callFragment(2, fragmentProfile)
                   }else{
                       startActivity(Intent(this, WelcomeActivity::class.java))
                   }
                }
            }
        }
    }

    private fun callFragment(index : Int, fragment: Fragment) {
        menuItem =menu.getItem(index)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }
}