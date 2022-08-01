package ru.netology


fun main() {

    val nid1: Int = NotesService.add(Notes(1, "Title", "note 1"))
    val nid2: Int = NotesService.add(Notes(1, "Another Title", "note 2"))
//    NotesService.printNotes()
//    NotesService.createComments(Comment(1, nid1, 1, 1, "comment note 1"))
//    NotesService.createComments(Comment(1, nid1, 1, 1, "another comment note 1"))
    val commentTestEdit = NotesService.createComments(Comment(1, nid2, 1, 1, "comment note 2"))
    val commentTestDelete = NotesService.createComments(Comment(1, nid2, 1, 1, "another comment note 2"))
//    NotesService.printComment(nid1)
//    NotesService.printComment(nid2)
//    NotesService.deleteNote(nid1)
//    NotesService.printNotes()
//    NotesService.printComment(nid1) // не печатает
//    NotesService.printComment(nid2)
    NotesService.deleteComment(commentTestDelete)
//    NotesService.edit(nid2, Notes(5,"Заголовок","Замена заметки"))
//    NotesService.printNotes()
//    NotesService.printComment(nid2)
//    NotesService.editeComment(commentTestEdit, Comment(message = "Отредактированный комментарий"))
//    NotesService.printComment(nid2)
//    println(NotesService.getNotes())
//    println(NotesService.getById(nid1))
    println(NotesService.getComments(nid2))
    NotesService.restoreComment(commentTestDelete)
    println(NotesService.getComments(nid2))
//    NotesService.deleteNote(1)
    println(NotesService.deleteAllCommentNote(nid2))
    println(NotesService.getComments(nid2))


}

data class Notes(
    val nid: Int = 1,
    val title: String = "",
    val text: String = "",
    val privacy: Int = 0,
    val commentPrivacy: Int = 0,
    val privacyView: String = "",
    val privacyComment: String = "",
    val delete: Boolean = false
)

data class Comment(
    val cid: Int = 1,
    val noteId: Int = 0,
    val ownerId: Int = 0,
    val replyTo: Int = 0,
    val message: String = "",
    val guid: String = "",
    val delete: Boolean = false
)

class NoteNotFoundException(message: String) : RuntimeException(message)

class CommentNotFoundException(message: String) : RuntimeException(message)

class CrudService<T>(val element: MutableList<T> = mutableListOf())


object NotesService {
    private var notes = CrudService<Notes>().element       //mutableListOf<Notes>()
    private val comments = CrudService<Comment>().element  //mutableListOf<Comment>()

    fun add(note: Notes): Int {
        val nid = note.hashCode()
        notes.add(note.copy(nid = nid))
        return notes.last().nid
    }

    fun createComments(comment: Comment): Int {
        val cid = comment.hashCode()
        for (note in notes)
            if (comment.noteId == note.nid) {

                comments.add(comment.copy(cid = cid))
            }
        return comments.last().cid
    }

    fun deleteNote(nid: Int): Boolean {
        for ((i, deleteNote) in notes.withIndex())
            if (nid == deleteNote.nid) {
                notes[i] = notes[i].copy(delete = true)
                deleteAllCommentNote(notes[i].nid)
                return true
            }
        throw NoteNotFoundException("Note not found")
    }

    fun deleteComment(cid: Int): Boolean {
        for ((i, deleteComment) in comments.withIndex())
            if (cid == deleteComment.cid) {
                comments[i] = comments[i].copy(delete = true)
                return true
            }
        throw CommentNotFoundException("Comment not found")
    }

    fun deleteAllCommentNote(nid: Int): Boolean {
        var canDelete = false
        for ((i, deleteComment) in comments.withIndex())
            if (nid == deleteComment.noteId) {
                comments[i] = comments[1].copy(delete = true)
                canDelete = true
            }
        return canDelete
    }

    fun edit(nid: Int, note: Notes): Boolean {
        for ((i, editNote) in notes.withIndex())
            if (!note.delete && nid == editNote.nid) {
                notes[i] = note.copy(nid = notes[i].nid)
                return true
            }
        return false
    }

    fun editeComment(cid: Int, comment: Comment): Boolean {

        for ((i, editeComment) in comments.withIndex()) {
            if (!comment.delete && cid == editeComment.cid) {
                comments[i] = comment.copy(cid = comments[i].cid, noteId = comments[i].noteId)
                return true
            }
        }
        return false
    }

    fun getNotes(): List<Notes> {
        val allNotes = mutableListOf<Notes>()
        for (note in notes) {
            if (!note.delete) allNotes += note
        }
        return allNotes
    }

    fun getById(nid: Int): Notes {
        for (note in notes) {
            if (!note.delete && note.nid == nid) return note
        }
        throw NoteNotFoundException("Note not found")
    }

    fun getComments(nid: Int): List<Comment> {
        val allComments = mutableListOf<Comment>()
        for (comment in comments) {
            if (!comment.delete && comment.noteId == nid)
                allComments += comment
        }
        return allComments
    }

    fun restoreComment(cid: Int): Boolean {
        val note: Notes
        for (comment in comments) {
            if (comment.cid == cid) {
                note = getById(comment.noteId)
                if (!note.delete) {
                    editeComment(cid, comment.copy(delete = false))
                    return true
                } else {
                    throw NoteNotFoundException("Note not found")
                }
            }
        }
        throw CommentNotFoundException("Comment not found")
    }


    fun printNotes() {
        for (note in notes)
            if (!note.delete) println(note)
    }

    fun printComment(noteId: Int) {
        for (comment in comments) {
            if (comment.noteId == noteId && !comment.delete) {
                println(comment)
            }
        }
    }
}

