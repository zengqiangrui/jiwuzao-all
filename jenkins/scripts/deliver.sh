#!/bin/bash
# 该脚本被jenkins/Jenkinsfile调用
# 将已经运行的docker停掉
docker stop major
docker stop manager

# 删除maven dockerfile重复生成的镜像(被覆盖的镜像没有tag)
docker rmi $(docker images | grep "^<none>"| awk '{print $3}')

# 运行镜像
docker run --rm --name major -p 10010:10010 -d jiwuzao/major
docker run --rm --name manager -p 10020:10020 -d jiwuzao/manager

# 输出运行情况
docker ps

echo "success"
