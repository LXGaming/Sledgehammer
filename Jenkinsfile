pipeline {
    agent any

    triggers {
        githubPush()
    }

    stages {
        stage("prepare") {
            steps {
                sh "chmod +x ./gradlew"
            }
        }

        stage("build") {
            steps {
                sh "./gradlew build --console=plain --refresh-dependencies"
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: "build/libs/*.jar", fingerprint: true, onlyIfSuccessful: true
        }
    }
}