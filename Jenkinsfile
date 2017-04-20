pipeline {
  agent any
  stages {
    stage('Checkout') {
      steps {
        git(url: 'https://gitlab.melexis.com/cbs/ape-functional-test', branch: 'master', poll: true, credentialsId: 'jenkins')
      }
    }
  }
}