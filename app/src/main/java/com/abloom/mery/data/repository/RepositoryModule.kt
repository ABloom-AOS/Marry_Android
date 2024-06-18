package com.abloom.mery.data.repository

import com.abloom.domain.announcement.repository.AnnouncementRepository
import com.abloom.domain.qna.repository.ProspectiveCoupleQnaRepository
import com.abloom.domain.question.repository.QuestionRepository
import com.abloom.domain.user.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsAnnouncementRepository(announcementRepository: DefaultAnnouncementRepository): AnnouncementRepository

    @Binds
    abstract fun bindsUserRepository(userRepository: DefaultUserRepository): UserRepository

    @Binds
    abstract fun bindsQuestionRepository(questionRepository: DefaultQuestionRepository): QuestionRepository

    @Binds
    abstract fun bindsQnaRepository(qnaRepository: DefaultQnaRepository): ProspectiveCoupleQnaRepository
}
