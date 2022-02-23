package me.devtools4.ts.api

sealed trait Side
case object Bid extends Side
case object Ask extends Side
