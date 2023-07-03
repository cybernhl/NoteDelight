package com.softartdev.notedelight.shared.files

import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import okio.FileMetadata
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.buffer
import okio.use

abstract class FileRepo(private val fileSystem: FileSystem, zeroPath: Path) {
    private val upFolder = "🔙.."
    private val fileContent = "📖"
    private lateinit var currentFileDir: Path
    private lateinit var currentFiles: List<Path>
    private lateinit var currentFileNames: List<String>
    private val _fileListFlow: MutableStateFlow<List<String>> = MutableStateFlow(
        value = listOf("🔁loading...")
    )
    val fileListFlow: Flow<List<String>> = _fileListFlow

    init {
        goTo(file = zeroPath)
    }

    fun onItemClicked(fileName: String) = when (fileName) {
        upFolder -> goTo(file = requireNotNull(currentFileDir.parent))
        fileContent -> {
            _fileListFlow.value = _fileListFlow.value + fileContent // FIXME
        }
        else -> {
            val index = currentFileNames.indexOf(fileName)
            if (index != -1) {
                val file = currentFiles[index]
                goTo(file)
            } else Napier.e("file not found: $fileName")
        }
    }

    private fun goTo(file: Path) {
        Napier.d("📂go to: $file")
        val metadata: FileMetadata = fileSystem.metadataOrNull(file) ?: return
        if (metadata.isDirectory) {
            currentFileDir = file
            currentFiles = fileSystem.list(dir = file)
            currentFileNames = currentFiles.map { curFile: Path ->
                val curMetadata = fileSystem.metadataOrNull(curFile) ?: return@map ""
                val icon = if (curMetadata.isDirectory) "📁" else "📄"
                return@map "$icon ${curFile.name}"
            }
        } else if (metadata.isRegularFile) {
            currentFileDir = file
            currentFiles = emptyList()
            currentFileNames = listOf(fileContent, readFile(file))
        } else {
            Napier.e("unknown file: $file")
            _fileListFlow.value = _fileListFlow.value + "❌ $metadata" // FIXME
        }
        val absolutePath = if (file.isAbsolute) file else fileSystem.canonicalize(file)
        _fileListFlow.value = listOf("📂${absolutePath.toString()}", upFolder) + currentFileNames
    }

    private fun readFile(file: Path): String {
        val stringBuilder = StringBuilder()
        fileSystem.source(file).use { fileSource ->
            fileSource.buffer().use { bufferedFileSource ->
                while (true) {
                    val line = bufferedFileSource.readUtf8Line() ?: break
                    stringBuilder.append(line)
                }
            }
        }
        return stringBuilder.toString()
    }
}