run-local-mongo:
	docker-compose -f docker-compose-local.yml up -d

down-local-mongo:
	docker-compose -f docker-compose-local.yml down

copy-env:
	cp env ./.env

touch-aws-credentials:
	mkdir -p ~/.aws
	touch ~/.aws/credentials
	echo "[default]" >> ~/.aws/credentials
	echo "aws_access_key_id=" >> ~/.aws/credentials
	echo "aws_secret_access_key=" >> ~/.aws/credentials

	touch ~/.aws/config
	echo "[default]" >> ~/.aws/config
	echo "region=ap-northeast-2" >> ~/.aws/config
	echo "output=json" >> ~/.aws/config

check-mongo-param:
	aws ssm get-parameter --name "/config/pumping/mongo.username" --query "Parameter.Value"