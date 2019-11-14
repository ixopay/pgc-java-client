# pgcclient-tokenization-android

This SDK enables you to tokenize card data natively from your Android application to our PCI-certified servers.

## Usage

- See [![](https://jitpack.io/v/PgcClient/pgcclient-tokenization-android.svg)](https://jitpack.io/#PgcClient/pgcclient-tokenization-android)
- [Javadoc](https://javadoc.jitpack.io/com/PgcClient/pgcclient-tokenization-android/latest/javadoc/index.html)


## Examples

### Tokenization API
- See Android invocation of [TokenizationApi.java](pgcclient-tokenization-api/src/main/java/pgc/api/tokenization/TokenizationApi.java) in [MainActivity.java](pgcclient-tokenization-demoapp/src/main/java/pgc/tokenizationdemo/MainActivity.java)
- See minimal example in [TestMain.java](pgcclient-tokenization-api/src/test/java/pgc/api/tokenization/TestMain.java)


### DemoApp
- Set appropriate credentials in [secrets.xml](pgcclient-tokenization-demoapp/src/main/res/values/secrets.xml)
- Start the demo application

## Development

### Requirements
- Java 8 or higher

### Dependencies
- [org.json](https://github.com/stleary/JSON-java) (built-in Android)
- [OkHttp 3.x](http://square.github.io/okhttp/) (built-in Android 4.4 or higher)

### Executing the tests
```
./gradlew test -Dgateway=<TEST_GATEWAY_HOST> -Dtokenization=<TEST_TOKENIZATION_HOST> -Dkey=<TEST_PUBLIC_INTEGRATION_KEY>
```

## License

[LICENSE](LICENSE)

## Changelog

[CHANGELOG.md](CHANGELOG.md)
