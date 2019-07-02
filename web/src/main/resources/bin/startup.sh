#!/bin/sh

export JAVA_HOME=/usr/lib/jvm/java-8-oracle
PATH=/usr/lib/jvm/java-8-oracle/bin:${PATH}

file="custom.pid"
if [ -f "$file" ]
then
        ps cax | grep $(cat custom.pid) > /dev/null
        if [ $? -eq 0 ];
        then
                echo "The micro service actually is running, you need to run the shutdown script to stop the micro service"
        else
                rm -f "$file"
                echo "Starting micro service"
                nohup $JAVA_HOME/bin/java -Dloader.path=/opt/erp/erp/config -Dlogging.config=../config/logback-spring.xml -jar -Dspring.profiles.active=prod ../lib/erp-web-*.jar > /dev/null 2>&1 & echo $! > custom.pid &
        fi
else
        echo "Starting micro service"
        nohup $JAVA_HOME/bin/java -Dloader.path=/opt/erp/erp/config -Dlogging.config=../config/logback-spring.xml -jar -Dspring.profiles.active=prod ../lib/erp-web-*.jar > /dev/null 2>&1 & echo $! > custom.pid &
fi