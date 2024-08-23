package cn.edu.ustc.ustcschedule.fragment;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.timeflow.R
import com.example.timeflow.databinding.FragmentGoalOverviewBinding

class GoalOverviewFragment : Fragment() {
    private var _binding: FragmentGoalOverviewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentGoalOverviewBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentGoalPicker = binding.currentGoalPicker
        if (currentGoalPicker.isChecked) {
            //TODO: show current goal's actions
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
