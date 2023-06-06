# CBC Demo Policy Extension - Camel Proxy Service


## To Deploy

1. Create a OCP Project if not done:
    ```
    oc new-project threescale-camel

    ```
2. Create `keystore.jks` from Ingress Controller:

    ```
    oc get secret -n openshift-ingress  ingress-certs-2023-06-05 -o go-template='{{index .data "tls.key"}}' | base64 -d | tee tls.key  > /dev/null
    oc get secret -n openshift-ingress  ingress-certs-2023-06-05 -o go-template='{{index .data "tls.crt"}}' | base64 -d | tee tls.crt  > /dev/null
    
    openssl pkcs12 -export -in tls.crt -inkey tls.key -out tls.p12

    keytool -importkeystore -srckeystore tls.p12 \
        -srcstoretype PKCS12 \
        -destkeystore keystore.jks \
        -deststoretype JKS
    
    oc create secret generic tls --from-literal=keystore_password=changeit --from-file=keystore.jks=keystore.jks --from-literal=truststore_password=changeit --from-file=truststore.jks=keystore.jks

    ```

2. Go to project folder and run the following command to deploy:
    
    ```
    ./mvnw clean && \
    ./mvnw install \
    -Dquarkus.kubernetes.deploy=true \
    -Dquarkus.container-image.group=threescale-camel \
    -Dquarkus.openshift.namespace=threescale-camel \
    -Dquarkus.log.category.\"blog.braindose\".level=DEBUG \
    -Dquarkus.container-image.registry=image-registry.openshift-image-registry.svc:5000 \
    -Dquarkus.openshift.mounts.tls.path=/tls \
    -Dquarkus.openshift.secret-volumes.tls.secret-name=tls
    
    ```