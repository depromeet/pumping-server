package com.dpm.pumping.user

import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.user.domain.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.juli.logging.Log
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.test.context.SpringBootTest

@DataMongoTest
class UserRepositoryTest(
    @Autowired val userRepository: UserRepository,
) {

    @BeforeEach
    fun `tear down`(){
        userRepository.deleteAll()
    }

    @Test
    fun `유저 생성 테스트`(){
        val platform = LoginPlatform(LoginType.APPLE, "OIAUTH")
        userRepository.save(User.createWithOAuth(platform))
        val findUser = userRepository.findByPlatform(platform)
        
        assertThat(platform).isEqualTo(findUser!!.platform)
    }
}
