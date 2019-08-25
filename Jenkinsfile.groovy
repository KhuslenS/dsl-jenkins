pipeline{
    agent any
    stages{
        stage("Run Command"){
            steps{
              sh '''
              echo Hello
              yum  install httpd -y
              ping 8.8.8.8
              '''
            }
        }
    }
}
