#!/usr/bin/env sh
java -Djava.security.egd=file:/dev/./urandom -jar -Xmn64m -Xms128m -Xmx750m /var/lib/mozi/app.jar
