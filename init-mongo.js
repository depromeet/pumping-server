// 데이터베이스 생성
db = db.getSiblingDB("pumping");

// 테스트 유저 데이터 추가
db.users.insertMany([
	{
		_class: "com.dpm.pumping.user.User",
		uid: "user-00001",
		login_platform: {
			oauth2id: "",
			login_type: "",
		},
		nickname: "GC",
		gender: "MALE",
		height: "111.11",
		weight: "123.12",
	},
	{
		_class: "com.dpm.pumping.user.User",
		uid: "user-00002",
		login_platform: {
			oauth2id: "",
			login_type: "",
		},
		nickname: "SM",
		gender: "FEMALE",
		height: "123.45",
		weight: "67.89",
	},
]);
