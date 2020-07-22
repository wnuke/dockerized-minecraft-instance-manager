package dev.wnuke.botmanager

import com.github.dockerjava.api.model.Container
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import com.github.dockerjava.transport.DockerHttpClient
import java.io.File
import kotlin.system.exitProcess


class DockerAPI() {
    private val dockerClientConfig: DockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
            .withDockerHost("unix:///var/run/docker.sock")
            .build()

    private val httpClient: DockerHttpClient = ApacheDockerHttpClient.Builder()
            .dockerHost(dockerClientConfig.dockerHost)
            .sslConfig(dockerClientConfig.sslConfig)
            .build()

    private val dockerClient = DockerClientImpl.getInstance(dockerClientConfig, httpClient)

    fun buildBotImage(path: String) {
        val botBuildFolder: File = File(path)
        if (File(botBuildFolder.absolutePath + "/Dockerfile").exists()) {
            dockerClient.buildImageCmd(botBuildFolder).withTags(setOf("dockermcbot:managed")).exec(DockerBuildCallback()).awaitCompletion()
        } else {
            println("${botBuildFolder.absolutePath} does not contain a Dockerfile, it is probably not the bot build folder.")
        }
    }

    fun getBotInstances(): HashSet<Container> {
        val allContainers = dockerClient.listContainersCmd().exec()
        val botContainers: HashSet<Container> = HashSet()
        for (container in allContainers) {
            if (container.image.equals("dockermcbot:managed")) {
                botContainers.add(container)
            }
        }
        return botContainers
    }

    fun printContainers() {
        for (container in dockerClient.listContainersCmd().withShowAll(true).exec()) {
            println("id: " + container.id + " image: " + container.image)
        }
    }

    fun createBotInstance() {

    }
}