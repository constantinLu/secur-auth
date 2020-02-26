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
                    bat 'mvn test'
                }
            }
        }
    }
    post {
        failure {
            emailext (
                subject: 'Test',
                body: ${FAILED_STAGE},
                recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']]
            )
        }
    }
}