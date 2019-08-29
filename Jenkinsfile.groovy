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
                        def exists = fileExists 'terraform_0.11.9_linux_amd64.zip'
                        if (exists) {
                            sh "unzip -o terraform_0.11.9_linux_amd64.zip"
                            sh "sudo mv -f terraform /bin"
                            sh "terraform version"
                        } else {
                            sh "wget https://releases.hashicorp.com/terraform/0.11.9/terraform_0.11.9_linux_amd64.zip"
                            sh "unzip -o terraform_0.11.9_linux_amd64.zip"
                            sh "sudo mv -f terraform /bin"
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
                            sh "sudo mv -f packer /sbin"
                            sh "packer version"
                        } else {
                            sh "wget https://releases.hashicorp.com/packer/1.4.3/packer_1.4.3_linux_amd64.zip"
                            sh "unzip -o packer_1.4.3_linux_amd64.zip"
                            sh "sudo mv -f packer /sbin"
                            sh "packer version"
                        }
                    }
                }
            }
        }
        stage("Pull Repo"){
            steps{
                git("https://github.com/Khuslentuguldur/Packer.git")
                sh "ls"
            }
        }
        stage("Build Image"){
            steps{
                // sh "packer build updated/updated.json"
                sh "ls"
            }
        }
        stage("Clone VPC repo"){
            steps{
                ws("terraform/"){
                    git "https://github.com/Khuslentuguldur/infrastructure-terraform.git"

                }
            }
        }
        stage("Terraform "){
            steps{
                ws("terraform/"){
                    sh "terraform init"
                    sh "terraform plan -var-file=dev.tfvars"

                }
            }
        }

    }
    post{
      success {
          echo "Done"
        }
        failure {
          mail to:  "farrukhsadyko@gmail.com", subject: “job”, body: "job completed"
        }
      }
    }
