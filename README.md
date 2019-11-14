
# README

[![Release](https://jitpack.io/v/com.ixopay/pgc-java-client.svg)](https://jitpack.io/#com.ixopay/pgc-java-client)

## Using the application

Run `./pgc-generate [mode]` and provide the required arguments to generate a client library.

Where `mode` is either

 * `java-client` for generating java client libraries
 * `tokenization-android` to create an Android tokenization project, including a demo app
   * NOTE: for the build to be successful the `--output-dir` must contain a `local.properties` file which sets the `sdk.dir` property to the location of a valid Android SDK

| short argument | long argument | description | example value |
|---|---|---|---|
| `-i` | `--input-dir` | **(depending on mode defaults to `./pgc-java-client-template` or `./pgc-tokenization-android-template`)** Template for generating the client library. | `./pgc-java-client-template` |
| `-o` | `--output-dir` | Target directory where the generate client will be located, must exist. | `./java-paymentgateway-cloud` |
| `-p` | `--package` | Java base package name for the generated client library, also used for Maven group-name. | `cloud.paymentgateway` |
| `-n` | `--name` | Short name for the generated client library, also used as GitHub repository name for generated README. | `java-paymentgateway-cloud` |
| `-u` | `--url` | Base URL of the payment gateway, used for fetching XSD schemas. | `https://paymentgateway.cloud` |
| | `--product-name` | Name of the product used in javadocs. | `PAYMGENTGATEWAY-CLOUD` |
| | `--github-organization` | Name of the GitHub organization used in the generated README. | `PaymentgatewayCloud` |
| `-g` | `--gradle-task` | Execute gradle task after generation from template. (default depends on mode) | |
| `-s` | `--disable-download-schemas` | Don't download schemas from `--url`. (default depends on mode) | |

### Example

```bash
mkdir ../java-paymentgateway-cloud

./pgc-generate java-client \
    --output-dir ../java-paymentgateway-cloud \
    --package cloud.paymentgateway \
    --name java-paymentgateway-cloud \
    --url https://paymentgateway.cloud \
    --product-name PAYMENTGATEWAY-CLOUD \
    --github-organization PaymentgatewayCloud
```

```bash
mkdir ../tokenization-android-paymentgateway-cloud
[ ! -z "$ANDROID_HOME" ] && (echo "sdk.dir=$ANDROID_HOME" > ../tokenization-android-paymentgateway-cloud/local.properties) || echo "ERROR: environment variable \$ANDROID_HOME is not set"  

[ ! -z "$ANDROID_HOME" ] && ./pgc-generate tokenization-android \
    --output-dir ../tokenization-android-paymentgateway-cloud \
    --package cloud.paymentgateway \
    --name tokenization-android-paymentgateway-cloud \
    --url https://paymentgateway.cloud \
    --tokenization-url https://secure.paymentgateway.cloud \
    --product-name PAYMENTGATEWAY-CLOUD \
    --github-organization PaymentgatewayCloud \
    || echo "ERROR: environment variable \$ANDROID_HOME is not set"
```
