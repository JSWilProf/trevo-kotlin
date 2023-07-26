package br.senai.sp.informatica.trevo.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.senai.sp.informatica.trevo.constants.URL_Foto
import br.senai.sp.informatica.trevo.databinding.ViewProdutoItemBinding
import br.senai.sp.informatica.trevo.model.ProdutoCache
import com.bumptech.glide.Glide

class ProdutosAdapter(val listener: (ProdutoCache) -> Unit) : PagingDataAdapter<ProdutoCache, ProdutosAdapter.ProdutoViewHolder>(
    COMARATOR
) {
    companion object {
        private val COMARATOR = object: DiffUtil.ItemCallback<ProdutoCache>() {
            override fun areContentsTheSame(oldItem: ProdutoCache, newItem: ProdutoCache): Boolean {
                return oldItem.idProduto == newItem.idProduto
            }

            override fun areItemsTheSame(oldItem: ProdutoCache, newItem: ProdutoCache): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val binding = ViewProdutoItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ProdutoViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class ProdutoViewHolder(
        private val binding: ViewProdutoItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(produto: ProdutoCache) {
            val visible = if (produto.maisVendido) View.VISIBLE else View.GONE
            with(produto) {
                binding.tvProduto.text = nome
                binding.iconeMaisVendido.visibility = visible
                binding.textoMaisVendido.visibility = visible
                Glide.with(context)
                    .load(URL_Foto +imagem)
                    .fitCenter()
                    .into(binding.imagemProduto)
                binding.btDetalhes.setOnClickListener {
                    listener(produto)
                }
            }
        }
    }
}
