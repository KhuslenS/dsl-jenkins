pipeline{
    agent any
    stages{
        stage("Run Command"){
            steps{
              sh '''
              set +xe
              echo Hello
              ech Hello
              sudo yum  install httpd -y
              ping 8.8.8.8
              '''
            }
        }
    }
}
