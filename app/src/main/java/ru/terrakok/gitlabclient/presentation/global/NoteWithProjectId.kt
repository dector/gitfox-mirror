package ru.terrakok.gitlabclient.presentation.global

import gitfox.entity.Note

data class NoteWithProjectId(val note: Note, val projectId: Long)