package com.thebrownfoxx.petrealm.models

object Sample {
    val listType = listOf(
        "Cat","Dog"
    )
    val Pet = Pet(
        name = "Tan",
        age = 8,
        type = "Cat",
        owner = null
    )

    val Pets = listOf(
        Pet(
            name = "Jericho",
            age = 69,
            type = "Doggo",
            owner = Owner(
                name = "Andrea"
            )
        )
    )
}