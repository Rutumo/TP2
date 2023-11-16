package fr.eric.tp2.repository

import androidx.lifecycle.LiveData
import fr.eric.tp2.Model.User
import fr.eric.tp2.Model.UserDao
import fr.eric.tp2.data.RestApiDataSource
import kotlinx.coroutines.delay
import javax.inject.Inject

interface UserRepository {
    suspend fun getNewUser(): User
    suspend fun deleteUser(toDelete: User)
    fun getAllUsers(): LiveData<List<User>>
}

class UserRepositoryImp @Inject constructor(
    private val dataSource: RestApiDataSource,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getNewUser(): User {
        delay(3000)
        val name = dataSource.getUserName().results[0].name!!
        val picture = dataSource.getUserPicture().results[0].picture!!
        val user = User(name.first, name.last, picture.thumbnail)

        userDao.insert(user)
        return user
    }

    override fun getAllUsers() = userDao.getAll()

    override suspend fun deleteUser(toDelete: User) = userDao.delete(toDelete)

}