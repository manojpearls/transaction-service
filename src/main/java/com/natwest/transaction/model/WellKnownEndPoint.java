package com.natwest.transaction.model;

import lombok.Data;

import java.util.List;

@Data
public class WellKnownEndPoint {
    private String version;
    private String issuer;
    private String authorization_endpoint;
    private String token_endpoint;
    private String jwks_uri;
    private String registration_endpoint;
    private List<String> scopes_supported;
    private List<String> claims_supported;
    private List<String> acr_values_supported;
    private List<String> response_types_supported;
    private List<String> response_modes_supported;
    private List<String> grant_types_supported;
    private List<String> subject_types_supported;
    private List<String> id_token_signing_alg_values_supported;
    private List<String> token_endpoint_auth_methods_supported;
    private List<String> token_endpoint_auth_signing_alg_values_supported;
    private List<String> claim_types_supported;
    private boolean claims_parameter_supported;
    private boolean request_parameter_supported;
    private boolean request_uri_parameter_supported;
    private List<String> request_object_signing_alg_values_supported;
    private List<String> request_object_encryption_alg_values_supported;
    private List<String> request_object_encryption_enc_values_supported;
    private boolean tls_client_certificate_bound_access_tokens;
}

