# Spring Boot using IBM MQ JMS 

### **application.properties**

        ibm.mq.queueManager=QM1
        ibm.mq.channel=DEV.ADMIN.SVRCONN
        ibm.mq.connName=localhost(1414)
        ibm.mq.user=admin
        ibm.mq.password=passw0rd



### **Docker Command**
    - Pull ibm mq image : I used IBM MQ V9.2.4.0
        - docker pull ibmcom/mq:latest

    - Create Volume 
        - docker volume create qm1data

    - Docker Run in detach mode 
        docker run --env LICENSE=accept --env MQ_QMGR_NAME=QM1 --volume qm1data:/mnt/mqm --publish 1414:1414 --publish 9443:9443 --detach --env MQ_APP_PASSWORD=passw0rd ibmcom/mq:latest

### **IBM Console**
    
    https://localhost:9443/ibmmq/console
    
    - Credentials
            Username: admin 
            password: passw0rd

    - Create Queue
            NOTIFICATION.Q


### **Curl Request**
        curl --location --request POST 'localhost:8080/notification' \
        --header 'Content-Type: application/json' \
        --data-raw '{
        "message":"hello world",
        "identifier": "U123-123"
        }'


### **Reference**

    MQ docker:
    https://github.com/ibm-messaging/mq-container
    
    MQ JMS spring starter:
    https://github.com/ibm-messaging/mq-jms-spring
    
    IBM JMS spring documentation:
    https://developer.ibm.com/technologies/java/tutorials/mq-jms-application-development-with-spring-boot/#