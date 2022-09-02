package com.example.primenumber.home

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.example.primenumber.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val generate_btn = binding.generateBtn

        generate_btn.setOnClickListener {
            var startNum = binding.etStart.text.toString().toInt()
            val endNum = binding.etEnd.text.toString().toInt()

            if(startNum < 0 || startNum > endNum ){
                val message = Snackbar.make(requireContext(), view , "Number should be greater than 0 and lesser than end number", Snackbar.LENGTH_SHORT)
                message.show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(startNum.toString()) || TextUtils.isEmpty(endNum.toString())) {
                val message = Snackbar.make(requireContext(), view , "Please fill in the fields", Snackbar.LENGTH_SHORT)
                message.show()
                return@setOnClickListener
            }

            val action = HomeFragmentDirections.actionHomeFragmentToResultFragment(startNum, endNum)
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

}
