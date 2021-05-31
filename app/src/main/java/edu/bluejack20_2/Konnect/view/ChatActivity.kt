package edu.bluejack20_2.Konnect.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.ChatTabAdapter
import edu.bluejack20_2.Konnect.base.BaseActivity

class ChatActivity : BaseActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager : ViewPager2

    private fun init(){
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.vp_pager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        init()
        val adapter = ChatTabAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager){tab, position ->
            when(position){
                0->{
                    tab.text = getString(R.string.chat)
                }
                1->{
                    tab.text = getString(R.string.connection)
                }
            }
        }.attach()


    }
}