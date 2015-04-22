# How to validate the purchase in PHP? #

To validate you will need a file with a public key.
For the instructions to get it look into the "test-key" directory in the android-test-billing source code.

```php

function validate_purchase_data_signature($data, $signature) {
$publicKeyFile = "file://" . dirname(__FILE__) . "/android-test-billing-public-key.x509";
$publicKey = openssl_get_publickey($publicKeyFile);
$result = openssl_verify($data, base64_decode($signature), $publicKey, OPENSSL_ALGO_SHA1);
openssl_free_key($publicKey);
return $result === 1;
}

```