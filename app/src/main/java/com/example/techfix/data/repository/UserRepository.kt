package com.example.techfix.data.repository

import com.example.techfix.data.dao.UserDao
import com.example.techfix.data.entity.User
import com.example.techfix.util.PasswordUtils

class UserRepository(private val userDao: UserDao) {

    suspend fun register(name: String, email: String, password: String, phone: String): Result<User> {
        val existing = userDao.getUserByEmail(email)
        if (existing != null) {
            return Result.failure(Exception("An account with this email already exists"))
        }
        val hashed = PasswordUtils.hash(password)
        val user = User(name = name, email = email, password = hashed, role = "customer", phone = phone)
        val newId = userDao.insertUser(user)
        return Result.success(user.copy(userId = newId.toInt()))
    }

    suspend fun login(email: String, password: String): Result<User> {
        val user = userDao.getUserByEmail(email)
            ?: return Result.failure(Exception("No account found with this email"))
        val passwordMatches = PasswordUtils.verify(password, user.password)
        return if (passwordMatches) {
            Result.success(user)
        } else {
            Result.failure(Exception("Incorrect password"))
        }
    }
}