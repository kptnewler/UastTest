import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.StandardFileSystems
import com.intellij.psi.PsiManager

import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.uast.UFile
import org.jetbrains.uast.UastFacade
import java.io.File

 class ImportPsi2222 {

    fun parse() {
        val disposable = Disposer.newDisposable()
        val env = KotlinCoreEnvironment.createForProduction(
            parentDisposable = disposable,
            configuration = createConfiguration(),
            configFiles = EnvironmentConfigFiles.JVM_CONFIG_FILES
        )

        val project = env.project

        val virtualFile = StandardFileSystems.local().findFileByPath("/Users/newler/AndroidStudioProjects/tapapkchecker/simple/src/main/java/com/taptap/appchecker/MainActivity.kt") ?: return

        val psiFile = PsiManager.getInstance(project).findFile(virtualFile) ?: return

        val uFile = UastFacade.convertElementWithParent(psiFile, UFile::class.java) as? UFile ?: return

        uFile.imports.forEach {
            println(it.toString())
        }
    }

    private fun createConfiguration(): CompilerConfiguration {
        val configuration = CompilerConfiguration()
        configuration.put(
            CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY,
            PrintingMessageCollector(
                System.err,
                MessageRenderer.PLAIN_FULL_PATHS,
                false
            )
        )
        return configuration
    }
}