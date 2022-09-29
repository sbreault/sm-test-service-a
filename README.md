

# ------------ OpenShift image build
Add OpenShift Maven extension:
```shell script
mvnw quarkus:add-extension -Dextensions="openshift"
```

# Double-check application.properties are set:
```shell script
quarkus.kubernetes.deployment-target=openshift
quarkus.container-image.registry=image-registry.openshift-image-registry.svc:5000
quarkus.container-image.group=prsb.quarkus
quarkus.openshift.part-of=test-app
quarkus.openshift.service-account=default
quarkus.openshift.expose=true
quarkus.kubernetes-client.trust-certs=true
quarkus.openshift.labels.app=getting-started
```

Once local testing is complete build a native image for OpenShift.
1. 
```shell script
oc login --token...
```
2. from project root issue native build command:
```shell script
mvnw clean package -Pnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true -Dquarkus.container-image.push=true
```
Note: PowerShell requires quotes around -D switches.

Quarkus images - https://quarkus.io/guides/container-image#s2i
OpenShift Client (oc) commands - https://docs.openshift.com/container-platform/4.6/cli_reference/openshift_cli/developer-cli-commands.html

# --------------- OpenShift create app and deploy
If an application (service, deployment, route) doesn't exist perform the following, otherwise OpenShift will reuse existing objects and modify them accordingly.

3. create service, deployment objects in OpenShift...
(be sure to 'oc login' using a valid token and set the correct project/namespace)
```shell script
oc get is
```
(make note of the image name for use in 'oc new-app' command below)

Create a service if it doesnt exist...!!! BE SURE TO PASS ENV VARIABLES IN USING -p SWITCHES)
```shell script
oc new-app --name=<artifactId> --image-stream="<artifactId>:<tag>"
oc get svc
```
4. create route
```shell script
oc expose svc <artifactId>
oc describe svc <artifactId>
```

5. edit route to use TLS
```shell script
oc patch route <artifactId> -p "{\"spec\":{\"tls\":{\"termination\":\"edge\"}}}"
```



## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `getting-started-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/getting-started-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/getting-started-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

# RESTEasy JAX-RS

<p>A Hello World RESTEasy resource</p>

Guide: https://quarkus.io/guides/rest-json
