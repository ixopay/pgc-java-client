
# README

[![Release](https://jitpack.io/v/com.ixopay/pgc-java-client.svg)](https://jitpack.io/#com.ixopay/pgc-java-client)

## Using the application

Run `./pgc-generate` and provide the required arguments to generate a client library.

| short argument | long argument | description | example value |
|---|---|---|---|
| `-i` | `--input-dir` | **(defaults to `./template-java-client`)** Template for generating the client library. | `./pgc-java-client-template` |
| `-o` | `--output-dir` | Target directory where the generate client will be located, must exist. | `./java-paymentgateway-cloud` |
| `-p` | `--package` | Java base package name for the generated client library, also used for Maven group-name. | `cloud.paymentgateway` |
| `-n` | `--name` | Short name for the generated client library, also used as GitHub repository name for generated README. | `java-paymentgateway-cloud` |
| `-u` | `--url` | Base URL of the payment gateway, used for fetching XSD schemas. | `https://paymentgateway.cloud` |
| | `--github-organization` | Name of the GitHub organization used in the generated README. | `PaymentgatewayCloud` |

### Example

```bash
mkdir ../java-paymentgateway-cloud

./pgc-generate \
    --output-dir ../java-paymentgateway-cloud \
    --package cloud.paymentgateway \
    --name java-paymentgateway-cloud \
    --url https://paymentgateway.cloud \
    --github-organization PaymentgatewayCloud
```
