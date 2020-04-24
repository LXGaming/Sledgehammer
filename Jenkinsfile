pipeline {
    agent any

    stages {
        stage("prepare") {
            steps {
                sh "chmod +x ./gradlew"
            }
        }

        stage("build") {
            steps {
                sh "./gradlew --refresh-dependencies --stacktrace clean build"
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: "build/libs/*.jar", fingerprint: true, onlyIfSuccessful: true
        }
    }
}