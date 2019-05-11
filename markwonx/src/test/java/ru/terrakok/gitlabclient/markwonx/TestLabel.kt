package ru.terrakok.gitlabclient.markwonx

import ru.terrakok.gitlabclient.markwonx.label.LabelDescription
import ru.terrakok.gitlabclient.markwonx.label.LabelType

data class TestLabel(val type: LabelType, val label: LabelDescription)