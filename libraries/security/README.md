When using `security` library you need to introduce next parameters in application.yml:
* jwt.shared-key: ${JWT_SHARED_KEY:DEFAULT64CHARKEY5368566D5970337336763979244226452948404D63510000}

* auth-internal-client.url: ${AUTH_INTERNAL_URL}
* auth-internal-client.token: ${AUTH_INTERNAL_TOKEN}