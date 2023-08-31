# Changelog

## [Unreleased]

## [2.0.0]

- **Breaking change**: The old CLI behavior is now under the `run` subcommand.

    Before:
    ```
    $ singular-solution --api-key=... etc.
    ```

    After:
    ```
    $ singular-solution run --api-key=... etc.
    ```

    Usage as a Docker container is unaffected by this change.

- **New**: `auth` subcommand can provide an access token and access secret given only an API key
  and API secret. Don't have an API key and API secret? Google around and you can find the official
  ones used for the first-party clients which have full access to the API endpoints needed.

    Example usage:
    ```
    $ singular-solution auth --api-key <key> --api-secret <secret>
    Visit the following URL in your browser to authorize the app:

        https://api.twitter.com/oauth/authorize?oauth_token=<token>

    Once completed, you should see a PIN. Paste that below:

    PIN: 123456

    SUCCESS!

    Consumer API key: <key>
    Consumer API secret: <secret>
    Access token: <access-token>
    Access token secret: <access-secret>
    ```


## [1.0.1]

 - Fix rate limiting logic
 - Print a big, greppable message ("MANUAL INTERVENTION NEEDED!!") if unblocking fails


## [1.0.0]

 - Initial release


[Unreleased]: https://github.com/JakeWharton/singular-solution/compare/2.0.0...HEAD
[2.0.0]: https://github.com/JakeWharton/singular-solution/releases/tag/2.0.0
[1.0.1]: https://github.com/JakeWharton/singular-solution/releases/tag/1.0.1
[1.0.0]: https://github.com/JakeWharton/singular-solution/releases/tag/1.0.0
