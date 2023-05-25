package com.example.interfaces

import com.example.data.models.Request.MarkAsFavRequest
import com.example.data.models.user_favorite.UserFavorite

interface UserFavRepository: Repository<UserFavorite> {
    fun markAsFav(request: MarkAsFavRequest): Any
}