package dev.wnuke.botmanager.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.Container
import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.Ports
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import com.github.dockerjava.transport.DockerHttpClient
import java.io.File
import java.io.IOException
import java.net.ServerSocket
import kotlin.system.exitProcess


/**
 * Client to interact with the Docker API to manage instances of the Minecraft bot
 */
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

    /**
     * Gets the Docker container object of an instance by it's port
     *
     * @param instancePort  Port of the instance to get
     * @return The Docker container object for that instance
     */
    fun getInstanceByPort(instancePort: Int): Container? {
        for (instance in getBotInstances()) {
            if ((instance.ports.getOrNull(0) ?: continue).publicPort == instancePort) return instance
        }
        return null
    }

    /**
     * Checks whether an instance exists with a port
     *
     * @param port  Port to check
     * @return Whether or not there is an instance on that port
     */
    fun instanceExists(port: Int): Boolean {
        if (try {
                    ServerSocket(port).close()
                    false
                } catch (_: IOException) {
                    true
                }) {
            for (container in getBotInstances()) {
                for (containerPort in container.ports) {
                    if (containerPort.publicPort == port) {
                        return true
                    }
                }
            }

        }
        return false
    }

    /**
     * Builds a Docker image using the Dockerfile in a directory, also uses the directory as build context
     *
     * @param path  Folder to use as build context, should also contain Dockerfile
     */
    fun buildBotImage(path: String) {
        try {
            val botBuildFolder = File(path)
            if (File(botBuildFolder.absolutePath + "/Dockerfile").exists()) {
                dockerClient.buildImageCmd(botBuildFolder)
                        .withTags(setOf("dockermcbot:managed"))
                        .exec(DockerBuildCallback())
                        .awaitCompletion()
            }
        } catch (_: Exception) {
        }
    }

    /**
     * Gets all Docker Containers that are instances of the bot
     *
     * @return HashSet of Containers
     */
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

    /**
     * Destroys all Docker Containers that are instances of the bot
     */
    fun destroyAllBotInstances() {
        try {
            for (instance in getBotInstances()) {
                dockerClient.removeContainerCmd(instance.id).withForce(true).exec()
            }
        } catch (_: Exception) {
        }
    }

    /**
     * Destroys all stopped Docker containers that are instances of the bot
     */
    fun pruneBotInstances() {
        return try {
            for (instance in getBotInstances()) {
                if (instance.state != "running") {
                    dockerClient.removeContainerCmd(instance.id).withForce(true).exec()
                }
            }
        } catch (_: Exception) {
        }
    }

    /**
     * Destroys the Docker container that contains an instance
     *
     * @param port  Port of the instance to destroy
     * @return Whether or not an instance was found and destroyed
     */
    fun destroyBotInstance(port: Int): Boolean {
        try {
            for (container in getBotInstances()) {
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

    /**
     * Creates an instance of the bot
     *
     * @param username  Username for the bot to launch with, if empty it will be a random string
     * @param password  Password to login with, if empty or invalid the bot will use offline mode
     * @return Whether or not the creation of an instance was successful
     */
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

