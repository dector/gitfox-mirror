package ru.terrakok.gitlabclient.presentation.global

import ru.terrakok.gitlabclient.entity.Note

data class NoteWithProjectId(val note: Note, val projectId: Long)