package br.senai.sp.informatica.trevo.ui.cliente

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import br.senai.sp.informatica.trevo.MainActivity
import br.senai.sp.informatica.trevo.R
import br.senai.sp.informatica.trevo.databinding.FragmentClienteBinding
import br.senai.sp.informatica.trevo.model.Cliente
import kotlinx.coroutines.launch

class ClienteFragment : Fragment() {
    private var _binding: FragmentClienteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val clienteViewModel = ViewModelProvider(this,
            ClienteViewModel.Factory(requireContext()))[ClienteViewModel::class.java]

        _binding = FragmentClienteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val activity = (activity as MainActivity)

        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        activity.supportActionBar?.run {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        lifecycleScope.launch {
            clienteViewModel.cliente().observe(viewLifecycleOwner) {
                binding.edNome.setText(it.nome)
                binding.edEmail.setText(it.email)
                binding.edFone.setText(it.telefone)

                binding.btSalvar.setOnClickListener {
                    val usuario = Cliente(
                        binding.edNome.text.toString(),
                        binding.edEmail.text.toString(),
                        binding.edFone.text.toString()
                    )
                    clienteViewModel.salvarDados(usuario)
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                        .navigateUp()
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