package campus.tech.kakao.map.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import campus.tech.kakao.map.data.db.entity.Place

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLog(place: Place)

    @Delete
    fun deleteLog(place: Place)

    @Query("SELECT * FROM research")
    fun getAllLogs(): List<Place>

    @Query("SELECT COUNT(*) FROM research")
    fun getPlaceCount(): Int
}