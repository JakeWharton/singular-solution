# Singular Solution

Keep your Twitter follower count at zero by blocking and then quickly unblocking any new followers.

Available as a binary and Docker container.


## Usage

Singular Solution consumes data from Twitter and requires you register your own Twitter application for keys:
https://developer.twitter.com/en/apply-for-access

You will need an OAuth access token and access secret as well as an API key and secret.

From there, you can run Singular Solution in one of two ways:

 * [Command line](#command-line)
 * [Docker](#docker)

### Command-line

Install on Mac OS with:
```
$ brew install JakeWharton/repo/singular-solution
```

For other platforms, download ZIP from
[latest release](https://github.com/JakeWharton/singular-solution/releases/latest)
and run `bin/singular-solution` or `bin/singular-solution.bat`.

```
$ singular-solution --help
Usage: singular-solution [OPTIONS]

  Block and quickly unblock all followers to keep count at zero

Options:
  --access-token KEY   OAuth access token
  --access-secret KEY  OAuth access token secret
  --api-key KEY        OAuth consumer API key
  --api-secret KEY     OAuth consumer API secret
  --dry-run            Print destructive actions instead of performing them
  -h, --help           Show this message and exit
```

## Docker

The container automatically triggers the binary using cron.

[![Docker Image Version](https://img.shields.io/docker/v/jakewharton/singular-solution?sort=semver)][hub]
[![Docker Image Size](https://img.shields.io/docker/image-size/jakewharton/singular-solution)][hub]

[hub]: https://hub.docker.com/r/jakewharton/singular-solution/

```
$ docker run -it --rm
    -e "CRON=*/3 * * * *" \
    -e "ACCESS_TOKEN=..." \
    -e "ACCESS_SECRET=..." \
    -e "API_KEY=..." \
    -e "API_SECRET=..." \
    jakewharton/singular-solution:trunk
```

To be notified when the binary is failing visit https://healthchecks.io, create a check, and specify
the ID to the container using the `HEALTHCHECK_ID` environment variable.

### Docker Compose

```yaml
version: '2'
services:
  singular-solution:
    image: jakewharton/singular-solution:trunk
    restart: unless-stopped
    environment:
      - "CRON=*/3 * * * *"
      - "ACCESS_TOKEN=..."
      - "ACCESS_SECRET=..."
      - "API_KEY=..."
      - "API_SECRET=..."
      #Optional:
      - "HEALTHCHECK_ID=..."
```


## Development

To run the latest code build with `./gradlew installDist`.  This will put the application into
`build/install/singular-solution/`. From there you can use the
[command-line instructions](#command-line) instructions to run.

The Docker containers can be built with `docker build .`, which also runs the full set of checks
as CI would.


# License

    Copyright 2022 Jake Wharton

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
