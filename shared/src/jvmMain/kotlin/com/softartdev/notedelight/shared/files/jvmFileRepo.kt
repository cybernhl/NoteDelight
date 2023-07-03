package com.softartdev.notedelight.shared.files

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import java.io.File

class JvmFileRepo : FileRepo(fileSystem = FileSystem.SYSTEM, zeroPath = zeroPath()) {

    companion object {

        private fun zeroPath(): Path {
            val absoluteFile = File("").absoluteFile
            return absoluteFile.path.toPath()
        }
    }
}