
pipeline {
    agent any
    
    environment {
		DOCKERHUB_CREDENTIALS=credentials('dockerhub') 
	}

    tools {
        maven "maven3" // name of the maven configured in the Global Tools Configuration page
    }

    stages {
         stage('get_commit_msg') {
	    steps {
		script {
		    env.GIT_COMMIT_MSG = sh (script: 'git log -1 --pretty=%B ${GIT_COMMIT}', returnStdout: true).trim()
	            env.CONTAINS_MSG = env.GIT_COMMIT_MSG.contains("#build")  		     
		}
		echo "commit message: ${GIT_COMMIT_MSG}"
		echo "Run pipeline ? : ${CONTAINS_MSG}" 		
	    }
	}
	stage('Abort pipeline Stage') {
            when {
                expression { !env.GIT_COMMIT_MSG.contains("#build") }
            }
            steps {
                echo "Not running the rest of the pipeline"
		error("Not running the rest of the pipeline") 
            }
        }
	
         stage('Checking variables'){
             steps {
                 sh 'printenv'                
             }	      
        }
	 stage('Checking docker,java'){
             steps {
                sh "docker ps"                                                      
                sh '''
                env | grep -e PATH -e JAVA_HOME
                which java
                java -version
                '''
                sh "mvn -version"	
	     }
        }       
        stage("Build") {
            steps {               
                sh "mvn clean install -Dmaven.test.skip=true"
            }
        }
        stage("Test") {
            steps {
                sh "mvn test"                
            }
        }
        stage("Build docker image") {
            steps {
                sh 'docker build -t rodrigofgs/customer-rewards:""$BUILD_ID"" .'                
            }
        }
        stage('Login') {
            steps {
		sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
	    }
	}
        
        stage('Push') {
	    steps {
		sh 'docker push rodrigofgs/customer-rewards:""$BUILD_ID""'
	      }
	}
    }

    post {
        failure {
            mail to: "estudantecomp@gmail.com",
            subject: "Jenkins pipeline containing errors",
            body: "Test"                       
        }
        always {
			sh 'docker logout'
		}
    }
    
}
