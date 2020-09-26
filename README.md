# Docker MC Instance Manager

A Kotlin application to manage instances of Docker MC

## Building and Running

Get access to the repo by asking wnuke (wnuke#1010 on Discord). Download the source code:

 - `git clone git@github.com:wnuke-dev/dockerized-minecraft-instance-manager`
 
If you want to contribute and you do not have an IDE you should download one, I recommend [IntelliJ IDEA CE](https://www.jetbrains.com/idea/).

#### Building:

 - `cd dockerized-minecraft-instance-manager`
 - `./gradlew build`
 
#### Running:
 
 - `cd build/distributions`
 - `unzip -o instance-manager.zip`
 - `./instance-manager/bin/instance-manager`
