package apps.crevion.com.salinia

/**
 * Created by yusufaw on 11/24/17.
 */

class Note(id: Number, content: String) {
    val id = id
    val content = content

    override fun toString(): String {
        return "Note(id=$id, content='$content')"
    }
}
