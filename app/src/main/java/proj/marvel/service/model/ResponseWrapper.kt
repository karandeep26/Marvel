package proj.marvel.service.model

class ResponseWrapper(
    val data: MutableList<MarvelResponse.Data.Result>?,
    val exception: Exception?,
    val allContentLoaded: Boolean
)