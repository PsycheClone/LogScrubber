pipeline {
  agent any
  stages {
    stage('error') {
      steps {
        git(url: 'https://gitlab.melexis.com/cbs/ape-functional-test', branch: 'master', poll: true)
      }
    }
  }
}