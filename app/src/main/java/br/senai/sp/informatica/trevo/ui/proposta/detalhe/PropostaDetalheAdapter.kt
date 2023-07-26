package br.senai.sp.informatica.trevo.ui.proposta.detalhe

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.senai.sp.informatica.trevo.constants.URL_Foto
import br.senai.sp.informatica.trevo.databinding.ViewPropostaProdutoItemBinding
import br.senai.sp.informatica.trevo.model.Produto
import com.bumptech.glide.Glide

class PropostaDetalheAdapter : PagingDataAdapter<Produto, PropostaDetalheAdapter.PropostaDetalheViewHolder>(
    COMARATOR
) {
    companion object {
        private val COMARATOR = object: DiffUtil.ItemCallback<Produto>() {
            override fun areContentsTheSame(oldItem: Produto, newItem: Produto): Boolean {
                return oldItem.idProduto == newItem.idProduto
            }

            override fun areItemsTheSame(oldItem: Produto, newItem: Produto): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropostaDetalheViewHolder {
        val binding = ViewPropostaProdutoItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return PropostaDetalheViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: PropostaDetalheViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class PropostaDetalheViewHolder(
        private val binding: ViewPropostaProdutoItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(produto: Produto) {
            with(produto) {
                binding.tvProduto.text = nome
                Glide.with(context)
                    .load(URL_Foto + imagem)
                    .fitCenter()
                    .into(binding.imagemProduto)
            }
        }
    }
}
