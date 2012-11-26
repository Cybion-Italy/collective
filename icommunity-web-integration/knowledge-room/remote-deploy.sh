#!/bin/bash
echo "deploying knowledge room stuff";
rsync -av --exclude=.DS_Store --exclude=*.sh --exclude=.svn --exclude=nbproject * cybion@94.75.243.141:/var/www/collective-dev/wp-content/themes/icommunity/knowledge-room/
# on production, change collective-dev to collective
