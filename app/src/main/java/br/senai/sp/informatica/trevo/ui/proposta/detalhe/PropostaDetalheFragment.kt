package br.senai.sp.informatica.trevo.ui.proposta.detalhe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.senai.sp.informatica.trevo.MainActivity
import br.senai.sp.informatica.trevo.databinding.FragmentPropostaProdutoBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PropostaDetalheFragment : Fragment() {
    private var _binding: FragmentPropostaProdutoBinding? = null
    private val binding get() = _binding!!
    private var idProposta: Int? = null
    private var produtos: List<Int>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idProposta = it.getInt("id")
            produtos = it.getIntArray("produtos")?.toList()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPropostaProdutoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val activity = (activity as MainActivity)

        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        activity.supportActionBar?.run {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.tvOrcamento.text = "Orçamento nº $idProposta"

        val propostaDetalheViewModel = ViewModelProvider(
            this@PropostaDetalheFragment,
            PropostaDetalheViewModel.Factory(produtos!!)
        )[PropostaDetalheViewModel::class.java]
        val lista: RecyclerView = binding.lista
        lista.layoutManager = LinearLayoutManager(context)

        val adapter = PropostaDetalheAdapter()
        lista.adapter = adapter

        lifecycleScope.launch {
            propostaDetalheViewModel.flow.collectLatest {
                adapter.submitData(it)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}