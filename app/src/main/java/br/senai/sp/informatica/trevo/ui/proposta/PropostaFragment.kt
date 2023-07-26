package br.senai.sp.informatica.trevo.ui.proposta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.senai.sp.informatica.trevo.MainActivity
import br.senai.sp.informatica.trevo.R
import br.senai.sp.informatica.trevo.databinding.FragmentPropostaBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PropostaFragment : Fragment() {
    private var _binding: FragmentPropostaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPropostaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val activity = (activity as MainActivity)
        val navigationController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)

        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        activity.supportActionBar?.run {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        val propostaViewModel = ViewModelProvider(
            this@PropostaFragment,
            PropostaViewModel.Factory(requireContext())
        )[PropostaViewModel::class.java]
        val lista: RecyclerView = binding.lista
        lista.layoutManager = GridLayoutManager(context, 2)

        val adapter = PropostaAdapter { proposta ->
            navigationController.navigate(R.id.nav_detalhe_proposta, Bundle().also {
                it.putInt("id", proposta.idProposta)
                it.putIntArray("produtos", proposta.produtos.toIntArray())
            })
        }
        lista.adapter = adapter

        lifecycleScope.launch {
            propostaViewModel.flow.collectLatest {
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