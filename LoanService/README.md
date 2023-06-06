# CBC Demo Loan Service - Backend API


## To Deploy

1. Create a OCP Project if not done:
    ```
    oc new-projecy cbc-backends

    ```
2. Go to project folder and run the following command to deploy:
    
    ```
    ./mvnw clean && \
    ./mvnw install \
    -Dquarkus.kubernetes.deploy=true \
    -Dquarkus.container-image.group=cbc-backends \
    -Dquarkus.openshift.namespace=cbc-backends \
    -Dquarkus.log.category.\"blog.braindose\".level=DEBUG \
    -Dquarkus.container-image.registry=image-registry.openshift-image-registry.svc:5000
    
    ```
