package org.challenge.calculator.services;

import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.challenge.calculator.exception.CalculatorOperationException;
import org.challenge.calculator.utils.ServiceUsageCalculator;
import org.challenge.calculator.entity.User;
import org.challenge.calculator.model.ServiceRequest;
import org.challenge.calculator.model.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service("randomStringService")
public class RandomStringServiceImpl extends  CalculatorService{
    private static final String RANDOM_GENERATOR_URL = "https://www.random.org/strings/?num=1&len=10&digits=on&upperalpha=on&loweralpha=on&unique=on&format=plain&rnd=new";
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomStringServiceImpl.class);


    @Override
    public ServiceResponse execute(ServiceRequest serviceRequest) {
        ServiceResponse serviceResponse = null;

        if(isRequestValid(serviceRequest)){
            String randomString = getRandomStringFromThirdParty(RANDOM_GENERATOR_URL, null);
            serviceResponse = new ServiceResponse();
            serviceResponse.setServiceUUID(serviceRequest.getServiceUUID());
            serviceResponse.setUsername(serviceRequest.getUsername());
            serviceResponse.setExecutionDate(new Date().getTime());
            serviceResponse.setResponse(randomString);

        }else{
            LOGGER.error("Some of the parameters are incorrect. Please check");
            throw new CalculatorOperationException("Some of the parameters are incorrect. Please check");
        }

        return serviceResponse;

    }

    private String getRandomStringFromThirdParty(String url, Map<String, String> urlParameters) {
        String randomString = "";
        HttpClient httpClient = buildHttpClient();

        try {
            Optional<URI> uriOptional = buildURI(url, urlParameters);

            if (uriOptional.isPresent()) {
                HttpRequest.Builder builder = HttpRequest.newBuilder()
                        .GET()
                        .uri(uriOptional.get())
                        .setHeader("Content-Type", "text/plain;charset=UTF-8");

                HttpRequest request = builder.build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                randomString = response != null ? response.body() : randomString;

            } else {
                LOGGER.info("The passed url is wrong. Aborting call");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return randomString;
    }

    private Optional<URI> buildURI(String url, Map<String, String> urlParameters) throws URISyntaxException {
        URI uri = null;

        if(StringUtils.isNotBlank(url)){
            URIBuilder uriBuilder = new URIBuilder(url);
            if(!Collections.isEmpty(urlParameters)){
                urlParameters.entrySet().forEach(parameter->{
                    uriBuilder.addParameter(parameter.getKey(), parameter.getValue());
                });
            }
            uri = uriBuilder.build();
        }
        return Optional.ofNullable(uri);
    }

    private HttpClient buildHttpClient(){
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }
}
