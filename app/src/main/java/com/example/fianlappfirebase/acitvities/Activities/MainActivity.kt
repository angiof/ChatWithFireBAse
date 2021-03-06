package com.example.fianlappfirebase.acitvities.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.extencionfuntions.goToActivity
import com.example.fianlappfirebase.R
import com.example.fianlappfirebase.acitvities.Fragemnts.ChatFragment
import com.example.fianlappfirebase.acitvities.Fragemnts.InfoFragment
import com.example.fianlappfirebase.acitvities.Fragemnts.RatesFragment
import com.example.fianlappfirebase.acitvities.adapters.PagersAdapters
import com.example.mylibrery.toolbarActy
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : toolbarActy() {

    private var prevBottomSelected: MenuItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ToLoadToolbar(toolbarxml as Toolbar)
        setUpButtonNavigationBatr()
        setUpViewPager(getPagerAdapter())
        getPagerAdapter()


    }

    private fun getPagerAdapter(): PagersAdapters {
        val adapter = PagersAdapters(supportFragmentManager)
        adapter.addFragment(InfoFragment())
        adapter.addFragment(RatesFragment())
        adapter.addFragment(ChatFragment())
        return adapter
    }


    private fun setUpViewPager(adapters: PagersAdapters) {
        viewpager.adapter = adapters
        viewpager.offscreenPageLimit = adapters.count
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (prevBottomSelected == null) {
                    buttonavigation.menu.getItem(0).isChecked = false

                } else {
                    prevBottomSelected!!.isChecked = false
                }

                buttonavigation.menu.getItem(position).isChecked = true
                prevBottomSelected = buttonavigation.menu.getItem(position)

            }


        })
    }

    private fun setUpButtonNavigationBatr() {
        buttonavigation.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.button_nav_info -> {

                    viewpager.currentItem = 0; true
                }


                R.id.button_nav_rates -> {
                    viewpager.currentItem = 1;true
                }
                R.id.button_nav_chat -> {
                    viewpager.currentItem = 2;true
                }
                else -> false

            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_geral_exit, menu)
        return super.onCreateOptionsMenu(menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menu_log_aut->{
                FirebaseAuth.getInstance().signOut()
                goToActivity<LoginAcitivty>(){
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                }
            }

        }
        return super.onOptionsItemSelected(item)
    }


}


