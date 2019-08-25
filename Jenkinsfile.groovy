pipeline{
    agent any
    stages{
        stage("Run Command"){
            steps{
              sh '''
              set +xe
              echo Hello
              sudo yum  install httpd wget unzip -y
              '''
            }
        }
        stage("Download Terraform"){
            steps{
                ws("tmp/"){
                    script {
                        def exists = fileExists 'terraform_0.12.7_linux_amd64.zip'
                        if (exists) {
                            sh "unzip -o terraform_0.12.7_linux_amd64.zip"
                            sh "sudo mv terraform /bin"
                            sh "terraform version"
                        } else {
                            sh "wget https://releases.hashicorp.com/terraform/0.12.7/terraform_0.12.7_linux_amd64.zip"
                            sh "unzip -o terraform_0.12.7_linux_amd64.zip"
                            sh "sudo mv terraform /bin"
                        }
                    }
                }
            }
        }
        stage("write to a file"){
            steps{
                ws("tmp/"){
                    writeFile text: "Test", file: "Testfile"
                }
            }
        }
        stage("Download Packer"){
            steps{
                ws("tmp/"){
                    script {
                        def exists = fileExists 'packer_1.4.3_linux_amd64.zip'
                        if (exists) {
                            sh "unzip -o packer_1.4.3_linux_amd64.zip"
                            sh "rm -rf /sbin/packer"
                            sh "sudo mv packer /bin"
                            sh "packer version"
                        } else {
                            sh "wget https://releases.hashicorp.com/packer/1.4.3/packer_1.4.3_linux_amd64.zip"
                            sh "rm -rf /sbin/packer"
                            sh "unzip -o packer_1.4.3_linux_amd64.zip"
                            sh "sudo mv packer /bin"
                            sh "packer version"
                        }
                    }
                }
            }
        }
    }
}
