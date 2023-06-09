CUR_DIR=${shell pwd}

run:
	gradle bootRun

ncp-deploy:
	chmod +x $(CUR_DIR)/gradlew
	nohup $(CUR_DIR)/gradlew bootRun > log.log 2>&1 &

