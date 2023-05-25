package com.example.services

import com.example.data.models.post.PostDto
import com.example.data.models.post.toPostModel
import com.example.data.models.reply.ReplyDto
import com.example.data.models.reply.toReplyModel
import com.example.data.models.thread.ThreadDto
import com.example.data.models.thread.toThreadModel
import com.example.data.models.topic.Topic
import com.example.data.models.user_favorite.UserFavorite
import com.example.services.MongoDBManager.postRepository
import com.example.services.MongoDBManager.replyRepository
import com.example.services.MongoDBManager.threadRepository
import com.example.services.MongoDBManager.topicRepository
import com.example.services.MongoDBManager.userFavRepository
import org.bson.types.ObjectId

class DBDummyData {

    private val topic1 = Topic(name = "Climate change and environmental sustainability")
    private val topic2 = Topic(name = "Income inequality and wealth distribution")
    private val topic3 = Topic(name = "Technology and privacy")
    private val topic4 = Topic(name = "Education system reform")
    private val topic5 = Topic(name = "Global conflicts and terrorism")
    private val topic6 = Topic(name = "Social media and its impact on society")

    private val topics = listOf(topic1, topic2, topic3, topic4, topic5, topic6)

    // TOPIC 1
    private val thread11 = ThreadDto(name = "The impact of deforestation on climate change", topicId = "6461e03104a46575d452901b").toThreadModel()
    private val thread12 = ThreadDto(name = "Corporate responsibility in combating climate change", topicId = "6461e03104a46575d452901b").toThreadModel()
    private val thread13 = ThreadDto(name = "Climate justice and marginalized communities", topicId = "6461e03104a46575d452901b").toThreadModel()
    private val thread14 = ThreadDto(name = "Climate change denial and scientific consensus", topicId = "6461e03104a46575d452901b").toThreadModel()

    //TOPIC 2
    private val thread21 = ThreadDto(name = "The role of taxation in reducing income inequality", topicId = "6461e03104a46575d452901c").toThreadModel()
    private val thread22 = ThreadDto(name = "Universal basic income and poverty alleviation", topicId = "6461e03104a46575d452901c").toThreadModel()
    private val thread23 = ThreadDto(name = "Gender pay gap and workplace inequality", topicId = "6461e03104a46575d452901c").toThreadModel()
    private val thread24 = ThreadDto(name = "Education and social mobility", topicId = "6461e03104a46575d452901c").toThreadModel()

    //TOPIC 3
    private val thread31 = ThreadDto(name = "Big data and privacy concerns", topicId = "6461e03104a46575d452901d").toThreadModel()
    private val thread32 = ThreadDto(name = "Surveillance technologies and civil liberties", topicId = "6461e03104a46575d452901d").toThreadModel()
    private val thread33 = ThreadDto(name = "Online harassment and cyberbullying", topicId = "6461e03104a46575d452901d").toThreadModel()
    private val thread34 = ThreadDto(name = "Ethical considerations in artificial intelligence", topicId = "6461e03104a46575d452901d").toThreadModel()

    //TOPIC 4
    private val thread41 = ThreadDto(name = "Standardized testing and its impact on learning", topicId = "6461e03104a46575d452901e").toThreadModel()
    private val thread42 = ThreadDto(name = "Technology integration in classrooms", topicId = "6461e03104a46575d452901e").toThreadModel()
    private val thread43 = ThreadDto(name = "Teacher training and professional development", topicId = "6461e03104a46575d452901e").toThreadModel()
    private val thread44 = ThreadDto(name = "Education funding and resource disparities", topicId = "6461e03104a46575d452901e").toThreadModel()

    //TOPIC 5
    private val thread51 = ThreadDto(name = "Middle East conflicts and geopolitical implications", topicId = "6461e03104a46575d452901f").toThreadModel()
    private val thread52 = ThreadDto(name = "Cyber warfare and security", topicId = "6461e03104a46575d452901f").toThreadModel()
    private val thread53 = ThreadDto(name = "Countering violent extremism", topicId = "6461e03104a46575d452901f").toThreadModel()
    private val thread54 = ThreadDto(name = "Humanitarian crises and refugee resettlement", topicId = "6461e03104a46575d452901f").toThreadModel()

    //TOPIC 6
    private val thread61 = ThreadDto(name = "Social media and mental health", topicId = "6461e03104a46575d4529020").toThreadModel()
    private val thread62 = ThreadDto(name = "Fake news and disinformation", topicId = "6461e03104a46575d4529020").toThreadModel()
    private val thread63 = ThreadDto(name = "Social media and democracy", topicId = "6461e03104a46575d4529020").toThreadModel()
    private val thread64 = ThreadDto(name = "Privacy and data protection", topicId = "6461e03104a46575d4529020").toThreadModel()

    private val threads = listOf(
        thread11, thread12, thread13, thread14, thread21, thread22, thread23, thread24,
        thread31, thread32, thread33, thread34, thread41, thread42, thread43, thread44,
        thread51, thread52, thread53, thread54, thread61, thread62, thread63, thread64
    )

    //THREAD 11
    private val post111 = PostDto(text = "Deforestation has a profound impact on climate change. As trees are cleared, the ability of forests to absorb carbon dioxide diminishes. This leads to an increase in greenhouse gas emissions and contributes to the warming of our planet. It's crucial that we take immediate action to address deforestation and promote sustainable practices to protect our environment.",
        threadId = "646204c362dcb96adc0e7b64", createdBy = "645e07407d4be41e705879d0", createdByName = "Avin").toPostModel()

    private val post112 = PostDto(text = "The consequences of deforestation on climate change cannot be underestimated. Loss of forest cover disrupts ecosystems and reduces biodiversity. Moreover, forests act as natural carbon sinks, storing vast amounts of carbon. When trees are cut down, this carbon is released into the atmosphere, accelerating global warming. We need to prioritize reforestation efforts and support sustainable land use practices.",
        threadId = "646204c362dcb96adc0e7b64", createdBy = "645e07407d4be41e705879d0", createdByName = "Avin").toPostModel()

    //THREAD 21
    private val post211 = PostDto(text = "The link between education and social mobility is undeniable. A strong educational foundation empowers individuals to overcome socioeconomic barriers and pursue better opportunities. By investing in inclusive and equitable education systems, we can unlock the potential of every individual, promote social mobility, and foster a more just society.",
        threadId = "646204c362dcb96adc0e7b68", createdBy = "645e07407d4be41e705879d0", createdByName = "Avin").toPostModel()

    private val post212 = PostDto(text = "Education plays a crucial role in social mobility. It provides individuals with the knowledge, skills, and opportunities to improve their lives and contribute meaningfully to society. Accessible and quality education is essential for breaking the cycle of poverty, reducing inequality, and promoting upward mobility.",
        threadId = "646204c362dcb96adc0e7b68", createdBy = "645e3a40915b1b1ed782e512", createdByName = "Virat").toPostModel()

    //THREAD 31
    private val post311 = PostDto(text = "The rise of big data has sparked legitimate concerns about privacy. With the extensive collection and analysis of personal information, there is a need for robust safeguards to protect individuals' privacy rights. Striking a balance between utilizing the potential of big data and safeguarding privacy is crucial to build trust and ensure responsible data practices.",
        threadId = "646204c362dcb96adc0e7b6c", createdBy = "645e3a40915b1b1ed782e512", createdByName = "Virat").toPostModel()

    private val post312 = PostDto(text = "The vast amount of data being collected in today's digital age raises significant privacy concerns. Companies and organizations must handle personal data with utmost care, ensuring transparency, informed consent, and strong security measures. We need comprehensive regulations and ethical frameworks to address privacy concerns associated with big data analytics.",
        threadId = "646204c362dcb96adc0e7b6c", createdBy = "645e3a40915b1b1ed782e512", createdByName = "Virat").toPostModel()

    //THREAD 51
    private val post511 = PostDto(text = "Countering violent extremism requires a multifaceted approach involving education, social integration, and community engagement. It is crucial to address the root causes of extremism, such as social marginalization, economic disparities, and ideological indoctrination. By fostering dialogue, understanding, and providing alternative pathways, we can effectively counter the allure of violent extremism.",
        threadId = "646204c462dcb96adc0e7b74", createdBy = "645e3a40915b1b1ed782e512", createdByName = "Virat").toPostModel()

    private val post512 = PostDto(text = "Countering violent extremism requires collaboration among governments, civil society organizations, and communities. It's essential to promote inclusive societies that provide opportunities for all individuals, regardless of their backgrounds. By addressing socioeconomic inequalities, promoting tolerance, and empowering communities, we can work together to prevent the spread of violent extremism and build a safer and more resilient society.",
        threadId = "646204c462dcb96adc0e7b74", createdBy = "645e3a40915b1b1ed782e512", createdByName = "Virat").toPostModel()

    private val posts = listOf(
        post111, post112, post211, post212, post311, post312, post511, post512
    )

    private val userFav1 = UserFavorite(userId = ObjectId("645e07407d4be41e705879d0"),
        favTopics = listOf(ObjectId("646206125019ef37b89e8244"), ObjectId("64622074a5fabb5b05c9fa2a")))

    private val userFav2 = UserFavorite(userId = ObjectId("645e3a40915b1b1ed782e512"),
        favTopics = listOf(ObjectId("646206125019ef37b89e824a"), ObjectId("64622074a5fabb5b05c9fa2b")))

    private val userFavs = listOf(
        userFav1, userFav2
    )

    //POST 111
    private val reply111 = ReplyDto(text = "Nice post!!",
        postId = "64622074a5fabb5b05c9fa2a", createdBy = "645e07407d4be41e705879d0", createdByName = "Virat").toReplyModel()

    private val reply112 = ReplyDto(text = "I agree",
        postId = "64622074a5fabb5b05c9fa2a", createdBy = "645e07407d4be41e705879d0", createdByName = "Avin").toReplyModel()

    //POST 211
    private val reply211 = ReplyDto(text = "Great!!",
        postId = "64622074a5fabb5b05c9fa2b", createdBy = "645e07407d4be41e705879d0", createdByName = "Avin").toReplyModel()

    private val reply212 = ReplyDto(text = "Wrong info",
        postId = "64622074a5fabb5b05c9fa2b", createdBy = "645e3a40915b1b1ed782e512", createdByName = "Virat").toReplyModel()

    private val replies = listOf(reply111, reply112, reply211, reply212)

    fun insertDummyTopics(){
        topicRepository.insertAll(topics)
    }

    fun insertDummyThreads(){
        threadRepository.insertAll(threads)
    }

    fun insertDummyPosts(){
        postRepository.insertAll(posts)
    }

    fun insertDummyUserFavs(){
        userFavRepository.insertAll(userFavs)
    }

    fun insertDummyReplies(){
        replyRepository.insertAll(replies)
    }

}