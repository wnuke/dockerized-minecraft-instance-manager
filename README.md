[![Discord](https://img.shields.io/discord/745728805678874800?logo=discord)](https://discord.gg/MwBvhEz)
[![build](https://github.com/wnuke-dev/dockerized-minecraft-instance-manager/workflows/Java%20CI%20with%20Gradle/badge.svg)]((https://github.com/wnuke-dev/dockerized-minecraft-instance-manager/actions?query=workflow%3A%22Java%20CI%20with%20Gradle%22))
# Docker MC Instance Manager

A Kotlin application to manage instances of Docker MC

## Building and Running

Download the source code:

 - `git clone git@github.com:wnuke-dev/dockerized-minecraft-instance-manager`

#### Building:

 - `cd dockerized-minecraft-instance-manager`
 - `./gradlew build`
 
#### Running:
 
 - `cd build/distributions`
 - `unzip -o instance-manager.zip`
 - `./instance-manager/bin/instance-manager`
