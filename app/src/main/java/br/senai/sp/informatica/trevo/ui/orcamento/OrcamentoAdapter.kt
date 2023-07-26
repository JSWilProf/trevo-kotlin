package br.senai.sp.informatica.trevo.ui.orcamento

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.senai.sp.informatica.trevo.constants.URL_Foto
import br.senai.sp.informatica.trevo.databinding.ViewOrcamentoItemBinding
import br.senai.sp.informatica.trevo.model.ProdutoItem
import com.bumptech.glide.Glide

class OrcamentoAdapter(val listener: (ProdutoItem) -> Unit) : PagingDataAdapter<ProdutoItem, OrcamentoAdapter.ProdutoViewHolder>(
    COMARATOR
) {
    companion object {
        private val COMARATOR = object: DiffUtil.ItemCallback<ProdutoItem>() {
            override fun areContentsTheSame(oldItem: ProdutoItem, newItem: ProdutoItem): Boolean {
                return oldItem.idProduto == newItem.idProduto
            }

            override fun areItemsTheSame(oldItem: ProdutoItem, newItem: ProdutoItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val binding = ViewOrcamentoItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ProdutoViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class ProdutoViewHolder(
        private val binding: ViewOrcamentoItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(produto: ProdutoItem) {
            with(produto) {
                binding.tvProduto.text = nome
                Glide.with(context)
                    .load(URL_Foto + imagem)
                    .fitCenter()
                    .into(binding.imagemProduto)
                binding.btDelete.setOnClickListener {
                    listener(produto)
                }
            }
        }
    }
}
