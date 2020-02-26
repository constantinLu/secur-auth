pipeline{
    agent any

    stages {
        stage('Build') {
            steps {
                FAILED_STAGE=env.STAGE_NAME
                bat 'mvn -B -DskipTests clean package'
            }
        }

        stage('Test') {
            steps {
                FAILED_STAGE=env.STAGE_NAME
                bat 'mvn test'
            }
        }
    }
    post {
        failure {
            emailext (
                subject: 'Test',
                body: 'Build failed at stage ' ${FAILED_STAGE},
                recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']]
            )
        }
    }
}