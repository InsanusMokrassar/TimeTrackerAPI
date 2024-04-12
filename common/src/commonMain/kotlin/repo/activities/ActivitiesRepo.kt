package dev.inmo.time_tracker.common.repo.activities

import dev.inmo.micro_utils.repos.CRUDRepo
import dev.inmo.micro_utils.repos.ReadCRUDRepo
import dev.inmo.micro_utils.repos.WriteCRUDRepo
import dev.inmo.time_tracker.common.models.acitivties.ActivityId
import dev.inmo.time_tracker.common.models.acitivties.NewActivity
import dev.inmo.time_tracker.common.models.acitivties.RegisteredActivity

interface ReadActivitiesRepo : ReadCRUDRepo<RegisteredActivity, ActivityId>
interface WriteActivitiesRepo : WriteCRUDRepo<RegisteredActivity, ActivityId, NewActivity>

interface ActivitiesRepo : CRUDRepo<RegisteredActivity, ActivityId, NewActivity>, ReadActivitiesRepo,
    WriteActivitiesRepo
