package edu.utap.recyclerviewfrag

// This is the model in MVVM
data class Data(val name: String, val rating: Boolean)
// This abstracts either stable storage (file or database)
//   or the network
class Repository {
    companion object {
        private var initialDataList = listOf (
            Data("The Grateful Dead", false),
            Data("Anthem Of The Sun", true),
            Data("Aoxomoxoa", true),
            Data("Live/Dead", true),
            Data("Workingman's Dead", true),
            Data("American Beauty", true),
            Data("Grateful Dead (Skull & Roses)", false),
            Data("Europe '72", true),
            Data("History Of The Grateful Dead, Vol. 1 (Bear's Choice)", false),
            Data("Wake Of The Flood", true),
            Data("Grateful Dead From The Mars Hotel", false),
            Data("Blues For Allah", true),
            Data("Steal Your Face", false),
            Data("Terrapin Station", true),
            Data("Shakedown Street", false),
            Data("Go To Heaven", false),
            Data("Reckoning", true),
            Data("Dead Set", false),
            Data("In The Dark", false),
            Data("Dylan And The Dead", false),
            Data("Built To Last", false),
            Data("Without A Net", true)
        )
    }

    fun fetchData(): List<Data> {
        return initialDataList
    }
}