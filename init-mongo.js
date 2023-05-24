// init-mongo.js

// 데이터베이스 생성
db = db.getSiblingDB('pumping');

// 테스트 유저 데이터 추가
db.users.insertOne({
  username: 'testuser',
  password: 'testpassword'
});
