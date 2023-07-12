CUR_DIR=${shell pwd}

run-local:
	gradle clean build && gradle bootRun

build-and-run-local:
	gradle build && java -jar build/libs/pumping-0.0.1-SNAPSHOT.jar

build-and-run-aws:
	chmod +x $(CUR_DIR)/gradlew
	$(CUR_DIR)/gradlew clean
	$(CUR_DIR)/gradlew bootJar -Pprofile=aws
	nohup java -jar $(CUR_DIR)/build/libs/pumping-0.0.1-SNAPSHOT.jar > log.log 2>&1 &

ncp-deploy:
	chmod +x $(CUR_DIR)/gradlew
	nohup $(CUR_DIR)/gradlew bootRun > log.log 2>&1 &
