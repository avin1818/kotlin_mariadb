package com.example.interfaces

import com.example.data.models.refreshToken.RefreshToken

interface RefreshTokenRepository: Repository<RefreshToken> {
    fun addRefreshToken(token: RefreshToken): Boolean
    fun findByRefreshToken(token: String): RefreshToken?
    fun findByUserId(userId: String): RefreshToken?
    fun deleteRefreshTokenForId(userId: String): Boolean
}