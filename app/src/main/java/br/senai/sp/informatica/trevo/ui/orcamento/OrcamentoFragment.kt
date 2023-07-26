package br.senai.sp.informatica.trevo.ui.orcamento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.senai.sp.informatica.trevo.MainActivity
import br.senai.sp.informatica.trevo.R
import br.senai.sp.informatica.trevo.databinding.FragmentOrcamentoBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OrcamentoFragment : Fragment() {
    private var _binding: FragmentOrcamentoBinding? = null
    private val binding get() = _binding!!
    private var _orcamentoViewModel: OrcamentoViewModel? = null
    private val orcamentoViewModel get() = _orcamentoViewModel!!
    private var _navigationController: NavController? = null
    private val navigationController get() = _navigationController!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrcamentoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val activity = (activity as MainActivity)
        _navigationController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)

        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        activity.supportActionBar?.run {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        _orcamentoViewModel = ViewModelProvider(this,
            OrcamentoViewModel.Factory(requireContext()))[OrcamentoViewModel::class.java]
        val lista: RecyclerView = binding.lista
        lista.layoutManager = LinearLayoutManager(context)

        val adapter = OrcamentoAdapter {
            AlertDialog.Builder(requireActivity())
                .setMessage("Confirma a exclusão deste Produto?")
                .setPositiveButton("Sim") { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        orcamentoViewModel.removeItem(it.idProduto)
                    }
                }
                .setNegativeButton("Não") {_, _ -> }
                .create()
                .show()
        }
        lista.adapter = adapter

        lifecycleScope.launch {
            orcamentoViewModel.flow.collectLatest {
                adapter.submitData(it)
            }
        }

        binding.btSolicitacao.setOnClickListener { _ ->
            if(!orcamentoViewModel.existeCadastro()) {
                navigationController.navigate(R.id.nav_cliente)
            }
            if(orcamentoViewModel.existeCadastro()) {
                CoroutineScope(Dispatchers.IO).launch {
                    if(orcamentoViewModel.existemProdutosNoOrcamento()) {
                        val mensagem = if (orcamentoViewModel.enviaSolicitacao()) {
                            "A solicitação foi encaminhada com sucesso"
                        } else {
                            "Houve falha no envio da solicitação!"
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            AlertDialog.Builder(requireActivity())
                                .setMessage(mensagem)
                                .setPositiveButton("Ok") { _, _ ->
                                    navigationController.navigateUp()
                                }
                                .create()
                                .show()
                        }
                    } else {
                        Snackbar.make(root, "O Orçamento não contém produtos", Snackbar.LENGTH_LONG).show()
                    }
                }
            } else {
                Snackbar.make(root, "É necessário informar seus dados", Snackbar.LENGTH_LONG).show()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}