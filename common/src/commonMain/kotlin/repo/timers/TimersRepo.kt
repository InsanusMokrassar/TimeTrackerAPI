package dev.inmo.time_tracker.common.repo.timers

import dev.inmo.micro_utils.repos.CRUDRepo
import dev.inmo.micro_utils.repos.ReadCRUDRepo
import dev.inmo.micro_utils.repos.WriteCRUDRepo
import dev.inmo.time_tracker.common.models.timers.NewTimer
import dev.inmo.time_tracker.common.models.timers.RegisteredTimer
import dev.inmo.time_tracker.common.models.timers.TimerId

interface ReadTimersRepo : ReadCRUDRepo<RegisteredTimer, TimerId>
interface WriteTimersRepo : WriteCRUDRepo<RegisteredTimer, TimerId, NewTimer>

interface TimersRepo : CRUDRepo<RegisteredTimer, TimerId, NewTimer>, ReadTimersRepo,
    WriteTimersRepo
