package ru.netology

import org.junit.Test

import org.junit.Assert.*

class NotesServiceTest {

    @Test
    fun add() {
        val nid: Int = NotesService.add(Notes(nid = 1))
        assert(nid != 1)
    }

    @Test
    fun createComments() {
        val nid: Int = NotesService.add(Notes(nid = 1))
        val cid: Int = NotesService.createComments(Comment(cid = 1, noteId = nid))
        assert(cid != 1)
    }

    @Test
    fun deleteNoteTrue() {
        val nid: Int = NotesService.add(Notes(nid = 2))
        val test = NotesService.deleteNote(nid)
        assertTrue(test)
    }

    @Test
    fun deleteNoteFalse() {
        val nid = 1
        var test = true
        try {
            NotesService.deleteNote(nid)
        } catch (e: NoteNotFoundException) {
            test = false
        }
        assertFalse(test)
    }

    @Test
    fun deleteCommentTrue() {
        val nid: Int = NotesService.add(Notes(nid = 1))
        val cid: Int = NotesService.createComments(Comment(cid = 1, noteId = nid))
        val test = NotesService.deleteComment(cid)
        assertTrue(test)
    }

    @Test
    fun deleteCommentFalse() {
        val test = try {
            NotesService.deleteComment(1)
        } catch (e: CommentNotFoundException) {
            false
        }
        assertFalse(test)
    }

    @Test
    fun deleteAllCommentNoteTrue() {
        val nid = NotesService.add(Notes(nid = 1))
        NotesService.createComments(Comment(cid = 1, noteId = nid))
        NotesService.createComments(Comment(cid = 2, noteId = nid))

        val test: Boolean = NotesService.deleteAllCommentNote(nid)

        assertTrue(test)
    }

    @Test
    fun deleteAllCommentNoteFalse() {

        val test: Boolean = NotesService.deleteAllCommentNote(1)

        assertFalse(test)
    }

    @Test
    fun editTrue() {
        val nid = NotesService.add(Notes(nid = 1))
        val test = NotesService.edit(nid, Notes(text = "test"))

        assertTrue(test)
    }

    @Test
    fun editFalse() {

        val test = NotesService.edit(1, Notes(text = "test"))

        assertFalse(test)
    }


    @Test
    fun editeCommentTrue() {
        val nid = NotesService.add(Notes(nid = 1))
        val cid = NotesService.createComments(Comment(cid = 1, noteId = nid))
        val test = NotesService.editeComment(cid, Comment(message = " test"))

        assertTrue(test)

    }

    @Test
    fun editeCommentFalse() {
        val test = NotesService.editeComment(1, Comment(message = " test"))

        assertFalse(test)

    }

    @Test
    fun getNotes() {
        var test = false

        NotesService.add(Notes(nid = 1))

        if (NotesService.getNotes().isNotEmpty()) {
            test = true
        }

        assertTrue(test)
    }

    @Test
    fun getByIdTrue() {
        val nid = NotesService.add(Notes(nid = 1))
        var test = false

        test = try {
            NotesService.getById(nid)
            true
        } catch (e: NoteNotFoundException) {

            false
        }

        assertTrue(test)
    }

    @Test
    fun getByIdFalse() {
        var test = false

        test = try {
            NotesService.getById(1)
            true
        } catch (e: NoteNotFoundException) {

            false
        }

        assertFalse(test)
    }

    @Test
    fun getComments() {
        val nid = NotesService.add(Notes(nid = 1))
        val test: Boolean

        NotesService.createComments(Comment(cid = 1, noteId = nid))
        test = NotesService.getComments(nid).isNotEmpty()

        assertTrue(test)
    }

    @Test
    fun restoreComment() {
        val nid = NotesService.add(Notes(nid = 1))
        val cid = NotesService.createComments(Comment(cid = 1, noteId = nid))

        val test = try {
            NotesService.restoreComment(cid)
            true
        } catch (e: CommentNotFoundException) {
            false
        }
        assertTrue(test)
    }

}