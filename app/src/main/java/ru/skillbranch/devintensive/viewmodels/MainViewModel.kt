package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.avatarimageview.extentions.insertIf
import ru.skillbranch.avatarimageview.extentions.shortMessage
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModel : ViewModel() {

    private val chatRepository = ChatRepository
    private val chats = Transformations.map(chatRepository.loadChats()) { chats ->

        val allArchivedMessages = chats
            .filter { it.isArchived }
            .flatMap { it.messages }
            .sortedBy { it.date.time }

        val lastMessage = allArchivedMessages.lastOrNull()
        val (shortMessage, author) = shortMessage(lastMessage)

        chats.orEmpty()
            .filter { !it.isArchived }
            .map { it.toChatItem() }
            .sortedBy { it.id }
            .toMutableList()
            .insertIf(
                ChatItem.archiveItem(
                    shortMessage,
                    allArchivedMessages.count { message -> !message.isReaded },
                    lastMessage?.date?.shortFormat(),
                    author
                ), 0
            ) {
                chats.any { it.isArchived }
            }
    }
    private val query = mutableLiveData("")


    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filter = {
            val queryStr = query.value!!
            val qChats = chats.value!!

            result.value = if (queryStr.isEmpty()) chats.value!!
            else qChats.filter { it.title.contains(queryStr, true) }
        }

        result.addSource(chats) { filter.invoke() }
        result.addSource(query) { filter.invoke() }
        return result
    }

    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }
}