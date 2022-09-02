package com.example.primenumber.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.example.primenumber.databinding.FragmentResultBinding
import kotlinx.coroutines.launch

class ResultFragment : Fragment() {
    lateinit var binding: FragmentResultBinding
    val args: ResultFragmentArgs by navArgs()
    val viewModel: ViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backBtn = binding.backBtn
        val progressBar = binding.loadingBar
        val result = binding.resultTv
        val title = binding.primeTitle
        result.visibility = View.GONE

        lifecycleScope.launch {
            if (viewModel.primeNumbers.isEmpty()) {
                viewModel.allPrimeNum(args.primeNumStart, args.primeNumEnd)
            }
            if (viewModel.primeNumbers.isEmpty()) {
                binding.noPrimeNum.visibility = View.VISIBLE
            }

            title.text = "Prime Numbers (${args.primeNumStart} to ${args.primeNumEnd})"
            result.text = viewModel.primeNumbers.sorted().toString()
                .replace("[", "")
                .replace("]", "")

            viewModel.loading.asLiveData().observe(viewLifecycleOwner) {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.GONE
                    result.visibility = View.VISIBLE
                }
            }
        }

        backBtn.setOnClickListener {
            val action = ResultFragmentDirections.actionResultFragmentToHomeFragment()
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

}

