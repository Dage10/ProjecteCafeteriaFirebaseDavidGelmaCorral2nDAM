package com.daviddam.projectecafeteriadavidgelmacorral2ndam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.fragment.app.activityViewModels
import com.daviddam.projectecafeteriadavidgelmacorral2ndam.databinding.FragmentPagamentBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PagamentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PagamentFragment : Fragment() {
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

    private lateinit var binding: FragmentPagamentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPagamentBinding.inflate(inflater, container, false)
        binding.recyclerSeleccionat.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())

        val modelCompartit: viewmodel.SharedViewModel by activityViewModels()
        val vmPagament: viewmodel.PagamentViewModel by viewModels()
        val preferencies = sharedPreference.SharedPreference(requireContext())

        modelCompartit.productesSeleccionats.observe(viewLifecycleOwner) { llista ->
            binding.recyclerSeleccionat.adapter = adapter.ProductesSeleccionatsAdapter(
                llista,
                { seleccionat -> modelCompartit.setQuantitat(seleccionat.producte.id, seleccionat.quantitat + 1) },
                { seleccionat -> modelCompartit.setQuantitat(seleccionat.producte.id, seleccionat.quantitat - 1) },
                { seleccionat -> modelCompartit.eliminarProducte(seleccionat.producte.id) }
            )
        }

        binding.btnPagar.setOnClickListener {
            val usuari = preferencies.getUsuari() ?: "unknown"
            val total = modelCompartit.preuTotal.value ?: 0.0
            val productes = modelCompartit.productesSeleccionats.value ?: emptyList()
            if (total > 0.0 && productes.isNotEmpty()) {
                vmPagament.pagar(usuari, total, productes)
            } else {
                android.widget.Toast.makeText(requireContext(), "No hi ha cap producte", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        
        vmPagament.comandaFeta.observe(viewLifecycleOwner) { feta ->
            if (feta == true) {
                modelCompartit.notificarComandaRealitzada()
                modelCompartit.eliminarProductes()
                android.widget.Toast.makeText(requireContext(), "Comanda registrada", android.widget.Toast.LENGTH_SHORT).show()
                vmPagament.clearComandaFeta()
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
         * @return A new instance of fragment PagamentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PagamentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}