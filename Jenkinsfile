pipeline {
    agent any
    options {
        timeout(time: 1, unit: 'HOURS') // set timeout 1 hour
    }
    environment {
        REPOSITORY_CREDENTIAL_ID = 'gitlab-jenkins-key'
        REPOSITORY_URL = 'git@git.swmgit.org:swm-13-main/13_swm56/belloga-raw-data-service.git'
        TARGET_BRANCH = 'master'

        CONTAINER_NAME = 'belloga-raw-data-service'

        SWAGGER_ID = 'belloga-swagger'
        OPEN_API_SPEC_NAME = 'open-api-3-raw-data-service.json'

        AWS_CREDENTIAL_NAME = 'belloga-aws'
        ECR_PATH = '023778162658.dkr.ecr.ap-northeast-2.amazonaws.com'
        IMAGE_NAME = '023778162658.dkr.ecr.ap-northeast-2.amazonaws.com/belloga-raw-data-service'
        REGION = 'ap-northeast-2'
    }
    stages{
        stage('init') {
            steps {
                echo 'init stage'
                deleteDir()
            }
            post {
                success {
                    echo 'success init in pipeline'
                }
                failure {
                    error 'fail init in pipeline'
                }
            }
        }
        stage('clone project') {
            steps {
                git url: "$REPOSITORY_URL",
                    branch: "$TARGET_BRANCH",
                    credentialsId: "$REPOSITORY_CREDENTIAL_ID"
                sh "ls -al"
            }
            post {
                success {
                    echo 'success clone project'
                }
                failure {
                    error 'fail clone project' // exit pipeline
                }
            }
        }
        stage('create application-aws') {
            steps {
                dir('src/main/resources') {
				withAWSParameterStore(credentialsId: "${env.AWS_CREDENTIAL_NAME}",
              				path: "/belloga/raw-data/aws/local",
              				naming: 'basename',
              				regionName: 'ap-northeast-2') {

                        writeFile file: 'application-aws.yml', text: "${env.KEY}"
                    }
			    }
            }
            post {
                success {
                    echo 'success create application-aws'
                }
                failure {
                    error 'fail create application-aws'
                }
            }
        }
        stage('build project') {
            steps {
                sh '''
        		 ./gradlew bootJar
        		 '''
            }
            post {
                success {
                    echo 'success build project'
                }
                failure {
                    error 'fail build project' // exit pipeline
                }
            }
        }
        /*
        stage('generate api docs by spring rest docs and send to swagger ui') {
            steps {
                sh '''
        		 ./gradlew openapi3
        		 '''
        		 // swagger UI로 보내는 로직
        		 dir('src/main/resources/static/docs') {
                    sshagent (credentials: ["$SWAGGER_ID"]) { // use SSH Agent
                        sh """
                            ssh -o StrictHostKeyChecking=no ubuntu@10.0.135.208 '
                            sudo docker container ls
                            '
                        """
                        sh 'scp ./$OPEN_API_SPEC_NAME ubuntu@10.0.135.208:/home/ubuntu/docs' //지정 경로로 파일 이동시켜 문서 변경
                    }
        		 }
            }
            post {
                success {
                    echo 'success generate api docs'
                }
                failure {
                    error 'fail generate api docs' // exit pipeline
                }
            }
        }
        */
        // 도커 이미지를 만든다. build number로 태그를 주되 latest 태그도 부여한다.
        stage('dockerizing project') {
            steps {
                sh '''
        		 docker build -t $IMAGE_NAME:$BUILD_NUMBER .
        		 docker tag $IMAGE_NAME:$BUILD_NUMBER $IMAGE_NAME:latest
        		 '''
            }
            post {
                success {
                    echo 'success dockerizing project'
                }
                failure {
                    error 'fail dockerizing project' // exit pipeline
                }
            }
        }
        // 빌드넘버 태그와 latest 태그 둘 다 올린다.
        stage('upload aws ECR') {
            steps {
                script{
                    // cleanup current user docker credentials
                    sh 'rm -f ~/.dockercfg ~/.docker/config.json || true'

                    docker.withRegistry("https://${ECR_PATH}", "ecr:${REGION}:${AWS_CREDENTIAL_NAME}") {
                      docker.image("${IMAGE_NAME}:${BUILD_NUMBER}").push()
                      docker.image("${IMAGE_NAME}:latest").push()
                    }
                }
            }
            post {
                success {
                    echo 'success upload image'
                }
                failure {
                    error 'fail upload image' // exit pipeline
                }
            }
        }
        stage('kubectl apply') {
            steps {
                sshagent (credentials: ['bestion-ssh']) { // use SSH Agent
                sh """
                    ssh -o StrictHostKeyChecking=no ubuntu@43.200.60.243 '
                    kubectl set image deploy raw-data-service-v1 raw-data-service=${IMAGE_NAME}:${BUILD_NUMBER}
                    '
                """
                }
            }
            post {
                success {
                    echo 'success kubectl rolling update'
                }
                failure {
                    error 'fail kubectl rolling update' // exit pipeline
                }
            }
        }
    }
}
