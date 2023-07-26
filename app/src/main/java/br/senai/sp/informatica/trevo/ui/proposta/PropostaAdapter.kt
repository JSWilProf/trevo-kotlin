package br.senai.sp.informatica.trevo.ui.proposta

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.senai.sp.informatica.trevo.databinding.ViewPropostaItemBinding
import br.senai.sp.informatica.trevo.model.PropostaResponse
import java.time.format.DateTimeFormatter

class PropostaAdapter(val listener: (PropostaResponse) -> Unit) : PagingDataAdapter<PropostaResponse, PropostaAdapter.PropostaViewHolder>(
    COMARATOR
) {
    companion object {
        private val COMARATOR = object: DiffUtil.ItemCallback<PropostaResponse>() {
            override fun areContentsTheSame(oldItem: PropostaResponse, newItem: PropostaResponse): Boolean {
                return oldItem.idProposta == newItem.idProposta
            }

            override fun areItemsTheSame(oldItem: PropostaResponse, newItem: PropostaResponse): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropostaViewHolder {
        val binding = ViewPropostaItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return PropostaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PropostaViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class PropostaViewHolder(
        private val binding: ViewPropostaItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(proposta: PropostaResponse) {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            with(proposta) {
                binding.tvData.text = data?.format(formatter)
                binding.tvNumero.text = idProposta.toString()
                binding.tvQuantidade.text = produtos.size.toString()
                binding.card.setOnClickListener {
                    listener(proposta)
                }
            }
        }
    }
}
