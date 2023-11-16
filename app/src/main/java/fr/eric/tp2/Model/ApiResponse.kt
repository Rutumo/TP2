package fr.eric.tp2.Model

data class ApiResponse(
    val results: List<Results> = emptyList(),
)

data class Results(
    val name: ResultsInfo?,
    val picture: UserPicture?,
)