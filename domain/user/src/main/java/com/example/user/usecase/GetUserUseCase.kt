package com.example.user.usecase

import com.example.network.ResponseState
import com.example.network.model.User
import com.example.user.repository.UserRepository
import javax.inject.Inject



class GetUserUseCase @Inject constructor(private val userRepo : UserRepository) {


/**
     * Executes the use case to fetch a list of users.
     * Uses the 'invoke' operator to allow calling the use case as a function.
     */

    suspend operator fun invoke(): ResponseState<List<User>> {
        return userRepo.getUsers()
    }
}

