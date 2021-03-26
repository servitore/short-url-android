package com.example.urlshortner.models.mappers

import com.example.urlshortner.models.Link
import com.example.urlshortner.models.LinkResponse

fun LinkResponse.responseToLink() = Link(link)