package ru.skillbranch.devintensive.utils

import android.graphics.drawable.Drawable
import android.view.View
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback

class ArchiveItemTouchHelperCallback(
    adapter: ChatAdapter,
    swipeListener: (ChatItem) -> Unit
) : ChatItemTouchHelperCallback(adapter, swipeListener) {

    override fun getIcon(view: View): Drawable {
        return view.resources.getDrawable(
            R.drawable.ic_baseline_unarchive_24,
            view.context.theme
        )
    }
}