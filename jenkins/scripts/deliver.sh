#!/bin/bash
cd /tmp
# nohup java -jar -Dspring.profiles.active=dev -server -d64 -Xms512m -Xmx512m -Xmn256m -Xss512k  /root/jenkins/workspace/jiwuzao/order/target/order-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &
nohup java -jar -Dspring.profiles.active=dev -server -d64 -Xms512m -Xmx512m -Xmn256m -Xss512k  /home/major/target/major-0.0.1-SNAPSHOT.jar >/tmp/jiwuzao-log/major-log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev -server -d64 -Xms512m -Xmx512m -Xmn256m -Xss512k  /home/manager/target/manager-0.0.1-SNAPSHOT.jar >/tmp/jiwuzao-log/manager-log 2>&1 
echo "success"
