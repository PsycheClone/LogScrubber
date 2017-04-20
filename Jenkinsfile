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
        sh 'mvn clean'
      }
    }
  }
}