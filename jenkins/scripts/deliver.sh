#!/bin/bash
cd /tmp
docker stop major
docker stop manager
docker rmi $(docker images | grep "^<none>"| awk '{print $3}')
docker run --rm --name major -p 10010:10010 -d jiwuzao/major
docker run --rm --name manager -p 10020:10020 -d jiwuzao/manager
echo "success"
