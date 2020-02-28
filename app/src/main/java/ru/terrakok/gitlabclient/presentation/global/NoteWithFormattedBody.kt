package ru.terrakok.gitlabclient.presentation.global

import ru.terrakok.gitlabclient.entity.Note

data class NoteWithFormattedBody(val note: Note, val body: CharSequence)
