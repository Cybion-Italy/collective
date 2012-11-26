#!/bin/bash
echo "deploying stuff as '$1' on '$2'";
rsync -av --exclude=.DS_Store --exclude=*.sh --exclude=.svn --exclude=nbproject * matteo@cibionte.cybion.eu:/var/www/collective/backend/
# on gaia: 
# rsync -e 'ssh -p 31000' -av --exclude=.DS_Store --exclude=*.sh --exclude=.svn --exclude=nbproject * mmoci@gaia.cybion.eu:/var/www/collective/backend/
# chmod: sudo chmod -R 0775 .