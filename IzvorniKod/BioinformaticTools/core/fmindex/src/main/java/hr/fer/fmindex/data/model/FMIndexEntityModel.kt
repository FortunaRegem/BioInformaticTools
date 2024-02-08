package hr.fer.fmindex.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.fer.common.constants.DatabaseConstants

@Entity(tableName = DatabaseConstants.FM_INDEX_TABLE)
@TypeConverters(FMIndexTypeConverters::class)
data class FMIndexEntityModel(
	@PrimaryKey(autoGenerate = false) val fmIndexId: Int,
	val sequence: String,
	val pattern: String,
	val suffixArray: List<Int>,
	val transformedStringBWT: String,
	val firstString: List<Pair<String, Int>>,
	val charRankings: Map<Char, List<Int>>,
	val resultsCount: Int,
	val dateCreated: Long,
)

class FMIndexTypeConverters {

	private val gson = Gson()

	@TypeConverter
	fun fromIntList(list: List<Int>): String = gson.toJson(list)

	@TypeConverter
	fun toIntList(json: String): List<Int> {
		val type = object : TypeToken<List<Int>>() {}.type
		return gson.fromJson(json, type)
	}

	@TypeConverter
	fun fromCharToListIntMap(map: Map<Char, List<Int>>): String = gson.toJson(map)

	@TypeConverter
	fun toCharToListIntMap(json: String): Map<Char, List<Int>> {
		val type = object : TypeToken<Map<Char, List<Int>>>() {}.type
		return gson.fromJson(json, type)
	}

	@TypeConverter
	fun fromListPairStringInt(value: List<Pair<String, Int>>): String {
		val type = object : TypeToken<List<Pair<String, Int>>>() {}.type
		return gson.toJson(value, type)
	}

	@TypeConverter
	fun toListPairStringInt(value: String): List<Pair<String, Int>> {
		val type = object : TypeToken<List<Pair<String, Int>>>() {}.type
		return gson.fromJson(value, type)
	}
}