package com.daviddam.projectecafeteriadavidgelmacorral2ndam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.fragment.app.activityViewModels
import com.daviddam.projectecafeteriadavidgelmacorral2ndam.databinding.FragmentBegudesBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BegudesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BegudesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentBegudesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBegudesBinding.inflate(inflater, container, false)
        binding.recyclerProductes.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())

        val viewModelBegudes: viewmodel.BegudesViewModel by viewModels()
        val modelCompartit: viewmodel.SharedViewModel by activityViewModels()

        viewModelBegudes.begudaProductes.observe(viewLifecycleOwner) { llista ->
            binding.recyclerProductes.adapter = adapter.ProducteAdapter(llista, alClicar = { producte -> viewModelBegudes.afegirProducte(producte)})
        }

        viewModelBegudes.producteAfegit.observe(viewLifecycleOwner) { producte ->
            producte?.let {
                modelCompartit.afegirProducte(it)
                viewModelBegudes.buidarProducteAfegit()
            }
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BegudesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BegudesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}