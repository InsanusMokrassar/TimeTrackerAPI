package dev.inmo.time_tracker.common.models.acitivties

import dev.inmo.micro_utils.repos.annotations.GenerateCRUDModel
import kotlinx.serialization.Serializable

@Serializable
@GenerateCRUDModel(IRegisteredActivity::class)
sealed interface Activity {
    val title: String
}

@Serializable
sealed interface IRegisteredActivity : Activity {
    val id: ActivityId
}