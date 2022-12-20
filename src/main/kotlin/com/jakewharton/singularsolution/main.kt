@file:JvmName("Main")

package com.jakewharton.singularsolution

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import twitter4j.Twitter
import twitter4j.TwitterException
import twitter4j.v1.RateLimitStatus

fun main(vararg args: String) {
	SingularSolutionCommand().main(args)
}

private class SingularSolutionCommand : CliktCommand(
	name = "singular-solution",
	help = "Block and quickly unblock all followers to keep count at zero",
) {
	private val accessToken by option(metavar = "KEY")
		.required()
		.help("OAuth access token")
	private val accessSecret by option(metavar = "KEY")
		.required()
		.help("OAuth access token secret")
	private val apiKey by option(metavar = "KEY")
		.required()
		.help("OAuth consumer API key")
	private val apiSecret by option(metavar = "KEY")
		.required()
		.help("OAuth consumer API secret")
	private val dryRun by option()
		.flag()
		.help("Print destructive actions instead of performing them")

	override fun run() = runBlocking {
		val twitter = Twitter.newBuilder()
			.oAuthAccessToken(accessToken, accessSecret)
			.oAuthConsumer(apiKey, apiSecret)
			.build()
		val twitterV1 = twitter.v1()

		val users = twitterV1.users()
		val friendsFollowers = twitterV1.friendsFollowers()

		if (dryRun) {
			println("!!! DRY RUN !!!\n")
		}

		var rateLimitStatus: RateLimitStatus? = null
		var cursor = -1L
		while (cursor != 0L) {
			rateLimitStatus?.sleepIfNeeded()

			print("Fetching followers…")
			val ids = try {
				friendsFollowers.getFollowersIDs(cursor)
			} catch (e: TwitterException) {
				if (e.exceededRateLimitation()) {
					println(" failed.")
					rateLimitStatus = e.rateLimitStatus
					continue
				} else {
					throw e
				}
			}
			cursor = ids.nextCursor
			rateLimitStatus = ids.rateLimitStatus
			println(" done. (count=${ids.iDs.size}, hasMore=${cursor != 0L})\n")

			rateLimitStatus.sleepIfNeeded(callCount = 2)

			for (id in ids.iDs) {
				print("$id: blocking…")
				if (!dryRun) {
					try {
						users.createBlock(id)
					} catch (e: Throwable) {
						if (e is TwitterException && e.statusCode == 404) {
							println(" user not found!")
							rateLimitStatus = e.rateLimitStatus
							continue
						}
						println(" failed!")
						throw e
					}
				}
				print(" unblocking…")
				if (!dryRun) {
					val result = try {
						users.destroyBlock(id)
					} catch (e: Throwable) {
						println(" failed! MANUAL INTERVENTION NEEDED!!")
						throw e
					}
					rateLimitStatus = result.rateLimitStatus
				}
				println(" done.")
			}
		}
		println("\nAll done!")
		if (dryRun) {
			println("!!! DRY RUN !!!")
		}
	}

	private suspend fun RateLimitStatus.sleepIfNeeded(callCount: Int = 1) {
		if (remaining > callCount) return

		println()
		var lastLength = 0
		for (i in secondsUntilReset downTo 1) {
			val message = "\rRate limited! Cooling off ${i.seconds}…"
			if (message.length < lastLength) {
				print("\r" + " ".repeat(lastLength - 1))
			}
			lastLength = message.length
			print(message)
			delay(1.seconds)
		}
		delay(secondsUntilReset.seconds)
		println("\rRate limited! Cooling off… done")
	}
}
