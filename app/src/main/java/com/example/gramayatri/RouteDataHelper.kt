package com.example.gramayatri

/**
 * Seed data for all bus routes.
 * 10 realistic Karnataka (KSRTC) routes with 8–12 stops each.
 */
object RouteDataHelper {

    val ALL_ROUTES: List<Pair<String, List<Stop>>> = listOf(

        "Mysore - Bangalore" to listOf(
            Stop("Mysore Bus Stand",       0,   "06:00"),
            Stop("Mandya Junction",       40,   "06:40"),
            Stop("Maddur",                60,   "07:00"),
            Stop("Channapatna",           80,   "07:20"),
            Stop("Ramanagara",           100,   "07:40"),
            Stop("Bidadi",               120,   "08:00"),
            Stop("Kengeri",              135,   "08:15"),
            Stop("Mysore Road Junction", 148,   "08:28"),
            Stop("Majestic (KSRTC)",     160,   "08:40")
        ),

        "Tumkur - Majestic" to listOf(
            Stop("Tumkur Bus Stand",         0,  "07:30"),
            Stop("Sira Road Junction",      15,  "07:45"),
            Stop("Dabaspet",                40,  "08:10"),
            Stop("Nelamangala",             60,  "08:30"),
            Stop("Hesaraghatta Cross",      72,  "08:42"),
            Stop("Peenya Industrial Area",  85,  "08:55"),
            Stop("Yeshwanthpur",           100,  "09:10"),
            Stop("Rajajinagar",            110,  "09:20"),
            Stop("Majestic (KSRTC)",       120,  "09:30")
        ),

        "Hassan - Bangalore" to listOf(
            Stop("Hassan Bus Stand",         0,  "05:30"),
            Stop("Channarayapatna",         45,  "06:15"),
            Stop("Arsikere",                85,  "06:55"),
            Stop("Tiptur",                 120,  "07:30"),
            Stop("Tumkur",                 165,  "08:15"),
            Stop("Nelamangala",            205,  "08:55"),
            Stop("Peenya",                 220,  "09:10"),
            Stop("Yeshwanthpur",           235,  "09:25"),
            Stop("Majestic (KSRTC)",       255,  "09:45")
        ),

        "Ramanagara - Bangalore" to listOf(
            Stop("Ramanagara Bus Stand",     0,  "09:00"),
            Stop("Bidadi",                  20,  "09:20"),
            Stop("Kumbalgodu",              38,  "09:38"),
            Stop("Uttarahalli",             50,  "09:50"),
            Stop("Banashankari",            60,  "10:00"),
            Stop("Jayanagar 4th Block",     70,  "10:10"),
            Stop("Shivajinagar",            82,  "10:22"),
            Stop("Majestic (KSRTC)",        90,  "10:30")
        ),

        "Kengeri - Majestic" to listOf(
            Stop("Kengeri Bus Stand",        0,  "08:00"),
            Stop("Uttarahalli",             12,  "08:12"),
            Stop("Banashankari",            22,  "08:22"),
            Stop("Jayanagar 4th Block",     32,  "08:32"),
            Stop("Lalbagh West Gate",       42,  "08:42"),
            Stop("KR Market",               52,  "08:52"),
            Stop("Majestic (KSRTC)",        60,  "09:00")
        ),

        "Channapatna - Mysore" to listOf(
            Stop("Channapatna Bus Stand",    0,  "10:00"),
            Stop("Maddur",                  22,  "10:22"),
            Stop("Mandya Junction",         42,  "10:42"),
            Stop("Pandavapura",             60,  "11:00"),
            Stop("Srirangapatna",           78,  "11:18"),
            Stop("Naganahalli",             88,  "11:28"),
            Stop("Mysore Bus Stand",       100,  "11:40")
        ),

        "Kanakapura - Jayanagar" to listOf(
            Stop("Kanakapura Bus Stand",     0,  "08:00"),
            Stop("Sathanur",                18,  "08:18"),
            Stop("Harohalli",               35,  "08:35"),
            Stop("Kumbalgodu",              55,  "08:55"),
            Stop("Uttarahalli",             70,  "09:10"),
            Stop("Banashankari",            80,  "09:20"),
            Stop("JP Nagar",                88,  "09:28"),
            Stop("Jayanagar 4th Block",     95,  "09:35")
        ),

        "Nelamangala - Yeshwanthpur" to listOf(
            Stop("Nelamangala Bus Stand",    0,  "09:00"),
            Stop("Hirekerur",               15,  "09:15"),
            Stop("Hesaraghatta Cross",      28,  "09:28"),
            Stop("Peenya",                  42,  "09:42"),
            Stop("Nagasandra",              50,  "09:50"),
            Stop("Mahalakshmi Layout",      58,  "09:58"),
            Stop("Rajajinagar",             65,  "10:05"),
            Stop("Yeshwanthpur",            72,  "10:12")
        ),

        "Bidadi - Electronic City" to listOf(
            Stop("Bidadi Industrial Area",   0,  "08:30"),
            Stop("Kengeri",                 20,  "08:50"),
            Stop("RV College",              35,  "09:05"),
            Stop("Jayanagar 4th Block",     50,  "09:20"),
            Stop("Silk Board",              65,  "09:35"),
            Stop("BTM Layout",              75,  "09:45"),
            Stop("Bommanahalli",            83,  "09:53"),
            Stop("Electronic City Phase 1", 95,  "10:05"),
            Stop("Electronic City Phase 2",100,  "10:10")
        ),

        "Mandya - Mysore" to listOf(
            Stop("Mandya Bus Stand",         0,  "07:00"),
            Stop("KM Road Junction",        10,  "07:10"),
            Stop("Pandavapura",             25,  "07:25"),
            Stop("Srirangapatna",           40,  "07:40"),
            Stop("Naganahalli",             50,  "07:50"),
            Stop("Bannur Road",             58,  "07:58"),
            Stop("Mysore Bus Stand",        70,  "08:10")
        )
    )

    /** All unique stop names across all routes, sorted A-Z */
    fun getAllUniqueStops(): List<String> =
        ALL_ROUTES.flatMap { (_, stops) -> stops.map { it.name } }
            .distinct().sorted()

    /** Routes (routeName + Stop info) that pass through the given stop */
    fun getRoutesForStop(stopName: String): List<Pair<String, Stop>> =
        ALL_ROUTES.mapNotNull { (routeName, stops) ->
            stops.find { it.name == stopName }?.let { Pair(routeName, it) }
        }

    /** Route names list */
    fun getRouteNames(): List<String> = ALL_ROUTES.map { it.first }
}
