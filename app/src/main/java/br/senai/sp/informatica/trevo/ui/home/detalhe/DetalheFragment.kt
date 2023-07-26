package br.senai.sp.informatica.trevo.ui.home.detalhe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import br.senai.sp.informatica.trevo.MainActivity
import br.senai.sp.informatica.trevo.constants.URL_Capa
import br.senai.sp.informatica.trevo.databinding.FragmentDetalheBinding
import br.senai.sp.informatica.trevo.model.mapper.ProdutoMapper
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetalheFragment : Fragment() {
    private var _binding: FragmentDetalheBinding? = null
    private val binding get() = _binding!!
    private var idProduto: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { idProduto = it.getInt("id") }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val detalheViewModel = ViewModelProvider(this,
            DetalheViewModel.Factory(requireContext())
        )[DetalheViewModel::class.java]

        _binding = FragmentDetalheBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val activity = (activity as MainActivity)

        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        activity.supportActionBar?.run {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        lifecycleScope.launch {
            idProduto?.run {
                detalheViewModel.produto(this).observe(viewLifecycleOwner) {
                    binding.tvProduto.text = it.nome
                    binding.tvDescricao.text = it.descricao
                    binding.tvCultura.text = it.culturas
                    binding.tvArea.text = it.area
                    Glide.with(this@DetalheFragment)
                        .load(URL_Capa + it.capa)
                        .fitCenter()
                        .into(binding.capaDetalhe)

                    binding.btOrcamento.setOnClickListener { _ ->
                        CoroutineScope(Dispatchers.IO).launch {
                            val mensagem = if(detalheViewModel.adicionaNoCarrinho(ProdutoMapper.toProdutoCarrinho(it))) {
                                "O produto foi adicionado na Cesta"
                            } else {
                                "O produto já está na Cesta!"
                            }
                            CoroutineScope(Dispatchers.Main).launch {
                                Snackbar.make(root,
                                    mensagem, Snackbar.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}