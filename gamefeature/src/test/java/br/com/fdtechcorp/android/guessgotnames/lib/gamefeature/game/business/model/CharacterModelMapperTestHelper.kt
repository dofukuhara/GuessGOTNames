package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model

import com.google.gson.Gson

object CharacterModelMapperTestHelper {
    fun getEmptyResponse() = parseJsonToCharactersListVo(emptyJsonResponse())
    fun getCompleteSingleItemResponse() = parseJsonToCharactersListVo(completeSingleItemResponse())
    fun getCompleteTenItemsResponse() = parseJsonToCharactersListVo(completeJsonTenItemsResponse())
    fun getMissingFirstNameResponse() = parseJsonToCharactersListVo(singleItemMissingFirstName())
    fun getMissingLastNameResponse() = parseJsonToCharactersListVo(singleItemMissingLastName())
    fun getMissingImageUrlResponse() = parseJsonToCharactersListVo(singleItemMissingImageUrl())
}

private fun parseJsonToCharactersListVo(json: String) = Gson().fromJson(json, CharactersListVo::class.java)

private fun emptyJsonResponse() = ""

private fun singleItemMissingFirstName() = "[\n" +
    "{\n" +
    "\"id\": 0,\n" +
    "\"lastName\": \"Targaryen\",\n" +
    "\"fullName\": \"Daenerys Targaryen\",\n" +
    "\"title\": \"Mother of Dragons\",\n" +
    "\"family\": \"House Targaryen\",\n" +
    "\"image\": \"daenerys.jpg\",\n" +
    "\"imageUrl\": \"https://thronesapi.com/assets/images/daenerys.jpg\"\n" +
    "}\n" +
    "]"

private fun singleItemMissingLastName() = "[\n" +
    "{\n" +
    "\"id\": 0,\n" +
    "\"firstName\": \"Daenerys\",\n" +
    "\"fullName\": \"Daenerys Targaryen\",\n" +
    "\"title\": \"Mother of Dragons\",\n" +
    "\"family\": \"House Targaryen\",\n" +
    "\"image\": \"daenerys.jpg\",\n" +
    "\"imageUrl\": \"https://thronesapi.com/assets/images/daenerys.jpg\"\n" +
    "}\n" +
    "]"

private fun singleItemMissingImageUrl() = "[\n" +
    "{\n" +
    "\"id\": 0,\n" +
    "\"firstName\": \"Daenerys\",\n" +
    "\"lastName\": \"Targaryen\",\n" +
    "\"fullName\": \"Daenerys Targaryen\",\n" +
    "\"title\": \"Mother of Dragons\",\n" +
    "\"family\": \"House Targaryen\",\n" +
    "\"image\": \"daenerys.jpg\"\n" +
    "}\n" +
    "]"

private fun completeSingleItemResponse() = "[\n" +
    "{\n" +
    "\"id\": 0,\n" +
    "\"firstName\": \"Daenerys\",\n" +
    "\"lastName\": \"Targaryen\",\n" +
    "\"fullName\": \"Daenerys Targaryen\",\n" +
    "\"title\": \"Mother of Dragons\",\n" +
    "\"family\": \"House Targaryen\",\n" +
    "\"image\": \"daenerys.jpg\",\n" +
    "\"imageUrl\": \"https://thronesapi.com/assets/images/daenerys.jpg\"\n" +
    "}\n" +
    "]"

private fun completeJsonTenItemsResponse() = "[\n" +
    "{\n" +
    "\"id\": 0,\n" +
    "\"firstName\": \"Daenerys\",\n" +
    "\"lastName\": \"Targaryen\",\n" +
    "\"fullName\": \"Daenerys Targaryen\",\n" +
    "\"title\": \"Mother of Dragons\",\n" +
    "\"family\": \"House Targaryen\",\n" +
    "\"image\": \"daenerys.jpg\",\n" +
    "\"imageUrl\": \"https://thronesapi.com/assets/images/daenerys.jpg\"\n" +
    "},\n" +
    "{\n" +
    "\"id\": 1,\n" +
    "\"firstName\": \"Samwell\",\n" +
    "\"lastName\": \"Tarly\",\n" +
    "\"fullName\": \"Samwell Tarly\",\n" +
    "\"title\": \"Maester\",\n" +
    "\"family\": \"House Tarly\",\n" +
    "\"image\": \"sam.jpg\",\n" +
    "\"imageUrl\": \"https://thronesapi.com/assets/images/sam.jpg\"\n" +
    "},\n" +
    "{\n" +
    "\"id\": 2,\n" +
    "\"firstName\": \"Jon\",\n" +
    "\"lastName\": \"Snow\",\n" +
    "\"fullName\": \"Jon Snow\",\n" +
    "\"title\": \"King of the North\",\n" +
    "\"family\": \"House Stark\",\n" +
    "\"image\": \"jon-snow.jpg\",\n" +
    "\"imageUrl\": \"https://thronesapi.com/assets/images/jon-snow.jpg\"\n" +
    "},\n" +
    "{\n" +
    "\"id\": 3,\n" +
    "\"firstName\": \"Arya\",\n" +
    "\"lastName\": \"Stark\",\n" +
    "\"fullName\": \"Arya Stark\",\n" +
    "\"title\": \"No One\",\n" +
    "\"family\": \"House Stark\",\n" +
    "\"image\": \"arya-stark.jpg\",\n" +
    "\"imageUrl\": \"https://thronesapi.com/assets/images/arya-stark.jpg\"\n" +
    "},\n" +
    "{\n" +
    "\"id\": 4,\n" +
    "\"firstName\": \"Sansa\",\n" +
    "\"lastName\": \"Stark\",\n" +
    "\"fullName\": \"Sansa Stark\",\n" +
    "\"title\": \"Lady of Winterfell\",\n" +
    "\"family\": \"House Stark\",\n" +
    "\"image\": \"sansa-stark.jpeg\",\n" +
    "\"imageUrl\": \"https://thronesapi.com/assets/images/sansa-stark.jpeg\"\n" +
    "},\n" +
    "{\n" +
    "\"id\": 5,\n" +
    "\"firstName\": \"Brandon\",\n" +
    "\"lastName\": \"Stark\",\n" +
    "\"fullName\": \"Brandon Stark\",\n" +
    "\"title\": \"Lord of Winterfell\",\n" +
    "\"family\": \"House Stark\",\n" +
    "\"image\": \"bran-stark.jpg\",\n" +
    "\"imageUrl\": \"https://thronesapi.com/assets/images/bran-stark.jpg\"\n" +
    "},\n" +
    "{\n" +
    "\"id\": 6,\n" +
    "\"firstName\": \"Ned\",\n" +
    "\"lastName\": \"Stark\",\n" +
    "\"fullName\": \"Ned Stark\",\n" +
    "\"title\": \"Lord of Winterfell\",\n" +
    "\"family\": \"House Stark\",\n" +
    "\"image\": \"ned-stark.jpg\",\n" +
    "\"imageUrl\": \"https://thronesapi.com/assets/images/ned-stark.jpg\"\n" +
    "},\n" +
    "{\n" +
    "\"id\": 7,\n" +
    "\"firstName\": \"Robert\",\n" +
    "\"lastName\": \"Baratheon\",\n" +
    "\"fullName\": \"Robert Baratheon\",\n" +
    "\"title\": \"Lord of the Seven Kingdoms\",\n" +
    "\"family\": \"House Baratheon\",\n" +
    "\"image\": \"robert-baratheon.jpeg\",\n" +
    "\"imageUrl\": \"https://thronesapi.com/assets/images/robert-baratheon.jpeg\"\n" +
    "},\n" +
    "{\n" +
    "\"id\": 8,\n" +
    "\"firstName\": \"Jamie\",\n" +
    "\"lastName\": \"Lannister\",\n" +
    "\"fullName\": \"Jamie Lannister\",\n" +
    "\"title\": \"Lord Commander of the Kingsguard\",\n" +
    "\"family\": \"House Lannister\",\n" +
    "\"image\": \"jaime-lannister.jpg\",\n" +
    "\"imageUrl\": \"https://thronesapi.com/assets/images/jaime-lannister.jpg\"\n" +
    "},\n" +
    "{\n" +
    "\"id\": 9,\n" +
    "\"firstName\": \"Cersei\",\n" +
    "\"lastName\": \"Lannister\",\n" +
    "\"fullName\": \"Cersei Lannister\",\n" +
    "\"title\": \"Lady of Casterly Rock\",\n" +
    "\"family\": \"House Lannister\",\n" +
    "\"image\": \"cersei.jpg\",\n" +
    "\"imageUrl\": \"https://thronesapi.com/assets/images/cersei.jpg\"\n" +
    "}\n" +
    "]"
