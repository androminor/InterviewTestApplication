package com.example.user

import com.example.network.model.NetworkUser
import com.example.network.model.User

fun NetworkUser.toDomain():User{
    return User(
        id = id,
        name = name,
        username = username,
        email = email,
        city = address.city
    )
}
/*

fun NetworkUser.toDomain(): User {
    return User(
        id = id,
        name = name,
        username = username,
        email = email,
        city = address.city
    )
}*/
