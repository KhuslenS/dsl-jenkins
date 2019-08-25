pipeline{
    agent any
    stages{
        stage("Run Command"){
            steps{
              sh '''
              echo Hello
              sudo yum  install httpd -y
              sudo ping 8.8.8.8
              '''
            }
        }
    }
}
