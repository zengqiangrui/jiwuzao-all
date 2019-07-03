pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
                sh 'cd major;mvn dockerfile:build'
                sh 'cd manager;mvn dockerfile:build'
            }
        }
        stage('Deliver') {
            steps {
                //sh '/home/jenkins/scripts/deliver.sh'
                sh 'java -version'
            }
        }
    }
}
