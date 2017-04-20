pipeline {
  agent any
  stages {
    stage('Checkout') {
      steps {
        git(url: 'git@gitlab.melexis.com:cbs/ape-functional-test.git', branch: 'master', poll: true, credentialsId: 'jenkins')
      }
    }
    stage('Build') {
      steps {
        sh 'mvn clean install -DskipTests'
      }
    }
    stage('') {
      steps {
        sh '''cd ape-adapter
sudo docker build --no-cache -t dsl.melexis.com:5000/ape/$module:$dockerContainerVersion .'''
      }
    }
  }
}