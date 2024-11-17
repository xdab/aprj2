JAVA = java
CLASSPATH = "target/*"
MAIN_CLASS = pl.so5dz.aprj2.App

MAVEN = mvn
MAVEN_OPTS = -T4
MAVEN_OPTS_BUILD = -Pbuild
MAVEN_OPTS_REBUILD = -Pbuild -DskipTests
MAVEN_PLUGIN_MODULES = plugin-igate

clean:
	$(MAVEN) clean

test:
	$(MAVEN) test

build:
	$(MAVEN) clean package $(MAVEN_OPTS) $(MAVEN_OPTS_BUILD)

rebuild_plugins:
	$(MAVEN) package -pl $(MAVEN_PLUGIN_MODULES) -am $(MAVEN_OPTS) $(MAVEN_OPTS_BUILD) $(MAVEN_OPTS_REBUILD)

rebuild:
	$(MAVEN) package $(MAVEN_OPTS) $(MAVEN_OPTS_BUILD) $(MAVEN_OPTS_REBUILD)

run:
	$(JAVA) -cp $(CLASSPATH) $(MAIN_CLASS)

install: 
	groupadd -f aprj2
	install -d -o root -g aprj2 /usr/local/bin/aprj2
	install -m 664 -o root -g aprj2 target/*.jar /usr/local/bin/aprj2
	cp -n system/config.example.xml /etc/aprj2.xml
	chmod 664 /etc/aprj2.xml
	chgrp aprj2 /etc/aprj2.xml
	cp -n system/aprj2.example.service /etc/systemd/system/aprj2.service
	chmod 664 /etc/systemd/system/aprj2.service
	chgrp aprj2 /etc/systemd/system/aprj2.service
	systemctl daemon-reload
