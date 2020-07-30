pipeline {
  agent {
    docker 'gradle:latest'
  }
  stages {
    stage('Gradle Build') {
      steps {
        withGradle {
            sh './gradlew distzip'
        }
      }
    }
  }
  post {
    always {
      archiveArtifacts artifacts: "nuke-bot-${env.BUILD_NUMBER}.jar", fingerprint: true, followSymlinks: false, onlyIfSuccessful: true
      script {
        def artifactUrl = env.BUILD_URL + "artifact/"
        def msg = "**Status:** " + currentBuild.currentResult.toLowerCase() + "\n"
        msg += "**Changes:** \n"
        if (!currentBuild.changeSets.isEmpty()) {
            currentBuild.changeSets.first().getLogs().each {
                msg += "- `" + it.getCommitId().substring(0, 8) + "` *" + it.getComment().substring(0, it.getComment().length()-1) + "*\n"
            }
        } else {
            msg += "no changes for this run\n"
        }
        if (msg.length() > 1024) msg.take(msg.length() - 1024)

        withCredentials([string(credentialsId: 'discord-webhook', variable: 'discordWebhook')]) {
            discordSend thumbnail: "http://wnuke.dev/radiation-symbol.png", successful: currentBuild.resultIsBetterOrEqualTo('SUCCESS'), description: "${msg}", link: env.BUILD_URL, title: "docker-mc-instance-manager #${BUILD_NUMBER}", webhookURL: "${discordWebhook}"
        }
      }
    }
  }
}