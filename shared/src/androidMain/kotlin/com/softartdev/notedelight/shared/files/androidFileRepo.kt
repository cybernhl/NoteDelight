package com.softartdev.notedelight.shared.files

import android.content.Context
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

class AndroidFileRepo(context: Context) : FileRepo(
    fileSystem = FileSystem.SYSTEM,
    zeroPath = zeroPath(context)
) {

    companion object {

        private fun zeroPath(context: Context): Path {
            val filesDir = context.filesDir
            return filesDir.path.toPath()
        }
    }
}