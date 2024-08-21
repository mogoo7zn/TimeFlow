package cn.edu.ustc.ui.clock

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.timeflow.databinding.FragmentDeadlineListBinding

class DeadlineFragment : Fragment(){
    private var _binding: FragmentDeadlineListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val deadlineViewModel =
            ViewModelProvider(this)[DeadlineViewModel::class.java]

        _binding = FragmentDeadlineListBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding
//        deadlineViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}