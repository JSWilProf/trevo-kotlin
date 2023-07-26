package br.senai.sp.informatica.trevo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.core.view.MenuProvider
import androidx.core.widget.doAfterTextChanged
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.senai.sp.informatica.trevo.MainActivity
import br.senai.sp.informatica.trevo.R
import br.senai.sp.informatica.trevo.databinding.ContentHomeBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : Fragment() {
    private var _binding: ContentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private var job: Job? = null
    private var pesquisaAtual = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val activity = (activity as MainActivity)

        activity.setSupportActionBar(binding.appBarMain.toolbar)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = container?.let { Navigation.findNavController(it) }!!

        val navHeader = navView.getHeaderView(0)
        navHeader.findViewById<TextView>(R.id.menu_proposta).setOnClickListener {
            navController.navigate(R.id.nav_proposta)
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        navHeader.findViewById<TextView>(R.id.menu_cliente).setOnClickListener {
            navController.navigate(R.id.nav_cliente)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        val toggle = ActionBarDrawerToggle(activity,
            drawerLayout, binding.appBarMain.toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeViewModel = ViewModelProvider(this,
            HomeViewModel.Factory(requireContext()))[HomeViewModel::class.java]

        activity?.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if(menuItem.itemId == R.id.ac_orcamento) {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (!homeViewModel.existemProdutosNoOrcamento()) {
                            CoroutineScope(Dispatchers.Main).launch {
                                AlertDialog.Builder(requireActivity())
                                    .setMessage("O Orçamento não contém produtos")
                                    .setPositiveButton("Ok") { _, _ ->
                                        navController.navigateUp()
                                    }
                                    .create()
                                    .show()
                            }
                        } else {
                            CoroutineScope(Dispatchers.Main).launch {
                                navController.navigate(R.id.nav_orcamento)
                            }
                        }
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


        val lista: RecyclerView = binding.appBarMain.fragmentHome.lista
        lista.layoutManager = LinearLayoutManager(context)

        val adapter = ProdutosAdapter { produto ->
            navController.navigate(R.id.nav_detalhe, Bundle().also {
                it.putInt("id", produto.idProduto)
            })
        }
        lista.adapter = adapter

        binding.appBarMain.fragmentHome.edPesquisa.doAfterTextChanged {
            pesquisaAtual = it?.toString() ?: ""
            runPesquisa(homeViewModel, adapter)
        }

        binding.appBarMain.fragmentHome.btClear.setOnClickListener {
            binding.appBarMain.fragmentHome.edPesquisa.setText("")
            pesquisaAtual = ""
            runPesquisa(homeViewModel, adapter)
        }

        lifecycleScope.launch {
            homeViewModel.flow.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun runPesquisa(homeViewModel: HomeViewModel, adapter: ProdutosAdapter) {
        if (job != null) {
            job!!.cancel()
        }

        job = lifecycleScope.launch {
            delay(500L)
            homeViewModel.pesquisar(pesquisaAtual)
            adapter.refresh()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}