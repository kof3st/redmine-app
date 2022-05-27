package me.kofesst.android.redminecomposeapp.data.model.tracker

import me.kofesst.android.redminecomposeapp.data.model.issue.TrackerDto

data class TrackersResponse(
    val trackers: List<TrackerDto>,
)