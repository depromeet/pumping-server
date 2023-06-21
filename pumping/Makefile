CUR_DIR=${shell pwd}

run-local:
	gradle bootRun

build-and-run-local:
	gradle build && java -jar build/libs/pumping-0.0.1-SNAPSHOT.jar

build-and-run-aws:
	chmod +x $(CUR_DIR)/gradlew
	$(CUR_DIR)/gradlew bootJar
	java -jar $(CUR_DIR)/build/libs/pumping-0.0.1-SNAPSHOT.jar

ncp-deploy:
	chmod +x $(CUR_DIR)/gradlew
	nohup $(CUR_DIR)/gradlew bootRun > log.log 2>&1 &
