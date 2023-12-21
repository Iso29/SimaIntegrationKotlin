package com.example.simaintegrationkotlin.ui.pickAndSignFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.simaintegrationkotlin.common.adapter.FragmentPageAdapter
import com.example.simaintegrationkotlin.common.base.BaseFragment
import com.example.simaintegrationkotlin.databinding.FragmentPickAndSignPdfBinding
import com.example.simaintegrationkotlin.ui.pickAndSignFragment.fragmentItems.SignChallengeFragment
import com.example.simaintegrationkotlin.ui.pickAndSignFragment.fragmentItems.SignPdfFragment
import com.google.android.material.tabs.TabLayout

//@AndroidEntryPoint
class PickAndSignPdfFragment : BaseFragment<FragmentPickAndSignPdfBinding>() {
    private val viewModel : PickAndSignViewModel by viewModels()
    private var adapter: FragmentPageAdapter? = null
    override val onInflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPickAndSignPdfBinding
        get() = FragmentPickAndSignPdfBinding::inflate
    override val onBind: FragmentPickAndSignPdfBinding.() -> Unit
        get() = {
            initTableLayout()
        }

    private fun initTableLayout(){
        adapter = FragmentPageAdapter(requireActivity().supportFragmentManager, lifecycle)

        adapter?.submitList(
            listOf(
                SignPdfFragment.newInstance(),
                SignChallengeFragment.newInstance()
            )
        )
        binding?.apply{
            viewPager.adapter = adapter
            tabLayout.addTab(tabLayout.newTab().setText("Sign PDF"))
            tabLayout.addTab(tabLayout.newTab().setText("Sign challenge"))
        }
    }

    override fun FragmentPickAndSignPdfBinding.setListeners() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { viewPager.currentItem = it.position }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }
}