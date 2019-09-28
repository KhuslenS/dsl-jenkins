pipeline{
    agent any
    parameters {string(defaultValue: "plan", description: "plan, apply, destroy'', name: 'USER_ACTION')}
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
                            sh "sudo mv -f terraform /bin"
                            sh "terraform version"
                        } else {
                            sh "wget https://releases.hashicorp.com/terraform/0.12.7/terraform_0.12.7_linux_amd64.zip"
                            sh "unzip -o terraform_0.12.7_linux_amd64.zip"
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
        stage("Get module  "){
            steps{
                ws("terraform/"){
                    sh "terraform get"

                }
            }
        }
        stage("Initialize Terraform "){
            steps{
                ws("terraform/"){
                    sh "terraform init"
                }
            }
        }
        stage("Terraform Plan "){
            steps{
                ws("terraform/"){
                    sh "terraform ${USER_ACTION} -var-file=dev.tfvars --auto-approve "

                }
            }
        }

    }
    post{
      success {
          echo "Done"
        }
        failure {
          mail to:  "khuslen.cyber@gmail.com", subject: “job”, body: "job completed"
        }
      }
    }
