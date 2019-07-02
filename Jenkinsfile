pipeline {
    agent none
    stages {
        stage('Build') {
            agent {
                docker {
                    image 'maven:3-alpine'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Deliver') {
            agent {
                docker {
                    image 'openjdk:8-jre'
                    args '-P -v /root/jenkins/workspace/jiwuzao:/home -v /tmp/jiwuzao-log:/tmp/jiwuzao-log'
                }
            }
            steps {
                //sh '/home/jenkins/scripts/deliver.sh'
                sh 'java -version'
            }
        }
    }
}
