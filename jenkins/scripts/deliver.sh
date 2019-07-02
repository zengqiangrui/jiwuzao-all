cd /tmp
## nohup java -jar -Dspring.profiles.active=dev -server -d64 -Xms512m -Xmx512m -Xmn256m -Xss512k  /root/jenkins/workspace/jiwuzao/order/target/order-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &
nohup java -jar -Dspring.profiles.active=dev -server -d64 -Xms512m -Xmx512m -Xmn256m -Xss512k  /root/jenkins/workspace/jiwuzao/major/target/major-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &
nohup java -jar -Dspring.profiles.active=dev -server -d64 -Xms512m -Xmx512m -Xmn256m -Xss512k  /root/jenkins/workspace/jiwuzao/manager/target/manager-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &
echo "success"