# 배포 및 실행

## 환경

- Java 17 JDK
- SpringBoot 3.2.1
- 서비스 포트번호: 443
- IDE
    - 인텔리제이 IDEA 2023.3.2
    - vscode 1.85.1

## 배포 시 사용되는 환경 변수

1. .env
2. application.yml

## 배포 및 실행 시 특이사항

1. /opt/openvidu의 openvidu 서버를 최초 실행 필요
2. backend/src/main/resources 위치에 application.yml 파일 저장
3. frontend 위치에 .env 파일 저장
4. docker-compose 혹은 docker 명령어를 통해 서비스 실행

## 프로퍼티가 정의된 파일 목록

- application.yml
    - application-dev.yml
    - application-local.yml
        - kakao 환경변수, JWT key
- .env
    - kakao 환경변수 파일
- Dockerfile
    - frontend/chipchippoker/dockerfile
    - frontend/chipchippoker/nginx/default.conf
    - frontend/openvidu-basic-node/dockerfile
    - backend/dockerfile
    - backend/docker-compose.yml
    - backend/prometheus.yml
    

## 배포 및 실행

실행을 위해서는 Jenkins 생략 가능합니다.

### 1. Docker-compose & Docker 설치

- docker-ce: Docker Community Edition의 약자
- docker-ce-cli: Docker Community Edition의 CLI 환경에서 추가로 설치해야 하는 패키지
- containerd.io: Docker 컨테이너 런타임

```bash
# Docker 설치
sudo apt-get -y install docker-ce docker-ce-cli containerd.io

# Docker-compose 설치
sudo curl -L "https://github.com/docker/compose/releases/download/v2.21.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# Docker-compose 권한부여
sudo chmod +x /usr/local/bin/docker-compose
```

### 2. Jenkins 설치 (선택)

```bash
# Jenkins 이미지 Pull
docker pull jenkins/jenkins:jdk17

# Jenkins Start
docker run -d --restart always --env JENKINS_OPTS=--httpPort=8080 -v /etc/localtime:/etc/localtime:ro -e TZ=Asia/Seoul -p 8080:8080 -v /jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -v /usr/local/bin/docker-compose:/usr/local/bin/docker-compose --name jenkins -u root jenkins/jenkins:jdk17
```

- 이후 Jenkins 플러그인 설치

### 3. GitLab Webhook 추가 ( Front, Back ) & Jenkins PipeLine (FreeStyle) ( 선택 )

- 깃허브 혹은 Gitlab, 각각의 Remote 저장소에서 Webhook 추가
- Jenkins 관리 > Credentials > Add Credentials에서 gitlab or github token 추가, 프로퍼티 파일 추가
- FrontEnd Pipeline
    
```groovy
pipeline {
    agent any

    stages {
        stage('Git Clone') {
            steps {
                git branch: 'frontend-develop',
                credentialsId: '{**Remote-Repoistory-Token**}',
                url: 'https://{**Repository-URL**}/{**ProjectName**}.git'
            }
        }
        stage('env file download') {
            steps {
                dir('frontend') {
                    dir('chipchippoker'){
                        withCredentials([file(credentialsId: '{**Env-File-Key-Name**}', variable: 'envFile')]) {
                            script {
                                if (fileExists('.env')) {
                                    sh 'sudo rm -r .env'
                                }
                                sh 'sudo cp $envFile .env'
                            }
                        }
                    }
                }
            }
        }
        stage('Container Stop & Image Delete') {
            steps {
                script{
                    sh "sudo docker stop vue-app-1"
                    sh "sudo docker image rm vue-app"
                }
            }
        }
        stage('Image Build & Start Container') {
            steps {
                dir('frontend') {
                    dir('chipchippoker'){
                        script {
                            sh "sudo docker buildx build --platform linux/amd64 -t vue-app ."
                            sh "sudo docker run -d -p 8887:80 --rm --name vue-app-1 vue-app"
                        }
                    }
                }
            }
        }
    }
}
```
    
- BackEnd Pipeline
    
```groovy
pipeline {
    agent any

    stages {
        stage('Git Clone') {
            steps {
                git branch: 'backend-develop',
                credentialsId: '{**Remote-Repoistory-Token**}',
                url: 'https://{**Repository-URL**}/{**ProjectName**}.git'
            }
        }
        stage('applicaion-dev.yml download') {
            steps {
                dir('backend') {
                    withCredentials([file(credentialsId: '{**Application-File-Key-Name**}', variable: 'devYmlFile')]) {
                        script {
                            if (fileExists('src/main/resources/application-dev.yml')) {
                                sh 'sudo rm src/main/resources/application-dev.yml'
                            }
                            sh 'sudo cp $devYmlFile src/main/resources'
                        }
                    }
                }
            }
        }
        stage('Jar Build') {
            steps {
                dir('backend') {
                    sh 'sudo chmod +x ./gradlew'
                    sh 'sudo ./gradlew clean bootJar'
                    sh 'sudo pwd'
                }
            }
        }
        stage('Containers Stop') {
            steps {
                dir('backend') {
                    script {
                        sh 'sudo docker-compose down'
                    }
                }
            }
        }
        stage('Containers Start') {
            steps {
                dir('backend') {
                    script {
                        sh 'sudo docker-compose up --build -d'
                    }
                }
            }
        }
    }
}
```
    

### 4. Server Container(Docker-compose) 실행

```bash
sudo chmod +x ./gradlew
sudo ./gradlew clean bootJar

# 이미지 재 빌드후 실행 ( application.yml(시크릿 파일)추가 )
sudo cp $devYmlFile src/main/resources
sudo docker-compose up --build -d
# 컨테이너 종료
sudo docker-compose down
```

### 5. Signal Server & OpenVidu Container(Docker-compose) 실행

```bash
# 시그널 서버 실행
sudo docker build -t signal-server .
sudo docker run -d -p 8500:8500 --rm --name signal-server-1 signal-server

# OpenVidu Container 실행
sudo su
cd /opt
curl https://s3-eu-west-1.amazonaws.com/aws.openvidu.io/install_openvidu_latest.sh | bash
cd openvidu
nano .env
./openvidu start
```

- Openvidu .env
    
```bash
# Openvidu env
# OpenVidu configuration
# ----------------------
# Documentation: https://docs.openvidu.io/en/stable/reference-docs/openvidu-config/

# NOTE: This file doesn't need to quote assignment values, like most shells do.
# All values are stored as-is, even if they contain spaces, so don't quote them.

# Domain name. If you do not have one, the public IP of the machine.
# For example: 198.51.100.1, or openvidu.example.com
DOMAIN_OR_PUBLIC_IP=**{YOUR_DOMAIN_HERE}**

# OpenVidu SECRET used for apps to connect to OpenVidu server and users to access to OpenVidu Dashboard
OPENVIDU_SECRET=CCP_VIDU

# Certificate type:
# - selfsigned:  Self signed certificate. Not recommended for production use.
#                Users will see an ERROR when connected to web page.
# - owncert:     Valid certificate purchased in a Internet services company.
#                Please put the certificates files inside folder ./owncert
#                with names certificate.key and certificate.cert
# - letsencrypt: Generate a new certificate using letsencrypt. Please set the
#                required contact email for Let's Encrypt in LETSENCRYPT_EMAIL
#                variable.
CERTIFICATE_TYPE=letsencrypt

# If CERTIFICATE_TYPE=letsencrypt, you need to configure a valid email for notifications
LETSENCRYPT_EMAIL=**{YOUR_EMAIL_HERE}**

# Proxy configuration
# If you want to change the ports on which openvidu listens, uncomment the following lines

# Allows any request to http://DOMAIN_OR_PUBLIC_IP:HTTP_PORT/ to be automatically
# redirected to https://DOMAIN_OR_PUBLIC_IP:HTTPS_PORT/.
# WARNING: the default port 80 cannot be changed during the first boot
# if you have chosen to deploy with the option CERTIFICATE_TYPE=letsencrypt
# HTTP_PORT=80

# Changes the port of all services exposed by OpenVidu.
# SDKs, REST clients and browsers will have to connect to this port
# HTTPS_PORT=443

# Old paths are considered now deprecated, but still supported by default. 
# OpenVidu Server will log a WARN message every time a deprecated path is called, indicating 
# the new path that should be used instead. You can set property SUPPORT_DEPRECATED_API=false 
# to stop allowing the use of old paths.
# Default value is true
# SUPPORT_DEPRECATED_API=false

# If true request to with www will be redirected to non-www requests
# Default value is false
# REDIRECT_WWW=false

# How many workers to configure in nginx proxy. 
# The more workers, the more requests will be handled
# Default value is 10240
# WORKER_CONNECTIONS=10240

# Access restrictions
# In this section you will be able to restrict the IPs from which you can access to
# Openvidu API and the Administration Panel
# WARNING! If you touch this configuration you can lose access to the platform from some IPs.
# Use it carefully.

# This section limits access to the /dashboard (OpenVidu CE) and /inspector (OpenVidu Pro) pages.
# The form for a single IP or an IP range is:
# ALLOWED_ACCESS_TO_DASHBOARD=198.51.100.1 and ALLOWED_ACCESS_TO_DASHBOARD=198.51.100.0/24
# To limit multiple IPs or IP ranges, separate by commas like this:
# ALLOWED_ACCESS_TO_DASHBOARD=198.51.100.1, 198.51.100.0/24
# ALLOWED_ACCESS_TO_DASHBOARD=

# This section limits access to the Openvidu REST API.
# The form for a single IP or an IP range is:
# ALLOWED_ACCESS_TO_RESTAPI=198.51.100.1 and ALLOWED_ACCESS_TO_RESTAPI=198.51.100.0/24
# To limit multiple IPs or or IP ranges, separate by commas like this:
# ALLOWED_ACCESS_TO_RESTAPI=198.51.100.1, 198.51.100.0/24
# ALLOWED_ACCESS_TO_RESTAPI=

# Whether to enable recording module or not
OPENVIDU_RECORDING=false

# Use recording module with debug mode.
OPENVIDU_RECORDING_DEBUG=false

# Openvidu Folder Record used for save the openvidu recording videos. Change it
# with the folder you want to use from your host.
OPENVIDU_RECORDING_PATH=/opt/openvidu/recordings

# System path where OpenVidu Server should look for custom recording layouts
OPENVIDU_RECORDING_CUSTOM_LAYOUT=/opt/openvidu/custom-layout

# if true any client can connect to
# https://OPENVIDU_SERVER_IP:OPENVIDU_PORT/recordings/any_session_file.mp4
# and access any recorded video file. If false this path will be secured with
# OPENVIDU_SECRET param just as OpenVidu Server dashboard at
# https://OPENVIDU_SERVER_IP:OPENVIDU_PORT
# Values: true | false
OPENVIDU_RECORDING_PUBLIC_ACCESS=false

# Which users should receive the recording events in the client side
# (recordingStarted, recordingStopped). Can be all (every user connected to
# the session), publisher_moderator (users with role 'PUBLISHER' or
# 'MODERATOR'), moderator (only users with role 'MODERATOR') or none
# (no user will receive these events)
OPENVIDU_RECORDING_NOTIFICATION=publisher_moderator

# Timeout in seconds for recordings to automatically stop (and the session involved to be closed)
# when conditions are met: a session recording is started but no user is publishing to it or a session
# is being recorded and last user disconnects. If a user publishes within the timeout in either case,
# the automatic stop of the recording is cancelled
# 0 means no timeout
OPENVIDU_RECORDING_AUTOSTOP_TIMEOUT=120

# Maximum video bandwidth sent from clients to OpenVidu Server, in kbps.
# 0 means unconstrained
OPENVIDU_STREAMS_VIDEO_MAX_RECV_BANDWIDTH=1000

# Minimum video bandwidth sent from clients to OpenVidu Server, in kbps.
# 0 means unconstrained
OPENVIDU_STREAMS_VIDEO_MIN_RECV_BANDWIDTH=300

# Maximum video bandwidth sent from OpenVidu Server to clients, in kbps.
# 0 means unconstrained
OPENVIDU_STREAMS_VIDEO_MAX_SEND_BANDWIDTH=1000

# Minimum video bandwidth sent from OpenVidu Server to clients, in kbps.
# 0 means unconstrained
OPENVIDU_STREAMS_VIDEO_MIN_SEND_BANDWIDTH=300

# All sessions of OpenVidu will try to force this codec. If OPENVIDU_STREAMS_ALLOW_TRANSCODING=true
# when a codec can not be forced, transcoding will be allowed
# Values: MEDIA_SERVER_PREFERRED, NONE, VP8, VP9, H264
# Default value is MEDIA_SERVER_PREFERRED
# OPENVIDU_STREAMS_FORCED_VIDEO_CODEC=MEDIA_SERVER_PREFERRED

# Allow transcoding if codec specified in OPENVIDU_STREAMS_FORCED_VIDEO_CODEC can not be applied
# Values: true | false
# Default value is false
# OPENVIDU_STREAMS_ALLOW_TRANSCODING=false

# true to enable OpenVidu Webhook service. false' otherwise
# Values: true | false
OPENVIDU_WEBHOOK=false

# HTTP endpoint where OpenVidu Server will send Webhook HTTP POST messages
# Must be a valid URL: http(s)://ENDPOINT
#OPENVIDU_WEBHOOK_ENDPOINT=

# List of headers that OpenVidu Webhook service will attach to HTTP POST messages
#OPENVIDU_WEBHOOK_HEADERS=

# List of events that will be sent by OpenVidu Webhook service
# Default value is all available events
OPENVIDU_WEBHOOK_EVENTS=[sessionCreated,sessionDestroyed,participantJoined,participantLeft,webrtcConnectionCreated,webrtcConnectionDestroyed,recordingStatusChanged,filterEventDispatched,mediaNodeStatusChanged,nodeCrashed,nodeRecovered,broadcastStarted,broadcastStopped]

# How often the garbage collector of non active sessions runs.
# This helps cleaning up sessions that have been initialized through
# REST API (and maybe tokens have been created for them) but have had no users connected.
# Default to 900s (15 mins). 0 to disable non active sessions garbage collector
OPENVIDU_SESSIONS_GARBAGE_INTERVAL=900

# Minimum time in seconds that a non active session must have been in existence
# for the garbage collector of non active sessions to remove it. Default to 3600s (1 hour).
# If non active sessions garbage collector is disabled
# (property 'OPENVIDU_SESSIONS_GARBAGE_INTERVAL' to 0) this property is ignored
OPENVIDU_SESSIONS_GARBAGE_THRESHOLD=3600

# Call Detail Record enabled
# Whether to enable Call Detail Record or not
# Values: true | false
OPENVIDU_CDR=false

# Path where the cdr log files are hosted
OPENVIDU_CDR_PATH=/opt/openvidu/cdr

# Kurento Media Server image
# --------------------------
# Docker hub kurento media server: https://hub.docker.com/r/kurento/kurento-media-server
# Uncomment the next line and define this variable with KMS image that you want use
# KMS_IMAGE=kurento/kurento-media-server:7.0.1

# Kurento Media Server Level logs
# -------------------------------
# Uncomment the next line and define this variable to change
# the verbosity level of the logs of KMS
# Documentation: https://doc-kurento.readthedocs.io/en/stable/features/logging.html
# KMS_DOCKER_ENV_GST_DEBUG=

# Openvidu Server Level logs
# --------------------------
# Uncomment the next line and define this variable to change
# the verbosity level of the logs of Openvidu Service
# RECOMENDED VALUES: INFO for normal logs DEBUG for more verbose logs
# OV_CE_DEBUG_LEVEL=INFO

# Java Options
# --------------------------
# Uncomment the next line and define this to add
# options to java command
# Documentation: https://docs.oracle.com/cd/E37116_01/install.111210/e23737/configuring_jvm.htm#OUDIG00058
# JAVA_OPTIONS=-Xms2048m -Xmx4096m -Duser.timezone=UTC
```
    

```bash
sudo ./openvidu start
```

### 6. Nginx Config 파일 작성

```bash
cd /opt/openvidu
sudo vi default.conf
```

- Default.config

```bash
# WebSocket 연결 지속시간
    proxy_read_timeout 21600000;
    proxy_send_timeout 21600000;

# 웹소켓 관련 API 프록시
    location /chipchippoker {
        proxy_pass http://localhost:8082;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "Upgrade";
        proxy_set_header Host $host;
    }

# REST API 프록시
    location /api/ {
        proxy_pass http://localhost:8082;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }

# OpenVidu Signal Server
    location /signal-server/ {
    proxy_pass http://localhost:8500;
    proxy_http_version 1.1;
    proxy_set_header Upgrade $http_upgrade;
    proxy_set_header Connection 'upgrade';
    proxy_set_header Host $host;
    proxy_cache_bypass $http_upgrade;
    }
```
    

### 7. RDB & MongoDB 실행 OR 연결

```bash
# RDB Schema name : chipchippoker_dev
# Mongodb Database Name : chipchippoker_dev
# Mongodb Collection Name : game_result
```

### 8. Front Container 실행

```bash
sudo docker build -t vue-app .
OR
sudo docker buildx build -t vue-app .

sudo docker run -d -p 5442:80 --rm --name vue-app-1 vue-app
```