# How to parse the purchase state in PHP? #

The purchase state JSON contains 2 long values : "nonce" and "purchaseTime", which will overflow the PHP integer type on 32-bit OS if you try using
```php

$purchase = json_decode($json); // long values will be converted to floats
```

If you have PHP >= 5.4.0, you can use :
```php

$purchase = json_decode($json, false, 512, JSON_BIGINT_AS_STRING);
```

On the previous PHP versions you can parse out "nonce" with a regular
expression :
```php

```
if (preg_match('/"nonce"\s*:\s*(\d+)/', $json, $matches))
  $nonce = $matches[1];
```
```