package org.challenge.calculator.services;

import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.challenge.calculator.exception.CalculatorException;
import org.challenge.calculator.exception.ErrorCause;
import org.challenge.calculator.model.ServiceRequest;
import org.challenge.calculator.model.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * This service returns random/non-sense string as result
 */
@Service("randomStringService")
public class RandomStringServiceImpl extends CalculatorService {
    //TODO move this to the configuration table, breaking up the parameters
    private static final String RANDOM_GENERATOR_URL = "https://www.random.org/strings/?num=1&len=10&digits=on&upperalpha=on&loweralpha=on&unique=on&format=plain&rnd=new";
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomStringServiceImpl.class);


    /**
     * This method does a get request to the RANDOOM_GENERATOR_URL and that's it.
     *
     * @param serviceRequest Object holding the data needed by the service to execute.
     * @return The string returned by the service pointed by RANDOM_GENERATOR_URL
     */
    @Override
    public ServiceResponse execute(ServiceRequest serviceRequest) {
        ServiceResponse serviceResponse;

        if (isRequestValid(serviceRequest)) {
            String randomString = getRandomStringFromThirdParty(RANDOM_GENERATOR_URL, null);
            serviceResponse = new ServiceResponse();
            serviceResponse.setServiceUUID(serviceRequest.getServiceUuid());
            serviceResponse.setUsername(serviceRequest.getUsername());
            serviceResponse.setExecutionDate(new Date().getTime());
            serviceResponse.setResponse(randomString);

        } else {
            LOGGER.error("Some of the parameters are incorrect. Please check");
            throw new CalculatorException("Some of the parameters are incorrect. Please check",
                    ErrorCause.INVALID_PARAMETERS);
        }

        return serviceResponse;

    }

    /**
     * This method does de actual call to the third party service
     *
     * @param url           url of the third party service
     * @param urlParameters any additional parameters that the third party might need
     * @return the response as a String
     */
    private String getRandomStringFromThirdParty(String url, Map<String, String> urlParameters) {
        String randomString = "";
        HttpClient httpClient = buildHttpClient();

        try {
            Optional<URI> uriOptional = buildURI(url, urlParameters);

            if (uriOptional.isPresent()) {
                HttpGet request = new HttpGet(uriOptional.get());
                // add request headers
                request.addHeader("Content-Type", "text/plain;charset=UTF-8");

                HttpResponse response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();
                String result = null;
                if (entity != null) {
                    // return it as a String
                    result = EntityUtils.toString(entity);
                    LOGGER.error("No result from " + uriOptional.get());
                }
                randomString = StringUtils.defaultIfBlank(result, randomString);

            } else {
                LOGGER.info("The passed url is wrong. Aborting call");
                throw new CalculatorException("The passed url is wrong. Aborting call", ErrorCause.INVALID_PARAMETERS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return randomString;
    }

    /**
     * Builds up the url adding the parameters to it
     */
    private Optional<URI> buildURI(String url, Map<String, String> urlParameters) throws URISyntaxException {
        URI uri = null;

        if (StringUtils.isNotBlank(url)) {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (!Collections.isEmpty(urlParameters)) {
                urlParameters.entrySet().forEach(parameter -> {
                    uriBuilder.addParameter(parameter.getKey(), parameter.getValue());
                });
            }
            uri = uriBuilder.build();
        }
        return Optional.ofNullable(uri);
    }

    private HttpClient buildHttpClient() {
        return HttpClients.createDefault();
    }
}
