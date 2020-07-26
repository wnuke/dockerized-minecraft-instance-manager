package dev.wnuke.botmanager

import com.github.dockerjava.api.async.ResultCallbackTemplate
import com.github.dockerjava.api.command.BuildImageResultCallback
import com.github.dockerjava.api.model.BuildResponseItem
import java.io.Closeable

class DockerBuildCallback : ResultCallbackTemplate<BuildImageResultCallback, BuildResponseItem>() {
    override fun onComplete() {
        println("Build finished.")
        close()
    }

    override fun onNext(`object`: BuildResponseItem?) {
        if (`object` != null && `object`.stream != null) {
            print(`object`.stream)
        }
    }

    override fun onError(throwable: Throwable?) {
        println("Build failed:")
        if (throwable != null) {
            println(throwable.localizedMessage)
        }
    }

    override fun onStart(closeable: Closeable?) {
        println("Starting the build...")
    }
}