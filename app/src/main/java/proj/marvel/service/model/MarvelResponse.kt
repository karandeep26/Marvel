package proj.marvel.service.model

import com.squareup.moshi.Json

data class MarvelResponse(@field:Json(name = "data") var data: Data) {
    data class Data(@field:Json(name = "results") var results: MutableList<Result>, @field:Json(name = "total") var total: Int) {
        data class Result(
            @field:Json(name = "name") var name: String, @field:Json(name = "description") var description: String,
            @field:Json(name = "thumbnail") var thumbnail: Thumbnail
        ) {
            data class Thumbnail(@field:Json(name = "path") var path: String, @field:Json(name = "extension") var extension: String)
        }
    }
}