CUR_DIR=${shell pwd}

run-local:
	gradle bootRun --args='--spring.profiles.active=local'

run-local-aws:
	gradle bootRun --args='--spring.profiles.active=aws'

ncp-deploy:
	chmod +x $(CUR_DIR)/gradlew
	nohup $(CUR_DIR)/gradlew bootRun > log.log 2>&1 &

