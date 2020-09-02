package ru.skillbranch.devintensive.models.data


data class ChatItem (
    val id: String,
    val avatar: String?,
    val initials: String,
    val title: String,
    val shortDescription: String?,
    val messageCount: Int = 0,
    val lastMessageDate: String?,
    val isOnline: Boolean = false,
    val chatType : ChatType = ChatType.SINGLE,
    var author :String? = null
){
    companion object {
        fun archiveItem(shortDescription: String?, messageCount: Int, lastMessageDate: String?, author: String?): ChatItem {
            return ChatItem(
                "-1",
                null,
                "",
                "Архив чатов",
                shortDescription,
                messageCount,
                lastMessageDate,
                false,
                ChatType.ARCHIVE,
                author
            )
        }
    }
}