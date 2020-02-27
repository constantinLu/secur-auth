pipeline{
    agent any

    triggers {
        pollSCM('*/1 * * * *')
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
        failure {
            emailext (
                attachLog: true,
                subject: "Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}",
                body: "Failed stage: ${FAILED_STAGE} . Please go to ${env.BUILD_URL} for more details.",
                to: "cosmina.iacob94@gmail.com, lunguucatalin@gmail.com, beatricedtoma@gmail.com, mada.magdalena97@gmail.com"
            )
        }
    }
}