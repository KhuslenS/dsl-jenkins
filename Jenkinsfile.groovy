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
        stage("Install Terraform in /tmp")
            steps{
              ws("/tmp"){
                sh "pwd"
              }
              // cd /tmp
              // yum install wget -y
              // wget https://releases.hashicorp.com/terraform/0.12.7/terraform_0.12.7_linux_amd64.zip
              // unzip terraform_0.12.7_linux_amd64.zip
              // mv terraform /bin

            }
            }
    }
}
