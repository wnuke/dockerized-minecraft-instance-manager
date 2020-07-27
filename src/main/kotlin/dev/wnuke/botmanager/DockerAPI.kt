package dev.wnuke.botmanager

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.Container
import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import com.github.dockerjava.transport.DockerHttpClient
import java.io.File
import kotlin.system.exitProcess


class DockerAPI {
    private val dockerClientConfig: DockerClientConfig

    private val httpClient: DockerHttpClient

    private val dockerClient: DockerClient

    init {
        try {
            dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost("unix:///var/run/docker.sock")
                    .build()
            httpClient = ApacheDockerHttpClient.Builder()
                    .dockerHost(dockerClientConfig.dockerHost)
                    .sslConfig(dockerClientConfig.sslConfig)
                    .build()
            dockerClient = DockerClientImpl.getInstance(dockerClientConfig, httpClient)
            dockerClient.pingCmd().exec()
        } catch (e: RuntimeException) {
            println("Cannot connect to Docker daemon, please verify it is running, exiting")
            exitProcess(1)
        }
    }

    fun buildBotImage(path: String) {
        val botBuildFolder = File(path)
        if (File(botBuildFolder.absolutePath + "/Dockerfile").exists()) {
            dockerClient.buildImageCmd(botBuildFolder)
                    .withTags(setOf("dockermcbot:managed"))
                    .exec(DockerBuildCallback())
                    .awaitCompletion()
        } else {
            println("${botBuildFolder.absolutePath} does not contain a Dockerfile, it is probably not the bot build folder.")
        }
    }

    fun getBotInstances(): HashSet<Container> {
        val allContainers = dockerClient.listContainersCmd().exec()
        val botContainers: HashSet<Container> = HashSet()
        for (container in allContainers) {
            if (container.image == "dockermcbot:managed") {
                botContainers.add(container)
            }
        }
        return botContainers
    }

    fun createBotInstance(username: String, password: String) {
        val namePrefix = "dockermcbotinst-"
        println("Creating new instance.")
        val ports: HashSet<Int> = HashSet()
        val names: HashSet<String> = HashSet()
        var port = 10000
        for (container in dockerClient.listContainersCmd().withShowAll(true).exec()) {
            for (portMap in container.ports) {
                if (portMap != null && portMap.publicPort != null) {
                    ports.add(portMap.publicPort!!)
                }
            }
            for (name in container.names) {
                names.add(name)
            }
        }
        while (ports.contains(port) || names.contains("/$namePrefix$port")) {
            port++
        }
        val tcp8000 = ExposedPort.tcp(8000)
        val portBindings = Ports()
        portBindings.bind(tcp8000, Ports.Binding.bindPort(port))
        if (dockerClient.listImagesCmd().withShowAll(true).withImageNameFilter("dockermcbot:managed").exec().isNotEmpty()) {
            val instanceID = dockerClient.createContainerCmd("dockermcbot:managed")
                    .withEnv("USERNAME=$username").withEnv("PASSWORD=$password")
                    .withExposedPorts(tcp8000)
                    .withName("$namePrefix$port")
                    .withHostConfig(HostConfig().withPortBindings(portBindings))
                    .exec().id
            dockerClient.startContainerCmd(instanceID).exec()
            println("Instance created with port $port.")
        } else {
            println("Bot image is not built, please run \"build <path>\" first.")
        }
    }
}

