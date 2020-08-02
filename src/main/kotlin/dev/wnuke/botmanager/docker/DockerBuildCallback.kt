package dev.wnuke.botmanager.docker

import com.github.dockerjava.api.async.ResultCallbackTemplate
import com.github.dockerjava.api.command.BuildImageResultCallback
import com.github.dockerjava.api.model.BuildResponseItem
import java.io.Closeable

/**
 * Callback for Docker image builds
 */
class DockerBuildCallback : ResultCallbackTemplate<BuildImageResultCallback, BuildResponseItem>() {
    /**
     * Executed when image build is finished
     */
    override fun onComplete() {
        println("Build finished.")
        close()
    }

    /**
     * Executed when build advances to another step
     */
    override fun onNext(`object`: BuildResponseItem?) {
        if (`object` != null && `object`.stream != null) {
            print(`object`.stream)
        }
    }

    /**
     * Executed when the build encounters an error
     */
    override fun onError(throwable: Throwable?) {
        println("Build failed:")
        if (throwable != null) {
            println(throwable.localizedMessage)
        }
    }

    /**
     * Executed when the build is started
     */
    override fun onStart(closeable: Closeable?) {
        println("Starting the build...")
    }
}