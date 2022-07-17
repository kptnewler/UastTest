import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.StandardFileSystems
import com.intellij.psi.PsiManager

import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UFile
import org.jetbrains.uast.UastFacade
import org.jetbrains.uast.visitor.AbstractUastVisitor
import org.jetbrains.uast.visitor.UastVisitor
import java.io.File

 class ImportPsi {

    fun parse() {
        val disposable = Disposer.newDisposable()
        val env = UastEnvironment.create(UastEnvironment.Configuration.create())

        val project = env.ideaProject

        val virtualFile = StandardFileSystems.local().findFileByPath("/Users/newler/IdeaProjects/UastTest/src/main/kotlin/ImportPsi.kt") ?: return

        val psiFile = PsiManager.getInstance(project).findFile(virtualFile) ?: return

        val uFile = UastFacade.convertElementWithParent(psiFile, UFile::class.java) as? UFile ?: return

        uFile.imports.forEach {
            it.accept(object : AbstractUastVisitor() {
                override fun visitElement(node: UElement): Boolean {
                    println(node.sourcePsi?.text)
                    return false
                }
            })
            println(it.importReference?.toString())
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