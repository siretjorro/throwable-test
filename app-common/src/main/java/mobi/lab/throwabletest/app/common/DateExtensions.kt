package mobi.lab.throwabletest.app.common

import java.time.OffsetDateTime

fun OffsetDateTime.toEpochMilli() = this.toInstant().toEpochMilli()
