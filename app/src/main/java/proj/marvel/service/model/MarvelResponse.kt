package proj.marvel.service.model

data class MarvelResponse(var data: Data) {
    data class Data(var results: MutableList<Result>, var total: Int) {
        data class Result(var name: String, var description: String, var thumbnail: Thumbnail) {
            data class Thumbnail(var path: String, var extension: String)
        }
    }
}