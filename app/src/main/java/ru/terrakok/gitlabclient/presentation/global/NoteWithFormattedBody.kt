package ru.terrakok.gitlabclient.presentation.global

import gitfox.entity.Note

data class NoteWithFormattedBody(val note: Note, val body: CharSequence)
