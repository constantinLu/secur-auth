pipeline{
    agent any

    triggers {
        pollSCM('')
    }

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

    COMMITTER_EMAIL = bat (
        script: "git --no-pager show -s --format=\'%ae\'",
        returnStdout: true
    ).trim()

    environment {
        EMAIL_TO = "${COMMITTER_EMAIL}"
    }

    post {
        success {
            emailext (
                attachLog: true,
                subject: "Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}",
                body: "Please go to ${env.BUILD_URL} for more details.",
                to: "${EMAIL_TO}"
            )
        }

        failure {
            emailext (
                attachLog: true,
                subject: "Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}",
                body: "Failed stage: ${FAILED_STAGE} . Please go to ${env.BUILD_URL} for more details.",
                to: "cosmina.iacob94@gmail.com"
            )
        }
    }
}