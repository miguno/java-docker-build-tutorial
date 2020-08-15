pipleline {
   agent none
stages{
      stage('SCM Checkout'){
          agent {
              git credentialsId: 'GIT_CREDENTIALS', url:  'https://github.com/miguno/java-docker-build-tutorial.git',branch: 'master'
                }
                  }
      stage('Build') {
         agent {
              def mavenHome =  tool name: "Maven-3.6.1", type: "maven"
              def mavenCMD = "${mavenHome}/bin/mvn"
              sh "${mavenCMD} clean package"
                }
                     } 
                     }
           }

                    


