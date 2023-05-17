package com.davidread.starwarsdatabase.viewmodel

import com.davidread.starwarsdatabase.model.datasource.ResourceResponse

/**
 * Common constants used by most `[...]ViewModelImplTest` classes.
 */
object BaseViewModelImplTestConstants {

    const val SINGLE_FRAGMENT_SCREEN_WIDTH_DP = 400
    const val MASTER_DETAIL_SCREEN_WIDTH_DP = 800

    fun getSuccessfulPersonResponse(id: Int = 1) = ResourceResponse.Person(
        name = "Person $id",
        height = "200",
        mass = "100",
        hairColor = "brown",
        skinColor = "unknown",
        eyeColor = "blue",
        birthYear = "200BBY",
        gender = "male",
        homeworldURL = "https://swapi.dev/api/planets/1/",
        filmsURLs = listOf(
            "https://swapi.dev/api/films/1/",
            "https://swapi.dev/api/films/2/",
            "https://swapi.dev/api/films/3/"
        ),
        speciesURLs = listOf(
            "https://swapi.dev/api/species/1/",
            "https://swapi.dev/api/species/2/",
            "https://swapi.dev/api/species/3/"
        ),
        vehiclesURLs = listOf(
            "https://swapi.dev/api/vehicles/1/",
            "https://swapi.dev/api/vehicles/2/",
            "https://swapi.dev/api/vehicles/3/"
        ),
        starshipsURLs = listOf(
            "https://swapi.dev/api/starships/1/",
            "https://swapi.dev/api/starships/2/",
            "https://swapi.dev/api/starships/3/"
        ),
        url = "https://swapi.dev/api/people/$id/"
    )

    fun getSuccessfulFilmResponse(id: Int = 1) = ResourceResponse.Film(
        title = "Film $id",
        episodeID = "1",
        openingCrawl = "Very long opening crawl. Tells a lot of story. Always three paragraphs.",
        director = "George Lucas",
        producer = "Gary Kurtz, Rick McCallum",
        releaseDate = "1977-05-25",
        characterURLs = listOf(
            "https://swapi.dev/api/people/1/",
            "https://swapi.dev/api/people/2/",
            "https://swapi.dev/api/people/3/"
        ),
        planetURLs = listOf(
            "https://swapi.dev/api/planets/1/",
            "https://swapi.dev/api/planets/2/",
            "https://swapi.dev/api/planets/3/"
        ),
        starshipURLs = listOf(
            "https://swapi.dev/api/starships/1/",
            "https://swapi.dev/api/starships/2/",
            "https://swapi.dev/api/starships/3/",
        ),
        vehicleURLs = listOf(
            "https://swapi.dev/api/vehicles/1/",
            "https://swapi.dev/api/vehicles/2/",
            "https://swapi.dev/api/vehicles/3/",
        ),
        speciesURLs = listOf(
            "https://swapi.dev/api/species/1/",
            "https://swapi.dev/api/species/2/",
            "https://swapi.dev/api/species/3/",
        ),
        url = "https://swapi.dev/api/films/$id/"
    )

    fun getSuccessfulStarshipResponse(id: Int = 1) = ResourceResponse.Starship(
        name = "Starship $id",
        model = "CR90 corvette",
        manufacturer = "Corellian Engineering Corporation",
        costInCredits = "3500000",
        length = "150",
        maxAtmospheringSpeed = "950",
        crew = "30-165",
        passengers = "600",
        cargoCapacity = "3000000",
        consumables = "1 year",
        hyperdriveRating = "2.0",
        mglt = "60",
        starshipClass = "corvette",
        pilotURLs = listOf(
            "https://swapi.dev/api/people/1/",
            "https://swapi.dev/api/people/2/",
            "https://swapi.dev/api/people/3/"
        ),
        filmURLs = listOf(
            "https://swapi.dev/api/films/1/",
            "https://swapi.dev/api/films/2/",
            "https://swapi.dev/api/films/3/"
        ),
        url = "https://swapi.dev/api/starships/$id/"
    )

    fun getSuccessfulVehicleResponse(id: Int = 1) = ResourceResponse.Vehicle(
        name = "Vehicle $id",
        model = "Digger Crawler",
        manufacturer = "Corellia Mining Corporation",
        costInCredits = "150000",
        length = "36.8",
        maxAtmospheringSpeed = "30",
        crew = "46",
        passengers = "30",
        cargoCapacity = "50000",
        consumables = "2 months",
        vehicleClass = "wheeled",
        pilotURLs = listOf(
            "https://swapi.dev/api/people/1/",
            "https://swapi.dev/api/people/2/",
            "https://swapi.dev/api/people/3/"
        ),
        filmURLs = listOf(
            "https://swapi.dev/api/films/1/",
            "https://swapi.dev/api/films/2/",
            "https://swapi.dev/api/films/3/"
        ),
        url = "https://swapi.dev/api/vehicles/$id/"
    )

    fun getSuccessfulSpeciesResponse(id: Int = 1) = ResourceResponse.Species(
        name = "Species $id",
        classification = "mammal",
        designation = "sentient",
        averageHeight = "180",
        skinColors = "caucasian, black, asian, hispanic",
        hairColors = "blonde, brown, black, red",
        eyeColors = "brown, blue, green, hazel, grey, amber",
        averageLifespan = "120",
        homeworldURL = "https://swapi.dev/api/planets/1/",
        language = "Galactic Basic",
        peopleURLs = listOf(
            "https://swapi.dev/api/people/1/",
            "https://swapi.dev/api/people/2/",
            "https://swapi.dev/api/people/3/"
        ),
        filmURLs = listOf(
            "https://swapi.dev/api/films/1/",
            "https://swapi.dev/api/films/2/",
            "https://swapi.dev/api/films/3/"
        ),
        url = "https://swapi.dev/api/species/$id/"
    )

    fun getSuccessfulPlanetResponse(id: Int = 1) = ResourceResponse.Planet(
        name = "Planet $id",
        rotationPeriod = "23",
        orbitalPeriod = "304",
        diameter = "10465",
        climate = "arid",
        gravity = "1 standard",
        terrain = "desert",
        surfaceWater = "1",
        population = "200000",
        residentURLs = listOf(
            "https://swapi.dev/api/people/1/",
            "https://swapi.dev/api/people/2/",
            "https://swapi.dev/api/people/3/"
        ),
        filmURLs = listOf(
            "https://swapi.dev/api/films/1/",
            "https://swapi.dev/api/films/2/",
            "https://swapi.dev/api/films/3/",
        ),
        url = "https://swapi.dev/api/planets/$id/"
    )
}