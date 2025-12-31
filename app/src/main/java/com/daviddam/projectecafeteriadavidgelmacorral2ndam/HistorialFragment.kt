package com.daviddam.projectecafeteriadavidgelmacorral2ndam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.fragment.app.activityViewModels
import com.daviddam.projectecafeteriadavidgelmacorral2ndam.databinding.FragmentHistorialBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistorialFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistorialFragment : Fragment() {
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

    private lateinit var binding: FragmentHistorialBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistorialBinding.inflate(inflater, container, false)
        binding.recyclerComandes.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())

        val sharedPref = sharedPreference.SharedPreference(requireContext())
        val usuari = sharedPref.getUsuari() ?: "unknown"

        val vmHistorial: viewmodel.HistorialViewModel by viewModels()
        val sharedModel: viewmodel.SharedViewModel by activityViewModels()

        vmHistorial.getOrdresUsuari(usuari).observe(viewLifecycleOwner) { llistaComandes ->
            binding.recyclerComandes.adapter = adapter.ComandaAdapter(
                llistaComandes,
                onEliminar = { comanda ->
                    androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("Eliminar comanda")
                        .setMessage("Segur que vols eliminar la comanda?")
                        .setPositiveButton("Eliminar") { _, _ ->
                            vmHistorial.deleteComanda(comanda)
                            android.widget.Toast.makeText(requireContext(), "Comanda eliminada", android.widget.Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("Cancel·lar", null)
                        .show()
                },
                onEditar = { comanda ->
                    val et = android.widget.EditText(requireContext())
                    et.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
                    et.setText(String.format("%.2f", comanda.total))
                    androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("Modificar preu")
                        .setView(et)
                        .setPositiveButton("Guardar") { _, _ ->
                            val text = et.text.toString().replace(',', '.')
                            val newTotal = text.toDoubleOrNull()
                            if (newTotal != null) {
                                vmHistorial.updateComanda(comanda.copy(total = newTotal))
                                android.widget.Toast.makeText(requireContext(), "Comanda actualitzada", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                        .setNegativeButton("Cancel·lar", null)
                        .show()
                }
            )
        }


        sharedModel.comandaRealitzada.observe(viewLifecycleOwner) { evt ->
            evt?.let {
                vmHistorial.handleComandaRealitzada()
                sharedModel.buidarComandaRealitzada()
            }
        }

        
        vmHistorial.comandaNova.observe(viewLifecycleOwner) { nova ->
            if (nova == true) {
                android.widget.Toast.makeText(requireContext(), "Nova comanda realitzada: actualitzat historial", android.widget.Toast.LENGTH_SHORT).show()
                binding.recyclerComandes.scrollToPosition(0)
                vmHistorial.clearComandaNova()
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
         * @return A new instance of fragment HistorialFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistorialFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}