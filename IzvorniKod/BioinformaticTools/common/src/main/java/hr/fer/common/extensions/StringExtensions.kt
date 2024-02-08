package hr.fer.common.extensions

fun String?.checkIfNullOrEmpty() = this.isNullOrEmpty()

fun String?.checkIfNotNullOrEmpty() = !this.isNullOrEmpty()