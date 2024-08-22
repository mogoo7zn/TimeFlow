package cn.edu.ustc.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timeflow.R
import cn.edu.ustc.adapter.HomeListAdapter
import com.example.timeflow.databinding.FragmentHomeBinding
import java.text.ParseException

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var daily_task: ListView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val listView : ListView = root.findViewById(R.id.daily_tasks)
//        val adapter : Adapter = HomeListAdapter()
//        val textView: TextView = binding
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    /**
     * Set the height of the ListView dynamically
     */
    fun setListViewHeight(listView: ListView) {
        val listAdapter = listView.adapter ?: return
        var totalHeight = 0
        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(1, 1)
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height =
            totalHeight + (listView.dividerHeight * (listAdapter.count - 1)) + listView.paddingTop
            + listView.paddingBottom
        listView.layoutParams = params
    }
    @Throws(ParseException::class)
    fun paint() {
//        val task_db: TaskDB = TaskDB()
//        schedules = dailySchedule_1.getScheduleList()
        daily_task.setAdapter(HomeListAdapter())
        val homeFragment = HomeFragment()
        homeFragment.setListViewHeight(daily_task)

//        ddl.setAdapter(DDLAdapter(getActivity(), dailySchedule_1.getDDLsList()))
//        com.example.timemanager.fragment.ScheduleFragment.setListViewHeight(ddl)
    }



}