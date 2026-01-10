package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daviddam.projectecafeteriadavidgelmacorral2ndam.databinding.ItemProducteSeleccionatBinding
import entity.ProducteSeleccionat

class ProductesSeleccionatsAdapter(
    private val llista: List<ProducteSeleccionat>,
    private val onIncrementar: (ProducteSeleccionat) -> Unit,
    private val onDisminuir: (ProducteSeleccionat) -> Unit,
    private val onEliminar: (ProducteSeleccionat) -> Unit
) : RecyclerView.Adapter<ProductesSeleccionatsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemProducteSeleccionatBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProducteSeleccionatBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, posicio: Int) {
        val item = llista[posicio]
        holder.binding.tvProducteNom.text = item.producte.nom
        holder.binding.tvProductePreu.text = "${item.producte.preu} €"
        holder.binding.tvProducteQuantitat.text = "${item.quantitat}"
        val context = holder.binding.root.context
        val imatgeResId = context.resources.getIdentifier(
            item.producte.imatgeNom,
            "drawable",
            context.packageName
        )
        holder.binding.imatgeProducte.setImageResource(imatgeResId)

        holder.binding.btnIncrementar.setOnClickListener {
            onIncrementar(item)
        }

        holder.binding.btnDisminuir.setOnClickListener {
            onDisminuir(item)
        }

        holder.binding.btnEliminar.setOnClickListener {
            onEliminar(item)
        }
    }

    override fun getItemCount(): Int = llista.size
}
