package com.example.services

import at.favre.lib.crypto.bcrypt.BCrypt

object BCryptService {

    fun createHashedPassword(password: String): String =
        BCrypt.withDefaults().hashToString(12, password.toCharArray())

    fun verifyPassword(password: String, hashedPassword: String): Boolean =
        BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified

}