pipeline{
    agent any

    triggers {
        pollSCM('')
    }

    environment {
        COMMITTER_EMAIL = bat (
            script: 'git --no-pager show -s --format=%%ae',
            returnStdout: true
        ).trim()
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

    post {
        success {
            emailext (
                attachLog: true,
                subject: "Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}",
                body: "Please go to ${env.BUILD_URL} for more details.",
                to: "${COMMITTER_EMAIL}"
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