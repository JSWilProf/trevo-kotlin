package br.senai.sp.informatica.trevo

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.senai.sp.informatica.trevo.service.TrevoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    companion object {
        var trevoSerice = TrevoService.create()
    }

    @Test
    fun listaProduto20() {
        CoroutineScope(Dispatchers.IO).launch {
            val produto = trevoSerice.getProduto(20).singleOrNull()
            assertTrue("O Produto não deveria ser nulo", produto != null)
        }
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("br.senai.sp.informatica.trevo", appContext.packageName)
    }
}