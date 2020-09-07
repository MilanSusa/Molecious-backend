# Molecious backend

Molecious backend is a Spring Boot application that uses [Skin Cancer Detection Inference API][1] in order to provide 
required API for [Molecious frontend][2].

## Prerequisites

1) Download JDK 11 and set `JAVA_HOME` environment variable to contain path to previously downloaded JDK.
2) Create `JWT_SECRET_KEY` environment variable with an adequate secret as value.
3) Create a Firebase project and set `FIREBASE_PROJECT_ID` environment variable to contain the id of created project.
4) Create `SKIN_CANCER_DETECTION_INFERENCE_API_BASE_URL` environment variable and set it to `http://localhost:8000`.
5) Follow the steps for running [Skin Cancer Detection Inference API][1].

[1]: https://github.com/MilanSusa/Skin-Cancer-Detection-Inference-API
[2]: https://github.com/MilanSusa/Molecious-frontend

## Running locally

Run the following command from the root of the project to start the application:

    mvnw spring-boot:run 

By default, server will start on port 8080, so you can access the API documentation via:
    
    http://localhost:8080/swagger-ui.html

## License

Licensed under the [MIT License](LICENSE).