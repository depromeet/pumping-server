import com.dpm.pumping.message.Message
import com.dpm.pumping.message.MessageRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Transactional
@Component
class DataInitializer(
    private val messageRepository: MessageRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        if (!messageRepository.existsByIndex(0)) {
            val messages = listOf(
                "원래 영웅은 성공전이 어두운법",
                "아이 맛있다 오늘 내 근육",
                "오늘 클럽 갈래? 헬스 클럽",
                "헬스클럽은 클럽보다 더 신나",
                "미안. 오늘 못놀아 근손실오거든",
                "횐님~ 오늘 운동 조지세요",
                "울지마…⭐️ 근손실오니까",
                "내 애인 = 덤벨",
                "적들의 덤벨은 점점 늘어나고 있다",
                "봉준호, 손흥민, 운동하는 너 let’s go",
                "통장에 돈빠지는 것보다 슬픈건 근손실",
                "근육 빠지니까 울음도 참자",
                "울면 오는것 : 옛날-망태할아범, 현재-근손실",
                "글루텐 밸리 스탑!!!",
                "전집중 호흡 제 1형 근성장~!",
                "굶지마. 단백질로 배를 채워.",
                "스트레스 받으면 근손실 오거든요",
                "3대 독자 말고 3대 500",
                "흔들리는 건 너의 지방뿐",
                "운동은 애인이랑 헤어졌을때보다 더 아프게 해야해",
                "누울 자리는 오직 벤치프레스뿐",
                "끊어야할 건 담배, 술, 헬스회원권"
            )

            val entities = messages.mapIndexed {
                    idx, message -> Message(content = message, index = idx)
            }
            messageRepository.saveAll(entities)
        }
    }
}
