package com.abloom.mery.presentation.ui.category

import com.abloom.domain.question.model.Category
import com.abloom.domain.question.model.Question

val dummyQuestions = mapOf(
    Category.FINANCE to listOf(
        Question(
            category = Category.FINANCE,
            content = "결혼하기 전에 서로의 재정 상태에 대해 투명하게 알고 있어야 한다고 생각해요?",
            id = 31L
        ),
        Question(
            category = Category.FINANCE,
            content = "지금까지 모아둔 순자산은 어느 정도인가요? 예금, 적금, 투자금, 자동차, 부동산 등으로 나눠서 최대한 구체적으로 말해주세요.",
            id = 32L
        ),
        Question(
            category = Category.FINANCE,
            content = "최근 1년간의 한 달 평균 수입은 어떻게 되나요? 결혼 후에 수입에 변화가 있을 것 같아요?",
            id = 33L
        ),
        Question(
            category = Category.FINANCE,
            content = "최근 1년간의 한 달 평균 지출은 어떻게 되나요? 결혼 후에 지출에 변화가 있을 것 같아요?",
            id = 34L
        ),
        Question(
            category = Category.FINANCE,
            content = "가지고 있는 빚이 있으세요? 있다면 얼마 정도인지, 왜 빚을 갖게 됐는지, 어떻게 갚아나갈 생각인지, 솔직하게 알려주세요.",
            id = 35L
        ),
        Question(
            category = Category.FINANCE,
            content = "가입되어 있는 보험이 있으세요? 있다면 어떤 종류의 보험이고 보장내용이 어떻게 되는지 알려줄 수 있나요?",
            id = 36L
        ),
        Question(
            category = Category.FINANCE,
            content = "돈을 어떤식으로 관리하고 있나요? 저축, 투자, 소비 등의 비율은 어떻게 돼요?",
            id = 37L
        ),
        Question(
            category = Category.FINANCE,
            content = "두 분은 어떤 방식으로 돈을 관리하면 좋을까요?",
            id = 38L
        ),
        Question(
            category = Category.FINANCE,
            content = "두 분이 번 돈을 한 곳에 모아서 같이 관리한다면, 저축, 투자, 소비 등에 대해 어떤 식으로 의사 결정을 하면 좋을까요?",
            id = 39L
        ),
    ),
    Category.COMMUNICATION to listOf(
        Question(
            category = Category.COMMUNICATION,
            content = "행복한 결혼 생활을 위해서 가장 중요한 것은 무엇이라고 생각하나요?",
            id = 1L
        ),
        Question(
            category = Category.COMMUNICATION,
            content = "부부 사이에 때로는 거짓말도 필요하다고 생각하시나요? 아니면 신뢰를 지기 위해 어떠한 경우에도 거짓말을 하면 안된다고 생각하시나요?",
            id = 10L
        ),
        Question(
            category = Category.COMMUNICATION,
            content = "결혼 생활에 대한 서로의 생각에 대해 얼마나 대화를 나눠본 것 같나요?",
            id = 2L
        ),
        Question(
            category = Category.COMMUNICATION,
            content = "앞으로 함께 답하게 될 질문들에 얼마나 솔직하게 대답할 수 있나요?",
            id = 3L
        ),
        Question(
            category = Category.COMMUNICATION,
            content = "이야기를 나누다보면 서로 생각이 다른 부분이 분명히 있을 거예요. 그럴 때 어떻게 생각을 맞춰나갔으면 좋겠나요?",
            id = 4L
        ),
        Question(
            category = Category.COMMUNICATION,
            content = "갈등이 생겼을 때 바로바로 해결했으면 좋겠어요? 아니면 시간을 두고 천천히 해결했으면 좋겠어요?",
            id = 5L
        ),
        Question(
            category = Category.COMMUNICATION,
            content = "상대방에게 들었던 말 중에 가장 기분이 좋았던 말은 무엇인가요?",
            id = 6L
        ),
        Question(
            category = Category.COMMUNICATION,
            content = "상대방에게 들었던 말 중에 가장 기분이 좋지 않았던 말은 무엇인가요?",
            id = 7L
        ),
        Question(
            category = Category.COMMUNICATION,
            content = "‘장난이라도 이런 말은 하지 말아줬으면’ 하는 것이 있다면 알려주세요.",
            id = 8L
        ),
    ),
    Category.VALUES to listOf(
        Question(
            category = Category.VALUES,
            content = "결혼을 한다는 것은 당신에게 어떤 의미인가요?",
            id = 11L
        ),
        Question(
            category = Category.VALUES,
            content = "상대방과의 결혼을 결심한 순간은 언제였어요?",
            id = 12L
        ),
        Question(
            category = Category.VALUES,
            content = "결혼을 하게 되면 뭐가 가장 좋을 것 같나요?",
            id = 13L
        ),
        Question(
            category = Category.VALUES,
            content = "결혼 생활에서 가장 걱정되는 것은 무엇인가요?",
            id = 14L
        ),
        Question(
            category = Category.VALUES,
            content = "결혼으로 인해 삶에서 변할 것 같은 세가지를 무엇인가요?",
            id = 15L
        ),
        Question(
            category = Category.VALUES,
            content = "결혼을 하더라도 삶에서 변하지 않을 것 같은 세 가지는 무엇인가요?",
            id = 16L
        ),
        Question(
            category = Category.VALUES,
            content = "꿈꾸는 이상적인 결혼 생활의 모습을 한 문장으로 말해주세요.",
            id = 17L
        ),
        Question(
            category = Category.VALUES,
            content = "결혼 생활의 롤모델이 있나요? 있다면 누구예요? 왜 롤모델이에요?",
            id = 18L
        ),
        Question(
            category = Category.VALUES,
            content = "인생에서 가장 중요한 것은 무엇이라고 생각해요?",
            id = 19L
        ),
    ),
    Category.LIFESTYLE to listOf(
        Question(
            category = Category.LIFESTYLE,
            content = "주말은 주로 어떻게 보내고 싶으세요?",
            id = 56L
        ),
        Question(
            category = Category.LIFESTYLE,
            content = "공휴일이나 휴가 같이 쉬는 날은 주로 어떻게 보내고 싶으세요?",
            id = 57L
        ),
        Question(
            category = Category.LIFESTYLE,
            content = "설날이나 추석 같은 명절은 주로 어떻게 보내고 싶으세요?",
            id = 58L
        ),
        Question(
            category = Category.LIFESTYLE,
            content = "여행은 얼마나 자주 같이 가고 싶나요? 특별히 가고 싶은 곳이 있어요?",
            id = 59L
        ),
        Question(
            category = Category.LIFESTYLE,
            content = "상대방과 같이 하고 싶은 취미 활동이 있다면 알려주세요. 얼마나 자주 하고 싶나요?",
            id = 60L
        ),
        Question(
            category = Category.LIFESTYLE,
            content = "결혼 후에 새로 해보고 싶은 취미 활동이 있어요?",
            id = 61L
        ),
        Question(
            category = Category.LIFESTYLE,
            content = "혼자만의 시간이 필요한가요? 필요하다면 얼마나 필요한가요?",
            id = 62L
        ),
        Question(
            category = Category.LIFESTYLE,
            content = "혼자만의 공간이 필요한가요? 필요하다면 어떤 공간이었으면 하나요?",
            id = 63L
        ),
        Question(
            category = Category.LIFESTYLE,
            content = "반려동물을 기르고 싶으세요? 만약 기르고 싶다면 어떤 동물을 기르고 싶으세요?",
            id = 64L
        ),
    ),
    Category.CHILD to listOf(
        Question(
            category = Category.CHILD,
            content = "아이가 친구를 괴롭히거나 물건을 훔치는 등 나쁜 짓을 한다면 어떤 식으로 훈육하면 좋을까요?",
            id = 100L
        ),
        Question(
            category = Category.CHILD,
            content = "아이를 가지는 것에 대해서 어떤 생각을 가지고 계신가요?",
            id = 81L
        ),
        Question(
            category = Category.CHILD,
            content = "두 분은 멀지 않은 기간 내에 아이를 가질 수 있을 만큼 부모로서 준비가 돼있나요? 어떤 부분을 더 준비하면 좋을까요?",
            id = 82L
        ),
        Question(
            category = Category.CHILD,
            content = "만약 아이를 가진다면, 첫 아이는 언제쯤 가지고 싶으세요?",
            id = 83L
        ),
        Question(
            category = Category.CHILD,
            content = "두 분의 첫 아이가 남자 아이였으면 좋겠나요, 여자 아이였으면 좋겠나요?",
            id = 84L
        ),
        Question(
            category = Category.CHILD,
            content = "아이를 몇 명 정도 가지고 싶으세요?",
            id = 85L
        ),
        Question(
            category = Category.CHILD,
            content = "만약 두 분이 난임, 불임 등으로 인해 아이를 가지지 못하게 되는 경우에는 어떻게 하면 좋을까요?",
            id = 86L
        ),
        Question(
            category = Category.CHILD,
            content = "아이가 태어난 후에 육아는 어떻게 나누는 게 좋을 것 같아요?",
            id = 87L
        ),
        Question(
            category = Category.CHILD,
            content = "아이가 태어난 후에 집안일은 어떻게 나누는 게 좋을 것 같아요?",
            id = 88L
        ),
        Question(
            category = Category.CHILD,
            content = "아이의 백일잔치나 돌잔치를 하고 싶나요? 만약 한다면 어떤 식으로 하고 싶나요?",
            id = 89L
        ),
    ),
    Category.FAMILY to listOf(
        Question(
            category = Category.FAMILY,
            content = "내 가족들이 어떤 분들인지 상대방에게 간단하게라도 알려줄 수 있나요?",
            id = 101L
        ),
        Question(
            category = Category.FAMILY,
            content = "가족들과의 사이는 어떠신가요? 상대방이 알아두어야 하는 가정사가 있나요?",
            id = 102L
        ),
        Question(
            category = Category.FAMILY,
            content = "내 가족과 상대방이 잘 지내기 위해 미리 알아두면 좋을 것들을 알려주세요.",
            id = 103L
        ),
        Question(
            category = Category.FAMILY,
            content = "혹시라도 내 가족들과 상대방 사이의 관계에서 걱정되는 게 있다면 미리 알려주세요.",
            id = 104L
        ),
        Question(
            category = Category.FAMILY,
            content = "내 부모님은 상대방에 대해 어떻게 생각하고 계신가요?",
            id = 105L
        ),
        Question(
            category = Category.FAMILY,
            content = "결혼 후에 내 부모님과의 관계는 어떻게 변할 것이라고 생각하나요?",
            id = 106L
        ),
        Question(
            category = Category.FAMILY,
            content = "부모님들과는 일년에 얼마나 시간을 보내게 될 것 같나요?",
            id = 107L
        ),
        Question(
            category = Category.FAMILY,
            content = "두 분의 결혼 생활에 부모님들이 얼마나 관여했으면 좋겠나요? 너무 심하게 관여하시면 어떻게 말씀드리는게 좋을까요?",
            id = 108L
        ),
        Question(
            category = Category.FAMILY,
            content = "상대방 부모님이랑 나 사이에 갈등이 생기면 어떻게 할거예요? 상대방은 어떻게 했으면 좋겠어요?",
            id = 109L
        ),
        Question(
            category = Category.FAMILY,
            content = "내 부모님이랑 상대방 사이에 갈등이 생기면 어떻게 할거예요? 상대방은 어떻게 했으면 좋겠어요?",
            id = 110L
        ),
        Question(
            category = Category.FAMILY,
            content = "두 분의 부모님들 사이에 갈등이 생기면, 두 분은 어떻게 대처하는게 좋을까요?",
            id = 111L
        ),
        Question(
            category = Category.FAMILY,
            content = "부모님들께 용돈을 드려야할까요? 만약 드린다면 언제부터, 얼마씩 드리면 적당할까요?",
            id = 112L
        ),
        Question(
            category = Category.FAMILY,
            content = "어버이날이나 생신 같은 특별한 날에 부모님들께 어떤 선물을 드리고 싶어요? 돈은 얼마 정도 쓰면 좋을까요?",
            id = 113L
        ),
        Question(
            category = Category.FAMILY,
            content = "부모님들께 두 분의 집 비밀번호를 알려드려야할까요?",
            id = 114L
        ),
    ),
    Category.SEX to listOf(
        Question(
            category = Category.SEX,
            content = "행복한 결혼 생활을 위해서 성관계가 얼마나 중요하다고 생각하시나요?",
            id = 121L
        ),
        Question(
            category = Category.SEX,
            content = "결혼 후 1년 동안 얼마나 자주 성관계를 하고 싶나요?",
            id = 122L
        ),
        Question(
            category = Category.SEX,
            content = "만약 상대방이 가끔씩 성관계를 거부한다면 어떨 것 같아요?",
            id = 123L
        ),
        Question(
            category = Category.SEX,
            content = "행복한 결혼 생활을 위해서 성관계가 얼마나 중요하다고 생각하시나요?",
            id = 121L
        ),
        Question(
            category = Category.SEX,
            content = "두 분이 함께했던 성관계 중에 좋았던 때는 언제인가요?",
            id = 124L
        ),
        Question(
            category = Category.SEX,
            content = "두 분이 함께했던 성관계 중에 싫었던 때는 언제인가요?",
            id = 125L
        ),
        Question(
            category = Category.SEX,
            content = "성관계할 때 '이것만은 꼭 기억해줬으면' 하는게 있어요?",
            id = 126L
        ),
        Question(
            category = Category.SEX,
            content = "성관계를 하고 싶을 때와 하고 싶지 않을 때, 상대방에게 어떤 식으로 신호를 줄거에요?",
            id = 127L
        ),
        Question(
            category = Category.SEX,
            content = "생리 기간에 성관계를 하는 것에 대해 어떻게 생각해요?",
            id = 128L
        ),
        Question(
            category = Category.SEX,
            content = "임신 기간에 성관계를 하는 것에 대해 어떻게 생각해요?",
            id = 129L
        ),
    ),
    Category.HEALTH to listOf(
        Question(
            category = Category.HEALTH,
            content = "예전에 격었던 신체적인 또는 정신적인 건강 문제는 어떤 것들이 있어요?",
            id = 136L
        ),
        Question(
            category = Category.HEALTH,
            content = "지금 겪고 있는 신체적인 또는 정신적인 건강 문제는 어떤 것들이 있어요?",
            id = 137L
        ),
        Question(category = Category.HEALTH, content = "앓고 있는 유전병이 있어요?", id = 138L),
        Question(
            category = Category.HEALTH,
            content = "당뇨, 심혈관질환, 치매 등에 가족력이 있어요? 이외에도 가족력이 있는 병이 있어요?",
            id = 139L
        ),
        Question(
            category = Category.HEALTH,
            content = "예전에 격었던 신체적인 또는 정신적인 건강 문제는 어떤 것들이 있어용?",
            id = 136L
        ),
        Question(
            category = Category.HEALTH,
            content = "건강을 위해 특별히 하고 있는 운동이 있나요?",
            id = 140L
        ),
        Question(
            category = Category.HEALTH,
            content = "건강을 위해 특별히 챙겨먹는 음식이나 영양제 같은 것들이 있나요?",
            id = 141L
        ),
        Question(
            category = Category.HEALTH,
            content = "건강을 위해 특별히 지키고 있는 생활 습관이 있나요?",
            id = 142L
        ),
        Question(
            category = Category.HEALTH,
            content = "가장 최근에 받은 건강검진 결과는 어땠나요?",
            id = 143L
        ),
        Question(
            category = Category.HEALTH,
            content = "성병에 걸린 적이 있으신가요? 있다면 어쩌다가 걸리셨나요? 치료는 잘 하셨나요?",
            id = 144L
        ),
    ),
    Category.WEDDING to listOf(
        Question(
            category = Category.WEDDING,
            content = "두 분의 결혼 준비 과정에서 필수라고 생각하는 건 무엇인가요?",
            id = 146L
        ),
        Question(
            category = Category.WEDDING,
            content = "결혼 준비가 어디까지 됐는지 점검해봐요. 된 것은 무엇이고, 안 된 것은 무엇인 것 같나요?",
            id = 147L
        ),
        Question(
            category = Category.WEDDING,
            content = "웨딩 플래너를 끼고 결혼 준비를 했으면 좋겠나요? 한다면 어디랑 하면 좋을까요?",
            id = 148L
        ),
        Question(
            category = Category.WEDDING,
            content = "총 결혼 준비 비용은 어느 정도를 생각하고 계세요?",
            id = 149L
        ),
        Question(
            category = Category.WEDDING,
            content = "결혼 비용은 두 분이 어떤 식으로 나누면 좋을까요? 부모님께는 어느 정도 지원 받을 수 있나요?",
            id = 150L
        ),
        Question(
            category = Category.WEDDING,
            content = "원하는 결혼식의 형식이나 규모가 있나요? 비용은 얼마나 들까요?",
            id = 151L
        ),
        Question(
            category = Category.WEDDING,
            content = "결혼식을 올리고 싶은 계절이 있나요? 대략이라도 생각해 놓은 날짜가 있나요?",
            id = 152L
        ),
        Question(
            category = Category.WEDDING,
            content = "주례, 사회, 축가를 부탁한다면 각각 어느 분께 부탁하는 게 좋을까요? 생각해둔 분이 계신가요?",
            id = 153L
        ),
        Question(
            category = Category.WEDDING,
            content = "신혼여행은 어디로 몇 박 며칠 가고 싶어요?",
            id = 154L
        ),
    ),
    Category.FUTURE to listOf(
        Question(
            category = Category.FUTURE,
            content = "죽기 전에 꼭 하고 싶은 일이 있나요? 있다면 무엇인가요?",
            id = 161L
        ),
        Question(
            category = Category.FUTURE,
            content = "나중에 꼭 배워보고 싶은 것이 있어요? 운동, 학문, 언어, 예술,...무엇이라도 좋아요.",
            id = 162L
        ),
        Question(category = Category.FUTURE, content = "몇 살 때까지 일하고 싶으신가요?", id = 163L),
        Question(
            category = Category.FUTURE,
            content = "이직을 하거나 직업을 바꿀 계획이 있다면 알려주세요.",
            id = 164L
        ),
        Question(category = Category.FUTURE, content = "드림타가 있나요? 있다면 어떤 차인가요?", id = 165L),
        Question(
            category = Category.FUTURE,
            content = "드림하우스가 있나요? 있다면 어떤 집인가요?",
            id = 166L
        ),
        Question(
            category = Category.FUTURE,
            content = "우리나라 말고 살아보고 싶은 나라가 있나요? 있다면 어디인가요?",
            id = 167L
        ),
        Question(
            category = Category.FUTURE,
            content = "앞으로 10년 정도 대한 계획이 있나요? 있다면 대략적으로라도 알려주세요.",
            id = 167L
        ),
        Question(
            category = Category.FUTURE,
            content = "살다보면 분명 한 번쯤은 있을법한 고난의 시기를 부부가 함께 이겨내기 위해서는 무서이 가장 중요하다고 생각하시나요?",
            id = 169L
        ),
        Question(
            category = Category.FUTURE,
            content = "삶의 마지막까지 함께 한다면, 주변 사람들에게 어떤 부부로 기억됐으면 좋겟나요?",
            id = 170L
        ),
    ),
    Category.PRESENT to listOf(
        Question(
            category = Category.PRESENT,
            content = "지금의 나 자신은 어떤 사람인지 한 문자으로 말한다면 무엇인가요?",
            id = 176L
        ),
        Question(
            category = Category.PRESENT,
            content = "지금의 상대방과 가장 잘 어울리는 단어를 한 가지만 고른다면 어떤 단어인가요? 이유도 알려주세요.",
            id = 177L
        ),
        Question(category = Category.PRESENT, content = "어떨 때 상대방이 가장 사랑스러워요?", id = 178L),
        Question(
            category = Category.PRESENT,
            content = "가끔은 상대방이 얄미울 때도 있나요? 그건 어떨 때인가요?",
            id = 179L
        ),
        Question(
            category = Category.PRESENT,
            content = "아직까지 말한 적이 없는 비밀을 하나만 상대방에게 알려주세요.",
            id = 180L
        ),
        Question(
            category = Category.PRESENT,
            content = "지금 가지고 계신 가장 큰 고민이 무엇인가요?",
            id = 181L
        ),
        Question(
            category = Category.PRESENT,
            content = "제일 좋아하는 영화가 무엇인가요? 왜 그 영화를 제일 좋아하세요?",
            id = 182L
        ),
        Question(
            category = Category.PRESENT,
            content = "평생 한 가지 음식만 먹어야 한다면 뭘 먹을거예요?",
            id = 183L
        ),
        Question(
            category = Category.PRESENT,
            content = "최근에 가장 많이 들은 노래를 하나만 상대방에게 추천해주세요.",
            id = 184L
        ),
    ),
    Category.PAST to listOf(
        Question(
            category = Category.PAST,
            content = "두 분이 어떻게 처음 만나게 됐는지 기억하시나요? 그때 상대방에 대해 어떤 생각이 들었나요?",
            id = 186L
        ),
        Question(
            category = Category.PAST,
            content = "두 분이 함께한 순간 중 가장 기억에 남는 순간은 언제예요?",
            id = 187L
        ),
        Question(
            category = Category.PAST,
            content = "두 분이 만난 날 중 가장 좋았던 날은 언제예요?",
            id = 188L
        ),
        Question(
            category = Category.PAST,
            content = "두 분이 만난 날 중 가장 싫었던 날은 언제예요?",
            id = 189L
        ),
        Question(
            category = Category.PAST,
            content = "자신의 어린 시절과 가장 잘 어울리는 단어를 한 가지만 말해주세요. 왜 그 단어인가요?",
            id = 190L
        ),
        Question(
            category = Category.PAST,
            content = "어릴 때 꿈은 무엇이었나요?",
            id = 191L
        ),
        Question(
            category = Category.PAST,
            content = "살면서 했던 일 중에 가장 자랑스러운 일은 무엇인가요?",
            id = 192L
        ),
        Question(
            category = Category.PAST,
            content = "인생에서 가장 부끄러웠던 순간은 언제였어요?",
            id = 193L
        ),
        Question(
            category = Category.PAST,
            content = "인생에서 가장 힘들었던 순간은 언제였어요?",
            id = 194L
        ),
        Question(
            category = Category.PAST,
            content = "집안의 경제 사정은 어땠나요? 지금은 어떤가요?",
            id = 195L
        ),
    )
)
