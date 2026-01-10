package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daviddam.projectecafeteriadavidgelmacorral2ndam.databinding.ItemComandaBinding
import entity.ComandaEntity

class ComandaAdapter(
    private val llistaComandes: List<ComandaEntity>,
    private val onEliminar: (ComandaEntity) -> Unit,
    private val onEditar: (ComandaEntity) -> Unit,
    private val onMostrarDetalls: (ComandaEntity) -> Unit
) : RecyclerView.Adapter<ComandaAdapter.ComandaViewHolder>() {

    inner class ComandaViewHolder(val binding: ItemComandaBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComandaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemComandaBinding.inflate(inflater, parent, false)
        return ComandaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComandaViewHolder, posicio: Int) {
        val comanda = llistaComandes[posicio]
        holder.binding.tvComandaUsuari.text = comanda.usuari
        holder.binding.tvComandaData.text = java.text.DateFormat.getDateTimeInstance().format(java.util.Date(comanda.timestamp))
        holder.binding.tvComandaTotal.text = String.format("%.2f €", comanda.total)
        holder.binding.btnEliminarComanda.setOnClickListener { onEliminar(comanda) }
        holder.binding.btnEditarComanda.setOnClickListener { onEditar(comanda) }
        holder.itemView.setOnLongClickListener {
            onEliminar(comanda)
            true
        }

        holder.itemView.setOnClickListener {
            onMostrarDetalls(comanda)
        }
    }

    override fun getItemCount(): Int = llistaComandes.size
}