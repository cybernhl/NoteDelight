package com.softartdev.notedelight.shared.files

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUserDomainMask

class IosFileRepo : FileRepo(fileSystem = FileSystem.SYSTEM, zeroPath = zeroPath()) {

    companion object {

        private fun zeroPath(): Path {
            val paths: List<*> = NSSearchPathForDirectoriesInDomains(
                directory = NSApplicationSupportDirectory,
                domainMask = NSUserDomainMask,
                expandTilde = true
            )
            val zeroPath: NSString = paths.first() as NSString
            return zeroPath.toString().toPath()
        }
    }
}