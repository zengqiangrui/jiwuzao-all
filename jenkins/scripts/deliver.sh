#!/bin/bash
cd /tmp
docker stop major
docker stop manager
docker run --rm --name major -P -d major
docker run --rm --name manager -P -d manager
echo "success"
