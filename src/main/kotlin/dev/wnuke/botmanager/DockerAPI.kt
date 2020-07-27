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

    fun buildBotImage(path: String): Boolean {
        try {
            val botBuildFolder = File(path)
            if (File(botBuildFolder.absolutePath + "/Dockerfile").exists()) {
                val imageID = dockerClient.buildImageCmd(botBuildFolder)
                        .withTags(setOf("dockermcbot:managed"))
                        .exec(DockerBuildCallback())
                        .awaitCompletion().awaitImageId()
                if (dockerClient.searchImagesCmd(imageID).exec().isNotEmpty()) {
                    return true
                }
            }
        } catch (_: Exception) {
        }
        return false
    }

    fun getBotInstances(): HashSet<Container> {
        try {
            val allContainers = dockerClient.listContainersCmd().withShowAll(true).exec()
            val botContainers: HashSet<Container> = HashSet()
            for (container in allContainers) {
                if (container.image == "dockermcbot:managed") {
                    botContainers.add(container)
                }
            }
            return botContainers
        } catch (_: Exception) {
        }
        return HashSet()
    }

    fun destroyAllBotInstances(): Boolean {
        return try {
            for (instance in getBotInstances()) {
                dockerClient.removeContainerCmd(instance.id).withForce(true).exec()
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    fun pruneBotInstances(): Boolean {
        return try {
            for (instance in getBotInstances()) {
                if (instance.state != "running") {
                    dockerClient.removeContainerCmd(instance.id).withForce(true).exec()
                }
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    fun destroyBotInstance(port: Int): Boolean {
        try {
            for (container in dockerClient.listContainersCmd().withShowAll(true).exec()) {
                for (portMap in container.ports) {
                    if (portMap != null && portMap.publicPort != null) {
                        if (portMap.publicPort == port) {
                            dockerClient.removeContainerCmd(container.id).withForce(true).exec()
                            return true
                        }
                    }
                }
            }
        } catch (_: Exception) {
        }
        return false
    }

    fun createBotInstance(username: String, password: String): Boolean {
        try {
            val namePrefix = "dockermcbotinst-"
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
                return true
            }
        } catch (_: Exception) {
        }
        return false
    }
}

