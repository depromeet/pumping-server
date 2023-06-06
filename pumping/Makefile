CUR_DIR=${shell pwd}

run:
	gradlew bootRun

ncp-deploy:
	gradle wrapper
	chmod +x $(CUR_DIR)/gradlew
	$(CUR_DIR)/gradlew bootRun

