pipeline{
    agent any

    stages {
        stage('Build') {
            steps {
                script {
                    FAILED_STAGE=env.STAGE_NAME
                    bat 'mvn -B -DskipTests clean package'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    FAILED_STAGE=env.STAGE_NAME
                    bat 'mvn clean test'
                }
            }
        }
    }
    post {
        failure {
            emailext (
                attachLog: true,
                subject: "Build Failure. ${currentBuild.currentResult}: Job ${env.JOB_NAME}",
                body: "Failed stage: ${FAILED_STAGE}",
                recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']]
            )
        }
    }
}